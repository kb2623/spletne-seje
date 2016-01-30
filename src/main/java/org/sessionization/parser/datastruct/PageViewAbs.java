package org.sessionization.parser.datastruct;

import org.hibernate.Session;
import org.sessionization.TimePoint;
import org.sessionization.database.HibernateUtil;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.LinkedList;
import java.util.List;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "type", discriminatorType = DiscriminatorType.INTEGER)
@Cacheable
@Table(name = "PageView")
public abstract class PageViewAbs implements TimePoint, HibernateUtil.HibernateTable {

	@OneToMany(cascade = CascadeType.ALL)
	@JoinTable(joinColumns = @JoinColumn(name = "pageview_id"))
	protected List<RequestAbs> requests;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
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
	public Object setDbId(Session session) {
		for (RequestAbs r : requests) {
			r.setDbId(session);
		}
		return null;
	}

	@Override
	public boolean equals(Object o) {
		if (o == null || !(o instanceof PageViewAbs)) return false;
		if (this == o) return true;
		PageViewAbs that = (PageViewAbs) o;
		if (getRequests() == null ? that.getRequests() == null : false) {
			return true;
		} else if (getRequests().size() == that.getRequests().size()) {
			return getRequests().containsAll(that.getRequests());
		} else {
			return false;
		}
	}

	@Override
	public int hashCode() {
		int result = getId() != null ? getId().hashCode() : 0;
		result = 31 * result + (getRequests() != null ? getRequests().hashCode() : 0);
		return result;
	}

	@Override
	public String toString() {
		return "PageViewAbs{" +
				"id=" + id +
				", requests=" + requests +
				'}';
	}
}