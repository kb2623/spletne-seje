package org.sessionization.fields.ncsa;

import org.sessionization.fields.Field;
import org.sessionization.fields.FieldType;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "remote_logname")
public class RemoteLogname implements Field {

	@Column(name = "name")
	private String logname;

	public RemoteLogname() {
		logname = null;
	}

	public RemoteLogname(String logname) {
		if(!logname.equalsIgnoreCase("-")) {
			this.logname = logname;
		} else {
			this.logname = null;
		}
	}

	public void setLogname(String logname) {
		this.logname = logname;
	}

	public String getLogname() {
		return logname != null ? logname : "";
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
		return o instanceof RemoteLogname && getLogname().equals(((RemoteLogname) o).getLogname());
	}

	@Override
	public String getKey() {
		return logname == null ? "" : logname;
	}

	@Override
	public FieldType getFieldType() {
		return FieldType.RemoteLogname;
	}

}
