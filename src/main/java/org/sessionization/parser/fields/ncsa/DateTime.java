package org.sessionization.parser.fields.ncsa;

import org.sessionization.TimePoint;
import org.sessionization.parser.LogField;

import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

@Embeddable
@Cacheable(false)
public class DateTime implements LogField, TimePoint {

	@Column
	private LocalDate date;

	@Column
	private LocalTime time;

	public DateTime() {
		date = null;
		time = null;
	}

	public DateTime(LocalDateTime dateTime) {
		date = dateTime.toLocalDate();
		time = dateTime.toLocalTime();
	}

	/**
	 * @param date      Čas predstavljen z nizom
	 * @param formatter Objek, ki predstavlja format
	 */
	public DateTime(String date, DateTimeFormatter formatter) {
		LocalDateTime dateTime = LocalDateTime.parse(date, formatter);
		this.date = dateTime.toLocalDate();
		this.time = dateTime.toLocalTime();
	}

	public LocalDate getDate() {
		return date;
	}

	public void setDate(LocalDate date) {
		this.date = date;
	}

	public LocalTime getTime() {
		return time;
	}

	public void setTime(LocalTime time) {
		this.time = time;
	}

	@Override
	public String izpis() {
		return date.toString() + "T" + time.toString();
	}

	@Override
	public LocalDate getLocalDate() {
		return date;
	}

	@Override
	public LocalTime getLocalTime() {
		return time;
	}

	@Override
	public boolean equals(Object o) {
		if (o == null || !(o instanceof DateTime)) return false;
		if (this == o) return true;
		DateTime that = (DateTime) o;
		return getDate() != null ? getDate().equals(that.getDate()) : that.getDate() == null
				&& getTime() != null ? getTime().equals(that.getTime()) : that.getTime() == null;
	}

	@Override
	public int hashCode() {
		int result = getDate() != null ? getDate().hashCode() : 0;
		result = 31 * result + (getTime() != null ? getTime().hashCode() : 0);
		return result;
	}

	@Override
	public String toString() {
		return "DateTime{" +
				"date=" + date +
				", time=" + time +
				'}';
	}
}
