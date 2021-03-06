package org.sessionization.parser.datastruct;

import org.sessionization.TimePoint;
import org.sessionization.parser.LogField;
import org.sessionization.parser.fields.Referer;
import org.sessionization.parser.fields.Resource;
import org.sessionization.parser.fields.UriSteam;
import org.sessionization.parser.fields.UserAgent;
import org.sessionization.parser.fields.w3c.MetaData;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.function.Consumer;

public class ParsedLine implements Iterable<LogField>, TimePoint {

	private LogField[] array;

	/**
	 * Konstruktor, ki kot parameter prejeme ze ustvarjeno tabelo. V primeru, ko uporabimo ta konstruktor je metoda add neuporabna, razen v primeru ko obstajajo v tabeli <code>null</code> vrednosti.
	 *
	 * @param array Seznam, ki vsebuje podatke o obdelani vrstici.
	 */
	public ParsedLine(Collection<LogField> list) {
		this.array = list.toArray(new LogField[list.size()]);
	}

	/**
	 * @param array
	 */
	public ParsedLine(LogField[] array) {
		this.array = array;
	}

	/**
	 * Metoda, ki prevrja ali je zahtevo opravil uporabnik ali spletni robot.
	 * Robota indentificiramo preko zahteve po resursu robots.txt ali pa po
	 * <code>user agetn stringu</code>
	 *
	 * @return Zahtevo opravil robot ali ne.
	 */
	public boolean isCrawler() {
		for (LogField f : array) {
			if (f instanceof UriSteam && !(f instanceof Referer)) {
				return ((UriSteam) f).getFile().equals("robots.txt");
			} else if (f instanceof UserAgent) {
				return ((UserAgent) f).isCrawler();
			}
		}
		return false;
	}

	/**
	 * Metoda pove ali vrstica v zapisu vsebuje resurs
	 *
	 * @return <code>true</code> -> Vrstica vsebuje resurs
	 * <code>false</code> -> Vrstica ne vsebuje resursa li na ne vsebuje polja, ki identificira resurs
	 */
	public boolean isWebPageResource() {
		for (LogField f : array) {
			if (f instanceof Resource) {
				return ((Resource) f).isWebPageResource();
			}
		}
		return false;
	}

	/**
	 * Vrne končnico zahtevane resursa
	 *
	 * @return OK -> niz, ki vsebuje končnico
	 * ERROR -> <code>null</code>
	 */
	public String getExtension() {
		for (int i = 0; i < array.length; i++) {
			if (array[i] instanceof Resource) {
				return ((Resource) array[i]).getExtension();
			}
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
	public LogField get(int i) {
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
	public LogField[] getArray() {
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
			if (f instanceof LogField && ((LogField) f).getKey() != null) {
				builder.append(((LogField) f).getKey());
			}
		}
		return builder.toString();
	}

	public boolean isMetaData() {
		for (LogField f : array) {
			if (f instanceof MetaData) {
				return true;
			}
		}
		return false;
	}

	@Override
	public LocalDate getLocalDate() {
		for (LogField f : array) {
			if (f instanceof TimePoint) {
				LocalDate date = ((TimePoint) f).getLocalDate();
				if (date != null) {
					return date;
				}
			}
		}
		return null;
	}

	@Override
	public LocalTime getLocalTime() {
		for (LogField f : array) {
			if (f instanceof TimePoint) {
				LocalTime time = ((TimePoint) f).getLocalTime();
				if (time != null) {
					return time;
				}
			}
		}
		return null;
	}

	@Override
	public Iterator<LogField> iterator() {
		return new Iterator<LogField>() {

			private int i = 0;

			@Override
			public boolean hasNext() {
				return i < array.length;
			}

			@Override
			public LogField next() throws NoSuchElementException {
				try {
					return array[i++];
				} catch (ArrayIndexOutOfBoundsException e) {
					throw new NoSuchElementException();
				}
			}
		};
	}

	@Override
	public void forEach(Consumer<? super LogField> consumer) {
		for (int i = 0; i < array.length; i++) {
			consumer.accept(array[i]);
		}
	}

	@Override
	public String toString() {
		return "ParsedLine{" +
				"array=" + Arrays.toString(array) +
				'}';
	}
}