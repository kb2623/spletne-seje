package org.sessionization.fields.w3c;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import org.sessionization.fields.Field;
import org.sessionization.fields.FieldType;

public class Date implements Field {

	private LocalDate date;

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