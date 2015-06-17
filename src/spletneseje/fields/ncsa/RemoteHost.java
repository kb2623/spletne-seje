package spletneseje.fields.ncsa;

import spletneseje.fields.Field;

public class RemoteHost implements Field {
	
	private String name;
	
	public RemoteHost(String name) {
		if(!name.equalsIgnoreCase("-")) this.name = name;
		else this.name = null;
	}

	@Override
	public String izpis() {
		return (name != null) ? name : "-";
	}

	@Override
	public String toString() {
		return name == null ? "-" : name;
	}

	@Override
	public String getKey() {
		return name;
	}

}
