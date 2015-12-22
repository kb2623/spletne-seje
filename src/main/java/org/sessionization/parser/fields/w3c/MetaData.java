package org.sessionization.parser.fields.w3c;

import org.sessionization.parser.LogField;

public class MetaData implements LogField {

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
}