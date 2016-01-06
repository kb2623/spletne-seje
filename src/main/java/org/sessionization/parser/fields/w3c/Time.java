package org.sessionization.parser.fields.w3c;

import org.sessionization.TimePoint;
import org.sessionization.parser.LogField;

import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

@Embeddable
@Cacheable(false)
public class Time implements LogField, TimePoint {

	@Column
	private LocalTime time;

	public Time() {
		time = null;
	}

	public Time(LocalTime time) {
		this.time = time;
	}

	/**
	 * @param time
	 * @param formatter
	 */
	public Time(String time, DateTimeFormatter formatter) {
		this.time = LocalTime.parse(time, formatter);
	}

	public LocalTime getTime() {
		return time != null ? time : LocalTime.MIN;
	}

	public void setTime(LocalTime time) {
		this.time = time;
	}

	@Override
	public String izpis() {
		return time.toString();
	}

	@Override
	public String toString() {
		return time.toString();
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Time time1 = (Time) o;
		if (!getTime().equals(time1.getTime())) return false;
		return true;
	}

	@Override
	public int hashCode() {
		return getTime().hashCode();
	}

	@Override
	public LocalTime getLocalTime() {
		return time;
	}
}
