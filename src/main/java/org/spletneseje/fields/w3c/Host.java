package org.spletneseje.fields.w3c;

import org.spletneseje.database.annotation.Entry;
import org.spletneseje.database.annotation.Table;
import org.spletneseje.fields.Field;
import org.spletneseje.fields.FieldType;

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
