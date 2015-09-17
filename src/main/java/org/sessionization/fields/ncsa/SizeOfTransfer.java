package org.sessionization.fields.ncsa;

import org.sessionization.fields.Field;
import org.sessionization.fields.FieldType;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class SizeOfTransfer implements Field {

	@Column(name = "size_of_transfer")
	private int size;

	public SizeOfTransfer() {
		size = 0;
	}

	public SizeOfTransfer(String niz) {
		size = Integer.valueOf(niz);
	}

	public void setSize(int size) {
		this.size = size;
	}

	public int getSize() {
		return size;
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
