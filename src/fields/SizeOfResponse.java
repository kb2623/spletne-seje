package fields;

public class SizeOfResponse implements Field {
	
	private int size;
	
	public SizeOfResponse(String size) {
		this.size = Integer.valueOf(size);
	}

	@Override
	public String izpis() {
		return "" + size;
	}

	@Override
	public String getKey() {
		return null;
	}

}
