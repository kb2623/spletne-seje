package org.sessionization.fields.ncsa;

import org.sessionization.fields.Field;
import org.sessionization.fields.FieldType;
import org.oosql.annotation.Column;
import org.oosql.annotation.Table;

@Table 
public class RemoteHost implements Field {

	@Column 
	private String name;
	
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
