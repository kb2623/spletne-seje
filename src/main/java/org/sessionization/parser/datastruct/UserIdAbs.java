package org.sessionization.parser.datastruct;

import org.sessionization.database.HibernateUtil;

import javax.persistence.*;

@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@Cacheable
public abstract class UserIdAbs implements HibernateUtil.HibernateTable {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
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
