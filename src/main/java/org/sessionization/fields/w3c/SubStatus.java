package org.sessionization.fields.w3c;

import org.sessionization.fields.LogField;

import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
@Cacheable
public class SubStatus implements LogField {

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
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		SubStatus subStatus = (SubStatus) o;
		if (getStatus() != subStatus.getStatus()) return false;
		return true;
	}

	@Override
	public int hashCode() {
		return getStatus();
	}
}
