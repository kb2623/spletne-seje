package org.sessionization.fields.w3c;

import org.sessionization.fields.LogField;

import javax.persistence.*;

@Entity
@Cacheable
public class ComputerName implements LogField {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	private String name;

	private ComputerName() {
		id = null;
		name = null;
	}

	public ComputerName(String name) {
		id = null;
		if (!name.equalsIgnoreCase("-")) {
			this.name = name;
		} else {
			this.name = null;
		}
	}

	public Integer getId() {
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
		return name == null ? "-" : name;
	}

	@Override
	public String toString() {
		return name != null ? name : "-";
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		ComputerName that = (ComputerName) o;
		if (getId() != null ? !getId().equals(that.getId()) : that.getId() != null) return false;
		if (!getName().equals(that.getName())) return false;
		return true;
	}

	@Override
	public int hashCode() {
		int result = getId() != null ? getId().hashCode() : 0;
		result = 31 * result + getName().hashCode();
		return result;
	}
}
