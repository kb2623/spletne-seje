package org.sessionization.parser.fields;

import org.hibernate.Query;
import org.hibernate.Session;
import org.sessionization.database.HibernateUtil;
import org.sessionization.parser.LogField;

import javax.persistence.*;
import java.util.List;

@Entity
@Cacheable
public class RemoteUser implements LogField, HibernateUtil.HibernateTable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(name = "name", nullable = false, unique = true)
	private String user;

	public RemoteUser() {
		user = null;
	}

	public RemoteUser(String user) {
		if (!user.equalsIgnoreCase("-")) this.user = user;
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
	public String toString() {
		return (user != null) ? user : "-";
	}

	@Override
	public String getKey() {
		return user != null ? user : "-";
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		RemoteUser that = (RemoteUser) o;
		if (getId() != null ? !getId().equals(that.getId()) : that.getId() != null) return false;
		if (!getUser().equals(that.getUser())) return false;
		return true;
	}

	@Override
	public int hashCode() {
		int result = getId() != null ? getId().hashCode() : 0;
		result = 31 * result + getUser().hashCode();
		return result;
	}

	@Override
	public Object setDbId(Session session) {
		Integer id = getId();
		if (id == null) {
			Query query = session.createQuery("select r.id form " + getClass().getSimpleName() + " as r where r.user = '" + getUser() + "'");
			List list = query.list();
			if (!list.isEmpty()) {
				id = (Integer) list.get(0);
				setId(id);
			}
		}
		return id;
	}
}
