package org.sessionization.fields;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Protocol implements Field {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	private String protocol;

	private float version;

	public Protocol() {
		id = null;
		protocol = null;
		version = 0;
	}

	public Protocol(String protocolAndVersion) {
		id = null;
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

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
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
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Protocol protocol1 = (Protocol) o;
		if (Float.compare(protocol1.getVersion(), getVersion()) != 0) return false;
		if (getId() != null ? !getId().equals(protocol1.getId()) : protocol1.getId() != null) return false;
		if (!getProtocol().equals(protocol1.getProtocol())) return false;
		return true;
	}

	@Override
	public int hashCode() {
		int result = getId() != null ? getId().hashCode() : 0;
		result = 31 * result + getProtocol().hashCode();
		result = 31 * result + (getVersion() != +0.0f ? Float.floatToIntBits(getVersion()) : 0);
		return result;
	}

	@Override
	public FieldType getFieldType() {
		return FieldType.ProtocolVersion;
	}
}
