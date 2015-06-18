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

package com.sleepycat.je.util;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.math.BigInteger;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import com.sleepycat.je.CacheMode;
import com.sleepycat.je.CheckpointConfig;
import com.sleepycat.je.Cursor;
import com.sleepycat.je.Database;
import com.sleepycat.je.DatabaseConfig;
import com.sleepycat.je.DatabaseEntry;
import com.sleepycat.je.DbInternal;
import com.sleepycat.je.Environment;
import com.sleepycat.je.EnvironmentConfig;
import com.sleepycat.je.EnvironmentStats;
import com.sleepycat.je.LockMode;
import com.sleepycat.je.OperationStatus;
import com.sleepycat.je.PreloadConfig;
import com.sleepycat.je.PreloadStats;
import com.sleepycat.je.PreloadStatus;
import com.sleepycat.je.Transaction;
import com.sleepycat.je.tree.BIN;
import com.sleepycat.je.tree.ChildReference;
import com.sleepycat.je.tree.IN;
import com.sleepycat.je.utilint.DbCacheSizeRepEnv;
import com.sleepycat.je.utilint.CmdUtil;
import com.sleepycat.util.RuntimeExceptionWrapper;

/**
 * Estimates the in-memory cache size needed to hold a specified data set.  To
 * get an estimate of the in-memory footprint for a given database, specify the
 * number of records and database characteristics and DbCacheSize will return
 * an estimate of the cache size required for holding the database in memory.
 * Based on this information a JE cache size can be chosen and then configured
 * using {@link EnvironmentConfig#setCacheSize} or using the
 * {@link EnvironmentConfig#MAX_MEMORY} property.
 *
 * <h4>Importance of the JE Cache</h4>
 *
 * The JE cache is not an optional cache. It is used to hold the metadata for
 * accessing JE data.  In fact the JE cache size is probably the most critical
 * factor to JE performance, since Btree nodes will have to be fetched during a
 * database read or write operation if they are not in cache. During a single
 * read or write operation, at each level of the Btree that a fetch is
 * necessary, an IO may be necessary at a different disk location for each
 * fetch.  In addition, if internal nodes (INs) are not in cache, then write
 * operations will cause additional copies of the INs to be written to storage,
 * as modified INs are moved out of the cache to make room for other parts of
 * the Btree during subsequent operations.  This additional fetching and
 * writing means that sizing the cache too small to hold the INs will result in
 * lower operation performance.
 * <p>
 * For best performance, all Btree nodes should fit in the JE cache, including
 * leaf nodes (LNs), which hold the record data, and INs, which hold record
 * keys and other metadata.  However, because system memory is limited, it is
 * sometimes necessary to size the cache to hold all or at least most INs, but
 * not the LNs.  This utility estimates the size necessary to hold only INs,
 * and the size to hold INs and LNs.
 * <p>
 * When most or all LNs do <em>not</em> fit in cache, using {@link
 * CacheMode#EVICT_LN} can be beneficial to reduce the Java GC cost of
 * collecting the LNs as they are moved out of cache.  A recommended approach
 * is to size the JE cache to hold all INs and size the Java heap to hold that
 * amount plus the amount needed for GC working space and application objects,
 * leaving any additional memory for use by the file system cache to hold LNs.
 * Tests show this approach results in low GC overhead and predictable latency.
 *
 * <h4>Estimating the JE Cache Size</h4>
 *
 * Estimating JE in-memory sizes is not straightforward for several reasons.
 * There is some fixed overhead for each Btree internal node, so fanout
 * (maximum number of child entries per parent node) and degree of node
 * sparseness impacts memory consumption. In addition, JE uses various compact
 * in-memory representations that depend on key sizes, key prefixing, how many
 * child nodes are resident, etc.  The physical proximity of node children also
 * allows compaction of child physical address values.
 * <p>
 * Therefore, when running this utility it is important to specify all {@link
 * EnvironmentConfig} and {@link DatabaseConfig} settings that will be used in
 * a production system.  The {@link EnvironmentConfig} settings are specified
 * by command line options for each property, using the same names as the
 * {@link EnvironmentConfig} parameter name values.  For example, {@link
 * EnvironmentConfig#LOG_FILE_MAX}, which influences the amount of memory used
 * to store physical record addresses, can be specified on the command line as:
 * <p>
 * {@code -je.log.fileMax LENGTH}
 * <p>
 * To be sure that this utility takes into account all relevant settings,
 * especially as the utility is enhanced in future versions, it is best to
 * specify all {@link EnvironmentConfig} settings used by the application.
 * <p>
 * The {@link DatabaseConfig} settings are specified using command line options
 * defined by this utility.
 * <ul>
 *   <li>{@code -nodemax ENTRIES} corresponds to {@link
 *   DatabaseConfig#setNodeMaxEntries}.</li>
 *   <li>{@code -duplicates} corresponds to passing true to {@link
 *   DatabaseConfig#setSortedDuplicates}.  Note that duplicates are configured
 *   for DPL MANY_TO_ONE and MANY_TO_MANY secondary indices.</li>
 *   <li>{@code -keyprefix LENGTH} corresponds to passing true {@link
 *   DatabaseConfig#setKeyPrefixing}.  Note that key prefixing is always used
 *   when duplicates are configured.</li>
 * </ul>
 * <p>
 * This utility estimates the JE cache size by creating an in-memory
 * Environment and Database.  In addition to the size of the Database, the
 * minimum overhead for the Environment is output.  The Environment overhead
 * shown is likely to be smaller than actually needed because it doesn't take
 * into account use of memory by JE daemon threads (cleaner, checkpointer, etc)
 * or the memory used for locks that are held by application operations and
 * transactions.  An additional amount should be added to account for these
 * factors.
 * <p>
 * This utility estimates the cache size for a single JE Database.  To estimate
 * the size for multiple Databases with different configuration parameters or
 * different key and data sizes, run this utility for each Database and sum the
 * sizes.  If you are summing multiple runs for multiple Databases that are
 * opened in a single Environment, the overhead size for the Environment should
 * only be added once.
 * <p>
 * In some applications with databases having variable key and data sizes, it
 * may be difficult to determine the key and data size input parameters for
 * this utility.  If a representative data set can be created, one approach is
 * to use the {@link DbPrintLog} utility with the {@code -S} option to find the
 * average key and data size for all databases, and use these values as input
 * parameters, as if there were only a single database.  With this approach, it
 * is important that the {@code DatabaseConfig} parameters are the same, or at
 * least similar, for all databases.
 *
 * <h4>Key Prefixing and Compaction</h4>
 *
 * Key prefixing deserves special consideration.  It can significantly reduce
 * the size of the cache and is generally recommended; however, the benefit can
 * be difficult to predict.  Key prefixing, in turn, impacts the benefits of
 * key compaction, and the use of the {@link
 * EnvironmentConfig#TREE_COMPACT_MAX_KEY_LENGTH} parameter.
 * <p>
 * For a given data set, the impact of key prefixing is determined by how many
 * leading bytes are in common for the keys in a single bottom internal node
 * (BIN).  For example, if keys are assigned sequentially as long (8 byte)
 * integers, and the {@link DatabaseConfig#setNodeMaxEntries maximum entries
 * per node} is 128 (the default value) then 6 or 7 of the 8 bytes of the key
 * will have a common prefix in each BIN.  Of course, when records are deleted,
 * the number of prefixed bytes may be reduced because the range of key values
 * in a BIN will be larger.  For this example we will assume that, on average,
 * 5 bytes in each BIN are a common prefix leaving 3 bytes per key that are
 * unprefixed.
 * <p>
 * Key compaction is applied when the number of unprefixed bytes is less than a
 * configured value; see {@link EnvironmentConfig#TREE_COMPACT_MAX_KEY_LENGTH}.
 * In the example, the 3 unprefixed bytes per key is less than the default used
 * for key compaction (16 bytes).  This means that each key will use 16 bytes
 * of memory, in addition to the amount used for the prefix for each BIN.  The
 * per-key overhead could be reduced by changing the {@code
 * TREE_COMPACT_MAX_KEY_LENGTH} parameter to a smaller value, but care should
 * be taken to ensure the compaction will be effective as keys are inserted and
 * deleted over time.
 * <p>
 * Because key prefixing depends so much on the application key format and the
 * way keys are assigned, the number of expected prefix bytes must be estimated
 * by the user and specified to DbCacheSize using the {@code -keyprefix}
 * argument.
 *
 * <h4>Key Prefixing and Duplicates</h4>
 * 
 * When {@link DatabaseConfig#setSortedDuplicates duplicates} are configured
 * for a Database (including DPL MANY_TO_ONE and MANY_TO_MANY secondary
 * indices), key prefixing is always used.  This is because the internal key in
 * a duplicates database BIN is formed by concatenating the user-specified key
 * and data.  In secondary databases with duplicates configured, the data is
 * the primary key, so the internal key is the concatenation of the secondary
 * key and the primary key.
 * <p>
 * Key prefixing is always used for duplicates databases because prefixing is
 * necessary to store keys efficiently.  When the number of duplicates per
 * unique user-specified key is more than the number of entries per BIN, the
 * entire user-specified key will be the common prefix.
 * <p>
 * For example, a database that stores user information may use email address
 * as the primary key and zip code as a secondary key.  The secondary index
 * database will be a duplicates database, and the internal key stored in the
 * BINs will be a two part key containing zip code followed by email address.
 * If on average there are more users per zip code than the number of entries
 * in a BIN, then the key prefix will normally be at least as long as the zip
 * code key.  If there are less (more than one zip code appears in each BIN),
 * then the prefix will be shorter than the zip code key.
 * <p>
 * It is also possible for the key prefix to be larger than the secondary key.
 * If for one secondary key value (one zip code) there are a large number of
 * primary keys (email addresses), then a single BIN may contain concatenated
 * keys that all have the same secondary key (same zip code) and have primary
 * keys (email addresses) that all have some number of prefix bytes in common.
 * Therefore, when duplicates are specified it is possible to specify a prefix
 * size that is larger than the key size.
 *
 * <h4>Record Versions and Oracle NoSQL Database</h4>
 *
 * This note applies only to when JE is used with Oracle NoSQL DB.  In Oracle
 * NoSQL DB, an internal JE environment configuration parameter is always
 * used: {@code -je.rep.preserveRecordVersion true}.  This allows using record
 * versions in operations such as "put if version", "delete if version", etc.
 * This feature performs best when the cache is sized large enough to hold the
 * record versions.
 * <p>
 * When using JE with Oracle NoSQL DB, always add {@code
 * -je.rep.preserveRecordVersion true} to the command line.  This ensures that
 * the cache sizes calculated are correct, and also outputs an additional line
 * showing how much memory is required to hold the internal nodes and record
 * versions (but not the leaf nodes).  This is the minimum recommended size
 * when the "... if version" operations are used.
 *
 * <h4>Running the DbCacheSize utility</h4>
 *
 * Usage:
 * <pre>
 * java { com.sleepycat.je.util.DbCacheSize |
 *        -jar je-&lt;version&gt;.jar DbCacheSize }
 *  -records <count>
 *      # Total records (key/data pairs); required
 *  -key <bytes>
 *      # Average key bytes per record; required
 *  [-data <bytes>]
 *      # Average data bytes per record; if omitted no leaf
 *      # node sizes are included in the output; required with
 *      # -duplicates, and specifies the primary key length
 *  [-keyprefix <bytes>]
 *      # Expected size of the prefix for the keys in each
 *      # BIN; default: key prefixing is not configured;
 *      # required with -duplicates
 *  [-nodemax <entries>]
 *      # Number of entries per Btree node; default: 128
 *  [-orderedinsertion]
 *      # Assume ordered insertions and no deletions, so BINs
 *      # are 100% full; default: unordered insertions and/or
 *      # deletions, BINs are 70% full
 *  [-duplicates]
 *      # Indicates that sorted duplicates are used, including
 *      # MANY_TO_ONE and MANY_TO_MANY secondary indices
 *  [-replicated]
 *      # Use a ReplicatedEnvironment; default: false
 *  [-ENV_PARAM_NAME VALUE]...
 *      # Any number of EnvironmentConfig parameters and
 *      # ReplicationConfig parameters (if -replicated)
 *  [-btreeinfo]
 *      # Outputs additional Btree information
 *  [-outputproperties]
 *      # Writes Java properties file to System.out:
 *      #  overhead, internalNodes, internalNodesAndVersions,
 *      #  allNodes (with -data);
 *      #  The following are also output but deprecated:
 *      #  minInternalNodes, maxInternalNodes,
 *      #  minAllNodes, maxAllNodes (with -data)
 * </pre>
 * <p>
 * You should run DbCacheSize on the same target platform and JVM for which you
 * are sizing the cache, as cache sizes will vary.  You may also need to
 * specify -d32 or -d64 depending on your target, if the default JVM mode is
 * not the same as the mode to be used in production.
 * <p>
 * To take full advantage of JE cache memory, it is strongly recommended that
 * <a href="http://download.oracle.com/javase/7/docs/technotes/guides/vm/performance-enhancements-7.html#compressedOop">compressed oops</a>
 * (<code>-XX:+UseCompressedOops</code>) is specified when a 64-bit JVM is used
 * and the maximum heap size is less than 32 GB.  As described in the
 * referenced documentation, compressed oops is sometimes the default JVM mode
 * even when it is not explicitly specified in the Java command.  However, if
 * compressed oops is desired then it <em>must</em> be explicitly specified in
 * the Java command when running DbCacheSize or a JE application.  If it is not
 * explicitly specified then JE will not aware of it, even if it is the JVM
 * default setting, and will not take it into account when calculating cache
 * memory sizes.
 * <p>
 * For example:
 * <pre>
 * $ java -jar je-X.Y.Z.jar DbCacheSize -records 554719 -key 16 -data 100
 *
 *  === Environment Cache Overhead ===
 *
 *  3,157,117 minimum bytes
 *
 *  To account for JE daemon operation and record locks,
 *  a significantly larger amount is needed in practice.
 *
 *  === Database Cache Size ===
 *
 *  Number of Bytes  Description
 *  ---------------  -----------
 *       23,747,224  Internal nodes only
 *      107,020,104  Internal nodes and leaf nodes
 * </pre>
 * <p>
 * This indicates that the minimum memory size to hold only the internal nodes
 * of the Database Btree is approximately 23MB. The maximum size to hold the
 * entire database, both internal nodes and data records, is approximately
 * 107MB.  To either of these amounts, at least 3MB (plus more for locks and
 * daemons) should be added to account for the environment overhead.
 */
