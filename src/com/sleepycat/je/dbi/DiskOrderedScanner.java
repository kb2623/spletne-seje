/*-
 *
 *  This file is part of Oracle Berkeley DB Java Edition
 *  Copyright (C) 2002, 2015 Oracle and/or its affiliates.  All rights reserved.
 *
 *  Oracle Berkeley DB Java Edition is free software: you can redistribute it
 *  and/or modify it under the terms of the GNU Affero General Public License
 *  as published by the Free Software Foundation, version 3.
 *
 *  Oracle Berkeley DB Java Edition is distributed in the hope that it will be
 *  useful, but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU Affero
 *  General Public License for more details.
 *
 *  You should have received a copy of the GNU Affero General Public License in
 *  the LICENSE file along with Oracle Berkeley DB Java Edition.  If not, see
 *  <http://www.gnu.org/licenses/>.
 *
 *  An active Oracle commercial licensing agreement for this product
 *  supercedes this license.
 *
 *  For more information please contact:
 *
 *  Vice President Legal, Development
 *  Oracle America, Inc.
 *  5OP-10
 *  500 Oracle Parkway
 *  Redwood Shores, CA 94065
 *
 *  or
 *
 *  berkeleydb-info_us@oracle.com
 *
 *  [This line intentionally left blank.]
 *  [This line intentionally left blank.]
 *  [This line intentionally left blank.]
 *  [This line intentionally left blank.]
 *  [This line intentionally left blank.]
 *  [This line intentionally left blank.]
 *  EOF
 *
 */

package com.sleepycat.je.dbi;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import com.sleepycat.je.CacheMode;
import com.sleepycat.je.DatabaseEntry;
import com.sleepycat.je.EnvironmentFailureException;
import com.sleepycat.je.cleaner.DbFileSummary;
import com.sleepycat.je.log.LogEntryType;
import com.sleepycat.je.log.LogManager;
import com.sleepycat.je.log.entry.LNLogEntry;
import com.sleepycat.je.log.entry.LogEntry;
import com.sleepycat.je.tree.BIN;
import com.sleepycat.je.tree.OldBINDelta;
import com.sleepycat.je.tree.Key;
import com.sleepycat.je.tree.IN;
import com.sleepycat.je.tree.LN;
import com.sleepycat.je.tree.SearchResult;
import com.sleepycat.je.tree.Tree;
import com.sleepycat.je.utilint.DbLsn;

