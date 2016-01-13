package org.sessionization.parser.datastruct;

import org.sessionization.TimePoint;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.LinkedList;
import java.util.List;

@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@Cacheable
public abstract class PageViewAbs implements TimePoint {

	@OneToMany(cascade = CascadeType.ALL)
	protected List<RequestAbs> requests;
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;

	public PageViewAbs() {
		id = null;
		requests = new LinkedList<>();
	}

	public PageViewAbs(ParsedLine line) {
		this();
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public List<RequestAbs> getRequests() {
		return requests;
	}

	public void setRequests(List<RequestAbs> requests) {
		this.requests = requests;
	}

	public abstract boolean addParsedLine(ParsedLine line);

	@Override
	public LocalDate getLocalDate() {
		if (requests != null) {
			return requests.get(requests.size() - 1).getLocalDate();
		} else {
			return null;
		}
	}

	@Override
	public LocalTime getLocalTime() {
		if (requests != null) {
			return requests.get(requests.size() - 1).getLocalTime();
		} else {
			return null;
		}
	}

	@Override
	public boolean equals(Object o) {
		if (o == null || !(o instanceof PageViewAbs)) return false;
		if (this == o) return true;
		PageViewAbs that = (PageViewAbs) o;
		if (getId() != null ? !getId().equals(that.getId()) : that.getId() != null) return false;
		if (getRequests() != null ? !getRequests().equals(that.getRequests()) : that.getRequests() != null) return false;
		return true;
	}

	@Override
	public int hashCode() {
		int result = getId() != null ? getId().hashCode() : 0;
		result = 31 * result + (getRequests() != null ? getRequests().hashCode() : 0);
		return result;
	}

	@Override
	public String toString() {
		return requests.toString();
	}
}