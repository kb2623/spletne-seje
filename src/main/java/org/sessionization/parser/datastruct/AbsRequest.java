package org.sessionization.parser.datastruct;

import org.sessionization.TimePoint;

import javax.persistence.*;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@Cacheable
public abstract class AbsRequest implements TimePoint {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	public AbsRequest() {
		this.id = null;
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
		if (!(o instanceof AbsRequest)) return false;
		AbsRequest that = (AbsRequest) o;
		if (getId() != null ? !getId().equals(that.getId()) : that.getId() != null) return false;
		return true;
	}

	@Override
	public int hashCode() {
		return getId() != null ? getId().hashCode() : 0;
	}
}