/**
 * Provides an enumeration of all key/data pairs in a database, striving to
 * fetch in disk order.
 *
 * Unlike SortedLSNTreeWalker, for which the primary use case is preload, this
 * class notifies the callback while holding a latch only if that can be done
 * without blocking (e.g., when the callback can buffer the data without
 * blocking).  In other words, while holding a latch, the callback will not be
 * notified if it might block.  This is appropriate for the DOS
 * (DiskOrderedCursor) use case, since the callback will block if the DOS queue
 * is full, and the user's consumer thread may not empty the queue as quickly
 * as it can be filled by the producer thread.  If the callback were allowed to
 * block while a latch is held, this would block other threads attempting to
 * access the database, including JE internal threads, which would have a very
 * detrimental impact.
 *
 * Algorithm
 * =========
 *
 * Terminology
 * -----------
 * callback: object implementing the RecordProcessor interface
 * process: invoking the callback with a key-data pair
 * iteration: top level iteration consisting of phase I and II
 * phase I: accumulate LSNs
 * phase II: sort, fetch and process LSNs
 *
 * Phase I and II
 * --------------
 * To avoid processing resident nodes (invoking the callback with a latch
 * held), a non-recursive algorithm is used.  Instead of recursively
 * accumulating LSNs in a depth-first iteration of the tree (like the
 * SortedLSNTreeWalker algorithm), level 2 INs are traversed in phase I and
 * LSNs are accumulated for LNs or BINs (more on this below).  When the memory
 * or LSN batch size limit is exceeded, phase I ends and all tree latches are
 * released.  During phase II the previously accumulated LSNs are fetched and
 * the callback is invoked for each key or key-data pair.  Since no latches are
 * held, it is permissible for the callback to block.
 *
 * One iteration of phase I and II processes some subset of the database.
 * Since INs are traversed in tree order in phase I, this subset is described
 * by a range of keys.  When performing the next iteration, the IN traversal is
 * restarted at the highest key that was processed by the previous iteration.
 * The previous highest key is used to avoid duplication of entries, since some
 * overlap between iterations may occur.
 *
 * LN and BIN modes
 * ----------------
 * As mentioned above, we accumulate LSNs for either LNs or BINs.  The BIN
 * accumulation mode provides an optimization for key-only traversals and for
 * all traversals of duplicate DBs (in a dup DB, the data is included in the
 * key).  In these cases we never need to fetch the LN, so we can sort and
 * fetch the BIN LSNs instead.  This supports at least some types of traversals
 * that are efficient when all BINs are not in the JE cache.
 *
 * We must only accumulate LN or BIN LSNs, never both, and never the LSNs of
 * other INs (above level 1).  If we broke this rule, there would be no way to
 * constrain memory usage in our non-recursive approach, since we could not
 * easily predict in advance how much memory would be needed to fetch the
 * nested nodes.  Even if we were able predict the memory needed, it would be
 * of little advantage to sort and fetch a small number of higher level nodes,
 * only to accumulate the LSNs of their descendants (which are much larger in
 * number).  The much smaller number of higher level nodes would likely be
 * fetched via random IO anyway, in a large data set anyway.
 *
 * The above justification also applies to the algorithm we use in LN mode, in
 * which we accumulate and fetch only LN LSNs.  In this mode we always fetch
 * BINs explicitly (not in LSN sorted order), if they are not resident, for the
 * reasons stated above.
 *
 * Furthermore, in BIN mode we must account for BIN-deltas.  Phase I must keep
 * a copy any BIN-deltas encountered in the cache.  And phase II must make two
 * passes for the accumulated LSNs: one pass to load the deltas and another to
 * load the full BINs and merge the previously loaded deltas.  Unfortunately
 * we must budget memory for the deltas during phase I; since most BIN LSNs are
 * for deltas, not full BINs, we assume that we will need to temporarily save a
 * delta for each LSN.  This two pass approach is not like the recursive
 * algorithm we rejected above, however, in two respects: 1) we know in advance
 * (roughly anyway) how much memory we will need for both passes, and 2) the
 * number of LSNs fetched in each pass is roughly the same.
 *
 * Data Lag
 * --------
 * In phase I, as an exception to what was said above, we sometimes process
 * nodes that are resident in the Btree (in the JE cache) if this is possible
 * without blocking.  The primary intention of this is to provide more recent
 * data to the callback.  When accumulating BINs, if the BIN is dirty then
 * fetching its LSN later means that some recently written LNs will not be
 * included.  Therefore, if the callback would not block, we process the keys
 * in a dirty BIN during phase I.  Likewise, when accumulating LNs in a
 * deferred-write database, we process dirty LNs if the callback would not
 * block.  When accumulating LN LSNs for a non-deferred-write database, we can
 * go further and process all resident LNs, as long as the callback would not
 * block, since we know that no LNs are dirty.
 *
 * In spite of our attempt to process resident nodes, we may not be able to
 * process all of them if doing so would cause the callback to block.  When we
 * can't process a dirty, resident node, the information added (new, deleted or
 * updated records) since the node was last flushed will not be visible to the
 * callback.
 *
 * In other words, the data presented to the callback may lag back to the time
 * of the last checkpoint.  It cannot lag further back than the last
 * checkpoint, because: 1) the scan doesn't accumulate LSNs any higher than the
 * BIN level, and 2) checkpoints flush all dirty BINs.  For a DOS, the user may
 * decrease the likelihood of stale data by increasing the DOS queue size,
 * decreasing the LSN batch size, decreasing the memory limit, or performing a
 * checkpoint immediately before the start of the scan.  Even so, it may be
 * impossible to guarantee that all records written at the start of the scan
 * are visible to the callback.
 */
class DiskOrderedScanner {

    /**
     * Interface implemented by the callback.
     */
    interface RecordProcessor {

        /**
         * Process a key-data pair, in user format (dup DB keys are already
         * split into key and data).
         *
         * @param key always non-null.
         * @param data is null only in keys-only mode.
         */
        void process(byte[] key, byte[] data);

        /**
         * Returns whether process() can be called nRecords times, immediately
         * after calling this method, without any possibility of blocking.
         * For example, with DOS this method returns true if the DOS queue has
         * nRecords or more remaining capacity.
         */
        boolean canProcessWithoutBlocking(int nRecords);

        boolean neverBlocks();
    }

