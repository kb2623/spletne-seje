package org.spletneseje.fields;

import org.spletneseje.database.annotation.Entry;
import org.spletneseje.database.annotation.Table;

@Table public class RemoteUser implements Field {

	@Entry private String user = null;
	
	public RemoteUser(String user) {
		if(!user.equalsIgnoreCase("-")) this.user = user;
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
