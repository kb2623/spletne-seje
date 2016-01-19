package org.sessionization.parser.fields.ncsa;

import org.sessionization.parser.LogField;

import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
@Cacheable
public class KeepAliveNumber implements LogField {

	@Column(name = "keep_alive_number")
	private int number;

	public KeepAliveNumber() {
		number = 0;
	}

	public KeepAliveNumber(String niz) {
		number = Integer.valueOf(niz);
	}

	public int getNumber() {
		return number;
	}

	public void setNumber(int number) {
		this.number = number;
	}

	@Override
	public String izpis() {
		return number + "";
	}

	@Override
	public boolean equals(Object o) {
		if (o == null || !(o instanceof KeepAliveNumber)) return false;
		if (this == o) return true;
		KeepAliveNumber that = (KeepAliveNumber) o;
		return getNumber() == that.getNumber();
	}

	@Override
	public int hashCode() {
		return getNumber();
	}

	@Override
	public String toString() {
		return number + "";
	}
}
