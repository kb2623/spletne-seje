package org.sessionization.parser.fields;

import javax.persistence.*;

@Entity
@Cacheable
public class CookiePair {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

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

	public synchronized Integer getId() {
		return id;
	}

	public synchronized void setId(Integer id) {
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
}

