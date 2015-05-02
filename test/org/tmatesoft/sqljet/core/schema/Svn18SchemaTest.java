/**
 * Svn17Schema.java
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
package org.tmatesoft.sqljet.core.schema;

import java.util.HashSet;
import java.util.Set;

import org.junit.Assert;
import org.junit.Test;
import org.tmatesoft.sqljet.core.AbstractNewDbTest;
import org.tmatesoft.sqljet.core.SqlJetException;
import org.tmatesoft.sqljet.core.table.SqlJetDb;

/**
 * @author TMate Software Ltd.
 * @author Sergey Scherbina (sergey.scherbina@gmail.com)
 * 
 */
public class Svn18SchemaTest extends AbstractNewDbTest {
    
    private static final String[] svn18WcTables =
    {
        "CREATE TABLE REPOSITORY ( id INTEGER PRIMARY KEY AUTOINCREMENT, root  TEXT UNIQUE NOT NULL, uuid  TEXT NOT NULL );", 
        
        "CREATE TABLE WCROOT ( id  INTEGER PRIMARY KEY AUTOINCREMENT, local_abspath  TEXT UNIQUE );",
        "CREATE TABLE PRISTINE ( checksum TEXT NOT NULL PRIMARY KEY, compression INTEGER, size INTEGER NOT NULL, refcount INTEGER NOT NULL, md5_checksum TEXT NOT NULL );" ,
        "CREATE TABLE ACTUAL_NODE ( wc_id  INTEGER NOT NULL REFERENCES WCROOT (id), local_relpath  TEXT NOT NULL, parent_relpath  TEXT, properties  BLOB, conflict_old  TEXT, conflict_new  TEXT, conflict_working  TEXT, prop_reject  TEXT, changelist  TEXT, text_mod  TEXT, tree_conflict_data  TEXT, conflict_data  BLOB, older_checksum  TEXT, left_checksum  TEXT, right_checksum TEXT, PRIMARY KEY (wc_id, local_relpath) );", 
        "CREATE TABLE LOCK ( repos_id  INTEGER NOT NULL REFERENCES REPOSITORY (id), repos_relpath TEXT NOT NULL, lock_token TEXT NOT NULL, lock_owner  TEXT, lock_comment  TEXT, lock_date  INTEGER, PRIMARY KEY (repos_id, repos_relpath) );", 
        
        "CREATE TABLE WORK_QUEUE ( id  INTEGER PRIMARY KEY AUTOINCREMENT, work  BLOB NOT NULL );" ,
        "CREATE TABLE WC_LOCK ( wc_id  INTEGER NOT NULL  REFERENCES WCROOT (id), local_dir_relpath TEXT NOT NULL, locked_levels  INTEGER NOT NULL DEFAULT -1, PRIMARY KEY (wc_id, local_dir_relpath) );" ,
        "CREATE TABLE NODES ( " +
                "wc_id INTEGER NOT NULL REFERENCES WCROOT (id), " +
                "local_relpath TEXT NOT NULL, " +
                "op_depth INTEGER NOT NULL, " +
                "parent_relpath TEXT, " +
                "repos_id INTEGER REFERENCES REPOSITORY (id), " +
                "repos_path TEXT, " +
                "revision INTEGER, " +
                "presence TEXT NOT NULL, " +
                "moved_here INTEGER, " +
                "moved_to TEXT, " +
                "kind TEXT NOT NULL, " +
                "properties  BLOB, " +
                "depth TEXT, " +
                "checksum  TEXT REFERENCES PRISTINE (checksum), " +
                "symlink_target TEXT, " +
                "changed_revision INTEGER, " +
                "changed_date INTEGER, " +
                "changed_author TEXT, " +
                "translated_size INTEGER, " +
                "last_mod_time INTEGER, " +
                "dav_cache BLOB, " +
                "file_external INTEGER, " +
                "inherited_props BLOB, " +
                "PRIMARY KEY (wc_id, local_relpath, op_depth) );", 
        "CREATE TABLE EXTERNALS ( wc_id  INTEGER NOT NULL REFERENCES WCROOT (id), local_relpath  TEXT NOT NULL, parent_relpath  TEXT NOT NULL, repos_id  INTEGER NOT NULL REFERENCES REPOSITORY (id), presence  TEXT NOT NULL, kind  TEXT NOT NULL, def_local_relpath         TEXT NOT NULL, def_repos_relpath         TEXT NOT NULL, def_operational_revision  TEXT, def_revision              TEXT, PRIMARY KEY (wc_id, local_relpath) );", 
    };

    private static final String[] svn18WcIndices =
    { 
        "CREATE INDEX I_UUID ON REPOSITORY (uuid);", 
        "CREATE INDEX I_ROOT ON REPOSITORY (root);", 
        "CREATE UNIQUE INDEX I_LOCAL_ABSPATH ON WCROOT (local_abspath);", 
        "CREATE INDEX IF NOT EXISTS I_PRISTINE_MD5 ON PRISTINE (md5_checksum);",
        "CREATE INDEX I_ACTUAL_PARENT ON ACTUAL_NODE (wc_id, parent_relpath);", 
        "CREATE UNIQUE INDEX I_NODES_PARENT ON NODES (wc_id, parent_relpath, op_depth);", 
        "CREATE UNIQUE INDEX I_NODES_MOVED ON NODES (wc_id, moved_to, op_depth);",
        "CREATE UNIQUE INDEX I_EXTERNALS_DEFINED ON EXTERNALS (wc_id, def_local_relpath, local_relpath);", 
    };

