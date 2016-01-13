package org.sessionization.parser.fields;

public interface Resource {

	/**
	 * Metoda, ki vrne koncnico zahtevanega resursa.
	 *
	 * @return Koncnica zahtevanega resursa.
	 */
	default String getExtension() {
		int indexOfExtension = getFile().lastIndexOf('.');
		int indexOfLastSeparator = getFile().lastIndexOf('/');
		return (indexOfExtension < indexOfLastSeparator) ? null : getFile().substring(indexOfExtension + 1);
	}

	/**
	 * Metoda, ki preverja ali je zahteva po spletni strani ali po resursu za spletno stran.
	 *
	 * @return Zahteva resurs ali spletna stran.
	 */
	default boolean isResource() {
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

	/**
	 * @return
	 */
	String getFile();
}
