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

	@OneToMany(cascade = CascadeType.ALL)
	protected List<PageViewAbs> pages;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	public UserSessionAbs() {
		id = null;
		pages = new ArrayList<>();
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

	public List<PageViewAbs> getPages() {
		return pages;
	}

	public void setPages(List<PageViewAbs> pages) {
		this.pages = pages;
	}

	protected boolean addPageView(PageViewAbs loadedPage) {
		if (pages != null && loadedPage != null) {
			return pages.add(loadedPage);
		} else {
			return false;
		}
	}

	public String getKey() {
		return "";
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
		if (!(o instanceof UserSessionAbs)) return false;
		UserSessionAbs that = (UserSessionAbs) o;
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
