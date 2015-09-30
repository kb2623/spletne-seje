package org.sessionization.parser;

import org.sessionization.fields.*;
import org.sessionization.fields.w3c.*;
import org.sessionization.fields.w3c.Date;
import org.sessionization.parser.datastruct.ParsedLine;

import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.ParseException;
import java.time.format.DateTimeFormatter;
import java.util.*;
@SuppressWarnings("deprecation")
public class IISParser extends AbsParser {

	private DateTimeFormatter timeFormat;
	private DateTimeFormatter dateFormat;

	/**
	 * Konstruktor ki uporabi prevzeti knstriktor razreda {@link ParserAbs}.
	 *
	 * @see parser.ParserAbs#ParserAbs()
	 * @see IISParser#setDefaultFields()
	 */
	public IISParser() {
		super();
		setDefaultFields();
	}
	/**
	 * Konstruktor ki tudi odpre datoteko
	 *
	 * @param path
	 * @throws FileNotFoundException
	 * @see AbsParser#AbsParser(String)
	 * @see IISParser#setDefaultFields()
	 */
	public IISParser(String path) throws FileNotFoundException {
		super(path);
		setDefaultFields();
	}
	/**
	 *
	 * @param input
	 * @see AbsParser#AbsParser(StringReader)
	 * @see IISParser#setDefaultFields()
	 */
	@Deprecated
	public IISParser(StringReader input) {
		super(input);
		setDefaultFields();
	}
	/**
	 *
	 * @param reader
	 * @see AbsParser#AbsParser(BufferedReader)
	 * @see IISParser#setDefaultFields()
	 */
	public IISParser(BufferedReader reader) {
		super(reader);
		setDefaultFields();
	}
	/**
	 * Metoda nastavi prevzete vrednosti poljem razreda:
	 * <p><code>dateFormat = dd/MM/yyyy</code></p>
	 * <p><code>timeFormat = HH:mm:ss</code></p>
	 * <p><code>locale = Locale.US</code></p>
	 */
	private void setDefaultFields() {
		dateFormat = DateTimeFormatter.ofPattern("dd/MM/yyyy").withLocale(Locale.US);
		timeFormat = DateTimeFormatter.ofPattern("HH:mm:ss").withLocale(Locale.US);
	}
	/**
	 * Metoda, ki zarcleni polja v vrstici log datoteke.
	 *
	 * @param logline Vrstica iz lod datoteke
	 * @return Seznam razcljenjenih polji
	 */
	private List<String> parse(String logline) {
		if(logline == null) return null;
		boolean inWord = false;
		List<String> tokens = new ArrayList<>();
		StringBuffer buff = new StringBuffer();
		for (char c : logline.toCharArray()) {
			switch(c) {
			case ' ':
				if(inWord) {
					buff.append(c);
				} else {
					break;
				}
			case ',':
				if(buff.length() > 0) {
					tokens.add(buff.toString());
					buff = new StringBuffer();
					inWord = false;
				}
				break;
			default:
				buff.append(c);
				inWord = true;
			}
		}
		if(buff.length() > 0) {
			tokens.add(buff.toString());
		}
		return tokens;
	}

	@Override
	public ParsedLine parseLine() throws ParseException, IOException, NullPointerException, URISyntaxException {
		if (super.fieldType == null) throw new NullPointerException("Tipi polji niso specificirani!!!");
		Field[] lineData = new Field[super.fieldType.size()];
		List<String> tokens = parse(super.getLine());
		if (tokens == null) return null;
		if(tokens.size() != super.fieldType.size()) throw new ParseException("Bad field types", super.getPos());
		for (int i = 0; i < super.fieldType.size(); i++) {
			switch (super.fieldType.get(i)) {
			case Referer:
				lineData[i] = new Referer(new URI(tokens.get(i)));
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
				lineData[i] = new Date(tokens.get(i), dateFormat);
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
			case UriSteam:
				lineData[i] = new UriSteam(tokens.get(i));
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
	/**
	 * Nastavljanje formata za parsanje datuma.
	 *
	 * @see DateTimeFormatter
	 * @param format Format datuma
	 * @param region Jezik uporabljen za zapis datuma
	 */
	public void setDateFormat(String format, Locale locale) {
		this.dateFormat = DateTimeFormatter.ofPattern(format == null ? "dd/MM/yyyy" : format).withLocale(locale == null ? Locale.US : locale);
	}
	/**
	 * Nastavljanje formata za parsanje časa
	 *
	 * @see DateTimeFormatter
	 * @param format Format ure
	 * @param region Jezik uporabljen v zapisu ure
	 */
	public void setTimeFormat(String format, Locale locale) {
		this.timeFormat = DateTimeFormatter.ofPattern(format == null ? "HH:mm:ss" : format).withLocale(locale == null ? Locale.US : locale);
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
