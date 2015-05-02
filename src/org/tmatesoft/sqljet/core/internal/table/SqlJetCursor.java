/**
 * SqlJetTable.java
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

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import org.tmatesoft.sqljet.core.SqlJetErrorCode;
import org.tmatesoft.sqljet.core.SqlJetException;
import org.tmatesoft.sqljet.core.SqlJetValueType;
import org.tmatesoft.sqljet.core.internal.ISqlJetMemoryPointer;
import org.tmatesoft.sqljet.core.internal.SqlJetUtility;
import org.tmatesoft.sqljet.core.table.ISqlJetCursor;
import org.tmatesoft.sqljet.core.table.SqlJetDb;

/**
 * Base implementation of {@link ISqlJetCursor}.
 * 
 * @author TMate Software Ltd.
 * @author Sergey Scherbina (sergey.scherbina@gmail.com)
 * 
 */
public abstract class SqlJetCursor implements ISqlJetCursor {

    protected final ISqlJetBtreeTable btreeTable;
    protected final SqlJetDb db;

    SqlJetCursor(ISqlJetBtreeTable table, SqlJetDb db) throws SqlJetException {
        if (db.isInTransaction()) {
            this.btreeTable = table;
            this.db = db;
        } else {
            throw new SqlJetException(SqlJetErrorCode.MISUSE, "Cursor requires active transaction");
        }
    }

	@Override
    public void close() throws SqlJetException {
        db.runReadTransaction((SqlJetDb db1) -> {
			btreeTable.close();
			return null;
		});
    }

	@Override
    public boolean eof() throws SqlJetException {
        return (Boolean) db.runReadTransaction((SqlJetDb db1) -> btreeTable.eof());
    }

	@Override
    public boolean first() throws SqlJetException {
        return (Boolean) db.runReadTransaction((SqlJetDb db1) -> btreeTable.first());
    }

	@Override
    public boolean last() throws SqlJetException {
        return (Boolean) db.runReadTransaction((SqlJetDb db1) -> btreeTable.last());
    }

	@Override
    public boolean next() throws SqlJetException {
        return (Boolean) db.runReadTransaction((SqlJetDb db1) -> btreeTable.next());
    }

	@Override
    public boolean previous() throws SqlJetException {
        return (Boolean) db.runReadTransaction((SqlJetDb db1) -> btreeTable.previous());
    }

	@Override
    public int getFieldsCount() throws SqlJetException {
        return (Integer) db.runReadTransaction((SqlJetDb db1) -> btreeTable.getFieldsCount());
    }

	@Override
    public SqlJetValueType getFieldType(final int field) throws SqlJetException {
        return (SqlJetValueType) db.runReadTransaction((SqlJetDb db1) -> btreeTable.getFieldType(field));
    }

	@Override
    public boolean isNull(final int field) throws SqlJetException {
        return (Boolean) db.runReadTransaction((SqlJetDb db1) -> btreeTable.isNull(field));
    }

	@Override
    public String getString(final int field) throws SqlJetException {
        return (String) db.runReadTransaction((	SqlJetDb db1) -> btreeTable.getString(field));
    }

	@Override
    public long getInteger(final int field) throws SqlJetException {
        return (Long) db.runReadTransaction((SqlJetDb db1) -> btreeTable.getInteger(field));
    }

	@Override
    public double getFloat(final int field) throws SqlJetException {
        return (Double) db.runReadTransaction((SqlJetDb db1) -> btreeTable.getFloat(field));
    }

	@Override
    public byte[] getBlobAsArray(final int field) throws SqlJetException {
        return (byte[]) db.runReadTransaction((SqlJetDb db1) -> {
			ISqlJetMemoryPointer buffer = btreeTable.getBlob(field);
			return buffer != null ? SqlJetUtility.readByteBuffer(buffer) : null;
		});
    }

	@Override
    public InputStream getBlobAsStream(final int field) throws SqlJetException {
        return (InputStream) db.runReadTransaction((SqlJetDb db1) -> {
			ISqlJetMemoryPointer buffer = btreeTable.getBlob(field);
			return buffer != null ? new ByteArrayInputStream(SqlJetUtility.readByteBuffer(buffer)) : null;
		});
    }

	@Override
    public Object getValue(final int field) throws SqlJetException {
        return db.runReadTransaction((SqlJetDb db1) -> {
			Object value = btreeTable.getValue(field);
			if (value instanceof ISqlJetMemoryPointer) {
				return new ByteArrayInputStream(SqlJetUtility.readByteBuffer((ISqlJetMemoryPointer) value));
			}
			return value;
		});
    }

	@Override
    public boolean getBoolean(final int field) throws SqlJetException {
        return (Boolean) db.runReadTransaction((SqlJetDb db1) -> btreeTable.getInteger(field) != 0);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.tmatesoft.sqljet.core.table.ISqlJetCursor#reverse()
     */
	@Override
    public ISqlJetCursor reverse() throws SqlJetException {
        return new SqlJetReverseOrderCursor(this);
    }

}
