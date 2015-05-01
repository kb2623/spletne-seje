package fields.w3c;

import fields.Field;

public class ComputerName implements Field {
	
	private String name;
	
	public ComputerName(String name) {
		if(!name.equalsIgnoreCase("-")) {
			this.name = name;
		}
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