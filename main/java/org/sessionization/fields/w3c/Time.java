package org.sessionization.fields.w3c;

import org.sessionization.fields.Field;
import org.sessionization.fields.FieldType;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

@Embeddable
public class Time implements Field {

	@Column(name = "time")
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
		return o instanceof Time && time.equals(((Time) o).getTime());
	}

	@Override
	public FieldType getFieldType() {
		return FieldType.Time;
	}
}
