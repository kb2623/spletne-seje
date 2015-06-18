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

package com.sleepycat.je.rep.impl.node;

import static com.sleepycat.je.rep.impl.RepParams.MIN_RETAINED_VLSNS;
import static com.sleepycat.je.rep.impl.RepParams.REP_STREAM_TIMEOUT;
import static com.sleepycat.je.utilint.VLSN.NULL_VLSN;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.sleepycat.je.rep.NodeType;
import com.sleepycat.je.rep.impl.RepGroupImpl;
import com.sleepycat.je.rep.impl.RepGroupImpl.BarrierState;
import com.sleepycat.je.rep.impl.RepImpl;
import com.sleepycat.je.rep.impl.RepNodeImpl;
import com.sleepycat.je.rep.vlsn.VLSNIndex;
import com.sleepycat.je.rep.vlsn.VLSNRange;
import com.sleepycat.je.utilint.LoggerUtils;
import com.sleepycat.je.utilint.VLSN;

/**
 * Represents this node's view of the global CBVLSN. Each node has its own view
 * of the global CBVLSN, based upon its local replicated copy of the rep group
 * db. There is a single instance of the GlobalCBVLSN and it exists for the
 * lifetime of the RepNode.
 *
 * A global CBVLSN is a per-environment value, and is safeguarded from
 * decreasing during the lifetime of a single RepImpl. Because nodes crash and
 * re-sync, and new nodes join, it's possible that the persisted local cbvlsns
 * can decrease, and drag the global cbvlsn value down, but those decreases
 * are ignored during the lifetime of the global CBVLSN instance.
 *
 * The global CBVLSN is used by:
 *
 * 1. The Cleaner
 * 2. The Feeder which only serves log records in the interval:
 *    [GlobalCBVLSN .. VLSNRange.last]
 * 3. The Replica which uses the interval [GlobalCBVLSN .. VLSNRange.last] at
 *    syncup time.
 */
class GlobalCBVLSN {

    private final RepImpl repImpl;
    private final Logger logger;
    private final long streamTimeoutMs;
    private final int minRetainedVLSNs;

    /*
     * ActiveSyncups serves to stop updates to the group CBVLSN when
     * replica/master syncups are in progress. GroupCBVLSN will not be updated
     * if activeSyncups > 0. ActiveSyncups and groupCBVLSN can only be updated
     * when synchronized.
     */
    private volatile VLSN groupCBVLSN = VLSN.NULL_VLSN;
    private int activeSyncups = 0;

    GlobalCBVLSN(RepNode repNode) {
        this.repImpl = repNode.getRepImpl();
        streamTimeoutMs =
            repImpl.getConfigManager().getDuration(REP_STREAM_TIMEOUT);
        minRetainedVLSNs =
            repImpl.getConfigManager().getInt(MIN_RETAINED_VLSNS);
        logger = LoggerUtils.getLogger(getClass());
    }

    VLSN getCBVLSN() {
        return groupCBVLSN;
    }

    /* ActiveSyncups gates the update of the global CBVLSN */
    synchronized void syncupStarted() {
        activeSyncups++;
        LoggerUtils.finest
            (logger, repImpl, "activeSyncups = " + activeSyncups);
    }

    /* ActiveSyncups gates the update of the global CBVLSN */
    synchronized void syncupEnded() {
        activeSyncups--;
        LoggerUtils.finest
            (logger, repImpl, "activeSyncups = " + activeSyncups);
    }

    /**
     * Return the current number of active syncups.
     *
     * @return the current number of active syncups
     */
    synchronized int getActiveSyncups() {
        return activeSyncups;
    }