public class DbCacheSize {

    /*
     * Undocumented command line options, used for comparing calculated to
     * actual cache sizes during testing.
     *
     *  [-measure]
     *      # Causes main program to write a database to find
     *      # the actual cache size; default: do not measure;
     *      # without -data, measures internal nodes only
     *
     * Only use -measure without -orderedinsertion when record count is 100k or
     * less, to avoid endless attempts to find an unused key value via random
     * number generation.  Also note that measured amounts will be slightly
     * less than calculated amounts because the number of prefix bytes is
     * larger for smaller key values, which are sequential integers from zero
     * to max records minus one.
     */

    private static final NumberFormat INT_FORMAT =
        NumberFormat.getIntegerInstance();

    private static final String MAIN_HEADER =
        "   Number of Bytes  Description\n" +
        "   ---------------  -----------";
    //   123456789012345678
    //                     12
    private static final int MIN_COLUMN_WIDTH = 18;
    private static final String COLUMN_SEPARATOR = "  ";

    /* IN density for non-ordered insertion. */
    private static final int DEFAULT_DENSITY = 70;
    /* IN density for ordered insertion. */
    private static final int ORDERED_DENSITY = 100;

    /* Parameters. */
    private final EnvironmentConfig envConfig = new EnvironmentConfig();
    private final Map<String, String> repParams =
        new HashMap<String, String>();
    private long records = 0;
    private int keySize = 0;
    private int dataSize = -1;
    private int nodeMaxEntries = 128;
    private int binMaxEntries = -1;
    private int keyPrefix = 0;
    private boolean orderedInsertion = false;
    private boolean duplicates = false;
    private boolean replicated = false;
    private boolean outputProperties = false;
    private boolean doMeasure = false;
    private boolean btreeInfo = false;

