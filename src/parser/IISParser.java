package parser;

import java.text.ParseException;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
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
		this.dateFormat = DateTimeFormatter.ofPattern("dd/MM/yyyy")
				.withLocale(Locale.getDefault());
		this.timeFormat = DateTimeFormatter.ofPattern("HH:mm:ss")
				.withLocale(Locale.getDefault());
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
	public void setDateFormat(String format, String region) {
		Locale locale;
		if(region == null) {
			locale = Locale.getDefault();
		} else {
			locale = new Locale.Builder().setRegion(region).build();
		}
		this.dateFormat = DateTimeFormatter.ofPattern(format)
				.withLocale(locale);
	}
	/**
	 * Nastavljanje formata za parsanje časa
	 * 
	 * @param format
	 * @param region 
	 */
	public void setTimeFormat(String format, String region) {
		Locale locale;
		if(region == null) {
			locale = Locale.getDefault();
		} else {
			locale = new Locale.Builder().setRegion(region).build();
		}
		this.timeFormat = DateTimeFormatter.ofPattern(format)
				.withLocale(locale);
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

}
