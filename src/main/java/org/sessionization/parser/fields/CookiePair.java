package org.sessionization.parser.fields;

import org.hibernate.Query;
import org.hibernate.Session;
import org.sessionization.database.HibernateUtil;

import javax.persistence.*;
import java.util.List;

@Entity
@Cacheable
public class CookiePair implements HibernateUtil.HibernateTable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(nullable = false)
	private String value;

	@OneToOne(cascade = CascadeType.ALL)
	private CookieKey key;

	public CookiePair() {
		id = null;
		value = null;
		key = null;
	}

	public CookiePair(String key, String value) {
		id = null;
		this.value = value;
		this.key = new CookieKey(key);
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public CookieKey getKey() {
		return key;
	}

	public void setKey(CookieKey key) {
		this.key = key;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		CookiePair that = (CookiePair) o;
		if (getId() != null ? !getId().equals(that.getId()) : that.getId() != null) return false;
		if (getValue() != null ? !getValue().equals(that.getValue()) : that.getValue() != null) return false;
		if (getKey() != null ? !getKey().equals(that.getKey()) : that.getKey() != null) return false;
		return true;
	}

	@Override
	public int hashCode() {
		int result = getId() != null ? getId().hashCode() : 0;
		result = 31 * result + (getValue() != null ? getValue().hashCode() : 0);
		result = 31 * result + (getKey() != null ? getKey().hashCode() : 0);
		return result;
	}

	@Override
	public String toString() {
		return key.toString() + " = " + value;
	}

	@Override
	public Object setDbId(Session session) {
		getKey().setDbId(session);
		Query query = session.createQuery(
				"select cp.id form " + getClass().getSimpleName() + " as cp where cp.key = " + getKey().getId() + " and cp.value = '" + getValue() + "'"
		);
		List list = query.list();
		Integer id = null;
		if (!list.isEmpty()) {
			id = (Integer) list.get(0);
			setId(id);
		}
		return id;
	}
}