    /* Calculated values. */
    private long envOverhead;
    private long uinSize;
    private long binSizeNoLNsOrVLSNs;
    private long binSizeNoLNsWithVLSNs;
    private long binSizeWithLNsAndVLSNs;
    private long btreeSizeUINsOnly;
    private long btreeSizeNoLNsOrVLSNs;
    private long btreeSizeNoLNsWithVLSNs;
    private long btreeSizeWithLNsAndVLSNs;
    private long measuredBtreeSizeNoLNsOrVLSNs;
    private long measuredBtreeSizeNoLNsWithVLSNs;
    private long measuredBtreeSizeWithLNsAndVLSNs;
    private long preloadBtreeSizeNoLNsOrVLSNs;
    private long preloadBtreeSizeNoLNsWithVLSNs;
    private long preloadBtreeSizeWithLNsAndVLSNs;
    private int btreeLevels;
    private long nBinNodes;
    private long nUinNodes;

    private File tempDir;

    DbCacheSize() {
    }

    void parseArgs(String[] args) {
        for (int i = 0; i < args.length; i += 1) {
            String name = args[i];
            String val = null;
            if (i < args.length - 1 && !args[i + 1].startsWith("-")) {
                i += 1;
                val = args[i];
            }
            if (name.equals("-records")) {
                if (val == null) {
                    usage("No value after -records");
                }
                try {
                    records = Long.parseLong(val);
                } catch (NumberFormatException e) {
                    usage(val + " is not a number");
                }
                if (records <= 0) {
                    usage(val + " is not a positive integer");
                }
            } else if (name.equals("-key")) {
                if (val == null) {
                    usage("No value after -key");
                }
                try {
                    keySize = Integer.parseInt(val);
                } catch (NumberFormatException e) {
                    usage(val + " is not a number");
                }
                if (keySize <= 0) {
                    usage(val + " is not a positive integer");
                }
            } else if (name.equals("-data")) {
                if (val == null) {
                    usage("No value after -data");
                }
                try {
                    dataSize = Integer.parseInt(val);
                } catch (NumberFormatException e) {
                    usage(val + " is not a number");
                }
                if (dataSize < 0) {
                    usage(val + " is not a non-negative integer");
                }
            } else if (name.equals("-keyprefix")) {
                if (val == null) {
                    usage("No value after -keyprefix");
                }
                try {
                    keyPrefix = Integer.parseInt(val);
                } catch (NumberFormatException e) {
                    usage(val + " is not a number");
                }
                if (keyPrefix < 0) {
                    usage(val + " is not a non-negative integer");
                }
            } else if (name.equals("-orderedinsertion")) {
                if (val != null) {
                    usage("No value allowed after " + name);
                }
                orderedInsertion = true;
            } else if (name.equals("-duplicates")) {
                if (val != null) {
                    usage("No value allowed after " + name);
                }
                duplicates = true;
            } else if (name.equals("-replicated")) {
                if (val != null) {
                    usage("No value allowed after " + name);
                }
                replicated = true;
            } else if (name.equals("-nodemax")) {
                if (val == null) {
                    usage("No value after -nodemax");
                }
                try {
                    nodeMaxEntries = Integer.parseInt(val);
                } catch (NumberFormatException e) {
                    usage(val + " is not a number");
                }
                if (nodeMaxEntries <= 0) {
                    usage(val + " is not a positive integer");
                }
            } else if (name.equals("-binmax")) {
                if (val == null) {
                    usage("No value after -binmax");
                }
                try {
                    binMaxEntries = Integer.parseInt(val);
                } catch (NumberFormatException e) {
                    usage(val + " is not a number");
                }
                if (binMaxEntries <= 0) {
                    usage(val + " is not a positive integer");
                }
            } else if (name.equals("-density")) {
                usage
                    ("-density is no longer supported, see -orderedinsertion");
            } else if (name.equals("-overhead")) {
                usage("-overhead is no longer supported");
            } else if (name.startsWith("-je.")) {
                if (val == null) {
                    usage("No value after " + name);
                }
                if (name.startsWith("-je.rep.")) {
                    repParams.put(name.substring(1), val);
                } else {
                    envConfig.setConfigParam(name.substring(1), val);
                }
            } else if (name.equals("-measure")) {
                if (val != null) {
                    usage("No value allowed after " + name);
                }
                doMeasure = true;
            } else if (name.equals("-outputproperties")) {
                if (val != null) {
                    usage("No value allowed after " + name);
                }
                outputProperties = true;
            } else if (name.equals("-btreeinfo")) {
                if (val != null) {
                    usage("No value allowed after " + name);
                }
                btreeInfo = true;
            } else {
                usage("Unknown arg: " + name);
            }
        }

        if (records == 0) {
            usage("-records not specified");
        }
        if (keySize == 0) {
            usage("-key not specified");
        }
    }

