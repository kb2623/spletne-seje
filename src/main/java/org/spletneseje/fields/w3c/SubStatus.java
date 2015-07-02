package org.spletneseje.fields.w3c;

import org.spletneseje.database.annotation.Entry;
import org.spletneseje.fields.Field;
import org.spletneseje.fields.FieldType;

public class SubStatus implements Field {

	@Entry private int status;
	
	public SubStatus(String status) {
		this.status = Integer.valueOf(status);
	}

	public int getStatus() {
		return status;
	}

	@Override
	public String izpis() {
		return String.valueOf(status);
	}

	@Override
	public String toString() {
		return String.valueOf(status);
	}

	@Override
	public boolean equals(Object o) {
		return o instanceof SubStatus && status == ((SubStatus) o).getStatus();
	}

	@Override
	public FieldType getFieldType() {
		return FieldType.SubStatus;
	}

}
