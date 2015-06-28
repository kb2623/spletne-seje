/**
 * SqlJetIndexScopeCursor.java
 * Copyright (C) 2009-2013 TMate Software Ltd
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; version 2 of the License.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * For information on how to redistribute this software under
 * the terms of a license other than GNU General Public License
 * contact TMate Software at support@sqljet.com
 */
package org.tmatesoft.sqljet.core.internal.table;

import org.tmatesoft.sqljet.core.SqlJetException;
import org.tmatesoft.sqljet.core.internal.SqlJetUtility;
import org.tmatesoft.sqljet.core.table.SqlJetDb;
import org.tmatesoft.sqljet.core.table.SqlJetScope;

/**
 * @author TMate Software Ltd.
 * @author Sergey Scherbina (sergey.scherbina@gmail.com)
 *
 */
public class SqlJetIndexScopeCursor extends SqlJetIndexOrderCursor {

    private Object[] firstKey;
    private Object[] lastKey;
    private long firstRowId;
    private long lastRowId;
    private boolean firstKeyIncluded;
    private boolean lastKeyIncluded;

    /**
     * @param table
     * @param db
     * @param indexName
     * @param firstKey
     * @param lastKey
     * @throws SqlJetException
     */
    public SqlJetIndexScopeCursor(ISqlJetBtreeDataTable table, SqlJetDb db, String indexName, Object[] firstKey, Object[] lastKey) throws SqlJetException {
        this(table, db, indexName, new SqlJetScope(firstKey, lastKey));
    }