    void cleanup() {
        if (tempDir != null) {
            emptyTempDir();
            tempDir.delete();
        }
    }

    long getBtreeSizeNoLNsOrVLSNs() {
        return btreeSizeNoLNsOrVLSNs;
    }

    long getBtreeSizeNoLNsWithVLSNs() {
        return btreeSizeNoLNsWithVLSNs;
    }

    long getBtreeSizeWithLNsAndVLSNs() {
        return btreeSizeWithLNsAndVLSNs;
    }

    long getMeasuredBtreeSizeNoLNsOrVLSNs() {
        return measuredBtreeSizeNoLNsOrVLSNs;
    }

    long getMeasuredBtreeSizeNoLNsWithVLSNs() {
        return measuredBtreeSizeNoLNsWithVLSNs;
    }

    long getMeasuredBtreeSizeWithLNsAndVLSNs() {
        return measuredBtreeSizeWithLNsAndVLSNs;
    }

    long getPreloadBtreeSizeNoLNsOrVLSNs() {
        return preloadBtreeSizeNoLNsOrVLSNs;
    }

    long getPreloadBtreeSizeNoLNsWithVLSNs() {
        return preloadBtreeSizeNoLNsWithVLSNs;
    }

    long getPreloadBtreeSizeWithLNsAndVLSNs() {
        return preloadBtreeSizeWithLNsAndVLSNs;
    }

    /**
     * Runs DbCacheSize as a command line utility.
     * For command usage, see {@link DbCacheSize class description}.
     */
    public static void main(final String[] args)
        throws Throwable {

        final DbCacheSize dbCacheSize = new DbCacheSize();
        try {
            dbCacheSize.parseArgs(args);
            dbCacheSize.calculateCacheSizes();
            if (dbCacheSize.outputProperties) {
                dbCacheSize.printProperties(System.out);
            } else {
                dbCacheSize.printCacheSizes(System.out);
            }
            if (dbCacheSize.doMeasure) {
                dbCacheSize.measure(System.out);
            }
        } finally {
            dbCacheSize.cleanup();
        }
    }

    /**
     * Prints usage and calls System.exit.
     */
    private static void usage(final String msg) {

        if (msg != null) {
            System.out.println(msg);
        }

        System.out.println
            ("usage:" +
             "\njava "  + CmdUtil.getJavaCommand(DbCacheSize.class) +
             "\n   -records <count>" +
             "\n      # Total records (key/data pairs); required" +
             "\n   -key <bytes> " +
             "\n      # Average key bytes per record; required" +
             "\n  [-data <bytes>]" +
             "\n      # Average data bytes per record; if omitted no leaf" +
             "\n      # node sizes are included in the output; required with" +
             "\n      # -duplicates, and specifies the primary key length" +
             "\n  [-keyprefix <bytes>]" +
             "\n      # Expected size of the prefix for the keys in each" +
             "\n      # BIN; default: zero, key prefixing is not configured;" +
             "\n      # required with -duplicates" +
             "\n  [-nodemax <entries>]" +
             "\n      # Number of entries per Btree node; default: 128" +
             "\n  [-orderedinsertion]" +
             "\n      # Assume ordered insertions and no deletions, so BINs" +
             "\n      # are 100% full; default: unordered insertions and/or" +
             "\n      # deletions, BINs are 70% full" +
             "\n  [-duplicates]" +
             "\n      # Indicates that sorted duplicates are used, including" +
             "\n      # MANY_TO_ONE and MANY_TO_MANY secondary indices;" +
             "\n      # default: false" +
             "\n  [-replicated]" +
             "\n      # Use a ReplicatedEnvironment; default: false" +
             "\n  [-ENV_PARAM_NAME VALUE]..." +
             "\n      # Any number of EnvironmentConfig parameters and" +
             "\n      # ReplicationConfig parameters (if -replicated)" +
             "\n  [-btreeinfo]" +
             "\n      # Outputs additional Btree information" +
             "\n  [-outputproperties]" +
             "\n      # Writes Java properties to System.out:" +
             "\n      #  overhead, internalNodes, internalNodesAndVersions," +
             "\n      #  allNodes (with -data); " +
             "\n      # Also output but deprecated:" +
             "\n      #  minInternalNodes, maxInternalNodes," +
             "\n      #  minAllNodes, maxAllNodes (with -data)");

        System.exit(2);
    }

