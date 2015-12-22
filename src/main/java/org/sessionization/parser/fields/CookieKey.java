package org.sessionization.parser.fields;

import javax.persistence.*;

@Entity
@Cacheable
public class CookieKey {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	private String name;

	public CookieKey() {
		id = null;
		name = null;
	}

	public CookieKey(String name) {
		this.name = name;
	}

	public synchronized Integer getId() {
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

		CookieKey cookieKey = (CookieKey) o;

		if (getId() != null ? !getId().equals(cookieKey.getId()) : cookieKey.getId() != null) return false;
		if (getName() != null ? !getName().equals(cookieKey.getName()) : cookieKey.getName() != null) return false;

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
}
