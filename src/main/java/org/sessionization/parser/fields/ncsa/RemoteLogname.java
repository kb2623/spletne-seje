package org.sessionization.parser.fields.ncsa;

import org.hibernate.Query;
import org.hibernate.Session;
import org.sessionization.database.HibernateUtil;
import org.sessionization.parser.LogField;

import javax.persistence.*;

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
			this.logname = "-";
		}
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getLogname() {
		return logname != null ? logname : "-";
	}

	public void setLogname(String logname) {
		this.logname = logname;
	}

	@Override
	public String izpis() {
		return (logname != null) ? logname : "-";
	}

	@Override
	public String getKey() {
		return logname == null ? "-" : logname;
	}

	@Override
	public Object setDbId(Session session) {
		if (getId() != null) {
			return getId();
		}
		Query query = session.createQuery("select r.id from " + getClass().getSimpleName() + " as r where r.logname like '" + getLogname() + "'");
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
		if (o == null || !(o instanceof RemoteLogname)) return false;
		if (this == o) return true;
		RemoteLogname that = (RemoteLogname) o;
		return getLogname() != null ? getLogname().equals(that.getLogname()) : that.getLogname() == null;
	}

	@Override
	public int hashCode() {
		int result = getId() != null ? getId().hashCode() : 0;
		result = 31 * result + getLogname().hashCode();
		return result;
	}

	@Override
	public String toString() {
		return "RemoteLogname{" +
				"id=" + id +
				", logname='" + logname + '\'' +
				'}';
	}
}
