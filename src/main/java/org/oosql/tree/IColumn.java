package org.oosql.tree;

import org.oosql.annotation.Column;

public interface IColumn {
	/**
	 *
	 * @return
	 */
	boolean isPrimaryKey();
	/**
	 *
	 * @return
	 */
	String[] izpis();
	/**
	 *
	 * @return
	 */
	Column getColumn();
}
