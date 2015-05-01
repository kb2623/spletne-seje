package fields.ncsa;

import fields.Field;

public class RemoteLogname implements Field {
	
	private String logname;
	
	public RemoteLogname(String logname) {
		if(!logname.equalsIgnoreCase("-")) {
			this.logname = logname;
		}
	}

	@Override
	public String izpis() {
		return (logname != null) ? logname : "-";
	}

	@Override
	public String getKey() {
		return logname;
	}

}
