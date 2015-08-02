package org.oosql.annotation;


import java.lang.annotation.Annotation;
import java.sql.JDBCType;

public class EnumTableC implements EnumTable {

	private String name;
	private Column kColumn;
	private Column vColumn;

	public EnumTableC(String name, Column kColumn, Column vColumn) {
		this.name 	 = name;
		this.kColumn = kColumn;
		this.vColumn = vColumn;
	}

	public EnumTableC(EnumTable table, String name, Column kColumn, Column vColumn) {
		this(
				name 	  == null ? table.name()		  : name,
				kColumn == null ? table.keyColumn()   : kColumn,
				vColumn == null ? table.valueColumn() : vColumn
		);
	}

	public EnumTableC(Table table) {
		this(
				table.name(),
				new ColumnC(new String[]{table.name() + "_id"}, 	true,	 false, false, JDBCType.INTEGER, 0,  null),
				new ColumnC(new String[]{table.name() + "_value"}, false, false, false, JDBCType.VARCHAR, 25, null)
		);
	}

	@Override
	public String name() {
		return name != null ? name : "";
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
