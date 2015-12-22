package org.sessionization.parser.fields.w3c;

import org.sessionization.parser.LogField;

import javax.persistence.*;

@Entity
@Cacheable
public class SiteName implements LogField {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	private String name;

	public SiteName() {
		id = null;
		name = null;
	}

	public SiteName(String name) {
		id = null;
		this.name = name;
	}

	public synchronized Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name != null ? name : "";
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String izpis() {
		return name;
	}

	@Override
	public String toString() {
		return name;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		SiteName siteName = (SiteName) o;
		if (getId() != null ? !getId().equals(siteName.getId()) : siteName.getId() != null) return false;
		if (!getName().equals(siteName.getName())) return false;
		return true;
	}

	@Override
	public int hashCode() {
		int result = getId() != null ? getId().hashCode() : 0;
		result = 31 * result + getName().hashCode();
		return result;
	}
}
