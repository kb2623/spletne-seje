package org.sessionization.parser;

import org.datastruct.concurrent.ObjectPool;
import org.sessionization.parser.datastruct.ParsedLine;

import java.io.*;
import java.text.ParseException;
import java.util.*;
import java.util.function.Consumer;
import java.util.regex.Pattern;

/**
 * Razred za parsanje log datoteke
 *
 * @author klemen
 */
public abstract class WebLogParser implements Iterable<ParsedLine>, AutoCloseable {

	/**
	 * Tabela, ki vsebuje vrste polji v log datoteki
	 *
	 * @see LogFieldTypeImp
	 */
	protected List<LogFieldTypeImp> fieldType;

	/**
	 * Tabela, ki vsebuje polja, ki jih ignoramo
	 *
	 * @see LogFieldTypeImp
	 */
	protected Set<LogFieldTypeImp> ignore;

	/**
	 *
	 */
	protected ObjectPool pool;

	/**
	 *
	 */
	protected Pattern delimiter;

	/**
	 * Datoteke za branje
	 *
	 * @see BufferedReader
	 */
	private LineNumberReader[] readers;

	/**
	 * Osnovni konstruktor, za prevzete nastavitve:<p>
	 * pozicija = 0<p>
	 * datoteka = null
	 */
	public WebLogParser() {
		readers = null;
		fieldType = null;
		delimiter = Pattern.compile(" ");
	}

	/**
	 * @param file
	 * @throws FileNotFoundException
	 */
	public WebLogParser(File... file) throws FileNotFoundException {
		this();
		openFile(file);
	}

	/**
	 *
	 * @param files
	 * @throws FileNotFoundException
	 */
	public void openFile(File... files) throws FileNotFoundException {
		readers = new LineNumberReader[files.length];
		for (int i = 0, j = 0; i < files.length; i++) {
			if (files[i].isFile()) {
				readers[j] = new LineNumberReader(new InputStreamReader(new FileInputStream(files[i])));
				j++;
			} else {
				System.err.println("Bad file [" + files[i].getAbsolutePath() + "]!!!");
			}
		}
	}

	/**
	 * Metoda za nstavljanje že odprte datoteke
	 *
	 * @param reader Vhodni tok
	 */
	public void openFile(BufferedReader... reader) {
		readers = new LineNumberReader[reader.length];
		for (int i = 0, j = 0; i < reader.length; i++) {
			if (reader[i] == null) {
				System.err.println("Bad reader at " + i + "!!!");
			} else {
				readers[j] = new LineNumberReader(reader[i]);
				j++;
			}
		}
	}

	/**
	 *
	 * @param input
	 */
	@Deprecated
	public void openFile(StringReader... input) {
		readers = new LineNumberReader[input.length];
		for (int i = 0, j = 0; i < input.length; i++) {
			if (input[i] == null) {
				System.err.println("Bad StringReader at " + i + "!!!");
			} else {
				readers[j] = new LineNumberReader(input[i]);
			}
		}
	}

	/**
	 *
	 * @return
	 * @throws IOException
	 */
	protected Scanner getLine() throws IOException {
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
		Scanner s = new Scanner(builder.toString());
		s.useDelimiter(delimiter);
		return s;
	}

	/**
	 *
	 * @return
	 * @throws ParseException
	 * @throws IOException
	 */
	public ParsedLine parseLine() throws ParseException, IOException {
		if (fieldType == null) {
			throw new ParseException("Set log format!!!", getPos());
		} else {
			return parseLine(getLine());
		}
	}

	ParsedLine parseLine(Scanner scanner) throws ParseException {
		List<LogField> lineData = new ArrayList<>(fieldType.size());
		for (LogFieldTypeImp ft : fieldType) {
			if (ignore != null ? !ignore.contains(ft) : true) {
				lineData.add(ft.parse(scanner, this));
			}
		}
		if (scanner.hasNext()) {
			throw new ParseException("Line has more fields than expected!!!", getPos());
		}
		return new ParsedLine(lineData);
	}

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
	}

	/**
	 * Metoda za nastaljanje polji, ki jih med parsanjem ignoriramo
	 *
	 * @param ignore Tabela s polji, ki jih zelimo ignorirati
	 */
	public void setIgnoreFieldType(List<LogFieldTypeImp> ignore) {
		if (ignore != null) {
			this.ignore = EnumSet.copyOf(ignore);
		}
	}

	/**
	 * @return
	 */
	public Set<LogFieldTypeImp> getIgnoreFieldTypes() {
		return ignore;
	}

	/**
	 *
	 * @return
	 */
	public ObjectPool getPool() {
		return pool;
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
	public List<LogFieldTypeImp> getFieldType() {
		return fieldType;
	}

	/**
	 * Metoda za nastavljanje tipov polji v log datoteki.
	 *
	 * @param fields Tipi polji v log datoteki
	 * @throws NullPointerException Ko je parameter <code>fields</code> enak null
	 * @see LogFieldTypeImp
	 */
	public void setFieldType(List<LogFieldTypeImp> fields) throws NullPointerException {
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
	protected LogField getTokenInstance(Class type, Object... args) throws ParseException {
		if (type != null) {
			if (pool != null) {
				return (LogField) pool.getObject(type, args);
			} else {
				return (LogField) ObjectPool.makeObject(type, args);
			}
		} else {
			throw new ParseException("Unknown field", readers[0].getLineNumber());
		}
	}

	/**
	 * Metoda, ki vrne stevilko vrstice v kateri se nahaja parser
	 *
	 * @return Vrstica v kateri se nahaja parser
	 */
	public int getPos() {
		if (readers != null) {
			return readers[0].getLineNumber();
		} else {
			return 0;
		}
	}

	@Override
	public void forEach(Consumer<? super ParsedLine> consumer) {
		for (Iterator<ParsedLine> it = iterator(); it.hasNext(); ) {
			consumer.accept(it.next());
		}
	}


	@Override
	public Iterator<ParsedLine> iterator() {
		try {
			return new Iterator<ParsedLine>() {

				private ParsedLine next;

				{
					next = parseLine();
				}

				@Override
				public boolean hasNext() {
					return next != null;
				}

				@Override
				public ParsedLine next() throws NoSuchElementException {
					if (next == null) {
						throw new NoSuchElementException();
					}
					ParsedLine tmp = next;
					try {
						next = parseLine();
					} catch (ParseException e) {
						next = null;
					} catch (IOException e) {
						next = null;
					}
					return tmp;
				}
			};
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

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
	}
}
