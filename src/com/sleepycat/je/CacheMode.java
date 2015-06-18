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
package com.sleepycat.je;

/**
 * Modes that can be specified for control over caching of records in the JE
 * in-memory cache.  When a record is stored or retrieved, the cache mode
 * determines how long the record is subsequently retained in the JE in-memory
 * cache, relative to other records in the cache.
 *
 * <p>When the cache overflows, JE must evict some records from the cache.  By
 * default, JE uses a Least Recently Used (LRU) algorithm for determining which
 * records to evict.  With the LRU algorithm, JE makes a best effort to evict
 * the "coldest" (least recently used or accessed) records and to retain the
 * "hottest" records in the cache for as long as possible.</p>
 *
 * <p>Note that JE uses an approximate LRU approach with some exceptions and
 * special cases.</p>
 * <ul>
 *     <li>
 *         Individual records (LNs or Leaf Nodes) do not appear on the LRU
 *         list, i.e., their "hotness" is not explicitly tracked. Instead,
 *         their containing Btree node (BIN or bottom internal node) appears on
 *         the LRU list. Each BIN contains roughly 100 LNs
 *         (see {@link com.sleepycat.je.EnvironmentConfig#NODE_MAX_ENTRIES}).
 *         When an LN is accessed, its BIN is moved to the hot end of the LRU
 *         list, implying that all other LNs in the same BIN also are treated
 *         as if they are hot. The same applies if the BIN is moved to the cold
 *         end of the LRU list.
 *     </li>
 *     <li>
 *         When a BIN contains LNs and the BIN reaches the cold end of the LRU
 *         list, memory can be reclaimed by evicting the LNs, and eviction of
 *         the BIN is deferred. The empty BIN is moved to the hot end of the
 *         LRU list.
 *     </li>
 *     <li>
 *         When a BIN contains no LNs, it may be evicted entirely. When the
 *         BINs parent node becomes empty, it may also be evicted, and so on.
 *         The BINs and INs are evicted on the basis of an LRU, but with two
 *         exceptions:
 *         <p>
 *         1) Dirty BINs and INs are evicted only after eviction of all
 *         non-dirty BINs and INs. This is important to reduce logging and
 *         associated cleaning costs.
 *         <p>
 *         2) A BIN may be mutated to a BIN-delta to reclaim memory, rather
 *         then being evicted entirely. A BIN-delta contains only the dirty
 *         entries (for LNs recently logged). A BIN-delta is used when its
 *         size relative to the full BIN will be small enough so that it will
 *         be more efficient, both on disk and in memory, to store the delta
 *         rather than the full BIN.
 *         (see {@link com.sleepycat.je.EnvironmentConfig#TREE_BIN_DELTA}).
 *         The advantage of keeping a BIN-delta in cache is that some
 *         operations, particularly record insertions, can be performed using
 *         the delta without having the complete BIN in cache. When a BIN is
 *         mutated to a BIN-delta to reclaim memory, it is placed at the hot
 *         end of the LRU list.
 *     </li>
 *     <li>
 *         To reduce contention among threads on the LRU list, multiple LRU
 *         lists may be configured. See
 *         {@link com.sleepycat.je.EnvironmentConfig#EVICTOR_N_LRU_LISTS}.
 *         As described in the javadoc for this parameter, there is a trade-off
 *         between thread contention and the accuracy of the LRU.
 *     </li>
 *     <li>
 *          A non-default cache mode may be explicitly specified to override
 *          the LRU behavior. See the CacheMode enumeration values for details.
 *          the normal LRU behavior described above. See the CacheMode
 *          enumeration values for details.
 *     </li>
 * </ul>
 *
 * <p>When no cache mode is explicitly specified, the default cache mode is
 * {@link #DEFAULT}. The default mode causes the normal LRU algorithm to be
 * used.</p>
 *
 * <p>An explicit cache mode may be specified as an {@link
 * EnvironmentConfig#setCacheMode Environment property}, a {@link
 * DatabaseConfig#setCacheMode Database property}, or a {@link
 * Cursor#setCacheMode Cursor property}.  If none are specified, {@link
 * #DEFAULT} is used.  If more than one non-null property is specified, the
 * Cursor property overrides the Database and Environment properties, and the
 * Database property overrides the Environment property.</p>
 *
 * <p>When all records in a given Database, or all Databases, should be treated
 * the same with respect to caching, using the Database and/or Environment
 * cache mode properties is sufficient. For applications that need finer
 * grained control, the Cursor cache mode property can be used to provide a
 * specific cache mode for individual records or operations.  The Cursor cache
 * mode property can be changed at any time, and the cache mode specified will
 * apply to subsequent operations performed with that Cursor.</p>
 *
 * <p>In a Replicated Environment where a non-default cache mode is desired,
 * the cache mode can be configured on the Master node as described above.
 * However, it is important to configure the cache mode on the Replica nodes
 * using an Environment property.  That way, the cache mode will apply to
 * <em>write</em> operations that are replayed on the Replica for all
 * Databases, even if the Databases are not open by the application on the
 * Replica.  Since all nodes may be Replicas at some point in their life cycle,
 * it is recommended to configure the desired cache mode as an Environment
 * property on all nodes in a Replicated Environment.</p>
 *
 * <p>On a Replica, per-Database control over the cache mode for <em>write</em>
 * operations is possible by opening the Database on the Replica and
 * configuring the cache mode.  Per-Cursor control (meaning per-record or
 * per-operation) control of the cache mode is not possible on a Replica for
 * <em>write</em> operations.  For <em>read</em> operations, both per-Database
 * and per-Cursor control is possible on the Replica, as described above.</p>
 * <p> 
 * The cache related stats in {@link EnvironmentStats} can provide some measure
 * of the effectiveness of the cache mode choice.
 */