    private static final LogEntryType[] LN_ONLY = new LogEntryType[] {
        LogEntryType.LOG_INS_LN /* Any LN type will do. */
    };

    private static final LogEntryType[] BIN_ONLY = new LogEntryType[] {
        LogEntryType.LOG_BIN
    };

    private static final LogEntryType[] BIN_OR_DELTA = new LogEntryType[] {
        LogEntryType.LOG_BIN,
        LogEntryType.LOG_BIN_DELTA,
        LogEntryType.LOG_OLD_BIN_DELTA,
    };

    private final DatabaseImpl dbImpl;

    private final boolean dupDb;

    private final boolean countOnly;
    private final boolean keysOnly;
    private final boolean binsOnly;

    private final long lsnBatchSize;
    private final long memoryLimit;

    private final RecordProcessor processor;

    private final Map<Long, DbFileSummary> dbFileSummaries;

    private final LSNAccumulator lsnAcc;

    private final List<byte[]> savedKeys;
    private final List<byte[]> savedData;

    private long memoryUsage;

    private byte[] prevEndingKey;
    private byte[] newEndingKey;

    private volatile int nIterations;

    DiskOrderedScanner(
        DatabaseImpl dbImpl,
        RecordProcessor processor,
        boolean binsOnly,
        boolean keysOnly,
        boolean countOnly,
        long lsnBatchSize,
        long memoryLimit) {
        
        this.dbImpl = dbImpl;
        this.processor = processor;

        dupDb = dbImpl.getSortedDuplicates();
        this.countOnly = countOnly;
        this.keysOnly = keysOnly || countOnly;
        this.binsOnly = binsOnly || dupDb || keysOnly || countOnly;

        this.lsnBatchSize = lsnBatchSize;
        this.memoryLimit = memoryLimit;

        dbFileSummaries = dbImpl.cloneDbFileSummaries();

        lsnAcc = new LSNAccumulator() {
            @Override
            void noteMemUsage(long increment) {
                addMemoryUsage(increment);
            }
        };

        if (!binsOnly) {
            savedKeys = new ArrayList<byte[]>();
            savedData = new ArrayList<byte[]>();
        } else {
            savedKeys = null;
            savedData = null;
        }
    }

    private void addMemoryUsage(long increment) {
        memoryUsage += increment;
    }

    int getNIterations() {
        return nIterations;
    }

    /**
     * Called to perform a disk-ordered scan.  Returns only when the scan is
     * complete; i.e, when all records in the database have been passed to the
     * callback.
     */
    void scan() {
        while (true) {

            /*
             * Phase I.
             */
            IN in = getFirstIN();

            List<BIN> binDeltas = binsOnly ? (new ArrayList<BIN>(500)) : null;
            try {
                while (in != null) {
                    if (binsOnly) {
                        accumulateBINs(in, binDeltas);
                    } else {
                        accumulateLNs(in);
                    }
                    if (accLimitExceeded()) {
                        break;
                    }
                    final IN prevIn = in;
                    in = null;
                    in = getNextIN(prevIn);
                }
            } finally {
                if (in != null) {
                    in.releaseLatch();
                }
            }

            /*
             * Phase II.
             */
            final long[] lsnArray = lsnAcc.getAndSortPendingLSNs();
            if (binsOnly) {
                fetchAndProcessBINs(lsnArray, binDeltas);
            } else {
                fetchAndProcessLNs(lsnArray);
            }

            nIterations += 1;

            if (in == null) {
                break;
            }

            /*
             * Prepare for next iteration.
             */
            lsnAcc.clear();
            memoryUsage = 0;
            prevEndingKey = newEndingKey;
        }
    }

    /**
     * Returns whether phase I should terminate because the memory or LSN batch
     * size limit has been exceeded.
     *
     * This method need not be called every LN processed; exceeding the
     * limits by a reasonable amount should not cause problems, since the
     * limits are very approximate measures anyway. It is acceptable to check
     * for exceeded limits once per BIN, and this is currently how it is used.
     */
    private boolean accLimitExceeded() {
        return memoryUsage >= memoryLimit ||
               lsnAcc.getNTotalEntries() > lsnBatchSize;
    }

