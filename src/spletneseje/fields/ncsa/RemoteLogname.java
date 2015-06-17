package spletneseje.fields.ncsa;

import spletneseje.fields.Field;

public class RemoteLogname implements Field {
	
	private String logname;
	
	public RemoteLogname(String logname) {
		if(!logname.equalsIgnoreCase("-")) this.logname = logname;
		else this.logname = null;
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
	public String getKey() {
		return logname;
	}

}
