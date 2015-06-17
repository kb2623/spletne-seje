package spletneseje.fields.w3c;

import spletneseje.fields.Field;

public class Win32Status implements Field {
	
	private int status;
	
	public Win32Status(String status) {
		this.status = Integer.valueOf(status);
	}

	@Override
	public String izpis() {
		return String.valueOf(status);
	}

	@Override
	public String toString() {
		return String.valueOf(status);
	}

	@Override
	public String getKey() {
		return null;
	}	

}
