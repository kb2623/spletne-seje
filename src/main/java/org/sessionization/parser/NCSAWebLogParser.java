package org.sessionization.parser;

import java.io.File;
import java.io.FileNotFoundException;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;

/**
 * Parser za formate: Common Log Format, Combined Log Format in Custom Log Formats
 *
 * @author klemen
 */
public class NCSAWebLogParser extends WebLogParser {

	private DateTimeFormatter formatter;

	/**
	 * Konstruktor ki nastavi polja na prevzete vrednosti
	 *
	 * @see ParserAbs#ParserAbs()
	 * @see NCSAWebLogParser#setDefaultFields()
	 */
	public NCSAWebLogParser() {
		super();
		setDefaultFields(Locale.US);
	}

	/**
	 * @param file
	 * @throws FileNotFoundException
	 */
	public NCSAWebLogParser(Locale locale, File[] file) throws FileNotFoundException {
		super(file);
		setDefaultFields(locale);
	}

	/**
	 *
	 * @param locale
	 * @param file
	 * @param list
	 * @throws FileNotFoundException
	 */
	public NCSAWebLogParser(Locale locale, File[] file, List<LogFieldType> list) throws FileNotFoundException {
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
	 * @param format       Format datuma
	 * @param localeString Niz, ki pove v katerem jeziku je zapisan datum
	 * @see DateTimeFormatter#ofPattern(String, Locale)
	 */
	public void setDateFormat(String format, Locale locale) {
		this.formatter = DateTimeFormatter.ofPattern(format == null ? "dd/MMM/yyyy:HH:mm:ss Z" : format).withLocale(locale == null ? Locale.getDefault() : locale);
	}

	/**
	 * @return
	 */
	public DateTimeFormatter getDateTimeFormatter() {
		return formatter;
	}

}
