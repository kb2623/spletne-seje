package spletneseje.fields.w3c;

import spletneseje.fields.Field;

public class ComputerName implements Field {
	
	private String name;
	
	public ComputerName(String name) {
		if(!name.equalsIgnoreCase("-")) this.name = name;
		else this.name = null;
	}

	@Override
	public String izpis() {
		return name == null ? "-" : name;
	}

	@Override
	public String toString() {
		return name != null ? name : "-";
	}

	@Override
	public String getKey() {
		return null;
	}

}