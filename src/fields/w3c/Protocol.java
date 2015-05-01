package fields.w3c;

import fields.Field;

public class Protocol implements Field {
	
	private String protocol;
	
	public Protocol(String protocolAndVersion) {
		String[] tab = protocolAndVersion.split("/");
		protocol = tab[0];
	}

	@Override
	public String izpis() {
		return protocol;
	}

	@Override
	public String getKey() {
		return null;
	}

}
