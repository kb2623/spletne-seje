package org.sessionization.fields.w3c;

import org.sessionization.fields.Field;
import org.sessionization.fields.FieldType;
import org.oosql.annotation.Column;
import org.oosql.annotation.Table;

@Table 
public class SiteName implements Field {

	@Column 
	private String name;

	public SiteName(String name) {
		this.name = name;
	}

	public String getName() {
		return name != null ? name : "";
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
