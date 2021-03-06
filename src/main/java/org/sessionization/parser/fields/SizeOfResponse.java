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
	public boolean equals(Object o) {
		if (o == null || !(o instanceof SizeOfResponse)) return false;
		if (this == o) return true;
		SizeOfResponse that = (SizeOfResponse) o;
		return getSize() == that.getSize();
	}

	@Override
	public int hashCode() {
		return getSize();
	}

	@Override
	public String toString() {
		return "SizeOfResponse{" +
				"size=" + size +
				'}';
	}
}
