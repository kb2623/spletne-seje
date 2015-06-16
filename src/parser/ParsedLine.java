package parser;

import java.util.Arrays;
import java.util.Iterator;
import java.util.function.Consumer;

import fields.Field;
import fields.ncsa.RequestLine;
import fields.w3c.UriStem;

public class ParsedLine implements Iterable<Field> {

	private Field[] array;
	/**
	 * Konstruktor, ki kot parameter prejeme ze ustvarjeno tabelo. V primeru, ko uporabimo ta konstruktor je metoda add neuporabna, razen v primeru ko obstajajo v tabeli <code>null</code> vrednosti.
	 *
	 * @param array Seznam, ki vsebuje podatke o obdelani vrstici.
	 */
	public ParsedLine(Field[] array) {
		this.array = array;
	}
	/**
	 * Konstruktor, ki ustvari tabelo in jo napolne z <code>null</code> vrednostmi.
	 *
	 * @param size
	 */
	public ParsedLine(int size) {
		array = new Field[size];
		Arrays.fill(array, null);
	}
	/**
	 * Metoda za dodajanje. Metoda doda nov element na prvo mesto tabeli, kjer se nahaja <code>null</code> vrednost
	 *
	 * @param field Element za vnos v tabelo
	 * @return <code>true</code>, ko je bil nov element dodan v tabelo, <code>false</code>, ko elemeta nismo mogli dodati
	 * @throws NullPointerException Parameter <code>field</code> je <code>null</code> vrednost
	 */
	public boolean add(Field field) throws NullPointerException{
		if (field == null) throw new NullPointerException();
		for (int i = 0; i < array.length; i++) {
			if (array[i] == null) {
				array[i] = field;
				return true;
			}
		}
		return false;
	}
	/**
	 * Metoda, ki ustvari kljuc za vrstico v log datoteki.
	 *
	 * @return Niz, ki predstavlja kljuc.
	 */
	public String getKey() {
		StringBuilder builder = new StringBuilder();
		for (Field f : array) if (f.getKey() != null) builder.append(f.getKey());
		return builder.toString();
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
	public String getExtension() {
		for (int i = 0; i < array.length; i++) {
			if (array[i] instanceof RequestLine) return ((RequestLine) array[i]).getExtension();
			if (array[i] instanceof UriStem) return ((UriStem) array[i]).getExtension();
		}
		return null;
	}
	/**
	 * Testna metoda za testiranje pravilnosti shranjevanja podatkov
	 *
	 * @return Niz, ki vsebuje vse podatke o vrstici iz log datoteke
	 */
	@Deprecated
	public String izpis() {
		StringBuilder builder = new StringBuilder();
		this.forEach(f -> builder.append(f.izpis()).append(' ').append("||").append(' '));
		return builder.toString();
	}

	public Field get(int i) {
		try {
			return array[i];
		} catch (ArrayIndexOutOfBoundsException e) {
			return null;
		}
	}

	@Override
	public Iterator<Field> iterator() {
		return new Iterator<Field>() {

			private int i;

			{
				i = 0;
			}

			@Override
			public boolean hasNext() {
				return i < array.length;
			}

			@Override
			public Field next() {
				try {
					return array[i++];
				} catch (ArrayIndexOutOfBoundsException e) {
					return null;
				}
			}
		};
	}

	@Override
	public void forEach(Consumer<? super Field> consumer) {
		for (int i = 0; i < array.length; i++) consumer.accept(array[i]);
	}
}
