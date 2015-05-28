package parser;

import java.text.ParseException;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

@SuppressWarnings("all")
public class IISParser extends AbsParser {

	private DateTimeFormatter timeFormat;
	private DateTimeFormatter dateFormat;
	private List<FieldType> fieldType;
	/**
	 * Konstruktor ki uporabi prevzeti knstriktor razreda {@link ParserAbs}.
	 * Dodatne prevzete nastavitve:<p>
	 * fieldType = <code>null</code><p>
	 * format datuma = <code>dd/MM/yyyy</code><p>
	 * format časa = <code>HH:mm:ss</code><p>
	 * locale = Sistemsko prevzet
	 *
	 * @see parser.ParserAbs#ParserAbs()
	 */
	public IISParser() {
		super();
		fieldType = null;
		this.dateFormat = DateTimeFormatter.ofPattern("dd/MM/yyyy").withLocale(Locale.US);
		this.timeFormat = DateTimeFormatter.ofPattern("HH:mm:ss").withLocale(Locale.US);
	}
	/**
	 *
	 * @param logline
	 * @return
	 */
	private List<String> parse(String logline) {
		if(logline == null) {
			return null;
		}
		boolean inWord = false;
		List<String> tokens = new ArrayList<>();
		char[] lc = logline.toCharArray();
		StringBuffer buff = new StringBuffer();
		for(int i = 0; i < lc.length; i++) {
			switch(lc[i]) {
			case ' ':
				if(inWord) {
					buff.append(lc[i]);
				} else {
					break;
				}
			case ',':
				if(buff.length() > 0) {
					tokens.add(buff.toString());
					buff = new StringBuffer();
					inWord = false;
				}
				break;
			default:
				buff.append(lc[i]);
				inWord = true;
			}
		}
		if(buff.length() > 0) {
			tokens.add(buff.toString());
		}
		return tokens;
	}
    /**
     *
     * @return
     * @throws ParseException
     */
	@Override
	public ParsedLine parseLine() throws ParseException {
		// TODO Začasna implementacija
		String niz;
		try {
			niz = super.getLine();
		} catch(Exception e) {
			niz = null;
		}
		return null;
	}
    /**
	 * Nastavljanje formata za parsanje datuma
	 * 
	 * @param format
	 * @param region 
	 */
	public void setDateFormat(String format, Locale locale) {
		this.dateFormat = DateTimeFormatter.ofPattern(format == null ? "dd/MM/yyyy" : format).withLocale(locale == null ? Locale.US : locale);
	}
	/**
	 * Nastavljanje formata za parsanje časa
	 * 
	 * @param format
	 * @param region 
	 */
	public void setTimeFormat(String format, Locale locale) {
		this.timeFormat = DateTimeFormatter.ofPattern(format == null ? "HH:mm:ss" : format).withLocale(locale == null ? Locale.US : locale);
	}
	/**
	 *
	 * @param fields
	 * @throws NullPointerException
	 */
	public void setFieldType(List<FieldType> fields) throws NullPointerException {
		if(fields == null) {
			throw new NullPointerException();
		} else {
			this.fieldType = fields;
		}
	}
    /**
     * Metoda, ki ustvari iterator
     *
     * @return Iterator za sprehod po datoteki
     */
    @Override
    public Iterator<ParsedLine> iterator() {
        try {
            return new IteratorParsedLine();
        } catch (ParseException e) {
            return null;
        }
    }
    /**
     * Razred, ki implementira iterator
     */
    public class IteratorParsedLine implements Iterator<ParsedLine> {

        private ParsedLine next;

        private IteratorParsedLine() throws ParseException {
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
            } catch (ParseException e) {
                return null;
            }
        }
    }
}
