package org.sessionization.parser.fields;

import org.sessionization.parser.LogField;

import javax.persistence.*;

@Entity
@Cacheable
public class RemoteUser implements LogField {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	private String user;

	public RemoteUser() {
		user = null;
	}

	public RemoteUser(String user) {
		if (!user.equalsIgnoreCase("-")) this.user = user;
	}

	public synchronized Integer getId() {
		return id;
	}

	public synchronized void setId(Integer id) {
		this.id = id;
	}

	public String getUser() {
		return user != null ? user : "";
	}

	public void setUser(String user) {
		this.user = user;
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
		return user != null ? user : "-";
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		RemoteUser that = (RemoteUser) o;
		if (getId() != null ? !getId().equals(that.getId()) : that.getId() != null) return false;
		if (!getUser().equals(that.getUser())) return false;
		return true;
	}

	@Override
	public int hashCode() {
		int result = getId() != null ? getId().hashCode() : 0;
		result = 31 * result + getUser().hashCode();
		return result;
	}
}
