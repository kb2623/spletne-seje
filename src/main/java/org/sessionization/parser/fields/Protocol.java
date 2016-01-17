package org.sessionization.parser.fields;

import org.hibernate.Query;
import org.hibernate.Session;
import org.sessionization.database.HibernateUtil;
import org.sessionization.parser.LogField;

import javax.persistence.*;
import java.util.List;

@Entity
@Cacheable
public class Protocol implements LogField, HibernateUtil.HibernateTable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(nullable = false)
	private String protocol;

	@Column(nullable = false)
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

	public String getProtocol() {
		return protocol != null ? protocol : "";
	}

	public void setProtocol(String protocol) {
		this.protocol = protocol;
	}

	public float getVersion() {
		return version;
	}

	public void setVersion(float version) {
		this.version = version;
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
	public Object setDbId(Session session) {
		Integer id = getId();
		if (id == null) {
			Query query = session.createQuery("select p.id form " + getClass().getSimpleName() + " as p where p.protocol = '" + getProtocol() + "' and p.version = " + getVersion());
			List list = query.list();
			if (!list.isEmpty()) {
				id = (Integer) list.get(0);
				setId(id);
			}
		}
		return id;
	}
}
