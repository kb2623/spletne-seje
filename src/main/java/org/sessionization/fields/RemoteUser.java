package org.sessionization.fields;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class RemoteUser implements Field {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	private String user;

	public RemoteUser() {
		user = null;
	}

	public RemoteUser(String user) {
		if(!user.equalsIgnoreCase("-")) this.user = user;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
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

	@Override
	public FieldType getFieldType() {
		return FieldType.RemoteUser;
	}
}
