package org.oosql.annotation;


import java.lang.annotation.Annotation;
import java.sql.JDBCType;

public class EnumTableC implements EnumTable {

	private Column kColumn;
	private Column vColumn;

	public EnumTableC(Column kColumn, Column vColumn) {
		this.kColumn = kColumn;
		this.vColumn = vColumn;
	}

	public EnumTableC(Table table) {
		this(
				new ColumnC(new String[]{table.name() + "_id"}, 	true,	 false, false, JDBCType.INTEGER, 0,  null),
				new ColumnC(new String[]{table.name() + "_value"}, false, false, false, JDBCType.VARCHAR, 25, null)
		);
	}

	public EnumTableC(EnumTable table, Column kColumn, Column vColumn) {
		this(
				kColumn == null ? table.keyColumn()   : kColumn,
				vColumn == null ? table.valueColumn() : vColumn
		);
	}

	@Override
	public Column keyColumn() {
		return kColumn;
	}

	@Override
	public Column valueColumn() {
		return vColumn;
	}

	@Override
	public Class<? extends Annotation> annotationType() {
		return null;
	}
}
