package org.spletneseje.fields.ncsa;

import org.spletneseje.database.annotation.Entry;
import org.spletneseje.database.annotation.Table;
import org.spletneseje.fields.Field;
import org.spletneseje.fields.FieldType;

@Table public class RemoteLogname implements Field {

	@Entry private String logname;
	
	public RemoteLogname(String logname) {
		if(!logname.equalsIgnoreCase("-")) {
			this.logname = logname;
		} else {
			this.logname = null;
		}
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
