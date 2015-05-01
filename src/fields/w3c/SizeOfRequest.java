package fields.w3c;

import fields.Field;

public class SizeOfRequest implements Field {
	
	private int size;
	
	public SizeOfRequest(String size) {
		this.size = Integer.valueOf(size);
	}

	@Override
	public String izpis() {
		return size+"";
	}

	@Override
	public String getKey() {
		return null;
	}

}