    /**
     * Recalculate the cached, global CBVLSN. The globalCBVLSN is computed as
     * the minimum of CBVLSNs after discarding CBVLSNs that are obsolete. A
     * CBVLSN is considered obsolete, if it has not been updated within a
     * configurable time interval relative to the time that the most recent
     * CBVLSN was updated.
     *
     * Note that the read of GroupInfo is not protected, and that groupInfo
     * could be changing. That's okay, because we guarantee that none of the
     * local CBVLSNs can be set to be LT globalCBVLSN. If a local CBVLSN is
     * written, and we miss it, it only means that this recalcuation of global
     * CBVLSN is too pessimistic -- it's too low.
     *
     * The low range of the VLSNIndex may be LTE the GlobalCBVLSN. The
     * VLSNIndex is only truncated when a log file is actually deleted. This
     * separates the cost of the Global CBVLSN recalculation from the cost of
     * the VLSNIndex truncation, sinc the latter may require I/O. This also
     * makes it easier and cheaper to obey the rule that the VLSNIndex
     * truncation only occur on bucket boundaries.
     */
    void recalculate(RepGroupImpl groupInfo) {

        /* Find the time the highest CBVLSN was computed. */
        VLSN maxCBVLSN = NULL_VLSN;
        long latestBarrierTime = 0;
        for (RepNodeImpl node : groupInfo.getElectableMembers()) {

            BarrierState nodeBarrier = node.getBarrierState();
            VLSN cbvlsn = nodeBarrier.getLastCBVLSN();

            /*
             * Count all nodes, including those that are in the middle of
             * syncup and have not established their low point when finding the
             * max time.
             */
            final long nodeBarrierTime = nodeBarrier.getBarrierTime();

            if (maxCBVLSN.compareTo(cbvlsn) <= 0) {

                /*
                 * Use min, since it represents the real change when they are
                 * equal.
                 */
                latestBarrierTime = cbvlsn.equals(maxCBVLSN) ?
                    Math.min(nodeBarrierTime, latestBarrierTime) :
                    nodeBarrierTime;
                maxCBVLSN = cbvlsn;
            }
        }

        if (latestBarrierTime == 0) {
            /* No cbvlsns entered yet, don't bother to recalculate. */
            return;
        }

        if (maxCBVLSN.isNull()) {
            /* No cbvlsns entered yet, don't bother to recalculate. */
            return;
        }

        /*
         * Now find the min CBVLSN that has not been timed out. This may mean
         * that the min CBVLSN == NULL_VLSN, for nodes that have not yet
         * finished syncup.
         */
        VLSN newGroupCBVLSN = maxCBVLSN;
        for (RepNodeImpl node : groupInfo.getElectableMembers()) {

            if (node.getType().isArbiter()) {

                /*
                 * skip arbiters
                 */
                continue;
            }

            BarrierState nodeBarrier = node.getBarrierState();
            VLSN nodeCBVLSN = nodeBarrier.getLastCBVLSN();

            if (((latestBarrierTime - nodeBarrier.getBarrierTime()) <=
                 streamTimeoutMs) &&
                (newGroupCBVLSN.compareTo(nodeCBVLSN) > 0)) {
                newGroupCBVLSN = nodeCBVLSN;
            }
        }

        /*
         * Adjust to retain min number of VLSNs, while ensuring we stay within
         * the current VLSN range.
         */
        newGroupCBVLSN =
            new VLSN(newGroupCBVLSN.getSequence() - minRetainedVLSNs);

        final VLSNIndex vlsnIndex = repImpl.getVLSNIndex();
        final VLSN rangeFirst = (vlsnIndex != null) ?
            vlsnIndex.getRange().getFirst() : VLSN.FIRST_VLSN;

        /*
         * Environments where the minRetainedVLSNs was expanded need to ensure
         * the global cbvlsn still stays within the vlsn range.
         */
        if (rangeFirst.compareTo(newGroupCBVLSN) > 0) {
            newGroupCBVLSN = rangeFirst;
        }

        updateGroupCBVLSN(groupInfo, newGroupCBVLSN);
    }

    /*
     * Update the group CBVLSN, but only if the newGroupCBVLSN is more recent
     * This is to ensure that the group CBVLSN can only advance during the
     * lifetime of this instance.
     */
    private void updateGroupCBVLSN(RepGroupImpl groupInfo,
                                   VLSN newGroupCBVLSN) {
        boolean changed = false;
        int numGatingSyncups = 0;
        String cbvlsnLoweredMessage = null;
        VLSN oldCBVLSN = VLSN.NULL_VLSN;

        synchronized(this) {

            /*
             * Be sure not to do anything expensive in this synchronized
             * section, such as logging.
             */
            if (newGroupCBVLSN.compareTo(groupCBVLSN) > 0) {
                if (activeSyncups == 0) {
                    VLSNRange currentRange = repImpl.getVLSNIndex().getRange();
                    if (!currentRange.contains(newGroupCBVLSN) &&
                        logger.isLoggable(Level.FINE)) {
                        cbvlsnLoweredMessage =
                            "GroupCBVLSN: " + newGroupCBVLSN +
                            " is outside VLSN range: " + currentRange +
                            " Current group:" + groupInfo;
                    } else {
                        oldCBVLSN = groupCBVLSN;
                        groupCBVLSN = newGroupCBVLSN;
                        changed = true;
                    }
                } else {

                    /*
                     * Any active syncups prohibit the global cbvlsn update.
                     * Save this fact for logging outside the critical section.
                     */
                    numGatingSyncups = activeSyncups;
                }
            }
        }

        if (logger.isLoggable(Level.FINE)) {
            if (cbvlsnLoweredMessage != null) {
                LoggerUtils.fine(logger, repImpl, cbvlsnLoweredMessage);
            }

            if (changed) {
                LoggerUtils.fine(logger, repImpl,
                                 "Global CBVLSN changed from " + oldCBVLSN +
                                 " to " + newGroupCBVLSN);
            }

            if (numGatingSyncups > 0) {
                LoggerUtils.fine(logger, repImpl,
                                 "Global CBVLSN update gated by " +
                                 numGatingSyncups + " syncups");
            }
        }
    }
}
