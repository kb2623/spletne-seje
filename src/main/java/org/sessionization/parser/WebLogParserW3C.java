package org.sessionization.parser;

import java.io.File;
import java.io.FileNotFoundException;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;

public abstract class WebLogParserW3C extends WebLogParser {

	private DateTimeFormatter timeFormat;
	private DateTimeFormatter dateFormat;

	/**
	 * Konstruktor ki uporabi prevzeti oknstriktor razreda {@link ParserAbs}.
	 *
	 * @see WebLogParser#WebLogParser()
	 * @see W3CWebLogParser#setDefaultFields()
	 */
	public WebLogParserW3C() {
		super();
		setDefaultFields(Locale.US);
	}

	/**
	 * @param locale
	 * @param file
	 * @throws FileNotFoundException
	 */
	public WebLogParserW3C(Locale locale, File[] file) throws FileNotFoundException {
		super(file);
		setDefaultFields(locale);
	}

	public WebLogParserW3C(Locale locale, File[] file, List<LogFieldType> ignore) throws FileNotFoundException {
		super(file);
		setDefaultFields(locale);
		super.setIgnoreFieldType(ignore);
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

}
