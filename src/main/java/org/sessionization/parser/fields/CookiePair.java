package org.sessionization.parser.fields;

import org.hibernate.Query;
import org.hibernate.Session;
import org.sessionization.database.HibernateUtil;

import javax.persistence.*;

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
		if (o == null || !(o instanceof CookiePair)) return false;
		if (this == o) return true;
		CookiePair that = (CookiePair) o;
		return getValue() != null ? getValue().equals(that.getValue()) : that.getValue() == null
				&& getKey() != null ? getKey().equals(that.getKey()) : that.getKey() == null;
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
		Integer keyId = (Integer) getKey().setDbId(session);
		if (getId() != null) {
			return getId();
		}
		Query query = session.createQuery("select cp.id form " + getClass().getSimpleName() + " as cp where cp.key = " + keyId + " and cp.value = '" + getValue() + "'");
		for (Object o : query.list()) {
			if (equals(session.load(getClass(), (Integer) o))) {
				setId((Integer) o);
				return o;
			}
		}
		return null;
	}
}

