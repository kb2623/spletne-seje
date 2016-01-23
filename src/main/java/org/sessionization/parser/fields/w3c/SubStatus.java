package org.sessionization.parser.fields.w3c;

import org.sessionization.parser.LogField;

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
	public boolean equals(Object o) {
		if (o == null || !(o instanceof SubStatus)) return false;
		if (this == o) return true;
		SubStatus that = (SubStatus) o;
		return getStatus() == that.getStatus();
	}

	@Override
	public int hashCode() {
		return getStatus();
	}

	@Override
	public String toString() {
		return "SubStatus{" +
				"status=" + status +
				'}';
	}
}
