package fields.ncsa;

import fields.Field;

public class RemoteHost implements Field {
	
	private String name;
	
	public RemoteHost(String name) {
		if(!name.equalsIgnoreCase("-")) {
			this.name = name;
		}
	}

	@Override
	public String izpis() {
		return (name != null) ? name : "-";
	}

	@Override
	public String getKey() {
		return name;
	}

}
