package org.sessionization.fields;

import java.net.MalformedURLException;

public class UriStem extends File implements Field {
	
	public UriStem(String resurse) throws MalformedURLException {
		super(resurse);
	}

	@Override
	public FieldType getFieldType() {
		return FieldType.UriStem;
	}
}