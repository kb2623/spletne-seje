package org.sessionization.parser.fields;

import org.hibernate.Session;
import org.sessionization.database.HibernateUtil;

import javax.persistence.*;

@Entity
@Cacheable
public class UriQueryKey implements HibernateUtil.HibernateTable {

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
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		UriQueryKey that = (UriQueryKey) o;
		if (getId() != null ? !getId().equals(that.getId()) : that.getId() != null) return false;
		if (getName() != null ? !getName().equals(that.getName()) : that.getName() != null) return false;
		return true;
	}

	@Override
	public int hashCode() {
		int result = getId() != null ? getId().hashCode() : 0;
		result = 31 * result + (getName() != null ? getName().hashCode() : 0);
		return result;
	}

	@Override
	public String toString() {
		return name;
	}

	//	return "select u.id form " + getClass().getSimpleName() + " u where u.name = '" + name + "'";
	@Override
	public Object setDbId(Session session) {
		// TODO: 1/14/16
		return null;
	}
}
