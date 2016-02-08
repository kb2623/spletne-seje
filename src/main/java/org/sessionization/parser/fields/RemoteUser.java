package org.sessionization.parser.fields;

import org.hibernate.Query;
import org.hibernate.Session;
import org.sessionization.database.HibernateTable;
import org.sessionization.parser.LogField;

import javax.persistence.*;

@Entity
@Cacheable
public class RemoteUser implements LogField, HibernateTable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(name = "name", nullable = false, unique = true)
	private String user;

	public RemoteUser() {
		user = null;
	}

	public RemoteUser(String user) {
		if (!user.equalsIgnoreCase("-")) {
			this.user = user;
		} else {
			this.user = "-";
		}
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getUser() {
		return user != null ? user : "";
	}

	public void setUser(String user) {
		this.user = user;
	}

	@Override
	public String izpis() {
		return (user != null) ? user : "-";
	}

	@Override
	public String getKey() {
		return user != null ? user : "-";
	}

	@Override
	public Object setDbId(Session session) {
		if (getId() != null) {
			return getId();
		}
		Query query = session.createQuery("select r.id from " + getClass().getSimpleName() + " as r where r.user like '" + getUser() + "'");
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
		if (o == null || !(o instanceof RemoteUser)) return false;
		if (this == o) return true;
		RemoteUser that = (RemoteUser) o;
		return getUser() != null ? getUser().equals(that.getUser()) : that.getUser() == null;
	}

	@Override
	public int hashCode() {
		int result = getId() != null ? getId().hashCode() : 0;
		result = 31 * result + getUser().hashCode();
		return result;
	}

	@Override
	public String toString() {
		return "RemoteUser{" +
				"id=" + id +
				", user='" + user + '\'' +
				'}';
	}
}
