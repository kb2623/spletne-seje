package org.oosql.annotation;

import java.sql.JDBCType;
import java.lang.annotation.Annotation;

public class ColumnC implements Column {

	private String[] name;
	private boolean  pk;
	private boolean  notNull;
	private boolean  unique;
	private JDBCType type;
	private int 	  typeLen;
	private String   constaraintName;

	public ColumnC() {
		this(
				null,
				false,
				false,
				false,
				JDBCType.INTEGER,
				0,
				null
		);
	}

	public ColumnC(String name, JDBCType type, int typeLen) {
		this(
				new String[]{name},
				true,
				false,
				false,
				type,
				typeLen,
				null
		);
	}

	public ColumnC(String[] name, boolean pk, boolean notNull, boolean unique, JDBCType type, int typeLen, String constaraintName) {
		this.name				= name;
		this.pk					= pk;
		this.notNull			= notNull;
		this.unique				= unique;
		this.type				= type;
		this.typeLen 			= typeLen;
		this.constaraintName = constaraintName;
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
				null,
				null,
				null,
				null,
				null,
				null
		);
	}

	@Override
	public String[] name() {
		return name != null ? name : new String[]{};
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
		return constaraintName != null ? constaraintName : "";
	}

	@Override
	public Class<? extends Annotation> annotationType() {
		return Column.class;
	}

	@Override
	public boolean equals(Object o) {
		if (o instanceof Column) {
			Column c = (Column) o;
			if (name().length == c.name().length) {
				for (int i = 0; i < name().length; i++) if (!name()[i].equals(c.name()[i])) {
					return false;
				}
				return pk() == c.pk() && notNull() == c.notNull() && type() == c.type() && typeLen() == c.typeLen()
						&& constaraintName().equals(c.constaraintName());
			}
			return false;
		} else {
			return false;
		}
	}
}