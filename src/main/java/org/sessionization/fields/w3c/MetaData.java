package org.sessionization.fields.w3c;

import org.sessionization.fields.Field;
import org.sessionization.fields.FieldType;

public class MetaData implements Field {
	
	private String data;
	
	public MetaData(String niz) {
		data = niz;
	}
	
	public String getMetaData() {
		return data;
	}

	@Override
	public String izpis() {
		return data;
	}

	@Override
	public String toString() {
		return data;
	}

	@Override
	public FieldType getFieldType() {
		return FieldType.MetaData;
	}

}
