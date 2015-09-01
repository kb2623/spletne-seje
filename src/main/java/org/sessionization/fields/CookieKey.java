package org.sessionization.fields;

import javax.persistence.*;

@Entity
@Table(name = "cookie_key")
public class CookieKey {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Colunm(name = "cookie_key_id")
	private int id;

	private String name;

	public CookieKey() {}

	public CookieKey(String name) {
		this.name = name;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getId() {
		return id;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	@Override
	public boolean equals(Object o) {
		if (o instanceof CookieKey) {
			return getName().equals(((CookieKey) o).getName());
		} else {
			return false;
		}
	}
}
