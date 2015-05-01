package fields.w3c;

import fields.Field;

public class SubStatus implements Field {
	
	private int status;
	
	public SubStatus(String status) {
		this.status = Integer.valueOf(status);
	}

	@Override
	public String izpis() {
		return status+"";
	}

	@Override
	public String getKey() {
		return null;
	}	

}
