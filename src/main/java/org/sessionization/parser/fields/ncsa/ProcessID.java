package org.sessionization.parser.fields.ncsa;

import org.sessionization.parser.LogField;

import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
@Cacheable
public class ProcessID implements LogField {

	@Column(name = "process_id")
	private int pId;

	public ProcessID() {
		pId = 0;
	}

	public ProcessID(String niz) {
		pId = Integer.valueOf(niz);
	}

	public int getpId() {
		return pId;
	}

	public void setpId(int pId) {
		this.pId = pId;
	}

	@Override
	public String izpis() {
		return pId + "";
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		ProcessID that = (ProcessID) o;
		return getpId() == that.getpId();
	}

	@Override
	public int hashCode() {
		return getpId();
	}

	@Override
	public String toString() {
		return pId + "";
	}
}
