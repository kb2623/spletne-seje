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

package com.sleepycat.je.tree;

/*
 * The state byte holds knownDeleted state in bit 0 and dirty state in bit
 * 1. Bit flags are used here because of the desire to keep the child
 * reference compact. State is persistent because knownDeleted is
 * persistent, but the dirty bit is cleared when read in from the log.
 *
 * -- KnownDeleted is a way of indicating that the reference is invalid
 * without logging new data. This happens in aborts and recoveries. If
 * knownDeleted is true, this entry is surely deleted. If knownDeleted is
 * false, this entry may or may not be deleted. Future space optimizations:
 * store as a separate bit array in the BIN, or subclass ChildReference and
 * make a special reference only used by BINs and not by INs.
 *
 * -- Dirty is true if the LSN or key has been changed since the last time
 * the owning node was logged. This supports the calculation of BIN deltas.
 */
public class EntryStates {

    /*
     * Note that MIGRATE_BIT is no longer used but is reserved because it was
     * accidentally set on logged INs in the past.
     */
    static final byte KNOWN_DELETED_BIT = 0x1;
    static final byte CLEAR_KNOWN_DELETED_BIT = ~0x1;
    static final byte DIRTY_BIT = 0x2;
    static final byte CLEAR_DIRTY_BIT = ~0x2;
    static final byte MIGRATE_BIT = 0x4; // no longer used, see above
    static final byte CLEAR_MIGRATE_BIT = ~0x4;
    static final byte PENDING_DELETED_BIT = 0x8;
    static final byte CLEAR_PENDING_DELETED_BIT = ~0x8;
    static final byte EMBEDDED_LN_BIT = 0x10;
    static final byte CLEAR_EMBEDDED_LN_BIT = ~0x10;
    static final byte NO_DATA_LN_BIT = 0x20;
    static final byte CLEAR_NO_DATA_LN_BIT = ~0x20;
}
