package org.sessionization.parser.fields;

import org.hibernate.Query;
import org.hibernate.Session;
import org.sessionization.database.HibernateTable;
import org.sessionization.parser.LogField;

import javax.persistence.*;

@Entity
@Cacheable
public class Protocol implements LogField, HibernateTable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(nullable = false, length = 25)
	private String protocol;

	@Column(nullable = false, unique = true, length = 25)
	private String version;

	public Protocol() {
		id = null;
		protocol = null;
		version = null;
	}

	public Protocol(String protocolAndVersion) {
		id = null;
		String[] tab = protocolAndVersion.split("/");
		if (tab.length == 1) {
			if (tab[0].equalsIgnoreCase("HTTP")) {
				protocol = tab[0];
				version = "0";
			} else {
				protocol = "HTTP";
				version = tab[0];
			}
		} else {
			protocol = tab[0];
			version = tab[1];
		}
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getProtocol() {
		return protocol;
	}

	public void setProtocol(String protocol) {
		this.protocol = protocol;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	@Override
	public String izpis() {
		return protocol + " version " + version;
	}

	@Override
	public Object setDbId(Session session) {
		if (getId() != null) {
			return getId();
		}
		Query query = session.createQuery("select p.id from " + getClass().getSimpleName() + " as p where p.protocol like '" + getProtocol() + "' and p.version like '" + getVersion() + "'");
		for (Object o : query.list()) {
			if (equals(session.load(getClass(), (Integer) o))) {
				setId((Integer) o);
				return o;
			}
		}
		return null;
	}

	@Override
	public boolean equals(Object o) {
		if (o == null || !(o instanceof Protocol)) return false;
		if (this == o) return true;
		Protocol that = (Protocol) o;
		return getVersion().compareTo(that.getVersion()) == 0 &&
				getProtocol() != null ? getProtocol().equals(that.getProtocol()) : that.getProtocol() == null;
	}

	@Override
	public int hashCode() {
		int result = getId() != null ? getId().hashCode() : 0;
		result = 31 * result + (getProtocol() != null ? getProtocol().hashCode() : 0);
		result = 31 * result + (getVersion() != null ? getVersion().hashCode() : 0);
		return result;
	}

	@Override
	public String toString() {
		return "Protocol{" +
				"id=" + id +
				", protocol='" + protocol + '\'' +
				", version=" + version +
				'}';
	}
}
