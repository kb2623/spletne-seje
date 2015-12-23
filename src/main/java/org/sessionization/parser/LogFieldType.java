package org.sessionization.parser;

import org.sessionization.database.ConnectionStatusConverter;
import org.sessionization.database.InetAddressConverter;
import org.sessionization.database.MethodConverter;
import org.sessionization.parser.fields.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.text.ParseException;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public enum LogFieldType {
	/**
	 * NCSA:
	 * Remote hostname. Will log the IP address if HostnameLookups is set to Off, which is
	 * the default. If it logs the hostname for only a few hosts, you probably have access
	 * control directives mentioning them by name. See the Require host documentation.
	 *
	 * Format string: <code>%h</code>
	 */
	RemoteHost(new String[]{"%h"}, org.sessionization.parser.fields.ncsa.RemoteHost.class) {
		@Override
		public boolean isKey() {
			return true;
		}
	},
	/**
	 * NCSA:
	 * Remote logname (from identd, if supplied). This will return a dash unless mod_ident is
	 * present and IdentityCheck is set On. See RFC 1413.
	 *
	 * Format string: <code>%l</code>
	 */
	RemoteLogname(new String[]{"%l"}, org.sessionization.parser.fields.ncsa.RemoteLogname.class) {
		@Override
		public boolean isKey() {
			return true;
		}
	},
	/**
	 * NCSA:
	 * Remote user if the request was authenticated. May be bogus if return status (%s) is
	 * 401 (unauthorized).
	 *
	 * Format string: <code>%u</code>
	 *
	 * W3C:
	 *
	 * Format string: <code>cs-username</code>
	 */
	RemoteUser(new String[]{"%u", "cs-username"}, org.sessionization.parser.fields.RemoteUser.class) {
		@Override
		public boolean isKey() {
			return true;
		}
	},
	/**
	 * NCSA:
	 * Time the request was received, in the format [18/Sep/2011:19:18:28 -0400].
	 * The last number indicates the timezone offset from GMT.
	 *
	 * Format string: <code>%t</code>
	 */
	DateTime(new String[]{"%t"}, org.sessionization.parser.fields.ncsa.DateTime.class) {
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
	 * NCSA:
	 * First line of request.
	 *
	 * Format string: <code>%r</code>
	 */
	RequestLine(new String[]{"%r"}, org.sessionization.parser.fields.ncsa.RequestLine.class) {
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
	 * NCSA:
	 * Status. For requests that have been internally redirected, this is the status of the
	 * original request. Use %>s for the final status.
	 *
	 * Format string: <code>%s</code>
	 *
	 * W3C:
	 *
	 * Format string: <code>sc-status</code>
	 */
	StatusCode(new String[]{"%s", "sc-status"}, org.sessionization.parser.fields.StatusCode.class),
	/**
	 * NCSA:
	 * Size of response in bytes, excluding HTTP headers.
	 *
	 * Format string: <code>%B</code>
	 *
	 * Size of response in bytes, excluding HTTP headers. In CLF format, i.e. a '-' rather
	 * than a 0 when no bytes are sent.
	 *
	 * Format string: <code>%b</code>
	 *
	 * W3C:
	 *
	 * Format string: <code>sc-bytes</code>
	 */
	SizeOfResponse(new String[]{"%B", "%b", "sc-bytes"}, org.sessionization.parser.fields.SizeOfResponse.class),
	/**
	 * NCSA:
	 * Bytes received, including request and headers. Cannot be zero. You need to enable
	 * mod_logio to use this.
	 *
	 * Format string: <code>%I</code>
	 *
	 * W3C:
	 *
	 * Format string: <code>cs-bytes</code>
	 */
	SizeOfRequest(new String[]{"%I", "cs-bytes"}, org.sessionization.parser.fields.SizeOfRequest.class),
	/**
	 * NCSA:
	 * Logs Referer on all requests. If no Referer then "-".
	 *
	 * Format string: <code>%{Referer}i</code>
	 *
	 * W3C:
	 *
	 * Format string: <code>cs(Referer)</code>
	 */
	Referer(new String[]{"%{Referer}i", "cs(Referer)"}, org.sessionization.parser.fields.Referer.class) {
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
	 * NCSA:
	 * Logs User-agent on all requests. If no User-agent then "-".
	 *
	 * Format string: <code>%{User-agent}i</code>
	 *
	 * W3C:
	 *
	 * Format string: <code>cs(User-Agent)</code>
	 */
	UserAgent(new String[]{"%{User-agent}i", "cs(User-Agent)"}, org.sessionization.parser.fields.UserAgent.class) {
		@Override
		public boolean isKey() {
			return true;
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
	 * NCSA:
	 * The contents of cookie in the request sent to the server. Only version 0 cookies are
	 * fully supported.
	 *
	 * Format string: <code>%C</code>
	 *
	 * W3C:
	 *
	 * Format string: <code>cs(Cookie)</code>
	 */
	Cookie(new String[]{"%C", "ca(Cookie)"}, org.sessionization.parser.fields.Cookie.class) {
		@Override
		public boolean isKey() {
			return true;
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
	 * NCSA:
	 * The request method.
	 *
	 * Format string: <code>%m</code>
	 *
	 * W3C:
	 *
	 * Format string: <code>cs-method</code>
	 */
	Method(new String[]{"%m", "cs-method"}, org.sessionization.parser.fields.Method.class) {
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
	 * W3C:
	 * Date at which transaction completed, field has type "date"
	 *
	 * Format string: <code>date</code>
	 */
	Date(new String[]{"date"}, org.sessionization.parser.fields.w3c.Date.class) {
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
	 * W3C:
	 * Time at which transaction completed, field has type "time"
	 *
	 * Format string: <code>time</code>
	 */
	Time(new String[]{"time"}, org.sessionization.parser.fields.w3c.Time.class) {
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
	 * NCSA:
	 * The canonical port of the server serving the request.
	 * The canonical port of the server serving the request or the server's actual port or the
	 * client's actual port. Valid formats are canonical, local, or remote.
	 *
	 * Format string: <code>%p</code>, <code>%{local}p</code>, <code>%{canonical}p</code>
	 *
	 * W3C:
	 *
	 * Format string: <code>s-port</code>
	 */
	ServerPort(new String[]{"%p", "%{local}p", "%{canonical}p", "s-port"}, Port.class) {
		@Override
		public LogField parse(Queue<String> queue, AbsWebLogParser parser) throws ParseException {
			return parser.getTokenInstance(getClassE(), queue.poll(), true);
		}
	},
	/**
	 * NCSA:
	 * The canonical port of the server serving the request or the server's actual port or the
	 * client's actual port. Valid formats are canonical, local, or remote.
	 *
	 * Format string: <code>%{remote}p</code>
	 *
	 * W3C:
	 *
	 * Format string: <code>c-port</code>
	 */
	ClientPort(new String[]{"%{remote}p", "c-port"}, Port.class) {
		@Override
		public LogField parse(Queue<String> queue, AbsWebLogParser parser) throws ParseException {
			return parser.getTokenInstance(getClassE(), queue.poll(), false);
		}
	},
	/**
	 * NCSA:
	 * Local IP-address.
	 *
	 * Format string: <code>%A</code>
	 *
	 * W3C:
	 *
	 * Format string: <code>s-ip</code>
	 */
	ServerIP(new String[]{"%A", "s-ip"}, Address.class) {
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
	 * NCSA:
	 * Client IP address of the request (see the mod_remoteip module).
	 *
	 * Format string: <code>%a</code>
	 *
	 * W3C:
	 *
	 * Format string: <code>c-ip</code>
	 */
	ClientIP(new String[]{"%a", "c-ip"}, Address.class) {
		@Override
		public boolean isKey() {
			return true;
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
	 * NCSA:
	 * The time taken to serve the request, in microseconds.
	 *
	 * Format string: <code>%D</code>
	 *
	 * W3C:
	 * The time taken to serve the request, in miliseconds.
	 *
	 * Format string: <code>time-taken</code>
	 */
	TimeTaken(new String[]{"%D", "time-taken"}, org.sessionization.parser.fields.TimeTaken.class) {
		@Override
		public LogField parse(Queue<String> queue, AbsWebLogParser parser) throws ParseException {
			return parser.getTokenInstance(getClassE(), queue.poll(), true);
		}
	},
	/**
	 * W3C:
	 *
	 * Format string: <code>sc-substatus</code>
	 */
	SubStatus(new String[]{"sc-substatus"}, org.sessionization.parser.fields.w3c.SubStatus.class),
	/**
	 * W3C:
	 *
	 * Format string: <code>sc-win32-status</code>
	 */
	Win32Status(new String[]{"sc-win32-status"}, org.sessionization.parser.fields.w3c.Win32Status.class),
	/**
	 * NCSA:
	 * The canonical ServerName of the server serving the request.
	 *
	 * Format string: <code>%v</code>
	 *
	 * W3C:
	 *
	 * Format string: <code>cs-host</code>
	 */
	Host(new String[]{"%v", "cs-host"}, org.sessionization.parser.fields.Host.class),
	/**
	 * W3C:
	 *
	 * Format string: <code>cs-version</code>
	 */
	ProtocolVersion(new String[]{"cs-version"}, Protocol.class),
	/**
	 * W3C:
	 *
	 * Format string: <code>s-sitename</code>
	 */
	SiteName(new String[]{"s-sitename"}, org.sessionization.parser.fields.w3c.SiteName.class),
	/**
	 * NCSA:
	 * The server name according to the UseCanonicalName setting.
	 *
	 * Format string: <code>%V</code>
	 *
	 * W3C:
	 *
	 * Format string: <code>s-computername</code>
	 */
	ComputerName(new String[]{"%V", "s-computername"}, org.sessionization.parser.fields.w3c.ComputerName.class),
	/**
	 * NCSA:
	 * The query string (prepended with a ? if a query string exists, otherwise an empty
	 * string).
	 *
	 * Format string: <code>%q</code>
	 *
	 * W3C:
	 *
	 * Format string: <code>cs-uri-query</code>
	 */
	UriQuery(new String[]{"%q", "cs-uri-query"}, org.sessionization.parser.fields.UriQuery.class) {
		@Override
		public Class[] getDependencies() {
			return new Class[]{
					UriQueryPair.class,
					UriQueryKey.class
			};
		}
	},
	/**
	 * NCSA:
	 * Filename.
	 *
	 * Format string: <code>%f</code>
	 *
	 * W3C:
	 *
	 * Format string: <code>cs-uri-stem</code>
	 */
	UriSteam(new String[]{"%f", "cs-uri-stem"}, UriSteam.class),
	/**
	 * NCSA;
	 * Bytes transferred (received and sent), including request and headers, cannot be
	 * zero. This is the combination of %I and %O. You need to enable mod_logio to use this.
	 *
	 * Format string: <code>%S</code>
	 *
	 * W3C:
	 *
	 * Format string: <code></code>
	 */
	SizeOfTransfer(new String[]{"%S"}, org.sessionization.parser.fields.ncsa.SizeOfTransfer.class),
	/**
	 * NCSA:
	 * Number of keepalive requests handled on this connection. Interesting if KeepAlive is
	 * being used, so that, for example, a '1' means the first keepalive request after the
	 * initial one, '2' the second, etc...; otherwise this is always 0 (indicating the
	 * initial request). Available in versions 2.2.11 and later.
	 *
	 * Format string: <code>%k</code>
	 */
	KeepAliveNumber(new String[]{"%k"}, org.sessionization.parser.fields.ncsa.KeepAliveNumber.class),
	/**
	 * NCSA:
	 * Connection status when response is completed:
	 * X = connection aborted before the response completed.
	 * + =	connection may be kept alive after the response is sent.
	 * - =	connection will be closed after the response is sent.
	 *
	 * Format string: <code>%X</code>
	 */
	ConnectionStatus(new String[]{"%X"}, org.sessionization.parser.fields.ncsa.ConnectionStatus.class) {
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
	 * NCSA:
	 * The process ID of the child that serviced the request.
	 *
	 * Format string: <code>%P</code>
	 */
	ProcessID(new String[]{"%P"}, org.sessionization.parser.fields.ncsa.ProcessID.class),
	/**
	 * Tip polja ki predstavlja opis polja v W3C formatu
	 */
	MetaData(null, null),
	Unknown(null, null);

	private final String[] format;
	private final Class classType;

	LogFieldType(String[] format, Class classType) {
		this.format = format;
		this.classType = classType;
	}

	/**
	 * @return
	 */
	public boolean isKey() {
		return false;
	}

	/**
	 * @return
	 */
	public Class<LogField> getClassE() {
		return classType;
	}

	/**
	 * @return
	 */
	public String getFieldName() {
		String s = getClassE().getSimpleName();
		return Character.toLowerCase(s.charAt(0)) + s.substring(1);
	}

	/**
	 * @return
	 */
	public String getSetterName() {
		return "set" + getClassE().getSimpleName();
	}

	/**
	 * @return
	 */
	public String getGetterName() {
		return "get" + getClassE().getSimpleName();
	}

	/**
	 * @return
	 */
	public Class[] getDependencies() {
		return new Class[0];
	}

	/**
	 * @param queue
	 * @param parser
	 * @return
	 * @throws ParseException
	 */
	public LogField parse(final Queue<String> queue, final AbsWebLogParser parser) throws ParseException {
		return parser.getTokenInstance(getClassE(), queue.poll());
	}

	/**
	 * @return
	 */
	public String[] getFormatString() {
		return format;
	}
}
