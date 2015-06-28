package org.spletneseje.fields.w3c;

import org.spletneseje.fields.Field;
import org.spletneseje.fields.FieldType;

public class UriStem implements Field {
	
	private String resurse;
	
	public UriStem(String resurse) {
		this.resurse = resurse;
	}
	
	public String getExtension() {
		int indexOfExtension = resurse.lastIndexOf('.');
		int indexOfLastSeparator = resurse.lastIndexOf('/');
		return (indexOfExtension < indexOfLastSeparator) ? null : resurse.substring(indexOfExtension + 1);
	}

	public String getResurse() {
		return resurse != null ? resurse : "";
	}

	@Override
	public String izpis() {
		return resurse;
	}

	@Override
	public String toString() {
		return resurse;
	}

	@Override
	public boolean equals(Object o) {
		return o instanceof UriStem && getResurse().equals(((UriStem) o).getResurse());
	}

	@Override
	public FieldType getFieldType() {
		return FieldType.UriStem;
	}
}
