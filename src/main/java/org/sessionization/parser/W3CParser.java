package org.sessionization.parser;

import java.io.*;
import java.net.URL;
import java.text.ParseException;
import java.time.format.DateTimeFormatter;
import java.util.*;

import org.sessionization.fields.*;
import org.sessionization.fields.w3c.*;
import org.sessionization.parser.datastruct.ParsedLine;

/**
 * Parser za formate: Extended Log Format
 * @author klemen
 */
@SuppressWarnings("deprecation")
public class W3CParser extends AbsParser {

	private DateTimeFormatter timeFormat;
	private DateTimeFormatter dateFormat;
	/**
	 * Konstruktor ki uporabi prevzeti oknstriktor razreda {@link ParserAbs}.
	 *
	 * @see AbsParser#AbsParser()
	 * @see W3CParser#setDefaultFields()
	 */
	public W3CParser() {
		super();
		setDefaultFields();
	}
	/**
	 *
	 * @param path Pot datoteko predstavljena z nizom
	 * @throws FileNotFoundException Datoteko ne obstaja
	 * @see AbsParser#AbsParser(String)
	 * @see W3CParser#setDefaultFields()
	 */
	public W3CParser(String path) throws FileNotFoundException {
		super(path);
		setDefaultFields();
	}
	/**
	 *
	 * @param input
	 * @see AbsParser#AbsParser(StringReader)
	 * @see W3CParser#setDefaultFields()
	 */
	@Deprecated
	public W3CParser(StringReader input) {
		super(input);
		setDefaultFields();
	}
	/**
	 *
	 * @param reader
	 * @see AbsParser#AbsParser(BufferedReader)
	 * @see W3CParser#setDefaultFields()
	 */
	public W3CParser(BufferedReader reader) {
		super(reader);
		setDefaultFields();
	}
	/**
	 * Metoda nastavi prevzete vrednosti poljem razreda:
	 * <p><code>fieldType = null</code></p>
	 * <p><code>dateFormat = dd/MM/yyyy</code></p>
	 * <p><code>timeFormat = HH:mm:ss</code></p>
	 * <p><code>locale = Locale.US</code></p>
	 */
	private void setDefaultFields() {
		dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd").withLocale(Locale.US);
		timeFormat = DateTimeFormatter.ofPattern("HH:mm:ss").withLocale(Locale.US);
	}
	/**
	 * Nastavljanje formata za parsanje datuma
	 *
	 * @param format
	 * @param localeString
	 */
	public void setDateFormat(String format, Locale locale) {
		this.dateFormat = DateTimeFormatter.ofPattern(format == null ? "yyyy-MM-dd" : format).withLocale(locale == null ? Locale.getDefault() : locale);
	}
	/**
     * Nastavljanje formata za parsanje &#x10d;asa
     *
     * @param format
     * @param localeString
     */
	public void setTimeFormat(String format, Locale locale) {
		this.timeFormat = DateTimeFormatter.ofPattern(format == null ? "HH:mm:ss" : format).withLocale(locale == null ? Locale.getDefault() : locale);
	}
	/**
	 *
	 * @param logline
	 * @return
	 * @see String
	 */
	private List<String> parse(String logline) {
		if(logline == null) {
			return null;
		}
		List<String> tokens = new ArrayList<>();
		StringBuffer buff = new StringBuffer();
		for (char c : logline.toCharArray()) {
			switch (c) {
			case ' ':
				if (buff.length() > 0) {
					tokens.add(buff.toString());
					buff = new StringBuffer();
				}
				break;
			default:
				buff.append(c);
			}
		}
		if(buff.length() > 0) {
			tokens.add(buff.toString());
		}
		return tokens;
	}

	@Override
	public ParsedLine parseLine() throws ParseException, IOException {
		List<String> tokens = parse(getLine());
		if(tokens == null) return null;
		if(tokens.get(0).charAt(0) == '#') {
			Field[] metaData = new Field[tokens.size()];
			if(tokens.get(0).equals("#Fields:")) super.setFieldType(LogFormats.ExtendedLogFormat.create(tokens));
			for (int i = 0; i < tokens.size(); i++) metaData[i] = new MetaData(tokens.get(i));
			return new ParsedLine(metaData);
		}
		Field[] lineData = new Field[super.fieldType.size()];
		if(super.fieldType == null) throw new ParseException("Bad log format", super.getPos());
		if(super.fieldType.size() != tokens.size()) throw new ParseException("Can't parse a line", super.getPos());
		for (int i = 0; i < super.fieldType.size(); i++) {
			switch (super.fieldType.get(i)) {
			case Referer:
				lineData[i] = new Referer(new URL(tokens.get(i)));
				break;
			case Cookie:
				lineData[i] = new Cookie(tokens.get(i), LogType.W3C);
				break;
			case UserAgent:
				lineData[i] = new UserAgent(tokens.get(i), LogType.W3C);
				break;
			case Method:
				lineData[i] = Method.setMethod(tokens.get(i));
				break;
			case Date:
				lineData[i] = new org.sessionization.fields.w3c.Date(tokens.get(i), dateFormat);
				break;
			case Time:
				lineData[i] = new Time(tokens.get(i), timeFormat);
				break;
			case SiteName:
				lineData[i] = new SiteName(tokens.get(i));
				break;
			case ComputerName:
				lineData[i] = new ComputerName(tokens.get(i));
				break;
			case ServerIP:
				lineData[i] = new Address(tokens.get(i), true);
				break;
			case ClientIP:
				lineData[i] = new Address(tokens.get(i), false);
				break;
			case UriStem:
				lineData[i] = new UriStem(tokens.get(i));
				break;
			case UriQuery:
				lineData[i] = new UriQuery(tokens.get(i));
				break;
			case ServerPort:
				lineData[i] = new Port(tokens.get(i), true);
				break;
			case ClientPort:
				lineData[i] = new Port(tokens.get(i), false);
				break;
			case RemoteUser:
				lineData[i] = new RemoteUser(tokens.get(i));
				break;
			case ProtocolVersion:
				lineData[i] = new Protocol(tokens.get(i));
				break;
			case Host:
				lineData[i] = new Host(tokens.get(i));
				break;
			case StatusCode:
				lineData[i] = new StatusCode(tokens.get(i));
				break;
			case SubStatus:
				lineData[i] = new SubStatus(tokens.get(i));
				break;
			case Win32Status:
				lineData[i] = new Win32Status(tokens.get(i));
				break;
			case SizeOfRequest:
				lineData[i] = new SizeOfRequest(tokens.get(i));
				break;
			case SizeOfResponse:
				lineData[i] = new SizeOfResponse(tokens.get(i));
				break;
			case TimeTaken:
				lineData[i] = new TimeTaken(tokens.get(i), false);
				break;
			default:
				throw new ParseException("Unknown field", super.getPos());
			}
		}
		return new ParsedLine(lineData);
	}

    @Override
    public Iterator<ParsedLine> iterator() {
        try {
            return new Iterator<ParsedLine>() {

				private ParsedLine next = parseLine();

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
					}
				}
			};
        } catch (ParseException | IOException e) {
            return null;
        }
    }
}
