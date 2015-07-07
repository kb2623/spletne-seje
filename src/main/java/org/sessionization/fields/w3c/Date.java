package org.sessionization.fields.w3c;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import org.oosqljet.SqlMapping;
import org.sessionization.fields.Field;
import org.sessionization.fields.FieldType;
import org.oosqljet.annotation.Entry;

public class Date implements Field, SqlMapping<LocalDate, Long> {

	@Entry private LocalDate date;
	/**
	 * 
	 * @param date
	 * @param formatter
	 */
	public Date(String date, DateTimeFormatter formatter) {
		this.date = LocalDate.parse(date, formatter);
	}

	public LocalDate getDate() {
		return date;
	}

	@Override
	public String izpis() {
		return date.toString();
	}

	@Override
	public String toString() {
		return date.toString();
	}

	@Override
	public boolean equals(Object o) {
		return o instanceof Date && getDate().equals(((Date) o).getDate());
	}

	@Override
	public FieldType getFieldType() {
		return FieldType.Date;
	}

    @Override
    public Long inMaping(LocalDate localDate) {
        // TODO
        return null;
    }

    @Override
    public LocalDate outMaping(Long in) {
        // TODO
        return null;
    }
}
