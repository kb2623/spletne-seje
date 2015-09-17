package org.sessionization.fields.ncsa;

import org.sessionization.fields.Field;
import org.sessionization.fields.FieldType;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Embeddable
public class DateTime implements Field {

	@Column(name = "date")
	private LocalDateTime dateTime;

	public DateTime() {
		dateTime = null;
	}
	/** 
	 * @param date Čas predstavljen z nizom
	 * @param formatter Objek, ki predstavlja format
	 */
	public DateTime(String date, DateTimeFormatter formatter) {
		this.dateTime = LocalDateTime.parse(date, formatter);
	}

	public void setDateTime(LocalDateTime dateTime) {
		this.dateTime = dateTime;
	}

	public LocalDateTime getDateTime() {
		return dateTime != null ? dateTime : LocalDateTime.MIN;
	}

	@Override
	public String izpis() {
		return dateTime.toString();
	}

	@Override
	public String toString() {
		return dateTime.toString();
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		DateTime dateTime1 = (DateTime) o;
		if (!getDateTime().equals(dateTime1.getDateTime())) return false;
		return true;
	}

	@Override
	public int hashCode() {
		return getDateTime().hashCode();
	}

	@Override
	public FieldType getFieldType() {
		return FieldType.DateTime;
	}
}
