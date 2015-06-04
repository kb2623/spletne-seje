package fields.w3c;

import fields.Field;

public class Protocol implements Field {
	
	private String protocol;
	private String version;
	
	public Protocol(String protocolAndVersion) {
		String[] tab = protocolAndVersion.split("/");
		if (tab.length == 1) {
			protocol = tab[0];
		} else {
			protocol = tab[0];
			version = tab[1];
		}
	}

	public String getProtocol() {
		return protocol;
	}

	public String getVersion() {
		return version != null ? version : "";
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
