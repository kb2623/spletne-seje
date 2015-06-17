package spletneseje.fields;

public class TimeTaken implements Field {
	
	private long time;
	
	public TimeTaken(String time, boolean milliseconds) {
		if(milliseconds) {
			this.time = Long.valueOf(time);
		} else {
			this.time = Long.valueOf(time) * 1000;
		}
	}

	@Override
	public String izpis() {
		return time + "";
	}

	@Override
	public String getKey() {
		return null;
	}

}
