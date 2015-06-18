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

import com.sleepycat.je.log.LogContext;

/**
 * Extends LogContext to add fields used by IN.beforeLog and afterLog methods.
 */
public class INLogContext extends LogContext {

    /**
     * Whether a BIN-delta may be logged.  A BIN-delta is logged rather than a
     * BIN if this field is true and other qualifications are met for a delta.
     * Used by BIN.beforeLog.
     *
     * Set by caller.
     */
    public boolean allowDeltas;

    /**
     * Whether a full BIN may be compressed before being logged. Some callers
     * cannot tolerate compression because they rely on the stability of
     * certain slots or slot indices.
     * Used by BIN.beforeLog.
     *
     * Set by caller.
     */
    public boolean allowCompress;
}
