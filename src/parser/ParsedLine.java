package parser;

import java.util.EnumMap;

import fields.Field;
import fields.ncsa.RequestLine;
import fields.w3c.UriStem;

public class ParsedLine {

	private EnumMap<FieldType, Field> map;
	/**
	 * Konstruktor.
	 *
	 * @param map Seznam, ki vsebuje podatke o obdelani vrstici.
	 */
	public ParsedLine(EnumMap<FieldType, Field> map) {
		this.map = map;
	}
	/**
	 * Metoda, ki ustvari kljuc za vrstico v log datoteki.
	 *
	 * @return Niz, ki predstavlja kljuc.
	 */
	public String getKey() {
		StringBuilder builder = new StringBuilder();
		map.values().stream().filter((f) -> (f.getKey() != null)).forEach((f) -> builder.append(f.getKey()));
		return builder.toString();
	}
	/**
	 * Getter za seznam, ki vsebuje obdelano vrstico.
	 *
	 * @return Seznam, ki predstavlja obdelano vrstico.
	 */
	public EnumMap<FieldType, Field> getMap() {
		return map;
	}
	/**
	 * Metoda, ki prevrja ali je zahtevo opravil uporabnik ali spletni robot.
	 *
	 * @return Zahtevo opravil robot ali ne.
	 */
	public boolean isCrawler() {
		//TODO Tukaj moraš preveriti zahtevan resurs in/ali User Agent String
		return false;
	}
	/**
	 * Metoda, ki preverja ali je zahteva po spletni strani ali po resursu za spletno stran.
	 *
	 * @return Zahteva resurs ali spletna stran.
	 */
	public boolean isResurse() {
		String extension = getExtension();
		switch ((extension != null) ? extension : "") {
		case "php": case "png": case "css": case "js": case "jpg": case "txt": case "gif": case "ico": case "xml": case "csv":
			return true;
		default:
			return false;
		}
	}
	/**
	 * Metoda, ki vrne koncnico zahtevanega resursa.
	 *
	 * @return Koncnica zahtevanega resursa.
	 */
	private String getExtension() {
		Field f = map.get(FieldType.RequestLine);
		if(f != null) {
			return ((RequestLine) f).getExtension();
		} else {
			f = map.get(FieldType.UriStem);
			return ((UriStem) f).getExtension();
		}
	}

}
