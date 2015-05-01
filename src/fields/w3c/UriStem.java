package fields.w3c;

import fields.Field;

public class UriStem implements Field {
	
	private String resurse;
	
	public UriStem(String resurse) {
		this.resurse = resurse;
	}
	
	public String getExtension() {
		int indexOfExtension = resurse.lastIndexOf('.');
		int indexOfLastSeparator = resurse.lastIndexOf('/');
		return (indexOfExtension < indexOfLastSeparator) ? 
				null : resurse.substring(indexOfExtension+1);
	}

	@Override
	public String izpis() {
		return resurse;
	}

	@Override
	public String getKey() {
		return null;
	}

}
