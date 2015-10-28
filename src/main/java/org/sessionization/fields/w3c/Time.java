package org.sessionization.fields.w3c;

import org.sessionization.fields.LogField;

import javax.persistence.Cacheable;
import javax.persistence.Embeddable;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

@Embeddable
@Cacheable(false)
public class Time implements LogField {

	private LocalTime time;

	public Time() {
		time = null;
	}
	/**
	 * 
	 * @param time
	 * @param formatter
	 */
	public Time(String time, DateTimeFormatter formatter) {
		this.time = LocalTime.parse(time, formatter);
	}

	public void setTime(LocalTime time) {
		this.time = time;
	}

	public LocalTime getTime() {
		return time != null ? time : LocalTime.MIN;
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
}
