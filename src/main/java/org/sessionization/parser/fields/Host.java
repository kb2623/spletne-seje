package org.sessionization.parser.fields;

import org.hibernate.Query;
import org.hibernate.Session;
import org.sessionization.database.HibernateTable;
import org.sessionization.parser.LogField;

import javax.persistence.*;

@Entity
@Cacheable
public class Host implements LogField, HibernateTable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(unique = true, nullable = false)
	private String host;

	public Host() {
		id = null;
		host = null;
	}

	public Host(String hostName) {
		id = null;
		if (!hostName.equals("-")) {
			host = hostName;
		} else {
			host = " ";
		}
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	@Override
	public String izpis() {
		return host == null ? "-" : host;
	}

	@Override
	public Object setDbId(Session session) {
		if (getId() != null) {
			return getId();
		}
		Query query = session.createQuery("select h.id from " + getClass().getSimpleName() + " as h where h.host like '" + getHost() + "'");
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
		if (o == null || !(o instanceof Host)) return false;
		if (this == o) return true;
		Host that = (Host) o;
		return getHost() != null ? getHost().equals(that.getHost()) : that.getHost() == null;
	}

	@Override
	public int hashCode() {
		int result = getId() != null ? getId().hashCode() : 0;
		result = 31 * result + getHost().hashCode();
		return result;
	}

	@Override
	public String toString() {
		return "Host{" +
				"id=" + id +
				", host='" + host + '\'' +
				'}';
	}
}
