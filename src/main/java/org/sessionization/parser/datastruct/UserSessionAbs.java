package org.sessionization.parser.datastruct;

import org.sessionization.TimePoint;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@Cacheable
public abstract class UserSessionAbs implements TimePoint {

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
		if (this == o) return true;
		if (!(o instanceof UserSessionAbs)) return false;
		UserSessionAbs that = (UserSessionAbs) o;
		if (getId() != null ? !getId().equals(that.getId()) : that.getId() != null) return false;
		return true;
	}

	@Override
	public int hashCode() {
		return getId() != null ? getId().hashCode() : 0;
	}
}
