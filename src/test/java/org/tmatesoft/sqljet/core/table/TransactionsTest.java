package org.tmatesoft.sqljet.core.table;

import static org.junit.Assert.*;

import org.junit.Test;
import org.tmatesoft.sqljet.core.AbstractNewDbTest;
import org.tmatesoft.sqljet.core.SqlJetException;

public class TransactionsTest extends AbstractNewDbTest {

	@Test
	public void testWriteInRead() throws SqlJetException {
		assertEquals(Boolean.TRUE, db.runReadTransaction(db1 -> db1.runWriteTransaction(db2 -> {
			doWrite(db2);
			return true;
		})));
	}

	@Test
	public void testReadInRead() throws SqlJetException {
		assertEquals(Boolean.TRUE, db.runReadTransaction(db1 -> db1.runReadTransaction(db2 -> true)));
	}

	@Test
	public void testReadInWrite() throws SqlJetException {
		assertEquals(Boolean.TRUE, db.runWriteTransaction(db1 -> db1.runReadTransaction(db2 -> {
			doWrite(db2);
			return true;
		})));
	}

	@Test
	public void testWriteInWrite() throws SqlJetException {
		assertEquals(Boolean.TRUE, db.runWriteTransaction(db1 -> db1.runWriteTransaction(db2 -> {
            doWrite(db2);
            return true;
        })));
	}

	private void doWrite(SqlJetDb db) throws SqlJetException {
		ISqlJetTable t = db.getTable(db.createTable("create table t(a integer primary key, b text);").getName());
		t.insert("test");
	}

}
