package org.sessionization.fields;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "remote_user")
public class RemoteUser implements Field {

	@Column(name = "user_name")
	private String user;

	public RemoteUser() {
		user = null;
	}

	public RemoteUser(String user) {
		if(!user.equalsIgnoreCase("-")) this.user = user;
	}

	public void setUser(String user) {
		this.user = user;
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
