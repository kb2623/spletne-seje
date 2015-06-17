package spletneseje.fields;

public class StatusCode implements Field {

	private int status;
	
	public StatusCode(String status) {
		this.status = Integer.valueOf(status);
	}
	
	@Override
	public String izpis() {
		return String.valueOf(status);
	}

	@Override
	public String toString() {
		return String.valueOf(status);
	}

	@Override
	public String getKey() {
		return null;
	}

}
