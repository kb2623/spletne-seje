package org.sessionization.fields;

import org.oosql.annotation.Column;
import org.oosql.annotation.Table;

import java.net.URL;

@Table 
public abstract class File implements Field {

	@Column 
	private String file;

	public File(String file) {
		if(!file.equals("-"))  this.file = file;
		else this.file = null;
	}
	/**
	 * Getter metoda za URL
	 *
	 * @return Url naslov
	 * @see URL
	 */
	public String getFile() {
		return file != null ? file : "";
	}
    /**
     * Metoda, ki vrne koncnico zahtevanega resursa.
     *
     * @return Koncnica zahtevanega resursa.
     */
    public String getExtension() {
		int indexOfExtension = file.lastIndexOf('.');
		int indexOfLastSeparator = file.lastIndexOf('/');
		return (indexOfExtension < indexOfLastSeparator) ? null : file.substring(indexOfExtension+1);
	}
    /**
     * Metoda, ki preverja ali je zahteva po spletni strani ali po resursu za spletno stran.
     *
     * @return Zahteva resurs ali spletna stran.
     */
    public boolean isResource() {
        String extension = getExtension();
        switch ((extension != null) ? extension : "") {
        case "php": case "png": case "css": case "js": case "jpg": case "txt": case "gif": case "ico": case "xml": case "csv":
            return true;
        default:
            return false;
        }
    }

	public String izpis() {
		return getFile();
	}

	@Override
	public boolean equals(Object o) {
		return o instanceof File && file != null ? file.equals(((File) o).getFile()) : ((File) o).getFile() == null;
	}

	@Override
	public String toString() {
		return getFile();
	}
}
