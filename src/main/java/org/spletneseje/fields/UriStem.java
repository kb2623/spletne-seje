package org.spletneseje.fields;

import org.spletneseje.database.annotation.Entry;

public class UriStem extends File {

	@Entry private String file;
	
	public UriStem(String resurse) {
		this.file = resurse;
	}

	@Override
	public String getExtension() {
		int indexOfExtension = file.lastIndexOf('.');
		int indexOfLastSeparator = file.lastIndexOf('/');
		return (indexOfExtension < indexOfLastSeparator) ? null : file.substring(indexOfExtension + 1);
	}

	@Override
	public String file() {
		return file;
	}

	public String getResurse() {
		return file != null ? file : "";
	}

	@Override
	public String izpis() {
		return file;
	}

	@Override
	public String toString() {
		return file;
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
