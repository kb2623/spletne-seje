package org.sessionization.fields;

import javax.persistence.*;

@Entity
@Table(name = "query_key")
public class UriQueryKey {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Colunm(name = "query_key_id)
	private int id;

	private String name;

	public UriQueryKey() {}

	public UriQueryKey(String name) {
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
		if (o instanceof UriQueryKey) {
			return getName().equals(((UriQueryKey) o).getName());
		} else {
			return false;
		}
	}
}
