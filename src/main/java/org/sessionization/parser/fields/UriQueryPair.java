package org.sessionization.parser.fields;

import org.hibernate.Query;
import org.hibernate.Session;
import org.sessionization.database.HibernateUtil;

import javax.persistence.*;

@Entity
@Cacheable
public class UriQueryPair implements HibernateUtil.HibernateTable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Lob
	@Column(nullable = false)
	private String value;

	@OneToOne(cascade = CascadeType.ALL)
	private UriQueryKey key;

	public UriQueryPair() {
		id = null;
		value = null;
		key = null;
	}

	public UriQueryPair(String key, String value) {
		id = null;
		this.value = value;
		this.key = new UriQueryKey(key);
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

	public UriQueryKey getKey() {
		return key;
	}

	public void setKey(UriQueryKey key) {
		this.key = key;
	}

	@Override
	public Object setDbId(Session session) {
		Integer keyId = (Integer) getKey().setDbId(session);
		if (getId() != null) {
			return getId();
		}
		if (keyId != null) {
			Query query = session.createQuery("select qp.id from " + getClass().getSimpleName() + " as qp where qp.value like '" + getValue() + "' and qp.key = " + keyId);
			for (Object o : query.list()) {
				if (equals(session.load(getClass(), (Integer) o))) {
					setId((Integer) o);
					return o;
				}
			}
		}
		return null;
	}

	@Override
	public boolean equals(Object o) {
		if (o == null || !(o instanceof UriQueryPair)) return false;
		if (this == o) return true;
		UriQueryPair that = (UriQueryPair) o;
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
		return "UriQueryPair{" +
				"id=" + id +
				", value='" + value + '\'' +
				", key=" + key +
				'}';
	}
}

