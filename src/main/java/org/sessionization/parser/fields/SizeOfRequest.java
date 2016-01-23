package org.sessionization.parser.fields;

import org.sessionization.parser.LogField;

import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
@Cacheable
public class SizeOfRequest implements LogField {

	@Column(name = "size_of_request")
	private int size;

	public SizeOfRequest() {
		size = 0;
	}

	public SizeOfRequest(String size) {
		this.size = Integer.valueOf(size);
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
		if (o == null || !(o instanceof SizeOfRequest)) return false;
		if (this == o) return true;
		SizeOfRequest that = (SizeOfRequest) o;
		return getSize() == that.getSize();
	}

	@Override
	public int hashCode() {
		return getSize();
	}

	@Override
	public String toString() {
		return "SizeOfRequest{" +
				"size=" + size +
				'}';
	}
}
