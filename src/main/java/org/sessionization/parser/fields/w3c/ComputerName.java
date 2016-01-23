package org.sessionization.parser.fields.w3c;

import org.hibernate.Query;
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
	public Object setDbId(Session session) {
		if (getId() != null) {
			return getId();
		}
		Query query = session.createQuery("select c.id form " + getClass().getSimpleName() + " c where c.name = '" + getName() + "'");
		for (Object o : query.list()) {
			if (equals(session.load(getClass(), (Integer) o))) {
				setId((Integer) o);
				return o;
			}
		}
		return null;
	}

	@Override
	public boolean equals(Object o) {
		if (o == null || !(o instanceof ComputerName)) return false;
		if (this == o) return true;
		ComputerName that = (ComputerName) o;
		return getName() != null ? getName().equals(that.getName()) : that.getName() == null;
	}

	@Override
	public int hashCode() {
		int result = getId() != null ? getId().hashCode() : 0;
		result = 31 * result + getName().hashCode();
		return result;
	}

	@Override
	public String toString() {
		return "ComputerName{" +
				"id=" + id +
				", name='" + name + '\'' +
				'}';
	}
}
