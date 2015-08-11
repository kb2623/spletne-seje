package org.oosql.annotation;

import java.lang.annotation.Annotation;

public class MapTableC implements MapTable {

	private Column keyColumn;
	private Column valueColumn;

	public MapTableC(String altName) {
		// TODO
	}

	@Override
	public Column valueColumn() {
		return valueColumn;
	}

	@Override
	public Column keyColumn() {
		return keyColumn;
	}

	@Override
	public Class<? extends Annotation> annotationType() {
		return MapTable.class;
	}
}
