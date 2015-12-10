package org.sessionization.parser;

import org.sessionization.fields.LogFieldType;
import org.sessionization.parser.datastruct.ParsedLine;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.ParseException;
import java.util.List;
import java.util.Locale;
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
	 *
	 * @param file
	 * @throws FileNotFoundException
	 */
	public IISWebLogParser(Locale locale, File[] file, List<LogFieldType> ignore) throws FileNotFoundException {
		super(locale, file);
	}
	/**
	 *
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
	}

	@Override
	protected String[] parse() throws ArrayIndexOutOfBoundsException, IOException {
		int i = -1;
		String logline = super.getLine();
		String[] tokens = new String[super.fieldType.size()];
		StringBuilder buff = new StringBuilder();
		for (char c : logline.toCharArray()) {
			switch (c) {
			case ' ':
				if (buff.length() > 0) {
					tokens[++i] = buff.toString();
					buff = new StringBuilder();
				}
				break;
			case ',':
				break;
			default:
				buff.append(c);
			}
		}
		if (buff.length() > 0) {
			tokens[++i] = buff.toString();
		}
		if (tokens.length != i + 1) {
			throw new IOException();
		}
		return tokens;
	}

	@Override
	public ParsedLine parseLine() throws ParseException {
		try {
			return new ParsedLine(process(parse()));
		} catch (ArrayIndexOutOfBoundsException | IOException e) {
			throw new ParseException("Bad line!!!", getPos());
		}
	}
}
