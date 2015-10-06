package org.sessionization.parser;

import org.sessionization.fields.*;
import org.sessionization.fields.ncsa.*;
import org.sessionization.parser.datastruct.ParsedLine;

import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.ParseException;
import java.time.format.DateTimeFormatter;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.NoSuchElementException;

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
	 * Konstruktor ki odpre tudi datoteko
	 *
	 * @param path Pot do datoteke predstavljena z nizom
	 * @throws FileNotFoundException Datoteka ne obstaja
	 * @see AbsParser#AbsParser(String)
	 * @see NCSAParser#setDefaultFields()
	 */
	public NCSAParser(Locale locale, String path) throws FileNotFoundException {
		super(path);
		setDefaultFields(locale);
	}
	/**
	 *
	 * @param input
	 * @see AbsParser#AbsParser(StringReader)
	 * @see IISParser#setDefaultFields()
	 */
	@Deprecated
	public NCSAParser(Locale locale, StringReader input) {
		super(input);
		setDefaultFields(locale);
	}
	/**
	 *
	 * @param file
	 * @throws FileNotFoundException
	 */
	public NCSAParser(Locale locale, File file) throws FileNotFoundException {
		super(file);
		setDefaultFields(locale);
	}
	/**
	 *
	 * @param reader
	 * @see AbsParser#AbsParser(BufferedReader)
	 * @see IISParser#setDefaultFields()
	 */
	public NCSAParser(Locale locale, BufferedReader reader) {
		super(reader);
		setDefaultFields(locale);
	}
	/**
	 *
	 * @param list
	 */
	public NCSAParser(Locale locale, File file, List<FieldType> list) throws FileNotFoundException {
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
	/**
	 * Metoda razcleni vhodni niz na vec delov in jih shrani v seznam.
	 *
	 * @param logline Vrstica v log datoteki
	 * @return Seznam razclenjenih nizov.
	 * @see String
	 */
	private String[] parse(String logline, int size) throws ArrayIndexOutOfBoundsException {
		if(logline == null) return null;
		int i = -1;
		String[] tokens = new String[size];
		StringBuilder buff = new StringBuilder();
		boolean inQuotes = false, inBrackets = false;
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
			default: buff.append(c);
			}
		}
		if (buff.length() > 0) tokens[++i] = buff.toString();
		return tokens;
	}

	@Override
	public ParsedLine parseLine() throws ParseException, NullPointerException, IOException, URISyntaxException {
		if (super.fieldType == null) throw new NullPointerException("Tipi polji niso specificirani!!!");
		Field[] lineData = new Field[super.fieldType.size()];
		String[] tokens;
		try {
			tokens = parse(super.getLine(), super.fieldType.size());
		} catch (ArrayIndexOutOfBoundsException e) {
			throw new ParseException("Napaka pri obdelavi vrstice!!!", super.getPos());
		}
		for(int i = 0; i < super.fieldType.size(); i++) {
			switch(super.fieldType.get(i)) {
			case RemoteHost:
				lineData[i] = new RemoteHost(tokens[i]);
				break;
			case Referer:
				lineData[i] = new Referer(new URI(tokens[i]));
				break;
			case RemoteLogname:
				lineData[i] = new RemoteLogname(tokens[i]);
				break;
			case RemoteUser:
				lineData[i] = new RemoteUser(tokens[i]);
				break;
			case RequestLine:
				String[] tab = tokens[i].split(" ");
				lineData[i] = new RequestLine(tab[0], tab[1], tab[2]);
				break;
			case SizeOfResponse:
				lineData[i] = new SizeOfResponse(tokens[i]);
				break;
			case SizeOfRequest:
				lineData[i] = new SizeOfRequest(tokens[i]);
				break;
			case SizeOfTransfer:
				lineData[i] = new SizeOfTransfer(tokens[i]);
				break;
			case StatusCode:
				lineData[i] = new StatusCode(tokens[i]);
				break;
			case Method:
				lineData[i] = Method.setMethod(tokens[i]);
			case ProtocolVersion:
				lineData[i] = new Protocol(tokens[i]);
				break;
			case DateTime:
				lineData[i] = new DateTime(tokens[i], formatter);
				break;
			case UserAgent:
				lineData[i] = new UserAgent(tokens[i], LogType.NCSA);
				break;
			case Cookie:
				lineData[i] = new Cookie(tokens[i], LogType.NCSA);
				break;
			case UriQuery:
				lineData[i] = new UriQuery(tokens[i]);
				break;
			case UriSteam:
				lineData[i] = new UriSteam(tokens[i]);
				break;
			case TimeTaken:
				lineData[i] = new TimeTaken(tokens[i], true);
				break;
			case ClientIP:
				lineData[i] = new Address(tokens[i], false);
				break;
			case ServerIP:
				lineData[i] = new Address(tokens[i], true);
				break;
			case ServerPort:
				lineData[i] = new Port(tokens[i], true);
				break;
			case ClientPort:
				lineData[i] = new Port(tokens[i], false);
				break;
			case ProcessID:
				lineData[i] = new ProcessID(tokens[i]);
				break;
			case KeepAliveNumber:
				lineData[i] = new KeepAliveNumber(tokens[i]);
				break;
			case ConnectionStatus:
				lineData[i] = ConnectionStatus.getConnectionStatus(tokens[i]);
				break;
			default: break;
			}
		}
		return new ParsedLine(lineData);
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
					if (next == null) throw new NoSuchElementException();
					ParsedLine tmp = next;
					try {
						next = parseLine();
						return tmp;
					} catch (EOFException e) {
						next = null;
						return tmp;
					} catch (ParseException | IOException e) {
						return null;
					} catch (URISyntaxException e) {
						return null;
					}
				}
			};
		} catch (ParseException | IOException e) {
			return null;
		} catch (URISyntaxException e) {
			return null;
		}
	}
}
