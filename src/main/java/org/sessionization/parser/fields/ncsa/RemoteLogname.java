package org.sessionization.parser.fields.ncsa;

import org.hibernate.Query;
import org.hibernate.Session;
import org.sessionization.database.HibernateUtil;
import org.sessionization.parser.LogField;

import javax.persistence.*;
import java.util.List;

@Entity
@Cacheable
public class RemoteLogname implements LogField, HibernateUtil.HibernateTable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(name = "name", unique = true, nullable = false)
	private String logname;

	public RemoteLogname() {
		id = null;
		logname = null;
	}

	public RemoteLogname(String logname) {
		id = null;
		if (!logname.equalsIgnoreCase("-")) {
			this.logname = logname;
		} else {
			this.logname = null;
		}
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getLogname() {
		return logname != null ? logname : "";
	}

	public void setLogname(String logname) {
		this.logname = logname;
	}

	@Override
	public String izpis() {
		return (logname != null) ? logname : "-";
	}

	@Override
	public String toString() {
		return (logname != null) ? logname : "-";
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		RemoteLogname that = (RemoteLogname) o;
		if (!getLogname().equals(that.getLogname())) return false;
		return true;
	}

	@Override
	public int hashCode() {
		int result = getId() != null ? getId().hashCode() : 0;
		result = 31 * result + getLogname().hashCode();
		return result;
	}

	@Override
	public String getKey() {
		return logname == null ? "-" : logname;
	}

	@Override
	public Object setDbId(Session session) {
		Integer id = getId();
		if (id == null) {
			Query query = session.createQuery("select r.id from " + getClass().getSimpleName() + " as r where r.logname = '" + getLogname() + "'");
			List list = query.list();
			if (!list.isEmpty()) {
				id = (Integer) list.get(0);
				setId(id);
			}
		}
		return id;
	}
}
