package org.sessionization.parser.datastruct;

import org.sessionization.TimePoint;

import javax.persistence.*;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@Cacheable
public abstract class AbsUserId implements TimePoint {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	public AbsUserId() {
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
		if (!(o instanceof AbsUserId)) return false;
		AbsUserId absUserId = (AbsUserId) o;
		if (getId() != null ? !getId().equals(absUserId.getId()) : absUserId.getId() != null) return false;
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

	/**
	 * @param line
	 * @return
	 */
	public abstract boolean addParsedLine(ParsedLine line);
}
