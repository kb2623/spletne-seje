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
public abstract class AbsUserSession implements TimePoint {

	@OneToMany(cascade = CascadeType.ALL)
	protected List<AbsPageView> pages;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	public AbsUserSession() {
		id = null;
		pages = null;
	}

	public AbsUserSession(ParsedLine line) {
		id = null;
		pages = new LinkedList<>();
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public List<AbsPageView> getPages() {
		return pages;
	}

	public void setPages(List<AbsPageView> pages) {
		this.pages = pages;
	}

	protected boolean addPageView(AbsPageView loadedPage) {
		if (pages != null) {
			return pages.add(loadedPage);
		} else {
			return false;
		}
	}

	public abstract boolean addParsedLine(ParsedLine line);

	@Override
	public LocalDate getLocalDate() {
		if (pages != null) {
			return pages.get(pages.size() - 1).getLocalDate();
		} else {
			return null;
		}
	}

	@Override
	public LocalTime getLocalTime() {
		if (pages != null) {
			return pages.get(pages.size() - 1).getLocalTime();
		} else {
			return null;
		}
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof AbsUserSession)) return false;
		AbsUserSession that = (AbsUserSession) o;
		if (getId() != null ? !getId().equals(that.getId()) : that.getId() != null) return false;
		if (getPages() != null ? !getPages().equals(that.getPages()) : that.getPages() != null) return false;
		return true;
	}

	@Override
	public int hashCode() {
		int result = getId() != null ? getId().hashCode() : 0;
		result = 31 * result + (getPages() != null ? getPages().hashCode() : 0);
		return result;
	}
}
