package spletneseje.fields;

public class SizeOfResponse implements Field {
	
	private int size;
	
	public SizeOfResponse(String size) {
		this.size = Integer.valueOf(size);
	}

	@Override
	public String izpis() {
		return String.valueOf(size);
	}

	@Override
	public String toString() {
		return String.valueOf(size);
	}

	@Override
	public String getKey() {
		return null;
	}

}