    /**
     * @param table
     * @param db
     * @param indexName
     * @param scope
     * @throws SqlJetException
     */
    public SqlJetIndexScopeCursor(ISqlJetBtreeDataTable table, SqlJetDb db, String indexName, SqlJetScope scope) throws SqlJetException {
        super(table, db, indexName);
        this.firstKey = SqlJetUtility.copyArray(scope.getLeftBound() != null ? scope.getLeftBound().getValue() : null);
        this.firstKeyIncluded = scope.getLeftBound() != null ? scope.getLeftBound().isInclusive() : true;
        this.lastKey = SqlJetUtility.copyArray(scope.getRightBound() != null ? scope.getRightBound().getValue() : null);
        this.lastKeyIncluded = scope.getRightBound() != null ? scope.getRightBound().isInclusive() : true;
        if (null == indexTable) {
            firstRowId = getRowIdFromKey(this.firstKey);
            lastRowId = getRowIdFromKey(this.lastKey);
            if (!firstKeyIncluded && firstRowId > 0) firstRowId++;
            if (!lastKeyIncluded && lastRowId > 0) lastRowId--;
        }
        first();
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * org.tmatesoft.sqljet.core.internal.table.SqlJetTableDataCursor#goTo(long)
     */
    @Override
    public boolean goTo(final long rowId) throws SqlJetException {
        return (Boolean) db.runReadTransaction(db1 -> {
            SqlJetIndexScopeCursor.super.goTo(rowId);
            return !eof();
        });
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * org.tmatesoft.sqljet.core.internal.table.SqlJetIndexOrderCursor#first()
     */
    @Override
    public boolean first() throws SqlJetException {
        return (Boolean) db.runReadTransaction(db1 -> {
            if (firstKey == null) {
                return SqlJetIndexScopeCursor.super.first();
            } else if (indexTable == null) {
                if (firstRowId == 0) {
                    return SqlJetIndexScopeCursor.super.first();
                } else {
                    return firstRowNum(goTo(firstRowId));
                }
            } else {
                long lookup = indexTable.lookupNear(false, firstKey);
                if (!firstKeyIncluded && lookup != 0) {
                    while (indexTable.compareKey(firstKey) == 0) {
                        if (indexTable.next()) {
                            lookup = indexTable.getKeyRowId();
                        } else {
                            lookup = 0;
                            break;
                        }
                    }
                }
                if (lookup != 0) return firstRowNum(goTo(lookup));
            }
            return false;
        });
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * org.tmatesoft.sqljet.core.internal.table.SqlJetIndexOrderCursor#next()
     */
    @Override
    public boolean next() throws SqlJetException {
        return (Boolean) db.runReadTransaction(db1 -> {
            if (lastKey == null) {
                return SqlJetIndexScopeCursor.super.next();
            } else if (indexTable == null) {
                SqlJetIndexScopeCursor.super.next();
                return !eof();
            } else {
                if (indexTable.next() && !eof()) return nextRowNum(goTo(indexTable.getKeyRowId()));
            }
            return false;
        });
    }

    /* (non-Javadoc)
     * @see org.tmatesoft.sqljet.core.internal.table.SqlJetIndexOrderCursor#previous()
     */
    @Override
    public boolean previous() throws SqlJetException {
        return (Boolean) db.runReadTransaction(db1 -> {
            if (firstKey == null) {
                return SqlJetIndexScopeCursor.super.previous();
            } else if (indexTable == null) {
                SqlJetIndexScopeCursor.super.previous();
                return !eof();
            } else {
                if (indexTable.previous() && !eof()) return previousRowNum(goTo(indexTable.getKeyRowId()));
            }
            return false;
        });
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * org.tmatesoft.sqljet.core.internal.table.SqlJetIndexOrderCursor#eof()
     */
    @Override
    public boolean eof() throws SqlJetException {
        return (Boolean) db.runReadTransaction(db1 -> SqlJetIndexScopeCursor.super.eof() || !checkScope());
    }

    /**
     * @return
     * @throws SqlJetException
     */
    private boolean checkScope() throws SqlJetException {
        if (indexTable == null) {
            if (getBtreeDataTable().eof()) return false;
            final long rowId = getRowId();
            if (firstRowId != 0 && firstRowId > rowId) return false;
            if (lastRowId != 0 && lastRowId < rowId) return false;
        } else {
            if (firstKey != null) {
                int compareResult = indexTable.compareKey(firstKey);
                if (compareResult < 0 || !firstKeyIncluded && compareResult == 0) return false;
            }
            if (lastKey != null) {
                int compareResult = indexTable.compareKey(lastKey);
                if (compareResult > 0 || (!lastKeyIncluded && compareResult == 0)) return false;
            }
        }
        return true;
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * org.tmatesoft.sqljet.core.internal.table.SqlJetIndexOrderCursor#last()
     */
    @Override
    public boolean last() throws SqlJetException {
        return (Boolean) db.runReadTransaction(db1 -> {
            if (lastKey == null) {
                return SqlJetIndexScopeCursor.super.last();
            } else if (indexTable == null) {
                return lastRowId == 0 ? SqlJetIndexScopeCursor.super.last() : lastRowNum(goTo(lastRowId));
            } else {
                long lookup = indexTable.lookupLastNear(lastKey);
                if (lookup != 0 && !lastKeyIncluded) {
                    while (indexTable.compareKey(lastKey) == 0) {
                        if (indexTable.previous()) {
                            lookup = indexTable.getKeyRowId();
                        } else {
                            lookup = 0;
                            break;
                        }
                    }
                }
                return lookup != 0 && lastRowNum(goTo(lookup));
            }
        });
    }

    private long getRowIdFromKey(Object[] key) {
        return (key != null && key.length > 0 && key[0] instanceof Long) ? (Long) key[0] : 0;
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * org.tmatesoft.sqljet.core.internal.table.SqlJetTableDataCursor#delete()
     */
    @Override
    public void delete() throws SqlJetException {
        db.runSynchronized(engine -> {
            SqlJetIndexScopeCursor.super.delete();
            return null;
        });
        db.runReadTransaction(db1 -> {
            if (!checkScope()) next();
            return false;
        });
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * org.tmatesoft.sqljet.core.internal.table.SqlJetTableDataCursor#getRowId()
     */
    @Override
    public long getRowId() throws SqlJetException {
        return (Long) db.runSynchronized(engine -> (indexTable != null && !indexTable.eof()) ? indexTable.getKeyRowId() : SqlJetIndexScopeCursor.super.getRowId());
    }

}