package org.sessionization.parser;

import org.datastruct.ObjectPool;
import org.sessionization.parser.datastruct.ParsedLine;

import java.io.*;
import java.text.ParseException;
import java.util.*;
import java.util.function.Consumer;

/**
 * Razred za parsanje log datoteke
 *
 * @author klemen
 */
public abstract class AbsWebLogParser implements Iterable<ParsedLine>, AutoCloseable {

	/**
	 * Tabela, ki vsebuje vrste polji v log datoteki
	 *
	 * @see LogFieldType
	 */
	protected List<LogFieldType> fieldType;

	/**
	 * Tabela, ki vsebuje polja, ki jih ignoramo
	 *
	 * @see LogFieldType
	 */
	protected Set<LogFieldType> ignore;

	/**
	 *
	 */
	protected ObjectPool pool;

	/**
	 * Vrstica v datotekah
	 */
	private int pos;

	/**
	 * Datoteke za branje
	 *
	 * @see BufferedReader
	 */
	private BufferedReader[] readers;

	/**
	 * Osnovni konstruktor, za prevzete nastavitve:<p>
	 * pozicija = 0<p>
	 * datoteka = null
	 */
	public AbsWebLogParser() {
		readers = null;
		fieldType = null;
		pos = 0;
	}

	/**
	 * @param file
	 * @throws FileNotFoundException
	 */
	public AbsWebLogParser(File... file) throws FileNotFoundException {
		this();
		openFile(file);
	}

	/**
	 * @param path
	 * @throws FileNotFoundException
	 */
	public void openFile(File... files) throws FileNotFoundException {
		readers = new BufferedReader[files.length];
		for (int i = 0, j = 0; i < files.length; i++) {
			if (files[i].isFile()) {
				readers[j] = new BufferedReader(new InputStreamReader(new FileInputStream(files[i])));
				j++;
			} else {
				System.err.println("Bad file [" + files[i].getAbsolutePath() + "]!!!");
			}
		}
		pos = 0;
	}

	/**
	 * Metoda za nstavljanje že odprte datoteke
	 *
	 * @param reader Vhodni tok
	 */
	public void openFile(BufferedReader... reader) {
		readers = new BufferedReader[reader.length];
		for (int i = 0, j = 0; i < reader.length; i++) {
			if (reader[i] == null) {
				System.err.println("Bad reader at " + i + "!!!");
			} else {
				readers[j] = reader[i];
				j++;
			}
		}
		pos = 0;
	}

	/**
	 * Metoda namenjena testiranju, ki spremeni niz
	 * v vhodni tok
	 *
	 * @param input
	 * @throws FileNotFoundException
	 */
	@Deprecated
	public void openFile(StringReader... input) {
		readers = new BufferedReader[input.length];
		for (int i = 0, j = 0; i < input.length; i++) {
			if (input[i] == null) {
				System.err.println("Bad StringReader at " + i + "!!!");
			} else {
				readers[j] = new BufferedReader(input[i]);
			}
		}
		pos = 0;
	}

	/**
	 * Metoda ki vrne celotno vrstico, ki jo je potrebno še parsati
	 *
	 * @return List of <code>Strings</code>
	 * @throws IOException  Napaka pri branju vrstice
	 * @throws EOFException Konec datoteke ali strima
	 * @see String
	 */
	public String getLine() throws IOException {
		StringBuilder builder = new StringBuilder();
		for (BufferedReader br : readers) {
			if (br != null) {
				String tmp = br.readLine();
				if (tmp != null) {
					builder.append(tmp);
				} else {
					throw new EOFException();
				}
			} else {
				break;
			}
		}
		pos++;
		return builder.toString();
	}

	/**
	 * @return
	 * @throws ArrayIndexOutOfBoundsException
	 * @throws IOException
	 */
	protected abstract String[] parse() throws ArrayIndexOutOfBoundsException, IOException;

	/**
	 * Metoda za obdelavo vrstice do take mere da se vsi nizi shranjeni v instancah razredov
	 *
	 * @return Obdelano vrstico
	 * @throws ParseException       Napaka pri obdelavi datoteke
	 * @throws IOException          Napaka pri branju datoeke
	 * @throws NullPointerException Lastnosti niso pravilno nstavljene
	 * @see LogField
	 * @see ParsedLine
	 */
	public abstract ParsedLine parseLine() throws ParseException;

	/**
	 * Metoda za zapiranje datoteke
	 *
	 * @throws IOException Napaka pri zapiranju datoteke
	 */
	public void closeFile() throws IOException {
		for (BufferedReader br : readers) {
			if (br != null) {
				br.close();
			} else {
				break;
			}
		}
		pos = 0;
	}

	/**
	 * Metoda za nastaljanje polji, ki jih med parsanjem ignoriramo
	 *
	 * @param ignore Tabela s polji, ki jih zelimo ignorirati
	 */
	public void setIgnoreFieldType(List<LogFieldType> ignore) {
		if (ignore != null) {
			this.ignore = EnumSet.copyOf(ignore);
		}
	}

	/**
	 * @return
	 */
	public Set<LogFieldType> getIgnoreFieldTypes() {
		return ignore;
	}

	/**
	 * @param pool
	 */
	public void setPool(ObjectPool pool) {
		if (pool != null) {
			this.pool = pool;
		}
	}

	/**
	 * Geter za seznam tipov polji v log datoteki
	 *
	 * @return Seznam tipov polji v datoteki
	 */
	public List<LogFieldType> getFieldType() {
		return fieldType;
	}

	/**
	 * Metoda za nastavljanje tipov polji v log datoteki.
	 *
	 * @param fields Tipi polji v log datoteki
	 * @throws NullPointerException Ko je parameter <code>fields</code> enak null
	 * @see LogFieldType
	 */
	public void setFieldType(List<LogFieldType> fields) throws NullPointerException {
		if (fields == null) {
			throw new NullPointerException();
		} else if (fields instanceof ArrayList) {
			this.fieldType = fields;
		} else {
			this.fieldType = new ArrayList<>(fields);
		}
	}

	/**
	 * @param type
	 * @param args
	 * @return
	 * @throws ParseException
	 */
	protected LogField getTokenInstance(LogFieldType type, Object... args) throws ParseException {
		if (type.getClassE() != null) {
			if (pool != null) {
				return (LogField) pool.getObject(type.getClassE(), args);
			} else {
				return (LogField) ObjectPool.makeObject(type.getClassE(), args);
			}
		} else {
			throw new ParseException("Unknown field", pos);
		}
	}

	/**
	 * Metoda, ki vrne stevilko vrstice v kateri se nahaja parser
	 *
	 * @return Vrstica v kateri se nahaja parser
	 */
	public int getPos() {
		return pos;
	}

	@Override
	public void forEach(Consumer<? super ParsedLine> consumer) {
		for (Iterator<ParsedLine> it = iterator(); it.hasNext(); ) {
			consumer.accept(it.next());
		}
	}

	@Override
	public abstract Iterator<ParsedLine> iterator();

	@Override
	public Spliterator<ParsedLine> spliterator() {
		throw new UnsupportedOperationException("Unsupported operation!!!");
	}

	@Override
	public void close() {
		try {
			closeFile();
		} catch (IOException ignore) {
		}
		pos = 0;
	}
}
