package org.oosql;

import org.oosql.annotation.DataType;

import java.lang.annotation.Annotation;

public interface SqlDataType extends DataType {
	@Override
	default Class<? extends Annotation> annotationType(){
		return DataType.class;
	}
}
