package org.oosql.annotation;

import java.lang.annotation.Annotation;

public class ColumnClass implements Annotation, Column {

	private boolean pk = true;

	public ColumnClass(boolean pk) {
		this.pk = pk;
	}
	/**
	 * @return
	 */
	@Override
	public String[] name() {
		return new String[0];
	}
	/**
	 * @return
	 */
	@Override
	public boolean pk() {
		return pk;
	}
	/**
	 * @return
	 */
	@Override
	public boolean notNull() {
		return false;
	}
	/**
	 * @return
	 */
	@Override
	public boolean unique() {
		return false;
	}

	@Override
	public DataType dataType() {
		return null;
	}

	@Override
	public Class<? extends Annotation> annotationType() {
		return Column.class;
	}
}
