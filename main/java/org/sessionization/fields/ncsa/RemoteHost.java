package org.sessionization.fields.ncsa;

import org.sessionization.fields.Field;
import org.sessionization.fields.FieldType;
import org.sessionization.fields.RemoteUser;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "remote_host")
public class RemoteHost implements Field {

	@Column(name = "name")
	private String name;

	public RemoteHost() {
		name = null;
	}
	
	public RemoteHost(String name) {
		if (!name.equalsIgnoreCase("-")) {
			this.name = name;
		} else {
			this.name = null;
		}
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name != null ? name : "";
	}

	@Override
	public String izpis() {
		return (name != null) ? name : "-";
	}

	@Override
	public String toString() {
		return name == null ? "-" : name;
	}

	@Override
	public boolean equals(Object o) {
		return o instanceof RemoteHost && getName().equals(((RemoteHost) o).getName());
	}

	@Override
	public String getKey() {
		return name != null ? name : "";
	}

	@Override
	public FieldType getFieldType() {
		return FieldType.RemoteHost;
	}
}
