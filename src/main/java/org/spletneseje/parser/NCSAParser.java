package org.spletneseje.parser;

import java.io.*;
import java.text.ParseException;
import java.time.format.DateTimeFormatter;
import java.util.*;

import org.spletneseje.fields.*;
import org.spletneseje.fields.ncsa.DateTime;
import org.spletneseje.fields.ncsa.RemoteHost;
import org.spletneseje.fields.ncsa.RemoteLogname;
import org.spletneseje.fields.ncsa.RequestLine;
import org.spletneseje.parser.datastruct.ParsedLine;

/**
 * Parser za formate: Common Log Format, Combined Log Format in Custom Log Formats
 * @author klemen
 */
public class NCSAParser extends AbsParser {

	private DateTimeFormatter formatter;
	/**
	 * Konstruktor ki nastavi polja na prevzete vrednosti
	 *
	 * @see ParserAbs#ParserAbs()
	 * @see NCSAParser#setDefaultFields()
	 */
	public NCSAParser() {
		super();
		setDefaultFields();
	}
	/**
	 * Konstruktor ki odpre tudi datoteko
	 *
	 * @param path Pot do datoteke predstavljena z nizom
	 * @throws FileNotFoundException Datoteka ne obstaja
	 * @see AbsParser#AbsParser(String)
	 * @see NCSAParser#setDefaultFields()
	 */
	public NCSAParser(String path) throws FileNotFoundException {
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
	public NCSAParser(StringReader input) {
		super(input);
		setDefaultFields();
	}
	/**
	 *
	 * @param reader
	 * @see AbsParser#AbsParser(BufferedReader)
	 * @see IISParser#setDefaultFields()
	 */
	public NCSAParser(BufferedReader reader) {
		super(reader);
		setDefaultFields();
	}
	/**
	 * Metoda ki nastavi polja na prevzete vrednosti.
	 * <p><code>formatter = dd/MMM/yyyy:HH:mm:ss Z</code></p>
	 * <p><code>locale = Locale.US</code></p>
	 */
	private void setDefaultFields() {
		formatter = DateTimeFormatter.ofPattern("dd/MMM/yyyy:HH:mm:ss Z").withLocale(Locale.US);
	}
	/**
	 * Nastavljanje formata za parsanje datuma.
	 * Ce je vhodni parameter <code>format</code> null, se nastavi format na prevzeti format.
	 * Ce je vhodni parameter <code>locale</code> null, se nastavi lokalnost na prevzeti format.
	 *
	 * @see DateTimeFormatter#ofPattern(String, Locale)
	 * @param format Format datuma
	 * @param localeString Niz, ki pove v katerem jeziku je zapisan datum
	 */
	public void setDateFormat(String format, Locale locale) {
		this.formatter = DateTimeFormatter.ofPattern(format == null ? "dd/MMM/yyyy:HH:mm:ss Z" : format).withLocale(locale == null ? Locale.getDefault() : locale);
	}
	/**
	 * Metoda razcleni vhodni niz na vec delov in jih shrani v seznam.
	 *
	 * @param logline Vrstica v log datoteki
	 * @return Seznam razclenjenih nizov.
	 * @see String
	 */
	private List<String> parse(String logline) {
		if(logline == null) {
			return null;
		}
		List<String> tokens = new ArrayList<>();
	    StringBuilder buff = new StringBuilder();
	    boolean inQuotes = false, inBrackets = false;
		for (char c : logline.toCharArray()) {
			switch (c) {
			case '"':
				if (inQuotes) {
					tokens.add(buff.toString());
					buff = new StringBuilder();
				}
				inQuotes = !inQuotes;
				break;
			case '[':
				if (!inBrackets && !inQuotes) {
					inBrackets = true;
				}
				break;
			case ']':
				if (inBrackets) {
					tokens.add(buff.toString());
					buff = new StringBuilder();
					inBrackets = false;
				}
				break;
			case ' ':
				if (!inBrackets && !inQuotes && buff.length() > 0) {
					tokens.add(buff.toString());
					buff = new StringBuilder();
				} else if (inBrackets || inQuotes) {
					buff.append(c);
				}
				break;
			default:
				buff.append(c);
			}
		}
	    if (buff.length() > 0) {
	    	tokens.add(buff.toString());
	    }
	    return tokens;
    }

	@Override
	public ParsedLine parseLine() throws ParseException, NullPointerException, IOException {
		if (super.fieldType == null) throw new NullPointerException("Tipi polji niso specificirani!!!");
		Field[] lineData = new Field[super.fieldType.size()];
		List<String> tokens = parse(super.getLine());
		if(tokens.size() != super.fieldType.size()) throw new ParseException("Bad field types", super.getPos());
		for(int i = 0; i < super.fieldType.size(); i++) {
			switch(super.fieldType.get(i)) {
			case RemoteHost:
				lineData[i] = new RemoteHost(tokens.get(i));
				break;
			case Referer:
				lineData[i] = new Referer(tokens.get(i));
				break;
			case RemoteLogname:
				lineData[i] = new RemoteLogname(tokens.get(i));
				break;
			case RemoteUser:
				lineData[i] = new RemoteUser(tokens.get(i));
				break;
			case RequestLine:
				String[] tab = tokens.get(i).split(" ");
				lineData[i] = new RequestLine(tab[0], tab[1], tab[2]);
				break;
			case SizeOfResponse:
				lineData[i] = new SizeOfResponse(tokens.get(i));
				break;
			case StatusCode:
				lineData[i] = new StatusCode(tokens.get(i));
				break;
			case DateTime:
				lineData[i] = new DateTime(tokens.get(i), formatter);
				break;
			case UserAgent:
				lineData[i] = new UserAgent(tokens.get(i), LogType.NCSA);
				break;
			case Cookie:
				lineData[i] = new Cookie(tokens.get(i), LogType.NCSA);
				break;
			case TimeTaken:
				lineData[i] = new TimeTaken(tokens.get(i), true);
				break;
			default:
				throw new ParseException("Unknown field type", super.getPos());
			}
		}
		return new ParsedLine(lineData);
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
					}
				}
			};
        } catch (ParseException | IOException e) {
            return null;
        }
    }
}