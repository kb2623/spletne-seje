package org.sessionization.parser;

import org.sessionization.fields.LogField;
import org.sessionization.fields.LogFieldType;
import org.sessionization.fields.LogType;
import org.sessionization.fields.Method;
import org.sessionization.fields.w3c.MetaData;
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
 * Parser za formate: Extended Log Format
 *
 * @author klemen
 */
@SuppressWarnings("deprecation")
public class W3CWebLogParser extends AbsWebLogParser {

	private DateTimeFormatter timeFormat;
	private DateTimeFormatter dateFormat;

	/**
	 * Konstruktor ki uporabi prevzeti oknstriktor razreda {@link ParserAbs}.
	 *
	 * @see AbsWebLogParser#AbsWebLogParser()
	 * @see W3CWebLogParser#setDefaultFields()
	 */
	public W3CWebLogParser() {
		super();
		setDefaultFields(Locale.US);
	}

	/**
	 * @param file
	 * @throws FileNotFoundException
	 */
	public W3CWebLogParser(Locale locale, File[] file) throws FileNotFoundException {
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
	protected String[] parse() throws ArrayIndexOutOfBoundsException, IOException {
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
		if (buff.length() > 0) {
			tokens.add(buff.toString());
		}
		return tokens.toArray(new String[tokens.size()]);
	}

	@Override
	public ParsedLine parseLine() throws ParseException {
		try {
			String[] tokens = parse();
			if (tokens[0].charAt(0) == '#') {
				List<LogField> metaData = new ArrayList<>(tokens.length);
				if (tokens[0].equals("#Fields:")) {
					super.setFieldType(LogFormats.ExtendedLogFormat.create(tokens));
				}
				for (int i = 0; i < tokens.length; i++) {
					metaData.add(new MetaData(tokens[i]));
				}
				return new ParsedLine(metaData);
			} else {
				return new ParsedLine(process(tokens));
			}
		} catch (ArrayIndexOutOfBoundsException | IOException e) {
			throw new ParseException("Bad line!!!", getPos());
		}
	}

	protected Collection<LogField> process(String[] tokens) throws ParseException {
		if (super.fieldType == null) throw new ParseException("Bad log format", super.getPos());
		if (super.fieldType.size() != tokens.length) throw new ParseException("Can't parse a line", super.getPos());
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
							throw new ParseException("Bad referer!!!", getPos());
						}
						break;
					case Cookie:
					case UserAgent:
						field = getTokenInstance(type, tokens[i], LogType.W3C);
						break;
					case Method:
						field = Method.setMethod(tokens[i]);
						break;
					case Date:
						field = getTokenInstance(type, tokens[i], dateFormat);
						break;
					case Time:
						field = getTokenInstance(type, tokens[i], timeFormat);
						break;
					case ServerIP:
					case ServerPort:
						field = getTokenInstance(type, tokens[i], true);
						break;
					case TimeTaken:
					case ClientIP:
					case ClientPort:
						field = getTokenInstance(type, tokens[i], false);
						break;
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
					if (next == null) throw new NoSuchElementException();
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
