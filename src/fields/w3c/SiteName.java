package fields.w3c;

import fields.Field;

public class SiteName implements Field {
	
	private String name;
	
	public SiteName(String name) {
		this.name = name;
	}

	@Override
	public String izpis() {
		return name;
	}

	@Override
	public String getKey() {
		return null;
	}

}
