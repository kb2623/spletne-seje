package spletneseje.fields.w3c;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import spletneseje.fields.Field;

public class Date implements Field{
	
	private LocalDate date;
	/**
	 * 
	 * @param date
	 * @param formatter
	 */
	public Date(String date, DateTimeFormatter formatter) {
		this.date = LocalDate.parse(date, formatter);
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
	public String getKey() {
		return null;
	}

}
