package org.sessionization.fields.w3c;

import org.sessionization.fields.Field;
import org.sessionization.fields.FieldType;

import javax.persistence.Embeddable;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

@Embeddable
public class Time implements Field {

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
		return time;
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
		if (getTime() != null ? !getTime().equals(time1.getTime()) : time1.getTime() != null) return false;
		return true;
	}

	@Override
	public int hashCode() {
		return getTime() != null ? getTime().hashCode() : 0;
	}

	@Override
	public FieldType getFieldType() {
		return FieldType.Time;
	}
}
