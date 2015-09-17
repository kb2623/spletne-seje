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
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		TimeTaken timeTaken = (TimeTaken) o;
		if (getTime() != timeTaken.getTime()) return false;
		return true;
	}

	@Override
	public int hashCode() {
		return (int) (getTime() ^ (getTime() >>> 32));
	}
}
