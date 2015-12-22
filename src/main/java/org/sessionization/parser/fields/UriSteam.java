package org.sessionization.parser.fields;

import org.sessionization.parser.LogField;

import javax.persistence.*;
import java.net.URL;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Cacheable
public class UriSteam implements LogField {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	private String file;

	public UriSteam() {
		id = null;
		file = null;
	}

	public UriSteam(String file) {
		id = null;
		if (!file.equals("-")) {
			this.file = file;
		} else {
			this.file = null;
		}
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
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

	public void setFile(String file) {
		this.file = file;
	}

	/**
	 * Metoda, ki vrne koncnico zahtevanega resursa.
	 *
	 * @return Koncnica zahtevanega resursa.
	 */
	public String getExtension() {
		int indexOfExtension = file.lastIndexOf('.');
		int indexOfLastSeparator = file.lastIndexOf('/');
		return (indexOfExtension < indexOfLastSeparator) ? null : file.substring(indexOfExtension + 1);
	}

	/**
	 * Metoda, ki preverja ali je zahteva po spletni strani ali po resursu za spletno stran.
	 *
	 * @return Zahteva resurs ali spletna stran.
	 */
	public boolean isResource() {
		String extension = getExtension();
		switch ((extension != null) ? extension : "") {
			case "png":
			case "css":
			case "js":
			case "jpg":
			case "txt":
			case "gif":
			case "ico":
			case "xml":
			case "csv":
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
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		UriSteam uriSteam = (UriSteam) o;
		if (getId() != null ? !getId().equals(uriSteam.getId()) : uriSteam.getId() != null) return false;
		if (!getFile().equals(uriSteam.getFile())) return false;
		return true;
	}

	@Override
	public int hashCode() {
		int result = getId() != null ? getId().hashCode() : 0;
		result = 31 * result + getFile().hashCode();
		return result;
	}

	@Override
	public String toString() {
		return getFile();
	}
}