package org.oosql.annotation;

import java.lang.annotation.Annotation;
import java.sql.JDBCType;

public class ArrayTableC implements ArrayTable {

	private Table 		arrayTable;
	private Table 		valueTable;
	private Column 	vColumn;
	private EnumTable eTable;

	public ArrayTableC(Table arrayTable, Table valueTable, Column valueColumn, EnumTable enumTable) {
		this.arrayTable = arrayTable;
		this.valueTable = valueTable;
		this.vColumn	 = valueColumn;
		this.eTable		 = enumTable;
	}

	public ArrayTableC(ArrayTable table, Table arrayTable, Table valueTable, Column valueColumn, EnumTable enumColumn) {
		this(
				arrayTable 	== null ? table.arrayTable() : arrayTable,
				valueTable 	== null ? table.valueTable() : valueTable,
				valueColumn == null ? table.valueColum() : valueColumn,
				enumColumn 	== null ? table.enumColumn() : enumColumn
		);
	}

	public ArrayTableC(String name) {
		this(
				new TableC(
						name + "_array",
						true,
						new ColumnC(new String[]{"array_id"}, true, false, false, JDBCType.INTEGER, 0, null),
						null
				),
				new TableC(
						name + "_value",
						false,
						null,
						null
				),
				new ColumnC(),
				new EnumTableC()
		);
	}

	@Override
	public Table arrayTable() {
		return arrayTable;
	}

	@Override
	public Table valueTable() {
		return valueTable;
	}

	@Override
	public Column valueColum() {
		return vColumn;
	}

	@Override
	public EnumTable enumColumn() {
		return eTable;
	}

	@Override
	public Class<? extends Annotation> annotationType() {
		return ArrayTable.class;
	}
}
