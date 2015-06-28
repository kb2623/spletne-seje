/**
 * ISqlJetTableDef.java
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
 */
package org.tmatesoft.sqljet.core.schema;

import java.util.List;

/**
 * Table Definition.
 *
 * @author TMate Software Ltd.
 * @author Dmitry Stadnik (dtrace@seznam.cz)
 */
public interface ISqlJetTableDef {

    /**
     * Returns table name.
     */
    String getName();

    String getQuotedName();

    /**
     * True if table was created temporarily.
     */
    boolean isTemporary();

    /**
     * Definitions of table columns.
     */
    List<ISqlJetColumnDef> getColumns();

    /**
     * Returns column definition with a given name or null if there is no such
     * definition.
     */
    ISqlJetColumnDef getColumn(String name);

    /**
     * Returns position of the specified column within the table definition.
     */
    int getColumnNumber(String name);

    /**
     * Returns all table constraints.
     */
    List<ISqlJetTableConstraint> getConstraints();

    /**
     * Returns true if primary key definition allows rowid to be used as primary
     * key column. In practice this means that the table has primary key that is
     * based in a single column of type 'integer'.
     */
    boolean isRowIdPrimaryKey();

    /**
     * Returns true if primary key has 'autoincrement' keyword.
     */
    boolean isAutoincremented();

    /**
     * @return name of the primary key index.
     */
    String getPrimaryKeyIndexName();

    /**
     * @return SQL representation of this table schema definition.
     */
    String toSQL();
}
