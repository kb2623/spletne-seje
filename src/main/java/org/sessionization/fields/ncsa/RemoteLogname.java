package org.sessionization.fields.ncsa;

import org.sessionization.fields.Field;
import org.sessionization.fields.FieldType;
import org.oosql.annotation.Column;
import org.oosql.annotation.Table;

@Table 
public class RemoteLogname implements Field {

	@Column 
	private String logname;
	
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
