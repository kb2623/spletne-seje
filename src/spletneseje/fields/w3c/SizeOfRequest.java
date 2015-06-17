package spletneseje.fields.w3c;

import spletneseje.fields.Field;

public class SizeOfRequest implements Field {
	
	private int size;
	
	public SizeOfRequest(String size) {
		this.size = Integer.valueOf(size);
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
	public String getKey() {
		return null;
	}

}
