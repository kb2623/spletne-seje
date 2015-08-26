package org.sessionization.fields.w3c;

import org.sessionization.fields.Field;
import org.sessionization.fields.FieldType;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class SubStatus implements Field {

	@Column(name = "sub_status")
	private int status;

	public SubStatus() {
		status = 0;
	}
	
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
