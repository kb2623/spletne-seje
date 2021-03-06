package org.sessionization.parser.fields;

import org.sessionization.parser.LogField;

import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
@Cacheable
public class TimeTaken implements LogField {

	@Column(name = "time_taken")
	private long time;

	public TimeTaken() {
		time = 0;
	}

	public TimeTaken(String time) {
		this.time = Long.valueOf(time);
	}

	public TimeTaken(Long time) {
		this.time = time;
	}

	public long getTime() {
		return time;
	}

	public void setTime(long time) {
		this.time = time;
	}

	@Override
	public String izpis() {
		return String.valueOf(time);
	}

	@Override
	public boolean equals(Object o) {
		if (o == null || !(o instanceof TimeTaken)) return false;
		if (this == o) return true;
		TimeTaken that = (TimeTaken) o;
		return getTime() == that.getTime();
	}

	@Override
	public int hashCode() {
		return (int) (getTime() ^ (getTime() >>> 32));
	}

	@Override
	public String toString() {
		return "TimeTaken{" +
				"time=" + time +
				'}';
	}
}
