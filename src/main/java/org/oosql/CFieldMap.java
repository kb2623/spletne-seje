package org.oosql;

import org.oosql.annotation.MapTable;
import org.oosql.annotation.MapTables;
import org.oosql.exception.OosqlException;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.Map;

public class CFieldMap extends CField {

	private MapTable mapAnno;
	private CField key;
	private CField value;

	public CFieldMap(Field field) throws OosqlException, ClassNotFoundException {
		super(field);
		if (Map.class.isAssignableFrom(field.getType())) {
			setMap(field, field.getGenericType().getTypeName(), 0);
		}
	}

	private void setMap(Field field, String typeName, int index) {
		mapAnno = field.getAnnotationsByType(MapTable.class)[index];

	}

	public MapTable getMapAnno() {
		return mapAnno;
	}


	@Override
	public Annotation getAnnotaion(Class annoType) {
		if (annoType == MapTable.class) {
			return mapAnno;
		} else {
			return super.getAnnotaion(annoType);
		}
	}
}
