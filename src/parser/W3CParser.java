package parser;

import java.io.IOException;
import java.text.ParseException;
import java.time.format.DateTimeFormatter;
import java.util.*;

import fields.*;
import fields.w3c.*;
import fields.w3c.Date;

/**
 * Parser za formate: Extended Log Format
 * @author klemen
 */

public class W3CParser extends AbsParser {

	private DateTimeFormatter timeFormat;
	private DateTimeFormatter dateFormat;
	private List<FieldType> fieldType;
	/**
	 * Konstruktor ki uporabi prevzeti oknstriktor razreda {@link ParserAbs}.
	 * Dodatne prevzete nastavitve:<p>
	 * fieldType = <code>null</code><p>
	 * format datuma = <code>yyyy-MM-dd</code><p>
	 * format časa = <code>HH:mm:ss</code><p>
	 * locale = Sistemsko prevzet
	 *
	 * @see parser.ParserAbs#ParserAbs()
	 */
	public W3CParser() {
		super();
		fieldType = null;
		this.dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd").withLocale(Locale.US);
		this.timeFormat = DateTimeFormatter.ofPattern("HH:mm:ss").withLocale(Locale.US);
	}
	/**
	 * Nastavljanje formata za parsanje datuma
	 *
	 * @param format
	 * @param localeString
	 */
	public void setDateFormat(String format, Locale locale) {
		this.dateFormat = DateTimeFormatter.ofPattern(format == null ? "yyyy-MM-dd" : format).withLocale(locale == null ? Locale.getDefault() : locale);
	}
	/**
     * Nastavljanje formata za parsanje &#x10d;asa
     *
     * @param format
     * @param localeString
     */
	public void setTimeFormat(String format, Locale locale) {
		this.timeFormat = DateTimeFormatter.ofPattern(format == null ? "HH:mm:ss" : format).withLocale(locale == null ? Locale.getDefault() : locale);
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
		StringBuffer buff = new StringBuffer();
		char[] lc = logline.toCharArray();
		for(int i = 0; i < lc.length; i++) {
			switch(lc[i]) {
			case ' ':
				if(buff.length() > 0) {
					tokens.add(buff.toString());
					buff = new StringBuffer();
				}
				break;
			default:
				buff.append(lc[i]);
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
		try {
			List<String> tokens = parse(getLine());
			EnumMap<FieldType, Field> data = new EnumMap<>(FieldType.class);
			if(tokens == null) {
				return null;
			}
			if(tokens.get(0).charAt(0) == '#') {
				if(tokens.get(0).equals("#Fields:")) {
					fieldType = FieldType.createExtendedLogFormat(tokens);
				}
				StringBuilder builder = new StringBuilder();
				tokens.stream().forEach((s) -> builder.append(s).append(' '));
				data.put(FieldType.MetaData, new MetaData(builder.toString()));
				return new ParsedLine(data);
			}
			if(fieldType == null) {
				throw new ParseException("Bad log format", super.getPos());
			}
			if(fieldType.size() != tokens.size()) {
				throw new ParseException("Can't parse a line", super.getPos());
			}
			for(int i = 0; i < fieldType.size(); i++) {
				switch (fieldType.get(i)) {
				case Referer:
					data.put(FieldType.Referer, new Referer(tokens.get(i)));
					break;
				case Cookie:
					data.put(FieldType.Cookie, new Cookie(tokens.get(i), Cookie.Type.W3C));
					break;
				case UserAgent:
					data.put(FieldType.UserAgent, new UserAgent(tokens.get(i), UserAgent.Type.W3C));
					break;
				case Method:
					int indexOfProtocolVersion = fieldType.indexOf(FieldType.ProtocolVersion);
					if(indexOfProtocolVersion != -1) {
						data.put(FieldType.Method, Method.setMethod(tokens.get(indexOfProtocolVersion).split("/")[0], tokens.get(i)));
					} else {
						data.put(FieldType.Method, Method.setMethod("http", tokens.get(i)));
					}
					break;
				case Date:
					data.put(FieldType.Date, new Date(tokens.get(i), dateFormat));
					break;
				case Time:
					data.put(FieldType.Time, new Time(tokens.get(i), timeFormat));
					break;
				case SiteName:
					data.put(FieldType.SiteName, new SiteName(tokens.get(i)));
					break;
				case ComputerName:
					data.put(FieldType.ComputerName, new ComputerName(tokens.get(i)));
					break;
				case ServerIP:
					data.put(FieldType.ServerIP, new Address(tokens.get(i), true));
					break;
				case ClientIP:
					data.put(FieldType.ClientIP, new Address(tokens.get(i), false));
					break;
				case UriStem:
					data.put(FieldType.UriStem, new UriStem(tokens.get(i)));
					break;
				case UriQuery:
					data.put(FieldType.UriQuery, new UriQuery(tokens.get(i)));
					break;
				case ServerPort:
					data.put(FieldType.ServerPort, new Port(tokens.get(i), true));
					break;
				case ClientPort:
					data.put(FieldType.ClientPort, new Port(tokens.get(i), false));
					break;
				case RemoteUser:
					data.put(FieldType.RemoteUser, new RemoteUser(tokens.get(i)));
					break;
				case ProtocolVersion:
					data.put(FieldType.ProtocolVersion, new Protocol(tokens.get(i)));
					break;
				case Host:
					data.put(FieldType.Host, new Host(tokens.get(i)));
					break;
				case StatusCode:
					data.put(FieldType.StatusCode, new StatusCode(tokens.get(i)));
					break;
				case SubStatus:
					data.put(FieldType.SubStatus, new SubStatus(tokens.get(i)));
					break;
				case Win32Status:
					data.put(FieldType.Win32Status, new Win32Status(tokens.get(i)));
					break;
				case SizeOfRequest:
					data.put(FieldType.SizeOfRequest, new SizeOfRequest(tokens.get(i)));
					break;
				case SizeOfResponse:
					data.put(FieldType.SizeOfResponse, new SizeOfResponse(tokens.get(i)));
					break;
				case TimeTaken:
					data.put(FieldType.TimeTaken, new TimeTaken(tokens.get(i), false));
					break;
				default:
					/* TODO Pršel si do neznanega polja
					 */
					break;
				}
			}
			return new ParsedLine(data);
		} catch(IOException e) {
			throw new ParseException(e.getMessage(), super.getPos());
		} catch(IllegalArgumentException e) {
			//TODO
		}
		return null;
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