    /**
     * Implements guts of phase I in binsOnly mode.  Accumulates BIN deltas and
     * BIN LSNs for the children of the given level 2 IN parent, and processes
     * resident BINs under certain conditions; see algorithm at top of file.
     * 
     * @param parent the exclusively latched level 2 parent.
     */
    private void accumulateBINs(IN parent, List<BIN> binDeltas) {

        for (int i = 0; i < parent.getNEntries(); i += 1) {

            /* Skip BINs that were processed on the previous iteration. */
            if (i + 1 < parent.getNEntries() &&
                prevEndingKey != null &&
                Key.compareKeys(prevEndingKey, parent.getKey(i + 1),
                                dbImpl.getKeyComparator()) >= 0) {
                continue;
            }

            /* Check limits once per BIN, for simplicity. */
            if (accLimitExceeded()) {
                return;
            }

            final long binLsn = parent.getLsn(i);
            final BIN bin = (BIN) parent.getTarget(i);

            if (bin != null) {
                bin.latch();
            }

            try {
                /*
                 * Stash away a copy of a BIN delta in cache, to avoid fetching
                 * it later.  A copy is needed because we release latches
                 * before moving to phase II.
                 */
                if (bin != null && bin.isBINDelta()) {
                    final BIN delta = bin.cloneBINDelta();
                    binDeltas.add(delta);
                    addMemoryUsage(delta.getInMemorySize());
                    continue;
                }

                /*
                 * If the BIN is not resident, or not dirty, or we cannot
                 * process all entries in the BIN now (without blocking), then
                 * accummulate this LSN for later processing. The reason for
                 * the dirtyness check is to avoid filling the
                 * DiskOrderedCursorImpl queue with the records of a clean
                 * BIN, thus failing to later put in the queue the records
                 * of another dirty BIN and degrading the accuracy of the
                 * scan as a result. However, if the processor never blocks
                 * (e.g. when countOnly is true), there is no reason to delay
                 * processing a clean BIN (and actually having to read it
                 * from the log in phase 2). 
                 */
                if (bin == null ||
                    (processor.neverBlocks() == false && !bin.getDirty()) ||
                    !processor.canProcessWithoutBlocking(bin.getNEntries())) {

                    lsnAcc.add(binLsn);

                    /*
                     * Most LSNs are deltas, so budget memory for a delta.
                     * Note: if bin != null, we know it's a full BIN. However,
                     * binLsn may still point to a delta. If binLsn does point
                     * to a full bin, then we don't need to increase the memory
                     * consumption, because during phase 2 the full BIN will be
                     * fetched and processed immediately.
                     */
                    if (bin == null || binLsn != bin.getLastFullLsn()) {
                        addMemoryUsage(
                            getDeltaMemSize(DbLsn.getFileNumber(binLsn)));
                    }

                    continue;
                }

                /* We can process all keys in a dirty BIN without blocking. */
                for (int j = 0; j < bin.getNEntries(); j += 1) {

                    if (skipSlot(bin, j)) {
                        continue;
                    }

                    /*
                     * Process key for resident BIN. If keysOnly is false and
                     * the data is embedded, process the data as well.
                     */
                    byte[] key = (countOnly ? null : bin.getKey(j));
                    byte[] data = (keysOnly ? null : bin.getData(j));

                    processRecord(key, data);
                }
            } finally {
                if (bin != null) {
                    bin.releaseLatch();
                }
            }
        }
    }

