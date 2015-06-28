package org.spletneseje.parser.datastruct;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.function.Consumer;

import org.spletneseje.fields.Field;
import org.spletneseje.fields.ncsa.RequestLine;
import org.spletneseje.fields.w3c.UriStem;

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
	 * Setter za tabelo. Metoda nastavi <code>array[index] = newValue</code> in vrne staro vrednost <code>array[index]</code>.
	 *
	 * @param index Indeks v tabeli
	 * @param newValue Nova vrednost
	 * @return Stara vrednost na <code>array[index]</code>
	 * @throws ArrayIndexOutOfBoundsException Ko je <code>index >= array.length</code>
	 */
	public Field add(int index, Field newValue) throws ArrayIndexOutOfBoundsException {
		Field ret = array[index];
		array[index] = newValue;
		return ret;
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
		try {
			StringBuilder builder = new StringBuilder();
			builder.append('[');
			for (int i = 0; true; ) {
				builder.append(array[i].izpis());
				if (++i < array.length) builder.append(" | ");
				else break;
			}
			return builder.append(']').toString();
		} catch (ArrayIndexOutOfBoundsException e) {
			return "[]";
		}
	}
	/**
	 * Getter za pridobivanje vrednosti v tabeli
	 *
	 * @param i Indeks v tabeli
	 * @return Vrednost indeksa v tabeli. Lahko dobimo tudi <code>null</code> vrednosti v primeru, ko vnesemo indeks, ki presega velikost tabele ali pa je tudi vrednost tabele na iskanem indeksu <code>null</code>.
	 */
	public Field get(int i) {
		try {
			return array[i];
		} catch (ArrayIndexOutOfBoundsException e) {
			return null;
		}
	}
	/**
	 * Getter za tabelo
	 *
	 * @return Tabela, ki vsebuje obdelano vrstico
	 */
	public Field[] getArray() {
		return array;
	}
	/**
	 * Getter za velikost tabele
	 *
	 * @return Velikost tabele oz. število polji v vrstici
	 */
	public int size() {
		return array.length;
	}
	/**
	 * Metoda, ki ustvari kljuc za vrstico v log datoteki.
	 *
	 * @return Niz, ki predstavlja kljuc.
	 */
	public String getKey() {
		StringBuilder builder = new StringBuilder();
		for (Object f : array) {
			if (f instanceof  Field && ((Field) f).getKey() != null) builder.append(((Field) f).getKey());
		}
		return builder.toString();
	}

	@Override
	public Iterator<Field> iterator() {
		return new Iterator<Field>() {

			private int i = 0;

			@Override
			public boolean hasNext() {
				return i < array.length;
			}

			@Override
			public Field next() throws NoSuchElementException {
				try {
					return array[i++];
				} catch (ArrayIndexOutOfBoundsException e) {
					throw new NoSuchElementException();
				}
			}
		};
	}

	@Override
	public void forEach(Consumer<? super Field> consumer) {
		for (int i = 0; i < array.length; i++) consumer.accept(array[i]);
	}

	@Override
	public String toString() {
		try {
			StringBuilder builder = new StringBuilder();
			builder.append('[');
			for (int i = 0; true; ) {
				builder.append(array[i].toString());
				if (++i < array.length) {
					builder.append(" | ");
				} else {
					break;
				}
			}
			return builder.append(']').toString();
		} catch (ArrayIndexOutOfBoundsException e) {
			return "[]";
		}
	}
}