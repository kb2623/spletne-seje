package org.sessionization.fields;

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
	public String toString() {
		return String.valueOf(status);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		StatusCode that = (StatusCode) o;
		if (getStatus() != that.getStatus()) return false;
		return true;
	}

	@Override
	public int hashCode() {
		return getStatus();
	}
}
