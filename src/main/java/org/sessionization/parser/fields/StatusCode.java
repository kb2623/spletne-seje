package org.sessionization.parser.fields;

import org.sessionization.parser.LogField;

import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
@Cacheable
public class StatusCode implements LogField {

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
	public boolean equals(Object o) {
		if (o == null || !(o instanceof StatusCode)) return false;
		if (this == o) return true;
		StatusCode that = (StatusCode) o;
		return getStatus() == that.getStatus();
	}

	@Override
	public int hashCode() {
		return getStatus();
	}

	@Override
	public String toString() {
		return "StatusCode{" +
				"status=" + status +
				'}';
	}
}
