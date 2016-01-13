package org.sessionization.parser.datastruct;

import org.sessionization.TimePoint;

import javax.persistence.*;

@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@Cacheable
public abstract class RequestAbs implements TimePoint {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
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
		if (this == o) return true;
		if (!(o instanceof RequestAbs)) return false;
		RequestAbs that = (RequestAbs) o;
		if (getId() != null ? !getId().equals(that.getId()) : that.getId() != null) return false;
		return true;
	}

	@Override
	public int hashCode() {
		return getId() != null ? getId().hashCode() : 0;
	}
}
