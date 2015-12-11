package org.sessionization.parser.fields;

import org.sessionization.database.InetAddressConverter;
import org.sessionization.fields.Address;
import org.sessionization.fields.LogField;

import java.io.Reader;

public class LoaclIP extends AbsParserField {

	public LoaclIP() {
		super();
	}

	@Override
	public String getFormatString() {
		return "%A";
	}

	@Override
	public LogField parse(Reader reader) {
		return null;
	}

	@Override
	public Class<? extends LogField> getCClass() {
		return Address.class;
	}

	@Override
	public Class[] getDependencies() {
		return new Class[]{
				InetAddressConverter.class
		};
	}
}