    /**
     * Calculates estimated cache sizes.
     */
    void calculateCacheSizes() {

        if (binMaxEntries <= 0) {
            binMaxEntries = nodeMaxEntries;
        }

        final Environment env = openCalcEnvironment(true);
        boolean success = false;
        try {
            envOverhead = env.getStats(null).getCacheTotalBytes();

            final int density = orderedInsertion ?
                ORDERED_DENSITY :
                DEFAULT_DENSITY;
    
            final int nodeAvg = (nodeMaxEntries * density) / 100;
            final int binAvg = (binMaxEntries * density) / 100;
            nBinNodes = (records + binAvg - 1) / binAvg;

            calcTreeSizes(env, nodeAvg, binAvg);

            btreeLevels = 1;
            nUinNodes = 0;
            btreeSizeUINsOnly = 0;

            for (long nodes = nBinNodes / nodeAvg;; nodes /= nodeAvg) {

                if (nodes == 0) {
                    nodes = 1; // root
                }

                btreeLevels += 1;
                nUinNodes += nodes;
                btreeSizeUINsOnly += nodes * uinSize;

                if (nodes == 1) {
                    break;
                }
            }

            btreeSizeNoLNsOrVLSNs =
                (nBinNodes * binSizeNoLNsOrVLSNs) + btreeSizeUINsOnly;

            btreeSizeNoLNsWithVLSNs =
                (nBinNodes * binSizeNoLNsWithVLSNs) + btreeSizeUINsOnly;

            btreeSizeWithLNsAndVLSNs =
                (nBinNodes * binSizeWithLNsAndVLSNs) + btreeSizeUINsOnly;

            success = true;
        } finally {

            /*
             * Do not propagate exception thrown by Environment.close if
             * another exception is currently in flight.
             */
            try {
                env.close();
            } catch (RuntimeException e) {
                if (success) {
                    throw e;
                }
            }
        }
    }

    private void calcTreeSizes(final Environment env,
                               final int nodeAvg,
                               final int binAvg) {

        if (nodeMaxEntries != binMaxEntries) {
            throw new IllegalArgumentException
                ("-binmax not currently supported because a per-BIN max is" +
                 " not implemented in the Btree, so we can't measure" +
                 " an actual BIN node with the given -binmax value");
        }
        assert nodeAvg == binAvg;

        if (nodeAvg > 0xFFFF) {
            throw new IllegalArgumentException
                ("Entries per node (" + nodeAvg + ") is greater than 0xFFFF");
        }

        /*
         * Either a one or two byte key is used, depending on whether a single
         * byte can hold the key for nodeAvg entries.
         */
        final byte[] keyBytes = new byte[(nodeAvg <= 0xFF) ? 1 : 2];
        final DatabaseEntry keyEntry = new DatabaseEntry();
        final DatabaseEntry dataEntry = new DatabaseEntry();

        /* Insert nodeAvg records into a single BIN. */
        final Database db = openDatabase(env, true);
        for (int i = 0; i < nodeAvg; i += 1) {
            if (keyBytes.length == 1) {
                keyBytes[0] = (byte) i;
            } else {
                assert keyBytes.length == 2;
                keyBytes[0] = (byte) (i >> 8);
                keyBytes[1] = (byte) i;
            }
            setKeyData(keyBytes, keyPrefix, keyEntry, dataEntry);

            final OperationStatus status;
            if (duplicates) {
                status = db.putNoDupData(null, keyEntry, dataEntry);
            } else {
                status = db.putNoOverwrite(null, keyEntry, dataEntry);
            }
            if (status != OperationStatus.SUCCESS) {
                throw new IllegalStateException(status.toString());
            }
        }

        /* Position a cursor at the first record to get the BIN. */
        final Cursor cursor = db.openCursor(null, null);
        OperationStatus status = cursor.getFirst(keyEntry, dataEntry, null);
        assert status == OperationStatus.SUCCESS;
        final BIN bin = DbInternal.getCursorImpl(cursor).getBIN();
        cursor.close();
        bin.latch();

        /*
         * Calculate BIN size including LNs. The recalcKeyPrefix and
         * compactMemory methods are called to simulate normal operation.
         * Normally prefixes are recalculated when a IN is split, and
         * compactMemory is called after fetching a IN or evicting an LN.
         */
        bin.recalcKeyPrefix();
        bin.compactMemory();
        binSizeWithLNsAndVLSNs = bin.getInMemorySize();

        /*
         * Evict all LNs so we can calculate BIN size without LNs.  This is
         * simulated by calling partialEviction directly.
         */
        bin.partialEviction();

        /*
         * Another variant is when VLSNs are cached, since they are evicted
         * after the LNs in a separate step.  This is simulated by calling
         * partialEviction a second time.
         */
        binSizeNoLNsWithVLSNs = bin.getInMemorySize();

        if (!duplicates && DbInternal.getEnvironmentImpl(env).getCacheVLSN()) {
            assert bin.getVLSNCache().getMemorySize() > 0;
            bin.partialEviction();
        }

        /* With embedded LNs the VLSN cache is not discarded by eviction */
        if (bin.getEnv().getMaxEmbeddedLN() < 0) {
            assert bin.getVLSNCache().getMemorySize() == 0;
        }

        /* There are no LNs or VLSNs remaining. */
        binSizeNoLNsOrVLSNs = bin.getInMemorySize();

        /*
         * To calculate IN size, get parent/root IN and artificially fill the
         * slots with nodeAvg entries.
         */
        final IN in = DbInternal.getDatabaseImpl(db).
                                 getTree().
                                 getRootINLatchedExclusive(CacheMode.DEFAULT);
        assert bin == in.getTarget(0);

        for (int i = 1; i < nodeAvg; i += 1) {

            final int result = in.insertEntry1(
                bin, bin.getKey(i), null, bin.getLsn(i),
                false/*blindInsertion*/);

            assert (result & IN.INSERT_SUCCESS) != 0;
            assert i == (result & ~IN.INSERT_SUCCESS);
        }

        in.recalcKeyPrefix();
        in.compactMemory();
        in.releaseLatch();
        bin.releaseLatch();
        uinSize = in.getInMemorySize();

        db.close();
    }
    
