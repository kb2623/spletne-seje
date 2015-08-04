package org.oosql.annotation;

import java.lang.annotation.Annotation;
import java.sql.JDBCType;

public class ArrayTableC implements ArrayTable {

	private Table 		arrayTable;
	private Table 		valueTable;
	private Column		arrayid;
	private String		dimPrefix;
	private JDBCType	dimType;
	private int			dimLen;
	private Column 	vColumn;
	private EnumTable eTable;

	public ArrayTableC(Table arrayTable, Table valueTable, Column arrayid, String dimPrefix, JDBCType dimType, int dimLen, Column valueColumn, EnumTable enumTable) {
		this.arrayTable = arrayTable;
		this.valueTable = valueTable;
		this.arrayid	 = arrayid;
		this.dimPrefix	 = dimPrefix;
		this.dimType	 = dimType;
		this.dimLen		 = dimLen;
		this.vColumn	 = valueColumn;
		this.eTable		 = enumTable;
	}

	public ArrayTableC(ArrayTable table, Table arrayTable, Table valueTable, Column arrayid, String dimPrefix, JDBCType dimType, Integer dimLen, Column valueColumn, EnumTable enumColumn) {
		this(
				arrayTable 	== null ? table.arrayTable() : arrayTable,
				valueTable 	== null ? table.valueTable() : valueTable,
				arrayid		== null ? table.arrayid()	  : arrayid,
				dimPrefix	== null ? table.dimPrefix()  : dimPrefix,
				dimType		== null ? table.dimType()	  : dimType,
				dimLen		== null ? table.dimLen()	  : dimLen,
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
						new ColumnC(),
						null
				),
				new ColumnC(
						new String[]{name + "_array_id"},
						true,
						false,
						false,
						JDBCType.INTEGER,
						0,
						null
				),
				"dim_",
				JDBCType.INTEGER,
				0,
				new ColumnC(new String[]{name + "_value"}, false, false, false, JDBCType.INTEGER, 0, null),
				new EnumTableC(name)
		);
	}

	public ArrayTableC(ArrayTable table, String name) {
		this(
				new TableC(
						table.arrayTable().name().isEmpty() ? name + "_array" : table.arrayTable().name(),
						true,
						new ColumnC(
								new String[]{"array_id"},
								true,
								false,
								false,
								JDBCType.INTEGER,
								0,
								null
						),
						table.arrayTable().pkConstraintName().isEmpty() ? null : table.arrayTable().pkConstraintName()
				),
				new TableC(
						table.valueTable().name().isEmpty() ? name + "_value" : table.valueTable().name(),
						false,
						new ColumnC(),
						table.valueTable().pkConstraintName().isEmpty() ? null : table.valueTable().pkConstraintName()
				),
				new ColumnC(
						table.arrayid().name().length == 0 ? new String[]{name + "_array_id"} : table.arrayid().name(),
						true,
						false,
						false,
						table.arrayid().type(),
						table.arrayid().typeLen(),
						table.arrayid().constaraintName()
				),
				table.dimPrefix().isEmpty() ? "dim_" : table.dimPrefix(),
				table.dimType(),
				table.dimLen(),
				new ColumnC(
						table.valueColum().name().length == 0 ? new String[]{name + "_value"} : table.valueColum().name(),
						table.valueColum().pk(),
						false,
						false,
						JDBCType.INTEGER,
						0,
						null
				),
				new EnumTableC(table.enumColumn(), name)
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
	public Column arrayid() {
		return arrayid;
	}

	@Override
	public String dimPrefix() {
		return dimPrefix != null ? dimPrefix : "";
	}

	@Override
	public JDBCType dimType() {
		return dimType;
	}

	@Override
	public int dimLen() {
		return dimLen;
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
