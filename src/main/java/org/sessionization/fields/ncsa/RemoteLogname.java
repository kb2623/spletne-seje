package org.sessionization.fields.ncsa;

import org.sessionization.fields.LogField;

import javax.persistence.*;

@Entity
@Cacheable
public class RemoteLogname implements LogField {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(name = "name")
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
		if (getId() != null ? !getId().equals(that.getId()) : that.getId() != null) return false;
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
}
