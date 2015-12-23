package org.sessionization.parser.fields;

import org.sessionization.parser.LogField;

import javax.persistence.*;

@Entity
@Cacheable
public class Host implements LogField {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	private String host;

	public Host() {
		id = null;
		host = null;
	}

	public Host(String hostName) {
		id = null;
		if (!hostName.equals("-")) {
			host = hostName;
		} else {
			host = null;
		}
	}

	public synchronized Integer getId() {
		return id;
	}

	public synchronized void setId(Integer id) {
		this.id = id;
	}

	public String getHost() {
		return host != null ? host : "";
	}

	public void setHost(String host) {
		this.host = host;
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
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Host host1 = (Host) o;
		if (getId() != null ? !getId().equals(host1.getId()) : host1.getId() != null) return false;
		if (!getHost().equals(host1.getHost())) return false;
		return true;
	}

	@Override
	public int hashCode() {
		int result = getId() != null ? getId().hashCode() : 0;
		result = 31 * result + getHost().hashCode();
		return result;
	}
}
