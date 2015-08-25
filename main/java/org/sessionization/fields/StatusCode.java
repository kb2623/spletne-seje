package org.sessionization.fields;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class StatusCode implements Field {

	@Column(name = "status_code")
	private int status;

	public StatusCode() {
		status = 0;
	}

	public StatusCode(String status) {
		this.status = Integer.valueOf(status);
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
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
		return o instanceof StatusCode && status == ((StatusCode) o).getStatus();
	}

	@Override
	public FieldType getFieldType() {
		return FieldType.StatusCode;
	}

}
