package org.sessionization.parser;

import org.sessionization.parser.datastruct.ParsedLine;
import org.sessionization.parser.fields.w3c.MetaData;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.ParseException;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.Scanner;

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
	 *
	 * @param locale
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
	 *
	 * @param locale
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

	public DateTimeFormatter getDateFormat() {
		return dateFormat;
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
	 * @return
	 */
	public DateTimeFormatter getTimeFormat() {
		return timeFormat;
	}

	@Override
	public ParsedLine parseLine() throws ParseException {
		try {
			Scanner tokens = getLine();
			if (tokens.hasNext(LogFieldType.MetaData.getPattern())) {
				MetaData metadata = (MetaData) LogFieldType.MetaData.parse(tokens, this);
				if (metadata.getMetaData().equals("Fields")) {
					super.setFieldType(LogFormats.ParseCmdArgs.create(metadata.getValues()));
				}
				return new ParsedLine(new LogField[]{metadata});
			} else {
				return super.parseLine(tokens);
			}
		} catch (IOException e) {
			e.printStackTrace();
			throw new ParseException("Bad line!!!", getPos());
		}
	}
}
