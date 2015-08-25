package org.sessionization.fields.ncsa;

import org.sessionization.fields.Field;
import org.sessionization.fields.FieldType;
import org.sessionization.fields.w3c.Date;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * <p>Razde hrani podatke o času in datumu</p>
 * @author klemen
 *
 */
@Embeddable
public class DateTime implements Field {

	@Column(name = "date_time")
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
		return dateTime;
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
		return o instanceof DateTime && dateTime.equals(((DateTime) o).getDateTime());
	}

	@Override
	public FieldType getFieldType() {
		return FieldType.DateTime;
	}
}
