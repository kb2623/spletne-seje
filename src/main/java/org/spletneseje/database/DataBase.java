package org.spletneseje.database;

import org.spletneseje.fields.*;
import org.spletneseje.parser.datastruct.ParsedLine;
import org.spletneseje.parser.datastruct.WebPage;
import org.tmatesoft.sqljet.core.SqlJetException;
import org.tmatesoft.sqljet.core.SqlJetTransactionMode;
import org.tmatesoft.sqljet.core.table.ISqlJetCursor;
import org.tmatesoft.sqljet.core.table.ISqlJetTable;
import org.tmatesoft.sqljet.core.table.SqlJetDb;

import java.io.File;
import java.text.ParseException;
import java.util.*;
import java.util.stream.Stream;

public class DataBase implements AutoCloseable {

    protected SqlJetDb data;
    protected EnumMap<FieldType, Boolean> tables;

    public DataBase(String path, boolean delete) throws SqlJetException {
        this(new File(path), delete);
    }

    public DataBase(File file, boolean delete) throws SqlJetException {
        if (delete) file.delete();
        data = SqlJetDb.open(file, true);
        data.getOptions().setAutovacuum(true);
        tables = new EnumMap<>(FieldType.class);
        Stream.of(FieldType.values()).forEach(this::setUpTables);
    }

    private void setUpTables(FieldType type) {
        tables.put(type, false);
    }

    public void createTables(List<FieldType> list) throws ParseException, SqlJetException {
        StringBuilder builder = new StringBuilder();
        String query;
        for (FieldType f : list) {
            if ((query = getCreateColumnString(f)) != null) builder.append(query).append(',');
        }
        if (builder.length() > 0) {
            builder.deleteCharAt(builder.length() - 1);
            createTable("CREATE TABLE log_entry(" + builder.toString() + ");");
        }
    }

    private void createTable(String... querys) throws SqlJetException {
        if (querys == null) return;
        for (String query : querys) {
            try {
                data.beginTransaction(SqlJetTransactionMode.WRITE);
                data.createTable(query);
                data.commit();
            } catch (SqlJetException e) {
                data.rollback();
                throw new SqlJetException("Can't create table with query [" + query + "]!!! :: " + e.getMessage());
            }
        }
    }

    private void createIndex(String... querys) throws SqlJetException {
        if (querys == null) return;
        for (String query : querys) {
            try {
                data.beginTransaction(SqlJetTransactionMode.WRITE);
                data.createIndex(query);
                data.commit();
            } catch (SqlJetException e) {
                data.rollback();
                throw new SqlJetException("Can't create inedxs with query [" + query + "]!!! :: " + e.getMessage());
            }
        }
    }

    private String getCreateColumnString(FieldType field) throws SqlJetException {
        if (tables.get(field)) return null;
        tables.put(field, true);
        switch (field) {
        case MetaData:
        case Unknown:
            return null;
        case RequestLine:
            return getCreateColumnString(FieldType.Method) + ','
                    + getCreateColumnString(FieldType.Referer) + ','
                    + getCreateColumnString(FieldType.UriQuery) + ','
                    + getCreateColumnString(FieldType.ProtocolVersion);
        default:
            createTable(field.createTableQuery());
            createIndex(field.createIndexQuery());
            return field.createTableConstraint();
        }
    }

    private long lookup(String tableName, String index, Object findObject) throws SqlJetException {
        long value = -1;
        data.beginTransaction(SqlJetTransactionMode.READ_ONLY);
        ISqlJetTable table = data.getTable(tableName);
        ISqlJetCursor cursor = table.lookup((index != null) ? index : table.getPrimaryKeyIndexName(), findObject);
        if (!cursor.eof()) value = cursor.getInteger(0);
        data.commit();
        return value;
    }

    private void insert(String tableName, Map<String, Object> map) throws SqlJetException {
        if (map == null || tableName == null) return;
        try {
            data.beginTransaction(SqlJetTransactionMode.WRITE);
            ISqlJetTable table = data.getTable(tableName);
            table.insertByFieldNames(map);
            data.commit();
        } catch (SqlJetException e) {
            data.rollback();
            throw new SqlJetException("Can't insert data " + map.values().toString() + " into " + tableName + "!!! :: " + e.getMessage());
        }
    }

    public boolean insert(WebPage lineData) throws SqlJetException {
        HashMap<String, Object> insertMap = new HashMap<>();
        //TODO
        insert("log_entry", insertMap);
        return false;
    }

    private Object insert(Field field, int level) throws SqlJetException {
        long tmpId = -1;
        switch (field.getFieldType()) {
        case MetaData:
        case Unknown:
            return null;
        case Time:
        case Date:
        case DateTime:
        case TimeTaken:
        case SubStatus:
        case Win32Status:
        case SizeOfRequest:
        case SizeOfResponse:
            return null; // TODO Vrni zapis
        case Referer:
            return null; // TODO Dobimo tabelo, kjer je potrebno obdelati vsak tapis v njej
        case ServerPort:
        case ClientPort:
            /*
            TODO
            pri teh dveh zapisih imamo dva nivoja
                1. novo predstavlja tabelo ključev, ki jih je porebno obdelati
                2. nivo predstavlja tabelo vrednost, ki jih je potrebno obdelati
             */
//            tmpId = lookup(field.getFieldType().getTableName(level), null, field.getData(level));
            if (tmpId == -1) {
                // Zapis že ne obstaja
            }
            return null;
        case UriQuery:
        case UriStem:
            return null;
        default:
//            tmpId = lookup(field.getFieldType().getTableName(level), field.getFieldType().getIndexName(level), field.getData(level));
            if (tmpId == -1) {
                HashMap<String, Object> map = new HashMap<>();
                // TODO Napolni mapo
                insert(field.getFieldType().getTableName(level), map);
            }
            return null;
        }
    }

    @Override
    public void close() throws SqlJetException {
        data.close();
    }
}
