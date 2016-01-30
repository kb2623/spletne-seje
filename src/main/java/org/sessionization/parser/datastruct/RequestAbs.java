package org.sessionization.parser.datastruct;

import org.sessionization.TimePoint;
import org.sessionization.database.HibernateUtil;

import javax.persistence.*;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "type", discriminatorType = DiscriminatorType.INTEGER)
@Cacheable
@Table(name = "Request")
public abstract class RequestAbs implements TimePoint, HibernateUtil.HibernateTable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	public RequestAbs() {
		this.id = null;
	}

	public RequestAbs(ParsedLine line) {
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
		if (o == null || !(o instanceof RequestAbs)) return false;
		return true;
	}

	@Override
	public int hashCode() {
		return getId() != null ? getId().hashCode() : 0;
	}
}
