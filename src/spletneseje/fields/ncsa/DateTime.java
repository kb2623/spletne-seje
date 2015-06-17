package spletneseje.fields.ncsa;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import spletneseje.fields.Field;
/**
 * <p>Razde hrani podatke o času in datumu</p>
 * @author klemen
 *
 */
public class DateTime implements Field {
	
	private LocalDateTime dateTime;
	/** 
	 * @param date Čas predstavljen z nizom
	 * @param formatter Objek, ki predstavlja format
	 */
	public DateTime(String date, DateTimeFormatter formatter) {
		this.dateTime = LocalDateTime.parse(date, formatter);
	}

	@Override
	public String izpis() {
		return dateTime.toString();
	}

	@Override
	public String getKey() {
		return null;
	}

}
