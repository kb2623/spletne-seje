package parser;

import java.io.IOException;
import java.text.ParseException;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;

import fields.*;
import fields.ncsa.*;
import java.util.Locale;
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
	 * Nastavljanje formata za parsanje datuma
	 * 
	 * @param format
	 * @param localeString 
	 */
	public void setDateFormat(String format, String localeString) {
		Locale locale;
		if (localeString == null) {
			locale = Locale.getDefault();
		} else {
			locale = new Locale(localeString);
		}
		this.formatter = DateTimeFormatter.ofPattern(format).withLocale(locale);
	}
	/**
	 *
	 * @param logline
	 * @return
	 * @see String
	 */
	private List<String> parse(String logline) {
		if(logline == null) {
			return null;
		}
		List<String> tokens = new ArrayList<>();
	    StringBuilder buff = new StringBuilder();
	    char[] lc = logline.toCharArray();
	    boolean inQuotes = false, inBrackets = false;
	    for (int i = 0; i < lc.length; i++) {
	    	switch(lc[i]) {
	    	case '"':
	    		if(inQuotes) {
	    			tokens.add(buff.toString());
	    			buff = new StringBuilder();
	    		}
	    		inQuotes = !inQuotes;
	    		break;
	    	case '[':
	    		if(!inBrackets && !inQuotes)  {
	    			inBrackets = true;
	    		}
	    		break;
	    	case ']':
	    		if(inBrackets) {
	    			tokens.add(buff.toString());
	    			buff = new StringBuilder();
	    			inBrackets = false;
	    		}
	    		break;
	    	case ' ':
	    		if(!inBrackets && !inQuotes && buff.length() > 0) {
	    			tokens.add(buff.toString());
	    			buff = new StringBuilder();
	    		} else if(inBrackets || inQuotes) {
	    			buff.append(lc[i]);
	    		}
	    		break;
    		default:
    			buff.append(lc[i]);
	    	}
    	}
	    if (buff.length() > 0) {
	    	tokens.add(buff.toString());
	    }
	    return tokens;
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
	 * 
	 * @return
	 * @throws ParseException
	 * @throws NullPointerException
	 * @throws IOException 
	 */
	@Override
	public ParsedLine parseLine() throws ParseException, NullPointerException, IOException {
		if(this.fieldType == null || formatter == null) {
			throw new NullPointerException("Fields types not specified");
		}
		EnumMap<FieldType, Field> data = new EnumMap<>(FieldType.class);
		List<String> tokens = parse(super.getLine());
		if(tokens == null) {
			return null;
		}
		if(tokens.size() != fieldType.size()) {
			throw new ParseException("Bad field types", super.getPos());
		}
		for(int i = 0; i < fieldType.size(); i++) {
			switch(fieldType.get(i)) {
			case RemoteHost:
				data.put(FieldType.RemoteHost ,new RemoteHost(tokens.get(i)));
				break;
			case Referer:
				data.put(FieldType.Referer ,new Referer(tokens.get(i)));
				break;
			case RemoteLogname:
				data.put(FieldType.RemoteLogname ,new RemoteLogname(tokens.get(i)));
				break;
			case RemoteUser:
				data.put(FieldType.RemoteUser ,new RemoteUser(tokens.get(i)));
				break;
			case RequestLine:
				String[] tab = tokens.get(i).split(" ");
				data.put(FieldType.RequestLine ,new RequestLine(tab[0], tab[1], tab[2]));
				break;
			case SizeOfResponse:
				data.put(FieldType.SizeOfResponse ,new SizeOfResponse(tokens.get(i)));
				break;
			case StatusCode:
				data.put(FieldType.StatusCode ,new StatusCode(tokens.get(i)));
				break;
			case DateTime:
				data.put(FieldType.DateTime ,new DateTime(tokens.get(i), formatter));
				break;
			case UserAgent:
				data.put(FieldType.UserAgent ,new UserAgent(tokens.get(i), UserAgent.Type.NCSA));
				break;
			case Cookie:
				data.put(FieldType.Cookie ,new Cookie(tokens.get(i), Cookie.Type.NCSA));
				break;
			case TimeTaken:
				data.put(FieldType.TimeTaken ,new TimeTaken(tokens.get(i), true));
				break;
			default:
				throw new ParseException("Unknown field type", super.getPos());
			}
		}
		return new ParsedLine(data);
	}

}
