package org.sessionization.parser.fields.w3c;

import org.sessionization.parser.LogField;

import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
@Cacheable
public class Win32Status implements LogField {

	@Column(name = "win_32_status")
	private int status;

	public Win32Status() {
		status = 0;
	}

	public Win32Status(String status) {
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
		if (o == null || !(o instanceof Win32Status)) return false;
		if (this == o) return true;
		Win32Status that = (Win32Status) o;
		return getStatus() == that.getStatus();
	}

	@Override
	public int hashCode() {
		return getStatus();
	}
}
