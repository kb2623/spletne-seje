package org.spletneseje.fields;

public class SizeOfRequest implements Field {
	
	private int size;
	
	public SizeOfRequest(String size) {
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
		return o instanceof SizeOfRequest && size == ((SizeOfRequest) o).getSize();
	}

	@Override
	public FieldType getFieldType() {
		return FieldType.SizeOfRequest;
	}

}