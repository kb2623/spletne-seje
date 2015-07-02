package org.spletneseje.fields.w3c;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import org.spletneseje.database.annotation.Entry;
import org.spletneseje.fields.Field;
import org.spletneseje.fields.FieldType;

public class Time implements Field {

	@Entry private LocalTime time;
	/**
	 * 
	 * @param time
	 * @param formatter
	 */
	public Time(String time, DateTimeFormatter formatter) {
		this.time = LocalTime.parse(time, formatter);
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
