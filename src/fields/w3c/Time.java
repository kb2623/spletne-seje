package fields.w3c;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import fields.Field;

public class Time implements Field {
	
	private LocalTime time;
	/**
	 * 
	 * @param time
	 * @param formatter
	 */
	public Time(String time, DateTimeFormatter formatter) {
		this.time = LocalTime.parse(time, formatter);
	}

	@Override
	public String izpis() {
		return time.toString();
	}

	@Override
	public String getKey() {
		return null;
	}

}
