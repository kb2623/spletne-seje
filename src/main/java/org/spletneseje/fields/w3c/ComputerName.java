package org.spletneseje.fields.w3c;

import org.spletneseje.fields.Field;
import org.spletneseje.fields.FieldType;

public class ComputerName implements Field {
	
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
	public FieldType getFieldType() {
		return FieldType.ComputerName;
	}
}