package org.sessionization.fields;

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
	public String toString() {
		return String.valueOf(size);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		SizeOfRequest that = (SizeOfRequest) o;
		if (getSize() != that.getSize()) return false;
		return true;
	}

	@Override
	public int hashCode() {
		return getSize();
	}
}
