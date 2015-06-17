package spletneseje.fields;

public class StatusCode implements Field {

	private int status;
	
	public StatusCode(String status) {
		this.status = Integer.valueOf(status);
	}
	
	@Override
	public String izpis() {
		return "" + status;
	}

	@Override
	public String getKey() {
		return null;
	}

}
