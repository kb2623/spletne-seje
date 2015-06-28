package org.spletneseje.fields;

public class RemoteUser implements Field {
	
	private String user;
	
	public RemoteUser(String user) {
		if(!user.equalsIgnoreCase("-")) {
			this.user = user;
		} else {
			user = null;
		}
	}

	public String getUser() {
		return user != null ? user : "";
	}

	@Override
	public String izpis() {
		return (user != null) ? user : "-";
	}

	@Override
	public String toString() {
		return (user != null) ? user : "-";
	}

	@Override
	public String getKey() {
		return user != null ? user : "";
	}

	@Override
	public boolean equals(Object o) {
		return o instanceof RemoteUser && getUser().equals(((RemoteUser) o).getUser());
	}

	@Override
	public FieldType getFieldType() {
		return FieldType.RemoteUser;
	}
}
