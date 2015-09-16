package org.sessionization.fields.w3c;

import org.sessionization.fields.Field;
import org.sessionization.fields.FieldType;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Embeddable
public class Date implements Field {

	@Column(name = "data")
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
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Date date1 = (Date) o;
		if (getDate() != null ? !getDate().equals(date1.getDate()) : date1.getDate() != null) return false;
		return true;
	}

	@Override
	public int hashCode() {
		return getDate() != null ? getDate().hashCode() : 0;
	}

	@Override
	public FieldType getFieldType() {
		return FieldType.Date;
	}
}
