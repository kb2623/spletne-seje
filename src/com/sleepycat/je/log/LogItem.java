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

package com.sleepycat.je.log;

import java.nio.ByteBuffer;

import com.sleepycat.je.log.entry.LogEntry;
import com.sleepycat.je.utilint.DbLsn;

/**
 * Item parameters that apply to a single logged item.  Passed to LogManager
 * log methods and to beforeLog and afterLog methods.
 */
public class LogItem {

    /**
     * Object to be marshaled and logged.
     *
     * Set by caller or beforeLog method.
     */
    public LogEntry entry = null;

    /**
     * The previous version of the node to be counted as obsolete, or NULL_LSN
     * if the entry is not a node or has no old LSN.
     *
     * Set by caller or beforeLog method.
     */
    public long oldLsn = DbLsn.NULL_LSN;

    /**
     * For LNs, oldSize should be set along with oldLsn before logging. It
     * should normally be obtained by calling BIN.getLastLoggedSize.
     */
    public int oldSize = 0;

    /**
     * Another LSN to be counted as obsolete in the LogContext.nodeDb database,
     * or NULL_LSN.  Used for obsolete BIN-deltas.
     *
     * Set by caller or beforeLog method.
     */
    public long auxOldLsn = DbLsn.NULL_LSN;

    /**
     * LSN of the new log entry.  Is NULL_LSN if a BIN-delta is logged.  If
     * not NULL_LSN for a tree node, is typically used to update the slot in
     * the parent IN.
     *
     * Set by log or afterLog method.
     */
    public long newLsn = DbLsn.NULL_LSN;

    /**
     * Size of the new log entry.  Is used to update the LN slot in the BIN.
     *
     * Set by log or afterLog method.
     */
    public int newSize = 0;

    /**
     * Whether the logged entry should be processed during recovery.
     *
     * Set by caller or beforeLog method.
     */
    public Provisional provisional = null;

    /**
     * Whether the logged entry should be replicated.
     *
     * Set by caller or beforeLog method.
     */
    public ReplicationContext repContext = null;

    /* Fields used internally by log method. */
    public LogEntryHeader header = null;
    protected ByteBuffer buffer = null;

    final public LogEntryHeader getHeader() {
        return header;
    }

    public final ByteBuffer getBuffer() {
        return buffer;
    }

    public final long getNewLsn() {
        return newLsn;
    }
}
