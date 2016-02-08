package org.sessionization.parser.fields;

import org.hibernate.Session;
import org.sessionization.database.HibernateTable;

import javax.persistence.*;

@Entity
@Cacheable
public class UriQueryKey implements HibernateTable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(unique = true, nullable = false)
	private String name;

	public UriQueryKey() {
		id = null;
		name = null;
	}

	public UriQueryKey(String name) {
		this.name = name;
		id = null;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public Object setDbId(Session session) {
		if (getId() != null) {
			return getId();
		}
		org.hibernate.Query query = session.createQuery("select u.id from " + getClass().getSimpleName() + " as u where u.name like '" + getName() + "'");
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
		if (o == null || !(o instanceof UriQueryKey)) return false;
		if (this == o) return true;
		UriQueryKey that = (UriQueryKey) o;
		return getName() != null ? getName().equals(that.getName()) : that.getName() == null;
	}

	@Override
	public int hashCode() {
		int result = getId() != null ? getId().hashCode() : 0;
		result = 31 * result + (getName() != null ? getName().hashCode() : 0);
		return result;
	}

	@Override
	public String toString() {
		return "UriQueryKey{" +
				"id=" + id +
				", name='" + name + '\'' +
				'}';
	}
}
