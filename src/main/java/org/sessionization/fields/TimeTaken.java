package org.sessionization.fields;

import org.oosqljet.annotation.Column;

public class TimeTaken implements Field {

	@Column private long time;
	
	public TimeTaken(String time, boolean milliseconds) {
		if(milliseconds) {
			this.time = Long.valueOf(time);
		} else {
			this.time = Long.valueOf(time) * 1000;
		}
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
