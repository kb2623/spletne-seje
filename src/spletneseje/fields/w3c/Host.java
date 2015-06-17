package spletneseje.fields.w3c;

import spletneseje.fields.Field;

public class Host implements Field {
	
	private String host;
	
	public Host(String hostName) {
		if (!hostName.equals("-")) host = hostName;
		else host = null;
	}

	@Override
	public String izpis() {
		return host == null ? "-" : host;
	}

	@Override
	public String toString() {
		return host != null ? host : "-";
	}

	@Override
	public String getKey() {
		return null;
	}

}
