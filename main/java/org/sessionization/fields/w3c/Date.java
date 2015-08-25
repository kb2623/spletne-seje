package org.sessionization.fields.w3c;

import org.sessionization.fields.Field;
import org.sessionization.fields.FieldType;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Embeddable
public class Date implements Field {

	@Column(name = "date")
	private LocalDate date;

	public Date() {
		date = null;
	}

	public Date(String date, DateTimeFormatter formatter) {
		this.date = LocalDate.parse(date, formatter);
	}

	public LocalDate getDate() {
		return date;
	}

	public void setDate(LocalDate date) {
		this.date = date;
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
