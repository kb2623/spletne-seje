package fields.w3c;

import fields.Field;

public class Host implements Field {
	
	private String host;
	
	public Host(String hostName) {
		host = hostName;
	}

	@Override
	public String izpis() {
		return host;
	}

	@Override
	public String getKey() {
		return null;
	}

}
