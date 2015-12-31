package org.sessionization.parser.datastruct;

import org.sessionization.TimePoint;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.LinkedList;
import java.util.List;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@Cacheable
public abstract class APageView implements TimePoint {

	@OneToMany(cascade = CascadeType.ALL)
	protected List<ARequest> requests;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	public APageView() {
		id = null;
		requests = new LinkedList<>();
	}

	public synchronized Integer getId() {
		return id;
	}

	public synchronized void setId(Integer id) {
		this.id = id;
	}

	public List<ARequest> getRequests() {
		return requests;
	}

	public void setRequests(List<ARequest> requests) {
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
		if (this == o) return true;
		if (!(o instanceof APageView)) return false;
		APageView that = (APageView) o;
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
}