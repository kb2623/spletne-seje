package org.sessionization.fields.w3c;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import org.oosql.ISqlMapping;
import org.sessionization.fields.Field;
import org.sessionization.fields.FieldType;

public class Time implements Field, ISqlMapping<LocalTime, String> {

	private LocalTime time;
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

	@Override
	public String inMapping(LocalTime in) {
		// TODO
		return null;
	}

	@Override
	public LocalTime outMapping(String in) {
		// TODO
		return null;
	}
}
