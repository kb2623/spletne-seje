/**
 * SqlJetPragmasHandler.java
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

import org.antlr.runtime.ANTLRStringStream;
import org.antlr.runtime.CharStream;
import org.antlr.runtime.CommonTokenStream;
import org.antlr.runtime.RecognitionException;
import org.antlr.runtime.tree.CommonTree;
import org.antlr.runtime.tree.Tree;
import org.tmatesoft.sqljet.core.SqlJetEncoding;
import org.tmatesoft.sqljet.core.SqlJetErrorCode;
import org.tmatesoft.sqljet.core.SqlJetException;
import org.tmatesoft.sqljet.core.internal.lang.SqlLexer;
import org.tmatesoft.sqljet.core.internal.lang.SqlParser;
import org.tmatesoft.sqljet.core.table.ISqlJetOptions;

/**
 * @author TMate Software Ltd.
 * @author Dmitry Stadnik (dtrace@seznam.cz)
 */
public class SqlJetPragmasHandler {

    private final ISqlJetOptions options;

    public SqlJetPragmasHandler(ISqlJetOptions options) {
        this.options = options;
    }

    private ISqlJetOptions getOptions() {
        return options;
    }

    /**
     * Executes pragma statement. If statement queries pragma value then it will
     * be returned.
	 * @param sql
	 * @return 
	 * @throws org.tmatesoft.sqljet.core.SqlJetException
     */
    public Object pragma(String sql) throws SqlJetException {
        return pragma(parsePragma(sql));
    }

    public Object pragma(CommonTree ast) throws SqlJetException {
        assert "pragma".equalsIgnoreCase(ast.getText());
        String name = ast.getChild(0).getText();
        // String database = "main";
        // if (ast.getChild(0).getChildCount() > 0) {
        // database = ast.getChild(0).getChild(0).getText();
        // }
        // TODO: use the specified database where appropriate
        if (ast.getChildCount() > 1) {
            // set or execute
            Object value = readPragmaValue(ast.getChild(1));
            if (null != name) switch (name) {
			case "auto_vacuum":
				int mode = readAutovacuumMode(value);
				getOptions().setAutovacuum(mode == 1);
				getOptions().setIncrementalVacuum(mode == 2);
				break;
			case "cache_size":
				if (value instanceof Number) {
					getOptions().setCacheSize(((Number) value).intValue());
				} else {
					throw new SqlJetException(SqlJetErrorCode.ERROR, "Invalid cache_size value: " + value);
				}
				break;
			case "encoding":
				if (value instanceof String) {
					SqlJetEncoding enc = SqlJetEncoding.decode((String) value);
					if (enc != null) {
						getOptions().setEncoding(enc);
					} else {
						throw new SqlJetException(SqlJetErrorCode.ERROR, "Unknown encoding: " + value);
					}
				} else {
					throw new SqlJetException(SqlJetErrorCode.ERROR, "Invalid encoding value: " + value);
				}
				break;
			case "legacy_file_format":
				getOptions().setLegacyFileFormat(toBooleanValue(value));
				break;
			case "schema_version":
				if (value instanceof Number) {
					int version = ((Number) value).intValue();
					getOptions().setSchemaVersion(version);
                } else {
					throw new SqlJetException(SqlJetErrorCode.ERROR, "Invalid schema_version value: " + value);
				}
				break;
			case "user_version":
				if (value instanceof Number) {
					int version = ((Number) value).intValue();
					getOptions().setUserVersion(version);
				} else {
					throw new SqlJetException(SqlJetErrorCode.ERROR, "Invalid user_version value: " + value);
				}
				break;
			}
			return null;
		} else {
			// get value
            if (null != name) switch (name) {
			case "auto_vacuum":
				int mode = 0;
				if (getOptions().isAutovacuum()) {
					mode = 1;
				}
				if (getOptions().isIncrementalVacuum()) {
					mode = 2;
				}
				return Integer.valueOf(mode);
			case "cache_size":
				return Integer.valueOf(getOptions().getCacheSize());
			case "encoding":
				return getOptions().getEncoding();
			case "legacy_file_format":
				return getOptions().isLegacyFileFormat();
			case "schema_version":
				return Integer.valueOf(getOptions().getSchemaVersion());
			case "user_version":
				return Integer.valueOf(getOptions().getUserVersion());
			}
			return null;
        }
    }

    private int readAutovacuumMode(Object value) throws SqlJetException {
        int mode = -1;
        if (value instanceof String) {
            String s = ((String) value).toLowerCase();
            if (null != s) switch (s) {
			case "none":
				mode = 0;
				break;
			case "full":
				mode = 1;
				break;
			case "incremental":
				mode = 2;
				break;
			}
        } else if (value instanceof Number) {
            int i = ((Number) value).intValue();
            if (i == 0 || i == 1 || i == 2) {
                mode = i;
            }
        }
        if (mode < 0) {
            throw new SqlJetException(SqlJetErrorCode.ERROR, "Invalid auto_vacuum value: " + value);
        }
        return mode;
    }

    private CommonTree parsePragma(String sql) throws SqlJetException {
        try {
            CharStream chars = new ANTLRStringStream(sql);
            SqlLexer lexer = new SqlLexer(chars);
            CommonTokenStream tokens = new CommonTokenStream(lexer);
            SqlParser parser = new SqlParser(tokens);
            return (CommonTree) parser.pragma_stmt().getTree();
        } catch (RecognitionException re) {
            throw new SqlJetException(SqlJetErrorCode.ERROR, "Invalid sql statement: " + sql);
        }
    }

    private Object readPragmaValue(Tree node) {
        String type = node.getText().toLowerCase();
        if(type.equals("true")||type.equals("false")) {
        	return Boolean.parseBoolean(type);
        }
        if(node.getChild(0)==null) {
        	return null;
        }
        String value = node.getChild(0).getText();
		switch (type) {
		case "float_literal":
			return Double.valueOf(value);
		case "id_literal":
			return value;
		case "string_literal":
			return value.substring(1, value.length() - 1);
		}
        throw new IllegalStateException();
    }

    protected boolean toBooleanValue(Object value) throws SqlJetException {
        if (value instanceof Boolean) {
        	return ((Boolean) value).booleanValue();
        } else if (value instanceof Number) {
            int i = ((Number) value).intValue();
            if (i == 0) {
                return false;
            } else if (i == 1) {
                return true;
            }
        } else if (value instanceof String) {
            String s = ((String) value).toLowerCase();
            if (null != s) switch (s) {
			case "yes":
			case "true":
			case "on":
				return true;
			case "no":
			case "false":
			case "off":
				return false;
			}
        }
        throw new SqlJetException(SqlJetErrorCode.ERROR, "Boolean value is expected.");
    }

    protected Object toResult(boolean value) {
        return value ? Integer.valueOf(1) : Integer.valueOf(0);
    }
}