public enum CacheMode {

    /**
     * The record's hotness is changed to "most recently used" by the
     * operation.
     *
     * <p>More specifically, the BIN containing the record's LN is moved to the
     * hot end of its LRU list.</p>
     *
     * <p>This cache mode is used when the application does not need explicit
     * control over the cache and a standard LRU approach is sufficient.</p>
     *
     * <p>Note that {@code null} may be specified to use the {@code DEFAULT}
     * mode.</p>
     */
    DEFAULT,

    /**
     * The record's hotness is changed to "most recently used" by the operation
     * and a best effort is made to prevent it from being evicted it from cache.
     *
     * <p>More specifically, the BIN containing the record's LN is moved to the
     * hot end of its LRU list, and a best effort is made to keep the node
     * from being evicted (assuming there is no subsequent access to this BIN
     * using a different cache mode).</p>
     *
     * <p>Use of this mode does not prevent a BIN from moving to the cold end
     * of the LRU list as other nodes are moved to the hot end. The "best
     * effort" referenced above consists of moving the BIN to the hot end when
     * it reaches the cold end. However, to prevent overflowing the cache, if
     * this node reaches the cold end again, before it is accessed again, it
     * will then be evicted. This implies that for a record accessed with
     * KEEP_HOT to be guaranteed to remain in cache while other records are
     * also being accessed, it must be accessed fairly frequently.</p>
     *
     * <p>This cache mode is normally used when the application intends to
     * keep this record cached for as long as possible, even when it is not
     * accessed frequently.</p>
     *
     * @deprecated please use {@link #DEFAULT} instead. In an upcoming release
     * this mode will function exactly as if {@link #DEFAULT} were specified.
     */
    KEEP_HOT,

    /**
     * The record's hotness or coldness is unchanged by the operation where
     * this cache mode is specified.
     *
     * <p>More specifically:
     * <ul>
     *     <li>If the record's LN was not present in the cache prior to the
     *     operation, then the LN will be evicted after the operation. When the
     *     operation is not performed via a cursor, the LN is evicted when the
     *     operation is complete. When a cursor is used, the LN is evicted when
     *     the cursor is moved to a different record or closed.</li>
     *
     *     <li>If the record's BIN was not present in the cache prior to the
     *     operation and is not dirty, then the BIN (and LN) will be evicted
     *     after the operation. When the operation is not performed via a
     *     cursor, the BIN is evicted when the operation is complete. When a
     *     cursor is used, the BIN is evicted only when the cursor moves to a
     *     different BIN or is closed. Because of the way BINs are evicted,
     *     when multiple operations are performed using a single cursor and not
     *     perturbing the cache is desired, it is important to use this cache
     *     mode for all of the operations.</li>
     *
     *     <li>If the BIN was present in the cache prior to the operation or is
     *     dirty, then the BIN will not be evicted after the operation, and the
     *     BIN's position in the LRU list will not be changed. When the
     *     operation loaded the BIN and the BIN becomes dirty, this is normally
     *     a write operation; however, other concurrent threads can also dirty
     *     the BIN.</li>
     * </ul>
     *
     * <p>This cache mode is normally used when the application prefers that
     * the operation should not perturb the cache, for example, when scanning
     * over all records in a database.</p>
     */
    UNCHANGED,

