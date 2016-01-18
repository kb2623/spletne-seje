package org.sessionization.parser.fields;

import org.sessionization.parser.LogField;

import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
@Cacheable
public class SizeOfResponse implements LogField {

	@Column(name = "size_of_response")
	private int size;

	public SizeOfResponse() {
		size = 0;
	}

	public SizeOfResponse(String size) {
		this.size = Integer.valueOf(size);
	}

	public int getSize() {
		return size;
	}

	@Override
	public String izpis() {
		return String.valueOf(size);
	}

	@Override
	public String toString() {
		return String.valueOf(size);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		SizeOfResponse that = (SizeOfResponse) o;
		return getSize() == that.getSize();
	}

	@Override
	public int hashCode() {
		return getSize();
	}
}
