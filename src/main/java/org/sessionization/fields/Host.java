package org.sessionization.fields;

import org.oosqljet.annotation.Column;
import org.oosqljet.annotation.Table;

@Table public class Host implements Field {

	@Column private String host;
	
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
