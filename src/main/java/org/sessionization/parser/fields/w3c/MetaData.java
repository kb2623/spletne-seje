package org.sessionization.parser.fields.w3c;

import org.sessionization.parser.LogField;

import java.util.List;

public class MetaData implements LogField {

	private String data;
	private List<String> values;

	public MetaData(String data, List<String> values) {
		if (data.charAt(data.length() - 1) == ':') {
			this.data = data.substring(1, data.length() - 1);
		} else {
			this.data = data.substring(1);
		}
		this.values = values;
	}

	public String getMetaData() {
		return data;
	}

	public String[] getValues() {
		return values.toArray(new String[values.size()]);
	}

	@Override
	public String izpis() {
		return data + " >> " + values.toString();
	}

	@Override
	public String toString() {
		return data + " >> " + values.toString();
	}
}
