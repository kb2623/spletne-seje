package org.sessionization.fields.ncsa;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.oosql.SqlMapping;
import org.sessionization.fields.Field;
import org.sessionization.fields.FieldType;

/**
 * <p>Razde hrani podatke o času in datumu</p>
 * @author klemen
 *
 */
public class DateTime implements Field, SqlMapping<LocalDateTime, String> {

	private LocalDateTime dateTime;
	/** 
	 * @param date Čas predstavljen z nizom
	 * @param formatter Objek, ki predstavlja format
	 */
	public DateTime(String date, DateTimeFormatter formatter) {
		this.dateTime = LocalDateTime.parse(date, formatter);
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

	@Override
	public String inMapping(LocalDateTime in) {
		// TODO
		return null;
	}

	@Override
	public LocalDateTime outMapping(String in) {
		// TODO
		return null;
	}
}
