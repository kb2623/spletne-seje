package org.sessionization.parser;

import org.sessionization.database.ConnectionStatusConverter;
import org.sessionization.database.InetAddressConverter;
import org.sessionization.database.MethodConverter;
import org.sessionization.parser.fields.*;
import org.sessionization.parser.fields.ncsa.ProcessID;

import java.net.URI;
import java.net.URISyntaxException;
import java.text.ParseException;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public enum LogFieldType {
	/**
	 * Apache atribut, lahko je IP ali pa niz
	 */
	RemoteHost {

		@Override
		public boolean isKey() {
			return true;
		}

		@Override
		public Class getClassE() {
			return org.sessionization.parser.fields.ncsa.RemoteHost.class;
		}

	},
	/**
	 * RFC 1413
	 */
	RemoteLogname {
		@Override
		public boolean isKey() {
			return true;
		}

		@Override
		public Class getClassE() {
			return org.sessionization.parser.fields.ncsa.RemoteLogname.class;
		}
	},
	/**
	 * Oseba ki zahteva resurs
	 */
	RemoteUser {
		@Override
		public boolean isKey() {
			return true;
		}

		@Override
		public Class getClassE() {
			return org.sessionization.parser.fields.RemoteUser.class;
		}
	},
	/**
	 * Datum in čas
	 */
	DateTime {
		@Override
		public Class getClassE() {
			return org.sessionization.parser.fields.ncsa.DateTime.class;
		}

		@Override
		public LogField parse(Queue<String> queue, AbsWebLogParser parser) throws ParseException {
			if (parser instanceof NCSAWebLogParser) {
				NCSAWebLogParser p = (NCSAWebLogParser) parser;
				return p.getTokenInstance(getClassE(), queue.poll(), ((NCSAWebLogParser) parser).getDateTimeFormatter());
			} else {
				throw new ParseException("Very bad warning!!!", parser.getPos());
			}
		}
	},
	/**
	 * Metoda, url in protokol
	 */
	RequestLine {
		@Override
		public Class getClassE() {
			return org.sessionization.parser.fields.ncsa.RequestLine.class;
		}

		@Override
		public Class[] getDependencies() {
			List<Class> list = new LinkedList<>();
			list.add(org.sessionization.parser.fields.Method.class);
			list.add(Protocol.class);
			list.add(org.sessionization.parser.fields.UriSteam.class);
			list.add(UriSteamQuery.class);
			list.add(org.sessionization.parser.fields.UriQuery.class);
			for (Class c : UriQuery.getDependencies()) {
				list.add(c);
			}
			return list.toArray(new Class[list.size()]);
		}
	},
	/**
	 * Status zahteve
	 */
	StatusCode {
		@Override
		public Class getClassE() {
			return org.sessionization.parser.fields.StatusCode.class;
		}
	},
	/**
	 * Preneseno število bytov od strežnika do klienta
	 */
	SizeOfResponse {
		@Override
		public Class getClassE() {
			return org.sessionization.parser.fields.SizeOfResponse.class;
		}
	},
	/**
	 * Preneseno število bytov od klienta do strežnika
	 */
	SizeOfRequest {
		@Override
		public Class getClassE() {
			return org.sessionization.parser.fields.SizeOfRequest.class;
		}
	},
	/**
	 * Stran s katere je prišel na našo stran
	 */
	Referer {
		@Override
		public Class getClassE() {
			return org.sessionization.parser.fields.Referer.class;
		}

		@Override
		public Class[] getDependencies() {
			List<Class> list = new LinkedList<>();
			list.add(org.sessionization.parser.fields.UriSteam.class);
			list.add(UriSteamQuery.class);
			list.add(org.sessionization.parser.fields.UriQuery.class);
			for (Class c : UriQuery.getDependencies()) {
				list.add(c);
			}
			list.add(org.sessionization.parser.fields.Host.class);
			return list.toArray(new Class[list.size()]);
		}

		@Override
		public LogField parse(Queue<String> queue, AbsWebLogParser parser) throws ParseException {
			try {
				URI uri = new URI(queue.poll());
				return parser.getTokenInstance(getClassE(), uri);
			} catch (URISyntaxException e) {
				throw new ParseException("Bad referer!!!", parser.getPos());
			}
		}
	},
	/**
	 * Identifikacija brskalnika in botov
	 */
	UserAgent {
		@Override
		public boolean isKey() {
			return true;
		}

		@Override
		public Class getClassE() {
			return org.sessionization.parser.fields.UserAgent.class;
		}

		@Override
		public LogField parse(Queue<String> queue, AbsWebLogParser parser) throws ParseException {
			LogType f;
			if (parser instanceof NCSAWebLogParser) {
				f = LogType.NCSA;
			} else {
				f = LogType.W3C;
			}
			return parser.getTokenInstance(getClassE(), queue.poll(), f);
		}
	},
	/**
	 * Pi�kot (name = value;)
	 */
	Cookie {
		@Override
		public boolean isKey() {
			return true;
		}

		@Override
		public Class getClassE() {
			return org.sessionization.parser.fields.Cookie.class;
		}

		@Override
		public Class[] getDependencies() {
			return new Class[]{
					CookiePair.class,
					CookieKey.class
			};
		}

		@Override
		public LogField parse(Queue<String> queue, AbsWebLogParser parser) throws ParseException {
			LogType f;
			if (parser instanceof NCSAWebLogParser) {
				f = LogType.NCSA;
			} else {
				f = LogType.W3C;
			}
			return parser.getTokenInstance(getClassE(), queue.poll(), f);
		}
	},
	/**
	 * W3C metoda
	 */
	Method {
		@Override
		public Class getClassE() {
			return org.sessionization.parser.fields.Method.class;
		}

		@Override
		public Class[] getDependencies() {
			return new Class[]{
					MethodConverter.class
			};
		}

		@Override
		public LogField parse(Queue<String> queue, AbsWebLogParser parser) throws ParseException {
			return org.sessionization.parser.fields.Method.setMethod(queue.poll());
		}
	},
	/**
	 * W3C datum
	 */
	Date {
		@Override
		public Class getClassE() {
			return org.sessionization.parser.fields.w3c.Date.class;
		}

		@Override
		public LogField parse(Queue<String> queue, AbsWebLogParser parser) throws ParseException {
			if (parser instanceof W3CWebLogParser) {
				W3CWebLogParser p = (W3CWebLogParser) parser;
				return parser.getTokenInstance(getClassE(), queue.poll(), p.getDateFormat());
			} else {
				throw new ParseException("Very bad error!!!", parser.getPos());
			}
		}
	},
	/**
	 * W3C �as
	 */
	Time {
		@Override
		public Class getClassE() {
			return org.sessionization.parser.fields.w3c.Time.class;
		}

		@Override
		public LogField parse(Queue<String> queue, AbsWebLogParser parser) throws ParseException {
			if (parser instanceof W3CWebLogParser) {
				W3CWebLogParser p = (W3CWebLogParser) parser;
				return p.getTokenInstance(getClassE(), queue.poll(), p.getTimeFormat());
			} else {
				throw new ParseException("Very bad error!!!", parser.getPos());
			}
		}
	},
	/**
	 * �tevilka port na strežniku
	 */
	ServerPort {
		@Override
		public Class getClassE() {
			return Port.class;
		}

		@Override
		public LogField parse(Queue<String> queue, AbsWebLogParser parser) throws ParseException {
			return parser.getTokenInstance(getClassE(), queue.poll(), true);
		}
	},
	/**
	 * �tevilka port na klientu
	 */
	ClientPort {
		@Override
		public Class getClassE() {
			return Port.class;
		}

		@Override
		public LogField parse(Queue<String> queue, AbsWebLogParser parser) throws ParseException {
			return parser.getTokenInstance(getClassE(), queue.poll(), false);
		}
	},
	/**
	 * IP strežnika
	 */
	ServerIP {
		@Override
		public Class getClassE() {
			return Address.class;
		}

		@Override
		public Class[] getDependencies() {
			return new Class[]{
					InetAddressConverter.class
			};
		}

		@Override
		public LogField parse(Queue<String> queue, AbsWebLogParser parser) throws ParseException {
			return parser.getTokenInstance(getClassE(), queue.poll(), true);
		}
	},
	/**
	 * IP klienta
	 */
	ClientIP {
		@Override
		public boolean isKey() {
			return true;
		}

		@Override
		public Class getClassE() {
			return Address.class;
		}

		@Override
		public Class[] getDependencies() {
			return new Class[]{
					InetAddressConverter.class
			};
		}

		@Override
		public LogField parse(Queue<String> queue, AbsWebLogParser parser) throws ParseException {
			return parser.getTokenInstance(getClassE(), queue.poll(), false);
		}
	},
	/**
	 * Čas porabljen za obdelavo zahteve (pri W3C v sekundah, pri Apache v milisekunah)
	 */
	TimeTaken {
		@Override
		public Class getClassE() {
			return org.sessionization.parser.fields.TimeTaken.class;
		}

		@Override
		public LogField parse(Queue<String> queue, AbsWebLogParser parser) throws ParseException {
			return parser.getTokenInstance(getClassE(), queue.poll(), true);
		}
	},
	/**
	 * Pod status protokola
	 */
	SubStatus {
		@Override
		public Class getClassE() {
			return org.sessionization.parser.fields.w3c.SubStatus.class;
		}
	},
	/**
	 * Status akcije, vezan na Windows
	 */
	Win32Status {
		@Override
		public Class getClassE() {
			return org.sessionization.parser.fields.w3c.Win32Status.class;
		}
	},
	/**
	 * Ime gostovanja
	 */
	Host {
		@Override
		public Class getClassE() {
			return org.sessionization.parser.fields.Host.class;
		}
	},
	/**
	 * Verija uporabljenega protokola in ime protokola
	 */
	ProtocolVersion {
		@Override
		public Class getClassE() {
			return Protocol.class;
		}
	},
	/**
	 * Ime strani
	 */
	SiteName {
		@Override
		public Class getClassE() {
			return org.sessionization.parser.fields.w3c.SiteName.class;
		}
	},
	/**
	 * Ime Računalnika
	 */
	ComputerName {
		@Override
		public Class getClassE() {
			return org.sessionization.parser.fields.w3c.ComputerName.class;
		}
	},
	/**
	 * Atributi zahteve
	 */
	UriQuery {
		@Override
		public Class getClassE() {
			return org.sessionization.parser.fields.UriQuery.class;
		}

		@Override
		public Class[] getDependencies() {
			return new Class[]{
					UriQueryPair.class,
					UriQueryKey.class
			};
		}
	},
	/**
	 * Ime zahtevanega resursa
	 */
	UriSteam {
		@Override
		public Class getClassE() {
			return UriSteam.class;
		}
	},
	/**
	 * NCSA
	 * Bytes transferred (received and sent), including request and headers, cannot be zero. This is the combination of %I and %O. You need to enable mod_logio to use this.
	 */
	SizeOfTransfer {
		@Override
		public Class getClassE() {
			return org.sessionization.parser.fields.ncsa.SizeOfTransfer.class;
		}
	},
	/**
	 * NCSA
	 * Number of keepalive requests handled on this connection. Interesting if KeepAlive is being used, so that, for example, a '1' means the first keepalive request after the initial one, '2' the second, etc...; otherwise this is always 0 (indicating the initial request). Available in versions 2.2.11 and later.
	 */
	KeepAliveNumber {
		@Override
		public Class getClassE() {
			return org.sessionization.parser.fields.ncsa.KeepAliveNumber.class;
		}
	},
	/**
	 * NCSA
	 * Connection status when response is completed:
	 * X = connection aborted before the response completed.
	 * + =	connection may be kept alive after the response is sent.
	 * - =	connection will be closed after the response is sent.
	 */
	ConnectionStatus {
		@Override
		public Class getClassE() {
			return org.sessionization.parser.fields.ncsa.ConnectionStatus.class;
		}

		@Override
		public Class[] getDependencies() {
			return new Class[]{
					ConnectionStatusConverter.class
			};
		}

		@Override
		public LogField parse(Queue<String> queue, AbsWebLogParser parser) throws ParseException {
			return org.sessionization.parser.fields.ncsa.ConnectionStatus.getConnectionStatus(queue.poll());
		}
	},
	/**
	 * ID procesa, ki je obdelal zahtevo
	 */
	ProcessID {
		@Override
		public Class getClassE() {
			return ProcessID.class;
		}
	},
	/**
	 * Tip polja ki predstavlja opis polja v W3C formatu
	 */
	MetaData {},
	Unknown;

	public boolean isKey() {
		return false;
	}

	public Class<LogField> getClassE() {
		return null;
	}

	public String getFieldName() {
		String s = getClassE().getSimpleName();
		return Character.toLowerCase(s.charAt(0)) + s.substring(1);
	}

	public String getSetterName() {
		return "set" + getClassE().getSimpleName();
	}

	public String getGetterName() {
		return "get" + getClassE().getSimpleName();
	}

	public Class[] getDependencies() {
		return new Class[0];
	}

	public LogField parse(final Queue<String> queue, final AbsWebLogParser parser) throws ParseException {
		return parser.getTokenInstance(getClassE(), queue.poll());
	}
}