    /**
     * Implements guts of phase I in LNs-only mode (binsOnly is false).
     * Accumulates LN LSNs for the BIN children of the given level 2 IN parent,
     * and processes resident LNs under certain conditions; see algorithm at
     * top of file.
     * 
     * @param parent the exclusively latched level 2 parent.
     */
    private void accumulateLNs(IN parent) {

        for (int i = 0; i < parent.getNEntries(); i += 1) {

            /* Skip BINs that were processed on the previous iteration. */
            if (i + 1 < parent.getNEntries() &&
                prevEndingKey != null &&
                Key.compareKeys(prevEndingKey, parent.getKey(i + 1),
                                dbImpl.getKeyComparator()) >= 0) {
                continue;
            }

            /* Check limits once per BIN, for simplicity. */
            if (accLimitExceeded()) {
                return;
            }

            boolean isLatched = false;

            /*
             * Explicitly fetch the BIN if it is not resident, merging it with
             * a delta if needed. Do not call parent.fetchIN(i) because we
             * don't want the BIN to be attached to the in-memory tree.
             */
            BIN bin = (BIN) parent.getTarget(i);

            if (bin == null) {
                final Object item = fetchItem(parent.getLsn(i), BIN_OR_DELTA);
                if (item instanceof BIN) {
                    bin = (BIN) item;
                    if (bin.isBINDelta(false)) {
                        bin = bin.reconstituteBIN(dbImpl);
                    } else {
                        bin.setDatabase(dbImpl);
                    }
                } else {
                    final OldBINDelta delta = (OldBINDelta) item;
                    bin = (BIN) fetchItem(delta.getLastFullLsn(), BIN_ONLY);
                    delta.reconstituteBIN(dbImpl, bin);
                }
            } else {
                BIN fullBIN = null;

                bin.latch();
                isLatched = true;

                try {
                    if (bin.isBINDelta()) {
                        fullBIN = bin.reconstituteBIN(dbImpl);
                    }
                } finally {
                    if (bin.isBINDelta()) {
                        bin.releaseLatch();
                        bin = fullBIN;
                        isLatched = false;
                    }
                }
            }

            try {
                boolean isDWDB = dbImpl.isDeferredWriteMode();

                for (int j = 0; j < bin.getNEntries(); j += 1) {

                    if (skipSlot(bin, j)) {
                        continue;
                    }

                    /*
                     * If the record is embedded, process it right now if the
                     * queue is not full, otherwise accumulate its key and
                     * value for processing during phase 2.
                     *
                     * If the record is not embedded, accumulate its LSN for
                     * processing during phase 2 if:
                     * - The LN is not cached, or
                     * - The DB is DW and the LN is cached and clean, or
                     * - The queue is full.
                     */
                    final LN ln = (LN) bin.getTarget(j);

                    if (bin.isEmbeddedLN(j)) {

                        if (!processor.canProcessWithoutBlocking(1)) {

                            byte[] key = bin.getKey(j);
                            byte[] data = bin.getData(j);

                            savedKeys.add(key);
                            savedData.add(data);

                            addMemoryUsage(
                                MemoryBudget.byteArraySize(key.length) +
                                MemoryBudget.OBJECT_ARRAY_ITEM_OVERHEAD);

                            addMemoryUsage(
                                MemoryBudget.byteArraySize(data.length) +
                                MemoryBudget.OBJECT_ARRAY_ITEM_OVERHEAD);

                            continue;
                        }

                    } else if (ln == null ||
                               (isDWDB && !ln.isDirty()) ||
                               !processor.canProcessWithoutBlocking(1)) {

                        if (!DbLsn.isTransientOrNull(bin.getLsn(j))) {
                            lsnAcc.add(bin.getLsn(j));
                        }

                        continue;
                    }

                    /* Process resident LN. */
                    processRecord(
                        bin.getKey(j),
                        (ln != null ? ln.getData() : bin.getData(j)));
                }
            } finally {
                if (isLatched) {
                    bin.releaseLatch();
                }
            }
        }
    }

    /**
     * Implements guts of phase II in binsOnly mode.
     */
    private void fetchAndProcessBINs(long[] lsnArray, List<BIN> binDeltas) {

        /*
         * Start out by saving the BIN deltas we collected from cache in an
         * array.  Add additional BIN deltas as we fetch them by LSN.
         *
         * Since most BINs are represented as deltas, for simplicity just
         * allocate an array large enough for all LSNs.  Note that memory for
         * deltas was previously accounted for.  Array elements may be type
         * BIN or OldBINDelta.
         */
        int nDeltas = binDeltas.size();
        final Object[] deltaArray = new Object[nDeltas + lsnArray.length];

        for (int i = 0; i < nDeltas; i += 1) {
            deltaArray[i] = binDeltas.get(i);
        }
        binDeltas.clear(); // Allow GC

        for (int i = 0; i < lsnArray.length; i += 1) {

            final Object item = fetchItem(lsnArray[i], BIN_OR_DELTA);

            /*
             * For a delta, queue fetching of the full BIN and combine the full
             * BIN with the delta when it is processed below.
             */
            if (item instanceof OldBINDelta) {
                deltaArray[nDeltas] = item;
                nDeltas += 1;
                continue;
            }

            final BIN bin = (BIN) item;

            if (bin.isBINDelta(false/*checkLatched*/)) {
                bin.setDatabase(dbImpl);
                deltaArray[nDeltas] = item;
                nDeltas += 1;
                continue;
            }

            /* LSN was for a full BIN, so we can just process it. */
            bin.setDatabase(dbImpl);
            processBIN(bin);
        }

        if (nDeltas == 0) {
            return;
        }

        /* Sort deltas by full BIN LSN. */
        Arrays.sort(deltaArray, 0, nDeltas, new Comparator<Object>() {

            public int compare(Object a, Object b) {
                return DbLsn.compareTo(getLsn(a), getLsn(b));
            }

            private long getLsn(Object o) {
                if (o instanceof OldBINDelta) {
                    return ((OldBINDelta) o).getLastFullLsn();
                }
                return ((BIN) o).getLastFullLsn();
            }
        });

        /*
         * Fetch each full BIN and merge it with its corresponding delta, and
         * process each resulting BIN.
         */
        for (int i = 0; i < nDeltas; i += 1) {

            final Object o = deltaArray[i];
            final BIN bin;

            if (o instanceof OldBINDelta) {
                final OldBINDelta delta = (OldBINDelta) o;
                bin = (BIN) fetchItem(delta.getLastFullLsn(), BIN_ONLY);
                delta.reconstituteBIN(dbImpl, bin);
            } else {
                final BIN delta = (BIN) o;
                bin = delta.reconstituteBIN(dbImpl);
            }

            processBIN(bin);
        }
    }

