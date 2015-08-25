package org.sessionization.fields.w3c;

import org.sessionization.fields.Field;
import org.sessionization.fields.FieldType;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "computer_name")
public class ComputerName implements Field {

	@Column(name = "name")
	private String name;

	private ComputerName() {
		name = null;
	}

	public ComputerName(String name) {
		if(!name.equalsIgnoreCase("-")) {
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