    private void setKeyData(final byte[] keyBytes,
                            final int keyOffset,
                            final DatabaseEntry keyEntry,
                            final DatabaseEntry dataEntry) {
        final byte[] fullKey;
        if (duplicates) {
            fullKey = new byte[keySize + dataSize];
        } else {
            fullKey = new byte[keySize];
        }

        if (keyPrefix + keyBytes.length > fullKey.length) {
            throw new IllegalArgumentException
                ("Key doesn't fit, allowedLen=" + fullKey.length +
                 " keyLen=" + keyBytes.length + " prefixLen=" + keyPrefix);
        }

        System.arraycopy(keyBytes, 0, fullKey, keyOffset, keyBytes.length);

        final byte[] finalKey;
        final byte[] finalData;
        if (duplicates) {
            finalKey = new byte[keySize];
            finalData = new byte[dataSize];
            System.arraycopy(fullKey, 0, finalKey, 0, keySize);
            System.arraycopy(fullKey, keySize, finalData, 0, dataSize);
        } else {
            finalKey = fullKey;
            finalData = new byte[Math.max(0, dataSize)];
        }

        keyEntry.setData(finalKey);
        dataEntry.setData(finalData);
    }

    /**
     * Prints Java properties for information collected by calculateCacheSizes.
     * Min/max sizes are output for compatibility with earlier versions; in the
     * past, min and max were different values.
     */
    private void printProperties(final PrintStream out) {
        out.println("overhead=" + envOverhead);
        out.println("internalNodes=" + btreeSizeNoLNsOrVLSNs);
        out.println("internalNodesAndVersions=" +
                    btreeSizeNoLNsWithVLSNs);
        if (dataSize >= 0) {
            out.println("allNodes=" + btreeSizeWithLNsAndVLSNs);
        }
        out.println("# Following are deprecated");
        out.println("minInternalNodes=" + btreeSizeNoLNsOrVLSNs);
        out.println("maxInternalNodes=" + btreeSizeNoLNsOrVLSNs);
        if (dataSize >= 0) {
            out.println("minAllNodes=" + btreeSizeWithLNsAndVLSNs);
            out.println("maxAllNodes=" + btreeSizeWithLNsAndVLSNs);
        }
    }

    /**
     * Prints information collected by calculateCacheSizes.
     */
    void printCacheSizes(final PrintStream out) {

        out.println();
        out.println("=== Environment Cache Overhead ===");
        out.println();
        out.print(INT_FORMAT.format(envOverhead));
        out.println(" minimum bytes");
        out.println();
        out.println("To account for JE daemon operation and record locks,");
        out.println("a significantly larger amount is needed in practice.");
        out.println();
        out.println("=== Database Cache Size ===");
        out.println();
        out.println(MAIN_HEADER);
        out.println(line(btreeSizeNoLNsOrVLSNs, "Internal nodes only"));
        if (dataSize >= 0) {
            if (btreeSizeNoLNsWithVLSNs != btreeSizeNoLNsOrVLSNs) {
                out.println(line(
                    btreeSizeNoLNsWithVLSNs,
                    "Internal nodes and record versions"));
            }
            out.println(line(
                btreeSizeWithLNsAndVLSNs, "Internal nodes and leaf nodes"));

            if (duplicates) {
                out.println
                    ("\nNote that leaf nodes do not use additional memory");
                out.println("in a database configured for duplicates.");
            }
        } else {
            if (!duplicates) {
                out.println("\nTo get leaf node sizing specify -data");
            }
        }

        if (btreeInfo) {
            out.println();
            out.println("=== Calculated Btree Information ===");
            out.println();
            out.println(line(btreeLevels, "Btree levels"));
            out.println(line(nUinNodes, "Upper internal nodes"));
            out.println(line(nBinNodes, "Bottom internal nodes"));
            out.println(line(btreeSizeUINsOnly, "Upper internal bytes"));
        }

        out.println();
        out.println("For further information see the DbCacheSize javadoc.");
    }

    private String line(final long num, final String comment) {

        final StringBuilder buf = new StringBuilder(100);

        column(buf, INT_FORMAT.format(num));
        buf.append(COLUMN_SEPARATOR);
        buf.append(comment);

        return buf.toString();
    }

    private void column(final StringBuilder buf, final String str) {

        int start = buf.length();

        while (buf.length() - start + str.length() < MIN_COLUMN_WIDTH) {
            buf.append(' ');
        }

        buf.append(str);
    }

