package org.sessionization.fields.w3c;

import org.sessionization.fields.FieldType;
import org.oosqljet.annotation.Entry;
import org.oosqljet.annotation.Table;
import org.sessionization.fields.Field;

@Table public class Host implements Field {

	@Entry private String host;
	
	public Host(String hostName) {
		if (!hostName.equals("-")) {
			host = hostName;
		} else {
			host = null;
		}
	}

	public String getHost() {
		return host != null ? host : "";
	}

	@Override
	public String izpis() {
		return host == null ? "-" : host;
	}

	@Override
	public String toString() {
		return host != null ? host : "-";
	}

	@Override
	public boolean equals(Object o) {
		return o instanceof Host && getHost().equals(((Host) o).getHost());
	}

	@Override
	public FieldType getFieldType() {
		return FieldType.Host;
	}

	@Override
	public String getKey() {
		return "";
	}
}
