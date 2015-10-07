package org.sessionization.parser;

import org.sessionization.fields.*;
import org.sessionization.fields.w3c.*;
import org.sessionization.parser.datastruct.ParsedLine;

import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.UnknownHostException;
import java.text.ParseException;
import java.time.format.DateTimeFormatter;
import java.util.*;

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
		setDefaultFields(Locale.US);
	}
	/**
	 *
	 * @param file
	 * @throws FileNotFoundException
	 */
	public W3CParser(Locale locale, File[] file) throws FileNotFoundException {
		super(file);
		setDefaultFields(locale);
	}
	/**
	 * Metoda nastavi prevzete vrednosti poljem razreda:
	 * <p><code>fieldType = null</code></p>
	 * <p><code>dateFormat = dd/MM/yyyy</code></p>
	 * <p><code>timeFormat = HH:mm:ss</code></p>
	 * <p><code>locale = Locale.US</code></p>
	 */
	private void setDefaultFields(Locale locale) {
		dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd").withLocale(locale);
		timeFormat = DateTimeFormatter.ofPattern("HH:mm:ss").withLocale(locale);
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

	@Override
	protected String[] parse() throws ArrayIndexOutOfBoundsException, IOException, ParseException {
		String logline = super.getLine();
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
		return tokens.toArray(new String[tokens.size()]);
	}

	@Override
	public ParsedLine parseLine() throws ParseException, IOException, URISyntaxException {
		String[] tokens = parse();
		if(tokens[0].charAt(0) == '#') {
			Field[] metaData = new Field[tokens.length];
			if(tokens[0].equals("#Fields:")) {
				super.setFieldType(LogFormats.ExtendedLogFormat.create(tokens));
			}
			for (int i = 0; i < tokens.length; i++) {
				metaData[i] = new MetaData(tokens[i]);
			}
			return new ParsedLine(metaData);
		} else {
			return new ParsedLine(process(tokens));
		}
	}

	protected Field[] process(String[] tokens) throws ParseException, URISyntaxException, UnknownHostException {
		if(super.fieldType == null) throw new ParseException("Bad log format", super.getPos());
		if(super.fieldType.size() != tokens.length) throw new ParseException("Can't parse a line", super.getPos());
		Field[] lineData = new Field[super.fieldType.size()];
		for (int i = 0; i < super.fieldType.size(); i++) {
			switch (super.fieldType.get(i)) {
			case Referer:
				lineData[i] = new Referer(new URI(tokens[i]));
				break;
			case Cookie:
				lineData[i] = new Cookie(tokens[i], LogType.W3C);
				break;
			case UserAgent:
				lineData[i] = new UserAgent(tokens[i], LogType.W3C);
				break;
			case Method:
				lineData[i] = Method.setMethod(tokens[i]);
				break;
			case Date:
				lineData[i] = new org.sessionization.fields.w3c.Date(tokens[i], dateFormat);
				break;
			case Time:
				lineData[i] = new Time(tokens[i], timeFormat);
				break;
			case SiteName:
				lineData[i] = new SiteName(tokens[i]);
				break;
			case ComputerName:
				lineData[i] = new ComputerName(tokens[i]);
				break;
			case ServerIP:
				lineData[i] = new Address(tokens[i], true);
				break;
			case ClientIP:
				lineData[i] = new Address(tokens[i], false);
				break;
			case UriSteam:
				lineData[i] = new UriSteam(tokens[i]);
				break;
			case UriQuery:
				lineData[i] = new UriQuery(tokens[i]);
				break;
			case ServerPort:
				lineData[i] = new Port(tokens[i], true);
				break;
			case ClientPort:
				lineData[i] = new Port(tokens[i], false);
				break;
			case RemoteUser:
				lineData[i] = new RemoteUser(tokens[i]);
				break;
			case ProtocolVersion:
				lineData[i] = new Protocol(tokens[i]);
				break;
			case Host:
				lineData[i] = new Host(tokens[i]);
				break;
			case StatusCode:
				lineData[i] = new StatusCode(tokens[i]);
				break;
			case SubStatus:
				lineData[i] = new SubStatus(tokens[i]);
				break;
			case Win32Status:
				lineData[i] = new Win32Status(tokens[i]);
				break;
			case SizeOfRequest:
				lineData[i] = new SizeOfRequest(tokens[i]);
				break;
			case SizeOfResponse:
				lineData[i] = new SizeOfResponse(tokens[i]);
				break;
			case TimeTaken:
				lineData[i] = new TimeTaken(tokens[i], false);
				break;
			default:
				throw new ParseException("Unknown field", super.getPos());
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
