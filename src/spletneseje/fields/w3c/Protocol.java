package spletneseje.fields.w3c;

import spletneseje.fields.Field;

public class Protocol implements Field {
	
	private String protocol;
	private String version;
	
	public Protocol(String protocolAndVersion) {
		String[] tab = protocolAndVersion.split("/");
		if (tab.length == 1) {
			protocol = tab[0];
			version = null;
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
		return protocol + (version != null ? " version " + version : "");
	}

	@Override
	public String toString() {
		return protocol + (version == null ? "" : " version " + version);
	}

	@Override
	public String getKey() {
		return null;
	}

}
