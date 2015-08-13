package org.oosql.annotation;

import java.lang.annotation.Annotation;
import java.util.List;

public class ColumnsC implements Columns {

	private Column[] value;

	public ColumnsC(Column[] value) {
		this.value = value;
	}

	@Override
	public Column[] value() {
		return value != null ? value : new Column[0];
	}

	@Override
	public Class<? extends Annotation> annotationType() {
		return Columns.class;
	}
}
