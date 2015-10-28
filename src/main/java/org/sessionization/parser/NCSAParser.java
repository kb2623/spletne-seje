package org.sessionization.parser;

import org.datastruct.ClassPool;
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
	public NCSAParser(Locale locale, File[] file, List<FieldType> list) throws FileNotFoundException {
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
	protected String[] parse() throws ArrayIndexOutOfBoundsException, IOException, ParseException {
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
			throw new ParseException("Bad line!!!", super.getPos());
		}
		return tokens;
	}

	@Override
	public ParsedLine parseLine() throws ParseException, NullPointerException, IOException, URISyntaxException {
		if (super.fieldType == null) throw new NullPointerException("Tipi polji niso specificirani!!!");
		String[] tokens;
		try {
			tokens = parse();
		} catch (ArrayIndexOutOfBoundsException e) {
			throw new ParseException("Napaka pri obdelavi vrstice!!!", super.getPos());
		}
		LogField[] lineData = new LogField[super.fieldType.size()];
		for(int i = 0; i < super.fieldType.size(); i++) {
			switch(super.fieldType.get(i)) {
			case RemoteHost:
				lineData[i] = ClassPool.getObject(RemoteHost.class, tokens[i]);
				break;
			case Referer:
				lineData[i] = ClassPool.getObject(Referer.class, new URI(tokens[i]));
				break;
			case RemoteLogname:
				lineData[i] = ClassPool.getObject(RemoteLogname.class, tokens[i]);
				break;
			case RemoteUser:
				lineData[i] = ClassPool.getObject(RemoteUser.class, tokens[i]);
				break;
			case RequestLine:
				String[] tab = tokens[i].split(" ");
				lineData[i] = ClassPool.getObject(RequestLine.class, tab[0], tab[1], tab[2]);
				break;
			case SizeOfResponse:
				lineData[i] = ClassPool.getObject(SizeOfResponse.class, tokens[i]);
				break;
			case SizeOfRequest:
				lineData[i] = ClassPool.getObject(SizeOfRequest.class, tokens[i]);
				break;
			case SizeOfTransfer:
				lineData[i] = ClassPool.getObject(SizeOfTransfer.class, tokens[i]);
				break;
			case StatusCode:
				lineData[i] = ClassPool.getObject(StatusCode.class, tokens[i]);
				break;
			case Method:
				lineData[i] = Method.setMethod(tokens[i]);
			case ProtocolVersion:
				lineData[i] = ClassPool.getObject(Protocol.class, tokens[i]);
				break;
			case DateTime:
				lineData[i] = ClassPool.getObject(DateTime.class, tokens[i], formatter);
				break;
			case UserAgent:
				lineData[i] = ClassPool.getObject(UserAgent.class, tokens[i], LogType.NCSA);
				break;
			case Cookie:
				lineData[i] = ClassPool.getObject(Cookie.class, tokens[i], LogType.NCSA);
				break;
			case UriQuery:
				lineData[i] = ClassPool.getObject(UriQuery.class, tokens[i]);
				break;
			case UriSteam:
				lineData[i] = ClassPool.getObject(UriSteam.class, tokens[i]);
				break;
			case TimeTaken:
				lineData[i] = ClassPool.getObject(TimeTaken.class, tokens[i], true);
				break;
			case ClientIP:
				lineData[i] = ClassPool.getObject(Address.class, tokens[i], false);
				break;
			case ServerIP:
				lineData[i] = ClassPool.getObject(Address.class, tokens[i], true);
				break;
			case ServerPort:
				lineData[i] = ClassPool.getObject(Port.class, tokens[i], true);
				break;
			case ClientPort:
				lineData[i] = ClassPool.getObject(Port.class, tokens[i], false);
				break;
			case ProcessID:
				lineData[i] = ClassPool.getObject(ProcessID.class, tokens[i]);
				break;
			case KeepAliveNumber:
				lineData[i] = ClassPool.getObject(KeepAliveNumber.class, tokens[i]);
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
