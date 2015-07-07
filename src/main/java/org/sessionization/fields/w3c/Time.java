package org.sessionization.fields.w3c;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import org.oosqljet.SqlMapping;
import org.sessionization.fields.Field;
import org.sessionization.fields.FieldType;
import org.oosqljet.annotation.Entry;

public class Time implements Field, SqlMapping<LocalTime, Long> {

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

    @Override
    public Long inMaping(LocalTime localTime) {
        // TODO
        return null;
    }

    @Override
    public LocalTime outMaping(Long in) {
        // TODO
        return null;
    }
}