    /**
     * When the cache is full, the record's BIN (and its LNs) are evicted after
     * the operation; otherwise, the record's hotness is changed to "least
     * recently used".
     *
     * <p>More specifically, if the cache is full, the record's LN and its
     * containing BIN are evicted according to the same rules as
     * {@link #EVICT_BIN}. If the cache is not full or eviction of the BIN is
     * not possible, the BIN is moved to the cold end of the LRU list.</p>
     *
     * <p>The reasons for using this cache mode are similar to those for using
     * {@link #EVICT_BIN} or {@link #UNCHANGED}. The main difference between
     * this mode and {@code EVICT_BIN} is that this mode does not cause
     * eviction until the cache is full. The main difference between this mode
     * and {@code UNCHANGED} is that this mode evicts the BIN even when it was
     * loaded (and possibly made hot) by prior operations.</p>
     *
     * @since 3.3.98
     *
     * @deprecated please use {@link #UNCHANGED} instead. In an upcoming
     * release this mode will function exactly as if {@link #UNCHANGED} were
     * specified.
     */
    MAKE_COLD,

    /**
     * The record's LN is evicted after the operation, and the containing
     * BIN is moved to the hot end of the LRU list.
     *
     * <p>More specifically, when an operation is not performed via a cursor,
     * the LN is evicted when the operation is complete. When a cursor is used,
     * the LN is evicted when the cursor is moved to a different record or is
     * closed. The BIN is moved to the hot end of the LRU in either case.</p>
     *
     * <p>This cache mode is normally used when not all LNs will fit into the
     * JE cache, and the application prefers to read the LN from the log file
     * when the record is accessed again, rather than have it take up space in
     * the JE cache and potentially cause expensive Java GC. By using this
     * mode, the file system cache can be relied on for holding LNs, which
     * complements the use of the JE cache to hold BINs and INs.</p>
     *
     * <p>Note that using this mode for all operations will prevent the cache
     * from filling, if all internal nodes fit in cache.</p>
     *
     * @since 3.3.98
     */
    EVICT_LN,

    /**
     * The record's BIN (and its LNs) are evicted after the operation.
     *
     * <p>More specifically:
     * <ul>
     *     <li>The record's LN will be evicted after the operation. When the
     *     operation is not performed via a cursor, the LN is evicted when the
     *     operation is complete. When a cursor is used, the LN is evicted when
     *     the cursor is moved to a different record or closed.</li>
     *
     *     <li>If the record's BIN is not dirty, then the BIN (and its LNs) will
     *     be evicted after the operation. When the operation is not performed
     *     via a cursor, the BIN is evicted when the operation is complete.
     *     When a cursor is used, the BIN is evicted only when the cursor moves
     *     to a different BIN or is closed. Because of the way BINs are
     *     evicted, when multiple operations are performed using a single
     *     cursor and eviction of the BIN is desired, it is important to use
     *     this cache mode for all of the operations.</li>
     *
     *     <li>If the BIN is dirty, then the BIN will not be evicted after the
     *     operation and will be moved to the hot end of the LRU list. In
     *     addition, all LNs in this BIN will be evicted after the
     *     operation. The BIN may be have been dirtied by this operation, if it
     *     is a write operation, or by earlier write operations.</p>
     * </ul>
     *
     * <p>This cache mode is normally used when not all BINs will fit into the
     * JE cache, and the application prefers to read the LN and BIN from the
     * log file when the record is accessed again, rather than have them take
     * up space in the JE cache and potentially cause expensive Java GC.</p>
     *
     * <p>Because this mode evicts all LNs in the BIN, even if they are "hot"
     * from the perspective of a different accessor, this mode should be used
     * with caution. One valid use case is where all accessors use this mode;
     * in this case the cache mode might be set on a per-Database or
     * per-Environment basis.</p>
     *
     * <p>Note that using this mode for all operations will prevent the cache
     * from filling, if all upper internal nodes fit in cache.</p>
     *
     * @since 4.0.97
     */
    EVICT_BIN,

    /**
     * @hidden
     * For internal use only.
     * Placeholder to avoid DPL class evolution errors. Never actually used.
     * @since 4.0.97
     */
    DYNAMIC
}
