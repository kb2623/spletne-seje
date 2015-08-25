package org.sessionization.fields.w3c;

import org.sessionization.fields.Field;
import org.sessionization.fields.FieldType;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "site_name")
public class SiteName implements Field {

	@Column(name = "name")
	private String name;

	public SiteName() {
		name = null;
	}

	public SiteName(String name) {
		this.name = name;
	}

	public String getName() {
		return name != null ? name : "";
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String izpis() {
		return name;
	}

	@Override
	public String toString() {
		return name;
	}

	@Override
	public boolean equals(Object o) {
		return o instanceof SiteName && getName().equals(((SiteName) o).getName());
	}

	@Override
	public FieldType getFieldType() {
		return FieldType.SiteName;
	}

	@Override
	public String getKey() {
		return "";
	}
}
