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
public class IISParser extends W3CParser {

	/**
	 * Konstruktor ki uporabi prevzeti knstriktor razreda {@link ParserAbs}.
	 *
	 * @see parser.ParserAbs#ParserAbs()
	 * @see IISParser#setDefaultFields()
	 */
	public IISParser() {
		super();
		setDefaultFields(Locale.US);
	}
	/**
	 *
	 * @param file
	 * @throws FileNotFoundException
	 */
	public IISParser(Locale locale, File[] file) throws FileNotFoundException {
		super();
		setDefaultFields(locale);
		super.openFile(file);
	}
	/**
	 *
	 * @param file
	 * @param list
	 * @throws FileNotFoundException
	 */
	public IISParser(Locale locale, File[] file, List<FieldType> list) throws FileNotFoundException {
		super();
		setDefaultFields(locale);
		super.openFile(file);
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
	protected String[] parse() throws ArrayIndexOutOfBoundsException, IOException, ParseException {
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
			throw new ParseException("Bad line!!!", super.getPos());
		}
		return tokens;
	}

	@Override
	public ParsedLine parseLine() throws ParseException, IOException, NullPointerException, URISyntaxException {
		return new ParsedLine(process(parse()));
	}
}