    /**
     * For testing, insert the specified data set and initialize
     * measuredBtreeSizeNoLNsWithVLSNs and measuredBtreeSizeWithLNsAndVLSNs.
     */
    void measure(final PrintStream out) {

        Environment env = openMeasureEnvironment(true);
        try {
            Database db = openDatabase(env, true);

            if (out != null) {
                out.println(
                    "Measuring with maximum cache size: " +
                    INT_FORMAT.format(env.getConfig().getCacheSize()));
            }

            insertRecords(out, env, db);

            measuredBtreeSizeWithLNsAndVLSNs = getStats(
                out, env, "Stats after insert");

            trimLNs(db);

            measuredBtreeSizeNoLNsWithVLSNs = getStats(
                out, env, "Stats after trimLNs");

            trimVLSNs(db);

            measuredBtreeSizeNoLNsOrVLSNs = getStats(
                out, env, "Stats after trimVLSNs");

            db.close();
            db = null;
            env.close();
            env = null;

            env = openMeasureEnvironment(false);
            db = openDatabase(env, false);

            PreloadStatus status = preloadRecords(out, db, false /*loadLNs*/);

            preloadBtreeSizeNoLNsOrVLSNs = getStats(
                out, env,
                "Stats for internal nodes only after preload (" +
                status + ")");

            status = preloadRecords(out, db, true /*loadLNs*/);

            preloadBtreeSizeWithLNsAndVLSNs = getStats(
                out, env,
                "Stats for all nodes after preload (" +
                status + ")");

            trimLNs(db);

            preloadBtreeSizeNoLNsWithVLSNs = getStats(
                out, env,
                "Stats for internal nodes plus VLSNs after preload (" +
                status + ")");

            db.close();
            db = null;
            env.close();
            env = null;
        } finally {

            /*
             * Do not propagate exception thrown by Environment.close if
             * another exception is currently in flight.
             */
            if (env != null) {
                try {
                    env.close();
                } catch (RuntimeException ignore) {
                }
            }
        }
    }

    private Environment openMeasureEnvironment(final boolean createNew) {
        final EnvironmentConfig config = envConfig.clone();
        config.setCachePercent(90);
        return openEnvironment(config, createNew);
    }

    private Environment openCalcEnvironment(final boolean createNew) {
        final EnvironmentConfig config = envConfig.clone();
        return openEnvironment(config, createNew);
    }

    private Environment openEnvironment(final EnvironmentConfig config,
                                        final boolean createNew) {
        mkTempDir();
        if (createNew) {
            emptyTempDir();
        }
        config.setTransactional(true);
        config.setAllowCreate(createNew);
        /* Cleaner and checkpointer interfere with cache size measurements. */
        config.setConfigParam(EnvironmentConfig.ENV_RUN_CLEANER, "false");
        config.setConfigParam(EnvironmentConfig.ENV_RUN_CHECKPOINTER, "false");

        final Environment newEnv;

        if (replicated) {
            try {
                final Class repEnvClass = Class.forName
                    ("com.sleepycat.je.rep.utilint.DbCacheSizeRepEnv");
                final DbCacheSizeRepEnv repEnv =
                    (DbCacheSizeRepEnv) repEnvClass.newInstance();
                newEnv = repEnv.open(tempDir, config, repParams);
            } catch (ClassNotFoundException e) {
                throw new IllegalStateException(e);
            } catch (InstantiationException e) {
                throw new IllegalStateException(e);
            } catch (IllegalAccessException e) {
                throw new IllegalStateException(e);
            }
        } else {
            if (!repParams.isEmpty()) {
                throw new IllegalArgumentException
                    ("Cannot set replication params in a standalone " +
                     "environment.  May add -replicated.");
            }
            newEnv = new Environment(tempDir, config);
        }

        /*
         * LSN compaction is typically effective (in a realistic data set) only
         * when the file size fits in 3 bytes and sequential keys are written.
         * Since a tiny data set is use for estimating, and a small data set
         * for testing, we disable the compact representation when it is
         * unlikely to be effective.
         */
        final long fileSize = Integer.parseInt(
            newEnv.getConfig().getConfigParam(EnvironmentConfig.LOG_FILE_MAX));

        if ((fileSize > IN.MAX_FILE_OFFSET) || !orderedInsertion) {
            IN.disableCompactLsns = true;
        }

        return newEnv;
    }

    private void mkTempDir() {
        if (tempDir == null) {
            try {
                tempDir = File.createTempFile("DbCacheSize", null);
            } catch (IOException e) {
                throw new IllegalStateException(e);
            }
            /* createTempFile creates a file, but we want a directory. */
            tempDir.delete();
            tempDir.mkdir();
        }
    }

    private void emptyTempDir() {
        if (tempDir == null) {
            return;
        }
        final File[] children = tempDir.listFiles();
        if (children != null) {
            for (File child : children) {
                child.delete();
            }
        }
    }

    private Database openDatabase(final Environment env,
                                  final boolean createNew) {
        final DatabaseConfig dbConfig = new DatabaseConfig();
        dbConfig.setTransactional(true);
        dbConfig.setAllowCreate(createNew);
        dbConfig.setExclusiveCreate(createNew);
        dbConfig.setNodeMaxEntries(nodeMaxEntries);
        dbConfig.setKeyPrefixing(keyPrefix > 0);
        dbConfig.setSortedDuplicates(duplicates);
        return env.openDatabase(null, "foo", dbConfig);
    }

