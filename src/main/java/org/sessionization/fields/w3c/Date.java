package org.sessionization.fields.w3c;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.oosql.SqlMapping;
import org.sessionization.fields.Field;
import org.sessionization.fields.FieldType;

public class Date implements Field, SqlMapping<LocalDate, String> {

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

	@Override
	public String inMapping(LocalDate in) {
		// TODO
		return null;
	}

	@Override
	public LocalDate outMapping(String in) {
		// TODO
		return null;
	}
}
