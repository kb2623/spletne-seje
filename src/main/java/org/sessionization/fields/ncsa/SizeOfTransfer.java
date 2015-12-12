package org.sessionization.fields.ncsa;

import org.sessionization.fields.LogField;

import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
@Cacheable
public class SizeOfTransfer implements LogField {

	@Column(name = "size_of_transfer")
	private int size;

	public SizeOfTransfer() {
		size = 0;
	}

	public SizeOfTransfer(String niz) {
		size = Integer.valueOf(niz);
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}

	@Override
	public String izpis() {
		return String.valueOf(size);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		SizeOfTransfer that = (SizeOfTransfer) o;
		if (getSize() != that.getSize()) return false;
		return true;
	}

	@Override
	public int hashCode() {
		return getSize();
	}

	@Override
	public String toString() {
		return String.valueOf(size);
	}
}