    /**
     * Process a BIN during phase II in binsOnly mode.
     * 
     * @param bin the exclusively latched BIN.
     */
    private void processBIN(BIN bin) {

        bin.latch();

        try {
            for (int i = 0; i < bin.getNEntries(); i += 1) {
                if (skipSlot(bin, i)) {
                    continue;
                }

                /* Only the key is needed, as in accumulateBINs. */

                byte[] key = (countOnly ? null : bin.getKey(i));
                byte[] data = (keysOnly ? null : bin.getData(i));

                processRecord(key, data);
            }
        } finally {
            bin.releaseLatch();
        }
    }

    /**
     * Implements guts of phase II in LNs-only mode (binsOnly is false).
     */
    private void fetchAndProcessLNs(long[] lsnArray) {

        for (int i = 0; i < savedKeys.size(); i += 1) {

            byte[] key = savedKeys.get(i);
            byte[] data = savedData.get(i);

            processRecord(key, data);

            addMemoryUsage(-MemoryBudget.byteArraySize(key.length));
            addMemoryUsage(-MemoryBudget.byteArraySize(data.length));
        }

        /* Subtract the size of the savedKeys and savedData slots */
        addMemoryUsage(
            - 2 * savedKeys.size() * MemoryBudget.OBJECT_ARRAY_ITEM_OVERHEAD);

        savedKeys.clear();
        savedData.clear();

        for (int i = 0; i < lsnArray.length; i += 1) {

            final LNLogEntry<?> entry =
                (LNLogEntry<?>) fetchEntry(lsnArray[i], LN_ONLY);

            entry.postFetchInit(dbImpl);

            final LN ln = entry.getMainItem();
            if (ln.isDeleted()) {
                continue;
            }

            processRecord(entry.getKey(), ln.getData());
        }
    }

    /**
     * Invokes the callback to process a single key-data pair. The parameters
     * are in the format stored in the Btree, and are translated here to
     * user-format for dup DBs.
     */
    private void processRecord(byte[] treeKey, byte[] treeData) {

        final byte[] key;
        final byte[] data;

        if (dupDb && !countOnly) {
            
            final DatabaseEntry keyEntry = new DatabaseEntry();
            final DatabaseEntry dataEntry =
                (keysOnly ? null : new DatabaseEntry());

            DupKeyData.split(treeKey, treeKey.length, keyEntry, dataEntry);
            key = keyEntry.getData();

            data = keysOnly ? null : dataEntry.getData();

        } else {
            key = (countOnly ? null : treeKey);
            data = ((countOnly || keysOnly) ? null : treeData);
        }

        processor.process(key, data);

        /* Save the highest valued key for this iteration. */
        if (newEndingKey == null ||
            Key.compareKeys(newEndingKey, treeKey,
                            dbImpl.getKeyComparator()) < 0) {
            newEndingKey = treeKey;
        }
    }

    /**
     * Fetches a log entry for the given LSN and returns its main item.
     *
     * @param expectTypes is used to validate the type of the entry; an
     * internal exception is thrown if the log entry does not have one of the
     * given types.
     */
    private Object fetchItem(long lsn, LogEntryType[] expectTypes) {
        return fetchEntry(lsn, expectTypes).getMainItem();
    }

