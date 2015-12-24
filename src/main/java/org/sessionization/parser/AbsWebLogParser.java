package org.sessionization.parser;

import org.datastruct.LinkQueue;
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
	public AbsWebLogParser() {
		readers = null;
		fieldType = null;
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
		return builder.toString();
	}

	/**
	 * @return
	 * @throws IOException
	 */
	protected Queue<String> parse() throws IOException {
		LinkQueue<String> queue = new LinkQueue<>();
		boolean inQuotes = false, inB1 = false, inB2 = false;
		final StringBuilder builder = new StringBuilder();
		for (char c : getLine().toCharArray()) {
			switch (c) {
				case '"':
					if (inQuotes) {
						queue.add(builder.toString());
						builder.setLength(0);
					}
					inQuotes = !inQuotes;
					break;
				case '[':
					if (!inQuotes && !inB1 && !inB2) {
						inB1 = true;
					}
					break;
				case ']':
					if (inB1) {
						queue.add(builder.toString());
						builder.setLength(0);
						inB1 = false;
					}
					break;
				case '{':
					if (!inQuotes && !inB1 && !inB2) {
						inB2 = true;
					}
					break;
				case '}':
					if (inB2) {
						queue.add(builder.toString());
						builder.setLength(0);
						inB2 = false;
					}
					break;
				case ' ':
					if (!inQuotes && !inB1 && !inB2 && builder.length() > 0) {
						queue.offer(builder.toString());
						builder.setLength(0);
					} else if (inQuotes || inB1 || inB2) {
						builder.append(c);
					}
					break;
				default:
					builder.append(c);
			}
		}
		if (builder.length() > 0) {
			queue.add(builder.toString());
		}
		return queue;
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
			return parseLine(parse());
		}
	}

	ParsedLine parseLine(Queue<String> tokens) throws ParseException {
		List<LogField> lineData = new ArrayList<>(fieldType.size());
		for (LogFieldType ft : fieldType) {
			if (ignore != null ? !ignore.contains(ft) : true) {
				lineData.add(ft.parse(tokens, this));
			}
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
