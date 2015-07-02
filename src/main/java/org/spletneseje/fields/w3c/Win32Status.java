package org.spletneseje.fields.w3c;

import org.spletneseje.database.annotation.Entry;
import org.spletneseje.fields.Field;
import org.spletneseje.fields.FieldType;

public class Win32Status implements Field {

	@Entry private int status;
	
	public Win32Status(String status) {
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
		return o instanceof Win32Status && status == ((Win32Status) o).getStatus();
	}

	@Override
	public FieldType getFieldType() {
		return FieldType.Win32Status;
	}

}
