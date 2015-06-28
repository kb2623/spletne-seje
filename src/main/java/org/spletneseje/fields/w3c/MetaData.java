package org.spletneseje.fields.w3c;

import org.spletneseje.fields.Field;
import org.spletneseje.fields.FieldType;

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
