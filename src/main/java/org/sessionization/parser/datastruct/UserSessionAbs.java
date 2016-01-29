package org.sessionization.parser.datastruct;

import org.sessionization.TimePoint;
import org.sessionization.database.HibernateUtil;

import javax.persistence.*;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Cacheable
public abstract class UserSessionAbs implements TimePoint, HibernateUtil.HibernateTable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	public UserSessionAbs() {
		id = null;
	}

	public UserSessionAbs(ParsedLine line) {
		this();
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public abstract String getKey();

	public abstract boolean addParsedLine(ParsedLine line);

	@Override
	public boolean equals(Object o) {
		if (o == null || !(o instanceof UserSessionAbs)) return false;
		return true;
	}

	@Override
	public int hashCode() {
		return getId() != null ? getId().hashCode() : 0;
	}
}
