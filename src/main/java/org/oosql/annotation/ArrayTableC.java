package org.oosql.annotation;

import java.sql.JDBCType;
import java.lang.annotation.Annotation;

public class ArrayTableC implements ArrayTable {

	private Table arrayTable;
	private Table valueTable;

	public ArrayTableC(Table arrayTable, Table valueTable) {
		this.arrayTable = arrayTable;
		this.valueTable = valueTable;
	}

	public ArrayTableC(ArrayTable table, Table arrayTable, Table valueTable) {
		this(
				arrayTable == null ? table.arrayTable() : arrayTable,
				valueTable == null ? table.valueTable() : valueTable
		);
	}

	public ArrayTableC(String name, int dimensions) {
		arrayTable = new TableC(
				name + "_array",
				true,
				new ColumnC(
						name + "_array_id",
						true,
						false,
						false,
						JDBCType.INTEGER,
						0
				),
				null
		);
		Column[] array = new Column[dimensions + 1];
		for (int i = 0; true; i++) {
			if (i < dimensions) {
				array[i] = new ColumnC(
						"dim_" + i,
						true,
						false,
						false,
						JDBCType.INTEGER,
						0
				);
			} else {
				array[i] = new ColumnC(
						name + "_value",
						false,
						false,
						false,
						JDBCType.INTEGER,
						0
				);
				break;
			}
		}
		valueTable = new TableC(
				name + "_value",
				true,
				new ColumnC(
						name + "_array_id",
						true,
						false,
						false,
						JDBCType.INTEGER,
						0
				),
				new ColumnsC(array)
		);
	}

	public ArrayTableC(ArrayTable table, String name) {
		this(
				new TableC(
						table.arrayTable().name().isEmpty() ? name + "_array" : table.arrayTable().name(),
						true,
						new ColumnC(
								"array_id",
								true,
								false,
								false,
								JDBCType.INTEGER,
								0
						),
						null
				),
				new TableC(
						table.valueTable().name().isEmpty() ? name + "_value" : table.valueTable().name(),
						false,
						new ColumnC(
								table.valueTable().id().name().isEmpty() ? name + "_array_id" : table.valueTable().id().name(),
								true,
								false,
								false,
								table.valueTable().id().type(),
								table.valueTable().id().typeLen()
						),
						new ColumnsC(new Column[] {
								new ColumnC(
										"dim_",
										true,
										false,
										false,
										JDBCType.INTEGER,
										0
								),
								new ColumnC(
										name + "_value",
										false,
										false,
										false,
										JDBCType.INTEGER,
										0
								)
						})
				)
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
	public Class<? extends Annotation> annotationType() {
		return ArrayTable.class;
	}
}