    private static final String[] svn18WcViews =
    {
        "CREATE VIEW NODES_BASE AS SELECT * FROM nodes WHERE op_depth = 0;",
        "CREATE VIEW NODES_CURRENT AS SELECT * FROM nodes AS n WHERE op_depth = (SELECT MAX(op_depth) FROM nodes AS n2 WHERE n2.wc_id = n.wc_id AND n2.local_relpath = n.local_relpath);"
    };
    
    private static final String[] svn18WcTriggers =
    {
        "CREATE TRIGGER nodes_insert_trigger AFTER INSERT ON nodes WHEN NEW.checksum IS NOT NULL BEGIN UPDATE pristine SET refcount = refcount + 1 WHERE checksum = NEW.checksum; END;",
        "CREATE TRIGGER nodes_delete_trigger AFTER DELETE ON nodes WHEN OLD.checksum IS NOT NULL BEGIN UPDATE pristine SET refcount = refcount - 1 WHERE checksum = OLD.checksum; END;",
        "CREATE TRIGGER nodes_update_checksum_trigger AFTER UPDATE OF checksum ON nodes WHEN NEW.checksum IS NOT OLD.checksum BEGIN UPDATE pristine SET refcount = refcount + 1 WHERE checksum = NEW.checksum; UPDATE pristine SET refcount = refcount - 1 WHERE checksum = OLD.checksum; END;",
    };

    private static final String NODES_TABLE_17 = "CREATE TABLE NODES ( " +
        "wc_id INTEGER NOT NULL REFERENCES WCROOT (id), " +
        "local_relpath TEXT NOT NULL, " +
        "op_depth INTEGER NOT NULL, " +
        "parent_relpath TEXT, " +
        "repos_id INTEGER REFERENCES REPOSITORY (id), " +
        "repos_path TEXT, " +
        "revision INTEGER, " +
        "presence TEXT NOT NULL, " +
        "moved_here INTEGER, " +
        "moved_to TEXT, " +
        "kind TEXT NOT NULL, " +
        "properties  BLOB, " +
        "depth TEXT, " +
        "checksum  TEXT REFERENCES PRISTINE (checksum), " +
        "symlink_target TEXT, " +
        "changed_revision INTEGER, " +
        "changed_date INTEGER, " +
        "changed_author TEXT, " +
        "translated_size INTEGER, " +
        "last_mod_time INTEGER, " +
        "dav_cache BLOB, " +
        "file_external INTEGER, " +
        "PRIMARY KEY (wc_id, local_relpath, op_depth) );"; 


    @Test
    public void createSVN18WcDb() throws SqlJetException {
        Set<String> tableNames = new HashSet<String>();
        Set<String> indexNames = new HashSet<String>();
        Set<String> viewNames = new HashSet<String>();
        Set<String> triggerNames = new HashSet<String>();
        
        for (String sql : svn18WcTables) {
            ISqlJetTableDef tableDef = db.createTable(sql);
            tableNames.add(tableDef.getName());
        }
        for (String sql : svn18WcIndices) {
            ISqlJetIndexDef indexDef = db.createIndex(sql);
            indexNames.add(indexDef.getName());
        }
        for (String sql : svn18WcViews) {
            ISqlJetViewDef viewDef = db.createView(sql);
            viewNames.add(viewDef.getName());
        }
        for (String sql : svn18WcTriggers) {
            ISqlJetTriggerDef triggerDef = db.createTrigger(sql);
            triggerNames.add(triggerDef.getName());
        }
        
        db.close();        
        db = SqlJetDb.open(file, false);
        ISqlJetSchema schema = db.getSchema();
        
        for(String tableName : schema.getTableNames()) {
            if (!tableName.startsWith("sqlite_")) {
                Assert.assertTrue("No table '" + tableName + "'", tableNames.remove(tableName));
            }
        }
        for(String viewName : schema.getViewNames()) {
            Assert.assertTrue(viewNames.remove(viewName));
        }
        for(String triggerName : schema.getTriggerNames()) {
            Assert.assertTrue(triggerNames.remove(triggerName));
        }
        for(String indexName : schema.getIndexNames()) {
            ISqlJetIndexDef indexDef = schema.getIndex(indexName);
            if (!indexDef.isImplicit()) {
                Assert.assertTrue(indexNames.remove(indexName));
            }
        }
        
        Assert.assertTrue(tableNames.isEmpty());
        Assert.assertTrue(indexNames.isEmpty());
        Assert.assertTrue(viewNames.isEmpty());
    }
    
    @Test
    public void testUpgradeSvn17ToSvn18() throws SqlJetException {
      db.createTable(NODES_TABLE_17);
      db.alterTable("ALTER TABLE NODES ADD COLUMN inherited_props BLOB;");
      db.close();

      db = SqlJetDb.open(file, true);
      db.refreshSchema();
      db.close();
    }
}
