package parser;

import java.io.IOException;
import java.text.ParseException;
import java.time.format.DateTimeFormatter;
import java.util.*;

import fields.*;
import fields.ncsa.*;

/**
 * Parser za formate: Common Log Format, Combined Log Format in Custom Log Formats
 * @author klemen
 */
public class NCSAParser extends AbsParser {

	private DateTimeFormatter formatter;
	private List<FieldType> fieldType;
	/**
	 * Konstruktor ki uporabi prevzeti oknstriktor razreda {@link ParserAbs}.
	 * Dodatne prevzete nastavitve:<p>
	 * fieldType = <code>null</code><p>
	 * format datuma = <code>dd/MMM/yyyy:HH:mm:ss Z</code><p>
	 * locale = Sistemsko prevzet
	 *
	 * @see parser.ParserAbs#ParserAbs()
	 */
	public NCSAParser() {
		super();
		fieldType = null;
		this.formatter = DateTimeFormatter.ofPattern("dd/MMM/yyyy:HH:mm:ss Z").withLocale(Locale.US);
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
	/**
	 * Metoda nastavi vrste polji v log datoteki
	 *
	 * @see FieldType
	 * @param fields Tipi polji v log datoteki
	 * @throws NullPointerException Vhodni parameter <code>fields</code> je enak null
	 */
	public void setFieldType(List<FieldType> fields) throws NullPointerException {
		if(fields == null) {
			throw new NullPointerException();
		} else {
			this.fieldType = fields;
		}
	}

	@Override
	public ParsedLine parseLine() throws ParseException, NullPointerException, IOException {
		if (fieldType == null) throw new NullPointerException("Tipi polji niso specificirani!!!");
		Field[] lineData = new Field[fieldType.size()];
		List<String> tokens = parse(super.getLine());
		if(tokens == null) return null;
		if(tokens.size() != fieldType.size()) throw new ParseException("Bad field types", super.getPos());
		for(int i = 0; i < fieldType.size(); i++) {
			switch(fieldType.get(i)) {
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
				lineData[i] = new UserAgent(tokens.get(i), UserAgent.Type.NCSA);
				break;
			case Cookie:
				lineData[i] = new Cookie(tokens.get(i), Cookie.Type.NCSA);
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

				private ParsedLine next;

				{
					next = parseLine();
				}

				@Override
				public boolean hasNext() {
					return next != null;
				}

				@Override
				public ParsedLine next() {
					try {
						ParsedLine tmp = next;
						next = parseLine();
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
