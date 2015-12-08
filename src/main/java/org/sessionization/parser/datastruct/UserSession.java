package org.sessionization.parser.datastruct;

import org.sessionization.fields.TimePoint;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Entity
@Cacheable
public class UserSession implements TimePoint {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@OneToMany(cascade = CascadeType.ALL)
	private List<PageViewAbs> pages;

	public UserSession() {
		id = null;
		pages = null;
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
		if (!(o instanceof UserSession)) return false;
		UserSession that = (UserSession) o;
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
