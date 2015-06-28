/**
 * OpenScopeTest.java
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
package org.tmatesoft.sqljet.issues._148;



import org.junit.Before;
import org.junit.Test;
import org.tmatesoft.sqljet.core.AbstractNewDbTest;
import org.tmatesoft.sqljet.core.SqlJetException;
import org.tmatesoft.sqljet.core.table.SqlJetScope;
import org.tmatesoft.sqljet.core.table.SqlJetScope.SqlJetScopeBound;

/**
 * @author TMate Software Ltd.
 * @author Sergey Scherbina (sergey.scherbina@gmail.com)
 *
 */
public class OpenScopeTest extends AbstractNewDbTest {

    @Before
    public void setUp() throws Exception {
        super.setUp();
        db.createTable("CREATE TABLE IF NOT EXISTS table (name TEXT, count INTEGER)");
        db.createIndex("CREATE UNIQUE INDEX IF NOT EXISTS names_idx ON table(name)");
        
        db.runWriteTransaction(db1 -> {

            db1.getTable("table").insert("XYZ", 1);
            db1.getTable("table").insert("XYZZ", 1);
            db1.getTable("table").insert("ABC", 1);
            db1.getTable("table").insert("ABCD", 1);
            db1.getTable("table").insert("ABCDEF", 1);
            db1.getTable("table").insert("A", 1);

            return null;
        });

        db.createTable("CREATE TABLE IF NOT EXISTS noindex (id INTEGER PRIMARY KEY)");
        db.runWriteTransaction(db1 -> {

            db1.getTable("noindex").insert(1);
            db1.getTable("noindex").insert(2);
            db1.getTable("noindex").insert(3);
            db1.getTable("noindex").insert(4);
            db1.getTable("noindex").insert(5);
            return null;
        });
    }

    @Test
    public void testOpenScope() throws SqlJetException {
        
        SqlJetScope closedScope = new SqlJetScope(new Object[] {"ABC"}, new Object[] {"XYZ"});
        SqlJetScope openScope = new SqlJetScope(new Object[] {"ABC"}, false, new Object[] {"XYZ"}, false);
        SqlJetScope emptyOpenScope = new SqlJetScope(new Object[] {"ABCD"}, false, new Object[] {"ABCDEF"}, false);
        SqlJetScope notMatchingOpenScope = new SqlJetScope(new Object[] {"AB"}, false, new Object[] {"XY"}, false);
        SqlJetScope notMatchingClosedScope = new SqlJetScope(new Object[] {"AB"}, true, new Object[] {"XY"}, true);
        SqlJetScope outOfBoundsClosedScope = new SqlJetScope(new Object[] {"XYZZZ"}, true, new Object[] {"XYZZZZZ"}, true);
        SqlJetScope outOfBoundsOpenScope = new SqlJetScope(new Object[] {"XYZZ"}, false, new Object[] {"XYZZZZZ"}, true);
        SqlJetScope unbounded = new SqlJetScope(null, (SqlJetScopeBound) null);
        SqlJetScope unbounded2 = new SqlJetScope(null, (Object[]) null);
        
        assertIndexScope(closedScope, "ABC", "ABCD", "ABCDEF", "XYZ");
        assertIndexScope(closedScope.reverse(), "XYZ", "ABCDEF", "ABCD", "ABC");
        assertIndexScope(openScope, "ABCD", "ABCDEF");
        assertIndexScope(openScope.reverse(), "ABCDEF", "ABCD");
        assertIndexScope(emptyOpenScope);
        assertIndexScope(emptyOpenScope.reverse());
        assertIndexScope(notMatchingClosedScope, "ABC", "ABCD", "ABCDEF");
        assertIndexScope(notMatchingOpenScope, "ABC", "ABCD", "ABCDEF");
        assertIndexScope(outOfBoundsClosedScope);
        assertIndexScope(outOfBoundsOpenScope);

        assertIndexScope(unbounded, "A", "ABC", "ABCD", "ABCDEF", "XYZ", "XYZZ" );
        assertIndexScope(unbounded.reverse(), "A", "ABC", "ABCD", "ABCDEF", "XYZ", "XYZZ" );
        assertIndexScope(unbounded2, "A", "ABC", "ABCD", "ABCDEF", "XYZ", "XYZZ" );
        assertIndexScope(unbounded2.reverse(), "A", "ABC", "ABCD", "ABCDEF", "XYZ", "XYZZ" );
    }

    @Test
    public void testNoIndexScope() throws SqlJetException {
        SqlJetScope closedScope = new SqlJetScope(new Object[] {2}, new Object[] {5});
        SqlJetScope openScope = new SqlJetScope(new Object[] {2}, false, new Object[] {5}, false);
        SqlJetScope emptyScope = new SqlJetScope(new Object[] {3}, false, new Object[] {4}, false);
        
        assertNoIndexScope(closedScope, (long) 2, (long) 3, (long) 4, (long) 5);
        assertNoIndexScope(openScope, (long) 3, (long) 4);
        assertNoIndexScope(emptyScope);

        assertNoIndexScope(closedScope.reverse(), (long) 5, (long) 4, (long) 3, (long) 2);
        assertNoIndexScope(openScope.reverse(), (long) 4, (long) 3);
        assertNoIndexScope(emptyScope.reverse());
        
        db.runWriteTransaction(db1 -> {
            db1.getTable("noindex").lookup(null, (long) 3).delete();
            return null;
        });

        assertNoIndexScope(openScope, (long) 4);
        assertNoIndexScope(openScope.reverse(), (long) 4);

        db.runWriteTransaction(db1 -> {
            db1.getTable("noindex").insert(3);
            db1.getTable("noindex").insert(6);
            return null;
        });
        assertNoIndexScope(openScope, (long) 3, (long) 4);
        assertNoIndexScope(openScope.reverse(), (long) 4, (long) 3);
        assertNoIndexScope(new SqlJetScope(null, (SqlJetScopeBound) null),
                (long) 1, (long) 2, (long) 3, (long) 4, (long) 5, (long) 6);
    }
    
    private void assertIndexScope(SqlJetScope scope, Object... expectedKeysInScope) throws SqlJetException {
        assertScope(scope, "table", "names_idx", expectedKeysInScope);
    }

    private void assertNoIndexScope(SqlJetScope scope, Object... expectedKeysInScope) throws SqlJetException {
        assertScope(scope, "noindex", null, expectedKeysInScope);
    }

}