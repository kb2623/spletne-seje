package org.sessionization.parser;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.Locale;
import java.util.regex.Pattern;

@SuppressWarnings("deprecation")
public class IISWebLogParser extends W3CWebLogParser {

	/**
	 * Konstruktor ki uporabi prevzeti knstriktor razreda {@link ParserAbs}.
	 *
	 * @see parser.ParserAbs#ParserAbs()
	 * @see IISWebLogParser#setDefaultFields()
	 */
	public IISWebLogParser() {
		super();
		setDefaultFields(Locale.US);
	}

	/**
	 * @param file
	 * @throws FileNotFoundException
	 */
	public IISWebLogParser(Locale locale, File[] file, List<LogFieldType> ignore) throws FileNotFoundException {
		super(locale, file);
	}

	/**
	 * @param file
	 * @param list
	 * @throws FileNotFoundException
	 */
	public IISWebLogParser(Locale locale, File[] file, List<LogFieldType> list, List<LogFieldType> ignore) throws FileNotFoundException {
		super(locale, file);
		super.setFieldType(list);
	}

	/**
	 * Metoda nastavi prevzete vrednosti poljem razreda:
	 * <p><code>dateFormat = dd/MM/yyyy</code></p>
	 * <p><code>timeFormat = HH:mm:ss</code></p>
	 * <p><code>locale = Locale.US</code></p>
	 */
	private void setDefaultFields(Locale locale) {
		super.setDateFormat("dd/MM/yyyy", locale);
		super.setTimeFormat("HH:mm:ss", locale);
		super.delimiter = Pattern.compile(", ");
	}
}
