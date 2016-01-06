package org.sessionization.parser;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.Locale;
import java.util.regex.Pattern;

@SuppressWarnings("deprecation")
public class IISWebLogParser extends WebLogParserW3C {

	/**
	 * Konstruktor ki uporabi prevzeti knstriktor razreda {@link ParserAbs}.
	 *
	 * @see parser.ParserAbs#ParserAbs()
	 * @see IISWebLogParser#setDefaultFields()
	 */
	public IISWebLogParser() {
		super();
	}

	public IISWebLogParser(Locale locale, File[] file) throws FileNotFoundException {
		super(locale, file);
		setDefaultFields();
	}

	/**
	 * @param file
	 * @throws FileNotFoundException
	 */
	public IISWebLogParser(Locale locale, File[] file, List<LogFieldType> ignore) throws FileNotFoundException {
		super(locale, file, ignore);
		setDefaultFields();
	}

	/**
	 * @param file
	 * @param list
	 * @throws FileNotFoundException
	 */
	public IISWebLogParser(Locale locale, File[] file, List<LogFieldType> list, List<LogFieldType> ignore) throws FileNotFoundException {
		super(locale, file);
		super.setFieldType(list);
		setDefaultFields();
	}

	/**
	 * Metoda nastavi prevzete vrednosti poljem razreda:
	 * <p><code>delimiter = ", "</code></p>
	 */
	private void setDefaultFields() {
		delimiter = Pattern.compile(", ");
	}
}
