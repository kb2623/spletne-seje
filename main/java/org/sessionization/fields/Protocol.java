package org.sessionization.fields;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "protpcol")
public class Protocol implements Field {

	@Column(name = "protocol")
	private String protocol;

	@Column(name = "version")
	private float version;

	public Protocol() {
		protocol = null;
		version = 0;
	}

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

	public void setProtocol(String protocol) {
		this.protocol = protocol;
	}

	public void setVersion(float version) {
		this.version = version;
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
