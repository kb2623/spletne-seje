package fields.w3c;

import fields.Field;

public class MetaData implements Field {
	
	private String data;
	
	public MetaData(String niz) {
		data = niz;
	}
	
	public String getMetaData() {
		return data;
	}

	@Override
	public String izpis() {
		return data;
	}

	@Override
	public String getKey() {
		return null;
	}

}
