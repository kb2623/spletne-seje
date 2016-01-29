package org.sessionization.parser.fields.w3c;

import org.sessionization.TimePoint;
import org.sessionization.parser.LogField;

import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Embeddable
@Cacheable(false)
public class Date implements LogField, TimePoint {

	@Column
	private LocalDate date;

	public Date() {
		date = null;
	}

	public Date(LocalDate date) {
		this.date = date;
	}

	public Date(String date, DateTimeFormatter formatter) {
		this.date = LocalDate.parse(date, formatter);
	}

	public LocalDate getDate() {
		return date != null ? date : LocalDate.MIN;
	}

	public void setDate(LocalDate date) {
		this.date = date;
	}

	@Override
	public String izpis() {
		return date.toString();
	}

	@Override
	public LocalDate getLocalDate() {
		return date;
	}

	@Override
	public boolean equals(Object o) {
		if (o == null || !(o instanceof Date)) return false;
		if (this == o) return true;
		Date that = (Date) o;
		return getDate() != null ? getDate().equals(that.getDate()) : that.getDate() == null;
	}

	@Override
	public int hashCode() {
		return getDate().hashCode();
	}

	@Override
	public String toString() {
		return "Date{" +
				"date=" + date +
				'}';
	}
}
