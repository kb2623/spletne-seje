package org.sessionization.fields.w3c;

import org.oosql.annotation.Column;
import org.oosql.annotation.Table;
import org.sessionization.fields.Field;
import org.sessionization.fields.FieldType;

@Table 
public class ComputerName implements Field {

	@Column 
	private String name;
	
	public ComputerName(String name) {
		if(!name.equalsIgnoreCase("-")) {
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
		return name == null ? "-" : name;
	}

	@Override
	public String toString() {
		return name != null ? name : "-";
	}

	@Override
	public boolean equals(Object o) {
		return o instanceof ComputerName && getName().equals(((ComputerName) o).getName());
	}
	
	@Override
	public String getKey() {
		return "";
	}

	@Override
	public FieldType getFieldType() {
		return FieldType.ComputerName;
	}
}
