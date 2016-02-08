package org.sessionization.parser.datastruct;

import org.sessionization.database.HibernateTable;
import org.sessionization.parser.fields.Address;
import org.sessionization.parser.fields.Cookie;
import org.sessionization.parser.fields.RemoteUser;
import org.sessionization.parser.fields.UserAgent;
import org.sessionization.parser.fields.ncsa.RemoteLogname;

import javax.persistence.*;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "type", discriminatorType = DiscriminatorType.INTEGER)
@Cacheable
@Table(name = "UserId")
public abstract class UserIdAbs implements HibernateTable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	public UserIdAbs() {
		this.id = null;
	}

	public UserIdAbs(ParsedLine line) {
		this();
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Address getAddress() {
		return null;
	}

	public RemoteLogname getRemoteLogname() {
		return null;
	}

	public RemoteUser getRemoteUser() {
		return null;
	}

	public UserAgent getUserAgent() {
		return null;
	}

	public Cookie getCookie() {
		return null;
	}

	@Override
	public boolean equals(Object o) {
		if (o == null || !(o instanceof UserIdAbs)) return false;
		return true;
	}

	@Override
	public int hashCode() {
		return getId() != null ? getId().hashCode() : 0;
	}

	/**
	 * @return
	 */
	public abstract String getKey();
}