    /**
     * Fetches a log entry for the given LSN and returns it.
     *
     * @param expectTypes is used to validate the type of the entry; an
     * internal exception is thrown if the log entry does not have one of the
     * given types.
     */
    private LogEntry fetchEntry(long lsn, LogEntryType[] expectTypes) {

        final LogManager logManager = dbImpl.getEnv().getLogManager();

        final LogEntry entry =
            logManager.getLogEntryHandleFileNotFound(lsn);

        final LogEntryType type = entry.getLogType();

        for (int i = 0; i < expectTypes.length; i += 1) {
            if (expectTypes[i].isLNType()) {
                if (type.isLNType()) {
                    return entry;
                }
            } else {
                if (type.equals(expectTypes[i])) {
                    return entry;
                }
            }
        }

        throw EnvironmentFailureException.unexpectedState
            ("Expected: " + Arrays.toString(expectTypes) +
             " but got: " + type + " LSN=" + DbLsn.getNoFormatString(lsn));
    }

    /**
     * Calculates a rough estimate of the memory needed for a BIN-delta object.
     */
    private int getDeltaMemSize(long fileNum) {

        final DbFileSummary summary = dbFileSummaries.get(fileNum);

        /*
         * If there are no deltas in this file, then the LSN must for a full
         * BIN, and no memory is needed for the delta.
         */
        if (summary == null) {
            return 0;
        }

        /*
         * The cleaner counts deltas as INs in the DbFileSummary, and most
         * (9/10 by default) are actually deltas, not INs.  We double the
         * average IN byte size in the file to very roughly approximate the
         * memory for a deserialized BIN-delta object.
         */
        final float avgINSize =
            (((float) summary.totalINSize) / summary.totalINCount);

        return (int) (avgINSize * 2);
    }

    /**
     * Returns whether to skip a BIN slot because its LN is deleted, or its key
     * has already been processed in a previous iteration.
     */
    private boolean skipSlot(BIN bin, int index) {

        if (bin.isEntryPendingDeleted(index) ||
            bin.isEntryKnownDeleted(index)) {
            return true;
        }

        /* Skip a slot that was processed in a previous iteration. */
        if (prevEndingKey != null &&
            Key.compareKeys(prevEndingKey, bin.getKey(index),
                            dbImpl.getKeyComparator()) >= 0) {
            return true;
        }

        return false;
    }

    /**
     * Returns the first level 2 IN in the database if prevEndingKey is null
     * (signifying the first iteration), or the level 2 IN containing
     * prevEndingKey if it is non-null.
     *
     * We take the liberty of fetching the BIN (first BIN or BIN for the
     * prevEndingKey), when it is not resident, although in an ideal world no
     * BINs would be added to the cache.  Since this only occurs once per
     * iteration, it is considered to be acceptable.
     *
     * @return the first IN, latched exclusively, or null if the database is
     * empty.
     */
    private IN getFirstIN() {

        /*
         * Use a retry loop to account for the possibility that after getting
         * the BIN we can't find its exact parent due to a split of some kind
         * while the BIN is unlatched.
         */
        final Tree tree = dbImpl.getTree();

        for (int i = 0; i < 25; i += 1) {

            final BIN bin;

            if (prevEndingKey == null) {
                bin = tree.getFirstNode(CacheMode.UNCHANGED);
            } else {
                bin = tree.search(prevEndingKey, CacheMode.UNCHANGED);
            }

            if (bin == null) {
                /* Empty database. */
                return null;
            }

            final SearchResult result = tree.getParentINForChildIN(
                bin, false, /*useTargetLevel*/
                true, /*doFetch*/ CacheMode.UNCHANGED);

            final IN parent = result.parent;

            if (!result.exactParentFound) {
                if (parent != null) {
                    parent.releaseLatch();
                }
                continue; /* Retry. */
            }

            return parent;
        }

        throw EnvironmentFailureException.unexpectedState(
            "Unable to find BIN for prevEndingKey: " +
            Arrays.toString(prevEndingKey));
    }

    /**
     * Returns the next level 2 IN in the database.
     *
     * @param prevIn must be non-null and latched.
     *
     * @return the next IN, latched exclusively, or null if there are no more
     * level 2 INs in the database.
     */
    private IN getNextIN(IN prevIn) {
        return dbImpl.getTree().getNextIN
            (prevIn, true /*forward*/, CacheMode.UNCHANGED);
    }
}
