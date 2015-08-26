package org.sessionization.fields;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "host")
public class Host implements Field {

	@Column(name = "host_name")
	private String host;

	public Host() {
		super();
		host = null;
	}
	
	public Host(String hostName) {
		if (!hostName.equals("-")) {
			host = hostName;
		} else {
			host = null;
		}
	}

	public void setHost(String host) {
		this.host = host;
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
