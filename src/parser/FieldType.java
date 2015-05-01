package parser;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

public enum FieldType {
	/** Apache atribut, lahko je IP ali pa niz */
	RemoteHost,
	/** RFC 1413 */
	RemoteLogname,
	/** Oseba ki zahteva resurs */
	RemoteUser,
	/** Datum in čas */
	DateTime,
	/** Metoda, url in protokol */
	RequestLine,
	/** Status zahteve */
	StatusCode,
	/** Preneseno število bytov od strežnika do klienta */
	SizeOfResponse,
	/** Preneseno število bytov od klienta do strežnika */
	SizeOfRequest,
	/** Stran s katere je prišel na našo stran */
	Referer,
	/** Identifikacija brskalnika in botov */
	UserAgent,
	/** Pi�kot (name = value;) */
	Cookie,
	/** W3C metoda */
	Method,
	/** W3C datum */
	Date,
	/** W3C �as */
	Time,
	/** �tevilka port na strežniku */
	ServerPort,
	/** �tevilka port na klientu */
	ClientPort,
	/** W3C IP strežnika */
	ServerIP,
	/** W3C IP klienta */
	ClientIP,
	/**	Čas porabljen za obdelavo zahteve (pri W3C v sekundah, pri Apache v milisekunah) */
	TimeTaken,
	/** Pod status protokola */
	SubStatus,
	/** Status akcije, vezan na Windows */
	Win32Status,
	/** Ime gostovanja */
	Host,
	/** Verija uporabljenega protokola in ime protokola */
	ProtocolVersion,
	/** Ime strani */
	SiteName,
	/** Ime Računalnika */
	ComputerName,
	/** Atributi zahteve */
	UriQuery,
	/** Ime zahtevanega resursa */
	UriStem,
	/** Neznano polje */
	Unknown,
	/** */
	MetaData;
	
	public static List<FieldType> createCommonLogFormat() {
		List<FieldType> lists = new ArrayList<>();
		lists.add(RemoteHost);
		lists.add(RemoteLogname);
		lists.add(RemoteUser);
		lists.add(DateTime);
		lists.add(RequestLine);
		lists.add(StatusCode);
		lists.add(SizeOfResponse);
		return lists;
	}
	
	public static List<FieldType> createCombinedLogFormat() {
		List<FieldType> lists = FieldType.createCommonLogFormat();
		lists.add(Referer);
		lists.add(UserAgent);
		return lists;
	}
	
	public static List<FieldType> createExtendedLogFormat(List<String> fieldLine) {
		List<FieldType> list = new ArrayList<>();
		for(int i = 1; i < fieldLine.size(); i++) {
			switch (fieldLine.get(i)) {
			case "cs(Referer)": 	list.add(Referer); 			break;
			case "cs(Cookie)":		list.add(Cookie); 			break;
			case "cs(User-Agent)": 	list.add(UserAgent);		break;
			case "cs-method":		list.add(Method);			break;
			case "date":			list.add(Date);				break;
			case "time":			list.add(Time);				break;
			case "s-sitename":		list.add(SiteName);			break;
			case "s-computername":	list.add(ComputerName);		break;
			case "s-ip":			list.add(ServerIP);			break;
			case "c-ip":			list.add(ClientIP);			break;
			case "cs-uri-stem":		list.add(UriStem);			break;
			case "cs-uri-query":	list.add(UriQuery);			break;
			case "s-port":			list.add(ServerPort);		break;
			case "c-port":			list.add(ClientPort);		break;
			case "cs-username":		list.add(RemoteUser);		break;
			case "cs-version":		list.add(ProtocolVersion);	break;
			case "cs-host":			list.add(Host);				break;
			case "sc-status":		list.add(StatusCode);		break;
			case "sc-substatus":	list.add(SubStatus);		break;
			case "sc-win32-status":	list.add(Win32Status);		break;
			case "sc-bytes":		list.add(SizeOfResponse);	break;
			case "cs-bytes":		list.add(SizeOfRequest);	break;
			case "time-taken":		list.add(TimeTaken);		break;
			default:				list.add(Unknown);			break;
			}
		}
		return list;
	}
	
	public static List<FieldType> createCustomLogFormat(String line) throws ParseException {
		List<FieldType> list = new ArrayList<>();
		String[] tab = line.split(" ");
		for(int i = 0; i < tab.length; i++) {
			switch (tab[i]) {
			case "%%": list.add(null);				break;
			case "%a": list.add(ClientIP); 			break;
			case "%A": list.add(ServerIP);			break;
			case "%B": list.add(SizeOfResponse); 	break;
			case "%b": list.add(SizeOfResponse);	break;
			case "%C": list.add(Cookie);			break;
			case "%D": list.add(null);				break;
			case "%e": list.add(null);				break;
			case "%f": list.add(null);				break;
			case "%h": list.add(RemoteHost);		break;
			case "%H": list.add(ProtocolVersion);	break;
			case "%i": list.add(null);				break;
			case "%k": list.add(null);				break;
			case "%l": list.add(RemoteLogname);		break;
			case "%L": list.add(null);				break;
			case "%m": list.add(Method);			break;
			case "%n": list.add(null);				break;
			case "%o": list.add(null);				break;
			case "%p": list.add(ServerPort);		break;
			case "%P": list.add(null);				break;
			case "%q": list.add(UriQuery);			break;
			case "%r": list.add(RequestLine);		break;
			case "%R": list.add(null);				break;
			case "%s": list.add(StatusCode);		break;
			case "%t": list.add(DateTime);			break;
			case "%T": list.add(TimeTaken);			break;
			case "%u": list.add(RemoteUser);		break;
			case "%U": list.add(UriStem);			break;
			case "%v": list.add(null);				break;
			case "%V": list.add(null);				break;
			case "%X": list.add(null);				break;
			case "%I": list.add(SizeOfRequest);		break;
			case "%O": list.add(null);				break;
			case "%S": list.add(null);				break;
			case "%^ti": list.add(null);			break;
			case "%^to": list.add(null);			break;
			default: throw new ParseException("Unknown field", i);
			}
		}
		return list;
	}
}
