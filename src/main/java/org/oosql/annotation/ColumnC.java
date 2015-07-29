package org.oosql.annotation;

import java.lang.annotation.Annotation;
import java.sql.JDBCType;

public class ColumnC implements Column {

	private String[] name;
	private boolean  pk;
	private boolean  notNull;
	private boolean  unique;
	private JDBCType type;
	private int 	  typeLen;
	private String   constaraintName;

	public ColumnC(String[] name, boolean pk, boolean notNull, boolean unique, JDBCType type, int typeLen, String constaraintName) {
		this.name				= name;
		this.pk					= pk;
		this.notNull			= notNull;
		this.unique				= unique;
		this.type				= type;
		this.typeLen 			= typeLen;
		this.constaraintName = constaraintName;
	}

	public ColumnC() {
		this(
				new String[]{},
				false,
				false,
				false,
				JDBCType.INTEGER,
				0,
				""
		);
	}

	public ColumnC(Column column, String[] name, Boolean pk, Boolean notNull, Boolean unique, JDBCType type, Integer typeLen, String constraintName) {
		this(
				name 	  			== null ? column.name()					: name,
				pk 	  			== null ? column.pk()					: pk,
				notNull 			== null ? column.notNull()				: notNull,
				unique  			== null ? column.unique()				: unique,
				type 	  			== null ? column.type()    			: type,
				typeLen			== null ? column.typeLen() 			: typeLen,
				constraintName == null ? column.constaraintName()	: constraintName
		);
	}

	public ColumnC(Column column, String name) {
		this(
				column,
				new String[]{name},
				null, null, null, null, null, null
		);
	}

	@Override
	public String[] name() {
		return name;
	}

	@Override
	public boolean pk() {
		return pk;
	}

	@Override
	public boolean notNull() {
		return notNull;
	}

	@Override
	public boolean unique() {
		return unique;
	}

	@Override
	public JDBCType type() {
		return type;
	}

	@Override
	public int typeLen() {
		return typeLen;
	}

	@Override
	public String constaraintName() {
		return constaraintName;
	}

	@Override
	public Class<? extends Annotation> annotationType() {
		return Column.class;
	}
}