    /**
     * Inserts records and ensures that no eviction occurs.  LNs (and VLSNs)
     * are left intact.
     */
    private void insertRecords(final PrintStream out,
                               final Environment env,
                               final Database db) {
        final DatabaseEntry keyEntry = new DatabaseEntry();
        final DatabaseEntry dataEntry = new DatabaseEntry();

        final int lastKey = (int) (records - 1);
        final byte[] lastKeyBytes = BigInteger.valueOf(lastKey).toByteArray();
        final int maxKeyBytes = lastKeyBytes.length;

        final int keyOffset;
        if (keyPrefix == 0) {
            keyOffset = 0;
        } else {

            /*
             * Calculate prefix length for generated keys and adjust key offset
             * to produce the desired prefix length.
             */
            final int nodeAvg = orderedInsertion ?
                nodeMaxEntries :
                ((nodeMaxEntries * DEFAULT_DENSITY) / 100);
            final int prevKey = lastKey - (nodeAvg * 2);
            final byte[] prevKeyBytes =
                padLeft(BigInteger.valueOf(prevKey).toByteArray(),
                        maxKeyBytes);
            int calcPrefix = 0;
            while (calcPrefix < lastKeyBytes.length &&
                   calcPrefix < prevKeyBytes.length &&
                   lastKeyBytes[calcPrefix] == prevKeyBytes[calcPrefix]) {
                calcPrefix += 1;
            }
            keyOffset = keyPrefix - calcPrefix;
        }

        /* Generate random keys. */
        List<Integer> rndKeys = null;
        if (!orderedInsertion) {
            rndKeys = new ArrayList<Integer>(lastKey + 1);
            for (int i = 0; i <= lastKey; i += 1) {
                rndKeys.add(i);
            }
            Collections.shuffle(rndKeys, new Random(123));
        }

        final Transaction txn = env.beginTransaction(null, null);
        final Cursor cursor = db.openCursor(txn, null);
        boolean success = false;
        try {
            for (int i = 0; i <= lastKey; i += 1) {
                final int keyVal = orderedInsertion ? i : rndKeys.get(i);
                final byte[] keyBytes = padLeft
                    (BigInteger.valueOf(keyVal).toByteArray(), maxKeyBytes);
                setKeyData(keyBytes, keyOffset, keyEntry, dataEntry);

                final OperationStatus status;
                if (duplicates) {
                    status = cursor.putNoDupData(keyEntry, dataEntry);
                } else {
                    status = cursor.putNoOverwrite(keyEntry, dataEntry);
                }
                if (status == OperationStatus.KEYEXIST && !orderedInsertion) {
                    i -= 1;
                    continue;
                }
                if (status != OperationStatus.SUCCESS) {
                    throw new IllegalStateException("Could not insert: " +
                                                    status);
                }

                if (i % 10000 == 0) {
                    checkForEviction(env, i);
                    if (out != null) {
                        out.print(".");
                        out.flush();
                    }
                }
            }
            success = true;
        } finally {
            cursor.close();
            if (success) {
                txn.commit();
            } else {
                txn.abort();
            }
        }

        checkForEviction(env, lastKey);

        /* Checkpoint to speed recovery and reset the memory budget. */
        env.checkpoint(new CheckpointConfig().setForce(true));

        /* Let's be sure the memory budget is updated. */
        iterateBINs(db, new BINVisitor() {
            @Override
            public void visitBIN(final BIN bin) {
                bin.updateMemoryBudget();
            }
        });
    }

    private void checkForEviction(Environment env, int recNum) {
        final EnvironmentStats stats = env.getStats(null);
        if (stats.getNNodesScanned() > 0) {
            throw new IllegalStateException
                ("*** Ran out of cache memory at record " + recNum +
                    " -- try increasing Java heap size ***");
        }
    }

    private void trimLNs(final Database db) {
        iterateBINs(db, new BINVisitor() {
            @Override
            public void visitBIN(final BIN bin) {
                bin.evictLNs();
                bin.updateMemoryBudget();
            }
        });
    }

    private void trimVLSNs(final Database db) {
        iterateBINs(db, new BINVisitor() {
            @Override
            public void visitBIN(final BIN bin) {
                bin.discardVLSNCache();
                bin.updateMemoryBudget();
            }
        });
    }

    private static interface BINVisitor {
        void visitBIN(BIN bin);
    }

    private void iterateBINs(final Database db, final BINVisitor visitor) {

        final DatabaseEntry key = new DatabaseEntry();
        final DatabaseEntry data = new DatabaseEntry();
        data.setPartial(0, 0, true);

        final Cursor c = db.openCursor(null, null);
        BIN prevBin = null;

        while (c.getNext(key, data, LockMode.READ_UNCOMMITTED) ==
               OperationStatus.SUCCESS) {

            final BIN bin = DbInternal.getCursorImpl(c).getBIN();

            if (bin == prevBin) {
                continue;
            }

            if (prevBin != null) {
                prevBin.latch();
                visitor.visitBIN(prevBin);
                prevBin.releaseLatch();
            }

            prevBin = bin;
        }

        c.close();

        if (prevBin != null) {
            prevBin.latch();
            visitor.visitBIN(prevBin);
            prevBin.releaseLatch();
        }
    }

    /**
     * Pads the given array with zeros on the left, and returns an array of
     * the given size.
     */
    private byte[] padLeft(byte[] data, int size) {
        assert data.length <= size;
        if (data.length == size) {
            return data;
        }
        final byte[] b = new byte[size];
        System.arraycopy(data, 0, b, size - data.length, data.length);
        return b;
    }

    /**
     * Preloads the database.
     */
    private static PreloadStatus preloadRecords(final PrintStream out,
                                                final Database db,
                                                final boolean loadLNs) {
        Thread thread = null;
        if (out != null) {
            thread = new Thread() {
                @Override
                public void run() {
                    while (true) {
                        try {
                            out.print(".");
                            out.flush();
                            Thread.sleep(5 * 1000);
                        } catch (InterruptedException e) {
                            break;
                        }
                    }
                }
            };
            thread.start();
        }
        final PreloadStats stats;
        try {
            stats = db.preload(new PreloadConfig().setLoadLNs(loadLNs));
        } finally {
            if (thread != null) {
                thread.interrupt();
            }
        }
        if (thread != null) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                throw new RuntimeExceptionWrapper(e);
            }
        }

        /* Checkpoint to reset the memory budget. */
        db.getEnvironment().checkpoint(new CheckpointConfig().setForce(true));

        return stats.getStatus();
    }

    /**
     * Returns the Btree size, and prints a few other stats for testing.
     */
    private static long getStats(final PrintStream out,
                                 final Environment env,
                                 final String msg) {
        if (out != null) {
            out.println();
            out.println(msg + ':');
        }

        final EnvironmentStats stats = env.getStats(null);
        final long btreeSize = DbInternal.getEnvironmentImpl(env).
            getMemoryBudget().getTreeMemoryUsage();

        if (out != null) {
            out.println
                ("CacheSize=" +
                 INT_FORMAT.format(stats.getCacheTotalBytes()) +
                 " BtreeSize=" + INT_FORMAT.format(btreeSize)  +
                 " BottomINs=" + INT_FORMAT.format(stats.getNCachedBINs()) +
                 " UpperINs=" +
                 INT_FORMAT.format(stats.getNCachedUpperINs()) +
                 " NCacheMiss=" + INT_FORMAT.format(stats.getNCacheMiss()));
        }

        if (stats.getNNodesScanned() > 0) {
            throw new IllegalStateException
                ("*** All records did not fit in the cache ***");
        }
        return btreeSize;
    }
}
