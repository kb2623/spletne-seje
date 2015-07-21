package org.sessionization.fields;

import org.oosqljet.annotation.Column;
import org.oosqljet.annotation.Table;

@Table public class Protocol implements Field {

	@Column private String protocol;
	@Column(unique = true) private float version;

	public Protocol(String protocolAndVersion) {
		String[] tab = protocolAndVersion.split("/");
		if (tab.length == 1) {
			if (tab[0].equalsIgnoreCase("http")) {
				protocol = tab[0];
				version = 0;
			} else {
				protocol = "HTTP";
				version = Float.valueOf(tab[0]);
			}
		} else {
			protocol = tab[0];
			version = Float.valueOf(tab[1]);
		}
	}

	public String getProtocol() {
		return protocol != null ? protocol : "";
	}

	public float getVersion() {
		return version;
	}

	@Override
	public String izpis() {
		return protocol + " version " + version;
	}

	@Override
	public String toString() {
		return protocol + (version > 0 ? ("/" + version) : "");
	}

	@Override
	public boolean equals(Object o) {
		return o instanceof Protocol && version == ((Protocol) o).getVersion() && getProtocol().equals(((Protocol) o).getProtocol());
	}

	@Override
	public FieldType getFieldType() {
		return FieldType.ProtocolVersion;
	}

	@Override
	public String getKey() {
		return "";
	}
}
