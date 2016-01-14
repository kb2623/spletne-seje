package org.sessionization.parser.fields.w3c;

import org.hibernate.Session;
import org.sessionization.database.HibernateUtil;
import org.sessionization.parser.LogField;

import javax.persistence.*;

@Entity
@Cacheable
public class ComputerName implements LogField, HibernateUtil.HibernateTable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(unique = true, nullable = false)
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

	//	return "select c.id form " + getClass().getSimpleName() + " c where c.name = '" + name + "'";
	@Override
	public Object setDbId(Session session) {
		// TODO: 1/14/16
		return null;
	}
}
