package org.sessionization.fields;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class SizeOfRequest implements Field {

	@Column(name = "size_of_request")
	private int size;

	public SizeOfRequest(String size) {
		this.size = Integer.valueOf(size);
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
	public String toString() {
		return String.valueOf(size);
	}

	@Override
	public boolean equals(Object o) {
		return o instanceof SizeOfRequest && size == ((SizeOfRequest) o).getSize();
	}

	@Override
	public FieldType getFieldType() {
		return FieldType.SizeOfRequest;
	}
}
