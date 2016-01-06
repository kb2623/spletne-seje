package org.sessionization.parser.datastruct;

import javax.persistence.*;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@Cacheable
public abstract class UserIdAbs {

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

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof UserIdAbs)) return false;
		UserIdAbs userIdAbs = (UserIdAbs) o;
		if (getId() != null ? !getId().equals(userIdAbs.getId()) : userIdAbs.getId() != null) return false;
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