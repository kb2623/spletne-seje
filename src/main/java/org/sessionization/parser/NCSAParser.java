package org.sessionization.parser;

import org.sessionization.fields.LogField;
import org.sessionization.fields.LogFieldType;
import org.sessionization.fields.LogType;
import org.sessionization.fields.Method;
import org.sessionization.fields.ncsa.ConnectionStatus;
import org.sessionization.parser.datastruct.ParsedLine;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.ParseException;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * Parser za formate: Common Log Format, Combined Log Format in Custom Log Formats
 * @author klemen
 */
public class NCSAParser extends AbsParser {

	private DateTimeFormatter formatter;
	/**
	 * Konstruktor ki nastavi polja na prevzete vrednosti
	 *
	 * @see ParserAbs#ParserAbs()
	 * @see NCSAParser#setDefaultFields()
	 */
	public NCSAParser() {
		super();
		setDefaultFields(Locale.US);
	}
	/**
	 *
	 * @param file
	 * @throws FileNotFoundException
	 */
	public NCSAParser(Locale locale, File[] file) throws FileNotFoundException {
		super(file);
		setDefaultFields(locale);
	}
	/**
	 *
	 * @param list
	 */
	public NCSAParser(Locale locale, File[] file, List<LogFieldType> list) throws FileNotFoundException {
		super(file);
		setDefaultFields(locale);
		setFieldType(list);
	}
	/**
	 * Metoda ki nastavi polja na prevzete vrednosti.
	 * <p><code>formatter = dd/MMM/yyyy:HH:mm:ss Z</code></p>
	 * <p><code>locale = Locale.US</code></p>
	 */
	private void setDefaultFields(Locale locale) {
		formatter = DateTimeFormatter.ofPattern("dd/MMM/yyyy:HH:mm:ss Z").withLocale(locale);
	}
	/**
	 * Nastavljanje formata za parsanje datuma.
	 * Ce je vhodni parameter <code>format</code> null, se nastavi format na prevzeti format.
	 * Ce je vhodni parameter <code>locale</code> null, se nastavi lokalnost na prevzeti format.
	 *
	 * @see DateTimeFormatter#ofPattern(String, Locale)
	 * @param format Format datuma
	 * @param localeString Niz, ki pove v katerem jeziku je zapisan datum
	 */
	public void setDateFormat(String format, Locale locale) {
		this.formatter = DateTimeFormatter.ofPattern(format == null ? "dd/MMM/yyyy:HH:mm:ss Z" : format).withLocale(locale == null ? Locale.getDefault() : locale);
	}

	@Override
	protected String[] parse() throws ArrayIndexOutOfBoundsException, IOException {
		int i = -1;
		String logline = super.getLine();
		String[] tokens = new String[super.fieldType.size()];
		StringBuilder buff = new StringBuilder();
		boolean inQuotes = false;
		boolean inBrackets = false;
		for (char c : logline.toCharArray()) {
			switch (c) {
				case '"':
					if (inQuotes) {
						tokens[++i] = buff.toString();
						buff = new StringBuilder();
					}
					inQuotes = !inQuotes;
					break;
				case '[':
					if (!inBrackets && !inQuotes) inBrackets = true;
					break;
				case ']':
					if (inBrackets) {
						tokens[++i] = buff.toString();
						buff = new StringBuilder();
						inBrackets = false;
					}
					break;
				case ' ':
					if (!inBrackets && !inQuotes && buff.length() > 0) {
						tokens[++i] = buff.toString();
						buff = new StringBuilder();
					} else if (inBrackets || inQuotes) {
						buff.append(c);
					}
					break;
				default:
					buff.append(c);
			}
		}
		if (buff.length() > 0) {
			tokens[++i] = buff.toString();
		}
		if (tokens.length != i + 1) {
			throw new IOException();
		}
		return tokens;
	}

	@Override
	public ParsedLine parseLine() throws ParseException {
		try {
			String[] tokens = parse();
			return new ParsedLine(process(tokens));
		} catch (ArrayIndexOutOfBoundsException | IOException e) {
			throw new ParseException("Bad line!!!", getPos());
		}
	}

	protected Collection<LogField> process(String[] tokens) throws ParseException {
		if (super.fieldType == null) {
			throw new ParseException("Field types are not set!!!", getPos());
		}
		List<LogField> lineData = new ArrayList<>(fieldType.size());
		for (int i = 0; i < super.fieldType.size(); i++) {
			LogFieldType type = fieldType.get(i);
			if (ignore != null ? !ignore.contains(type) : true) {
				LogField field = null;
				switch (type) {
					case Referer:
						try {
							field = getTokenInstance(type, new URI(tokens[i]));
						} catch (URISyntaxException e) {
							throw new ParseException("Bad URI!!!", getPos());
						}
						break;
					case RequestLine:
						String[] tab = tokens[i].split(" ");
						field = getTokenInstance(type, tab[0], tab[1], tab[2]);
						break;
					case DateTime:
						field = getTokenInstance(type, tokens[i], formatter);
						break;
					case UserAgent:
					case Cookie:
						field = getTokenInstance(type, tokens[i], LogType.NCSA);
						break;
					case TimeTaken:
					case ServerIP:
					case ServerPort:
						field = getTokenInstance(type, tokens[i], true);
						break;
					case ClientIP:
					case ClientPort:
						field = getTokenInstance(type, tokens[i], false);
						break;
					case ConnectionStatus:
						field = ConnectionStatus.getConnectionStatus(tokens[i]);
						break;
					case Method:
						field = Method.setMethod(tokens[i]);
					default:
						field = getTokenInstance(type, tokens[i]);
				}
				lineData.add(field);
			}
		}
		return lineData;
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
						return tmp;
					} catch (ParseException e) {
						next = null;
						return tmp;
					}
				}
			};
		} catch (ParseException e) {
			return null;
		}
	}
}
