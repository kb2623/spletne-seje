package org.spletneseje.database;

import org.tmatesoft.sqljet.core.SqlJetException;
import org.tmatesoft.sqljet.core.table.SqlJetDb;

public class DataBase implements AutoCloseable {

    protected SqlJetDb data;



    @Override
    public void close() throws SqlJetException {
        data.close();
    }
}
