package org.spletneseje.fields.ncsa;

import org.spletneseje.database.annotation.Entry;
import org.spletneseje.database.annotation.Table;
import org.spletneseje.fields.Field;
import org.spletneseje.fields.FieldType;

@Table public class RemoteHost implements Field {

	@Entry private String name;
	
	public RemoteHost(String name) {
		if (!name.equalsIgnoreCase("-")) {
			this.name = name;
		} else {
			this.name = null;
		}
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
