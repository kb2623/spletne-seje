package org.sessionization.fields;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class TimeTaken implements Field {

	@Column(name = "time_taken")
	private long time;

	private TimeTaken() {
		time = 0;
	}
	
	public TimeTaken(String time, boolean milliseconds) {
		if(milliseconds) {
			this.time = Long.valueOf(time);
		} else {
			this.time = Long.valueOf(time) * 1000;
		}
	}

	public void setTime(long time) {
		this.time = time;
	}

	public long getTime() {
		return time;
	}

	@Override
	public String izpis() {
		return String.valueOf(time);
	}

	@Override
	public String toString() {
		return String.valueOf(time);
	}

	@Override
	public boolean equals(Object o) {
		return o instanceof TimeTaken && time == ((TimeTaken) o).getTime();
	}

	@Override
	public FieldType getFieldType() {
		return FieldType.TimeTaken;
	}

}
