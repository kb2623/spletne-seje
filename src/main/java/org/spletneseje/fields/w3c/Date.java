package org.spletneseje.fields.w3c;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import org.spletneseje.database.annotation.Entry;
import org.spletneseje.fields.Field;
import org.spletneseje.fields.FieldType;

public class Date implements Field {

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

}
