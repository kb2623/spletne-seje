package org.spletneseje.fields;

public class StatusCode implements Field {

	private int status;
	
	public StatusCode(String status) {
		this.status = Integer.valueOf(status);
	}

	public int getStatus() {
		return status;
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
	public boolean equals(Object o) {
		return o instanceof StatusCode && status == ((StatusCode) o).getStatus();
	}

	@Override
	public FieldType getFieldType() {
		return FieldType.StatusCode;
	}

}
