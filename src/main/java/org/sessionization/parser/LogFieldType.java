package org.sessionization.parser;

import org.datastruct.concurrent.ObjectPool;
import org.sessionization.database.ConnectionStatusConverter;
import org.sessionization.database.InetAddressConverter;
import org.sessionization.database.MethodConverter;
import org.sessionization.parser.fields.*;
import org.sessionization.parser.fields.ncsa.RequestLine;
import org.sessionization.parser.fields.w3c.MetaData;

import java.net.InetAddress;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.UnknownHostException;
import java.text.ParseException;
import java.util.*;
import java.util.regex.Pattern;

public enum LogFieldType {
	/**
	 * NCSA:
	 * Remote hostname. Will log the IP address if HostnameLookups is set to Off, which is
	 * the default. If it logs the hostname for only a few hosts, you probably have access
	 * control directives mentioning them by name. See the Require host documentation.
	 *
	 * Format string: <code>%h</code>
	 */
	RemoteHost(new String[]{"%h"}, Address.class, null) {
		@Override
		public Class[] getDependencies() {
			return new Class[]{InetAddressConverter.class};
		}

		@Override
		public boolean isKey() {
			return true;
		}

		@Override
		public LogField parse(Scanner scanner, WebLogParser parser) throws ParseException {
			try {
				InetAddress address = InetAddress.getByName(scanner.next());
				return parser.getTokenInstance(classType, address, false);
			} catch (UnknownHostException e) {
				throw new ParseException("Bad remote hostname!!!", parser.getPos());
			}
		}
	},
	/**
	 * NCSA:
	 * Remote logname (from identd, if supplied). This will return a dash unless mod_ident is
	 * present and IdentityCheck is set On. See RFC 1413.
	 *
	 * Format string: <code>%l</code>
	 */
	RemoteLogname(new String[]{"%l"}, org.sessionization.parser.fields.ncsa.RemoteLogname.class, "([a-z0-9_-]+|-)") {
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
	RemoteUser(new String[]{"%u", "cs-username"}, org.sessionization.parser.fields.RemoteUser.class, "([a-z0-9_-]+|-)") {
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
	DateTime(new String[]{"%t"}, org.sessionization.parser.fields.ncsa.DateTime.class, "(\\[)([^\\]]+?)(\\])") {
		@Override
		public LogField parse(Scanner scanner, WebLogParser parser) throws ParseException {
			try {
				StringBuilder builder = new StringBuilder(scanner.findWithinHorizon(this.pattern, 0));
				if (scanner.hasNext()) {
					scanner.skip(scanner.delimiter());
				}
				builder.deleteCharAt(0).deleteCharAt(builder.length() - 1);
				return parser.getTokenInstance(classType, builder.toString(), ((NCSAWebLogParser) parser).getDateTimeFormatter());
			} catch (InputMismatchException e) {
				throw new ParseException("Bad date time!!!", parser.getPos());
			}
		}
	},
	/**
	 * NCSA:
	 * First line of request.
	 *
	 * Format string: <code>%r</code>
	 */
	RequestLine(new String[]{"%r"}, org.sessionization.parser.fields.ncsa.RequestLine.class, "(\")([^\"]+?)(\")") {
		@Override
		public Class[] getDependencies() {
			List<Class> list = new LinkedList<>();
			list.add(Protocol.class);
			list.add(org.sessionization.parser.fields.Method.class);
			for (Class c : Method.getDependencies()) {
				list.add(c);
			}
			list.add(org.sessionization.parser.fields.UriQuery.class);
			list.add(org.sessionization.parser.fields.UriSteam.class);
			list.add(UriSteamQuery.class);
			for (Class c : UriQuery.getDependencies()) {
				list.add(c);
			}
			return list.toArray(new Class[list.size()]);
		}

		@Override
		public LogField parse(Scanner scanner, WebLogParser parser) throws ParseException {
			try {
				StringBuilder builder = new StringBuilder(scanner.findWithinHorizon(this.pattern, 0));
				if (scanner.hasNext()) {
					scanner.skip(scanner.delimiter());
				}
				builder.deleteCharAt(0).deleteCharAt(builder.length() - 1);
				return parser.getTokenInstance(classType, builder.toString());
			} catch (InputMismatchException e) {
				throw new ParseException("Bad first line of request!!!", parser.getPos());
			}
		}

		@Override
		public Map<Class, ObjectPool.ObjectCreator> getCreators() {
			return ObjectCreators.RequestLine.getCreator();
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
	StatusCode(new String[]{"%s", "sc-status"}, org.sessionization.parser.fields.StatusCode.class, "[0-9]+"),
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
	SizeOfResponse(new String[]{"%B", "%b", "sc-bytes"}, org.sessionization.parser.fields.SizeOfResponse.class, "[0-9]+"),
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
	SizeOfRequest(new String[]{"%I", "cs-bytes"}, org.sessionization.parser.fields.SizeOfRequest.class, "[0-9]+"),
	/**
	 * NCSA:
	 * Logs Referrer on all requests. If no Referer then "-".
	 *
	 * Format string: <code>%{Referer}i</code>
	 *
	 * W3C:
	 *
	 * Format string: <code>cs(Referer)</code>
	 */
	RefererNCSA(new String[]{"%{Referer}i"}, Referer.class, "(\")([^\"]+?)(\")") {
		@Override
		public Class[] getDependencies() {
			List<Class> list = new LinkedList<>();
			list.add(org.sessionization.parser.fields.Host.class);
			list.add(org.sessionization.parser.fields.UriSteam.class);
			list.add(UriSteamQuery.class);
			list.add(org.sessionization.parser.fields.UriQuery.class);
			for (Class c : UriQuery.getDependencies()) {
				list.add(c);
			}
			return list.toArray(new Class[list.size()]);
		}

		@Override
		public LogField parse(Scanner scanner, WebLogParser parser) throws ParseException {
			try {
				StringBuilder builder = new StringBuilder(scanner.next(pattern));
				if (scanner.hasNext()) {
					scanner.skip(scanner.delimiter());
				}
				builder.deleteCharAt(0).deleteCharAt(builder.length() - 1);
				URI uri = new URI(builder.toString());
				return parser.getTokenInstance(classType, uri);
			} catch (InputMismatchException | URISyntaxException e) {
				throw new ParseException("Bad referer!!!", parser.getPos());
			}
		}

		@Override
		public Map<Class, ObjectPool.ObjectCreator> getCreators() {
			return ObjectCreators.Referer.getCreator();
		}
	},
	RefererW3C(new String[]{"cs(Referer)"}, Referer.class, null) {
		@Override
		public Class[] getDependencies() {
			List<Class> list = new LinkedList<>();
			list.add(org.sessionization.parser.fields.Host.class);
			list.add(org.sessionization.parser.fields.UriSteam.class);
			list.add(UriSteamQuery.class);
			list.add(org.sessionization.parser.fields.UriQuery.class);
			for (Class c : UriQuery.getDependencies()) {
				list.add(c);
			}
			return list.toArray(new Class[list.size()]);
		}

		@Override
		public LogField parse(Scanner scanner, WebLogParser parser) throws ParseException {
			try {
				URI uri = new URI(scanner.next());
				return parser.getTokenInstance(classType, uri);
			} catch (URISyntaxException e) {
				throw new ParseException("Bad referer!!!", parser.getPos());
			}
		}

		@Override
		public Map<Class, ObjectPool.ObjectCreator> getCreators() {
			return ObjectCreators.Referer.getCreator();
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
	UserAgentNCSA(new String[]{"%{User-agent}i"}, UserAgent.class, "(((\")([^\"]+?)(\"))|\\-)") {
		@Override
		public boolean isKey() {
			return true;
		}

		@Override
		public LogField parse(Scanner scanner, WebLogParser parser) throws ParseException {
			try {
				StringBuilder builder = new StringBuilder(scanner.findWithinHorizon(pattern, 0));
				if (scanner.hasNext()) {
					scanner.skip(scanner.delimiter());
				}
				builder.deleteCharAt(0).deleteCharAt(builder.length() - 1);
				return parser.getTokenInstance(classType, builder.toString(), LogType.NCSA);
			} catch (InputMismatchException e) {
				throw new ParseException("Bad user agent string!!!", parser.getPos());
			}
		}
	},
	UserAgentW3C(new String[]{"cs(User-Agent)"}, UserAgent.class, null) {
		@Override
		public boolean isKey() {
			return true;
		}

		@Override
		public LogField parse(Scanner scanner, WebLogParser parser) throws ParseException {
			return parser.getTokenInstance(classType, scanner.next(), LogType.W3C);
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
	CookieNCSA(new String[]{"%C"}, Cookie.class, "(((\")([^\"]+?)(\"))|\\-)") {
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
		public LogField parse(Scanner scanner, WebLogParser parser) throws ParseException {
			try {
				StringBuilder builder = new StringBuilder(scanner.findWithinHorizon(pattern, 0));
				if (scanner.hasNext()) {
					scanner.skip(scanner.delimiter());
				}
				builder.deleteCharAt(0).deleteCharAt(builder.length() - 1);
				return parser.getTokenInstance(classType, builder.toString(), LogType.NCSA);
			} catch (InputMismatchException e) {
				throw new ParseException("Bad cookie!!!", parser.getPos());
			}
		}

		@Override
		public Map<Class, ObjectPool.ObjectCreator> getCreators() {
			return ObjectCreators.Cookie.getCreator();
		}
	},
	CookieW3C(new String[]{"cs(Cookie)"}, Cookie.class, null) {
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
		public LogField parse(Scanner scanner, WebLogParser parser) throws ParseException {
			return parser.getTokenInstance(classType, scanner.next(), LogType.W3C);
		}

		@Override
		public Map<Class, ObjectPool.ObjectCreator> getCreators() {
			return ObjectCreators.Cookie.getCreator();
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
	Method(new String[]{"%m", "cs-method"}, org.sessionization.parser.fields.Method.class, null) {
		@Override
		public Class[] getDependencies() {
			return new Class[]{
					MethodConverter.class
			};
		}

		@Override
		public LogField parse(Scanner scanner, WebLogParser parser) throws ParseException {
			return org.sessionization.parser.fields.Method.setMethod(scanner.next());
		}
	},
	/**
	 * W3C:
	 * Date at which transaction completed, field has type "date"
	 *
	 * Format string: <code>date</code>
	 */
	Date(new String[]{"date"}, org.sessionization.parser.fields.w3c.Date.class, null) {
		@Override
		public LogField parse(Scanner scanner, WebLogParser parser) throws ParseException {
			return parser.getTokenInstance(classType, scanner.next(), ((WebLogParserW3C) parser).getDateFormat());
		}
	},
	/**
	 * W3C:
	 * Time at which transaction completed, field has type "time"
	 *
	 * Format string: <code>time</code>
	 */
	Time(new String[]{"time"}, org.sessionization.parser.fields.w3c.Time.class, null) {
		@Override
		public LogField parse(Scanner scanner, WebLogParser parser) throws ParseException {
			return parser.getTokenInstance(classType, scanner.next(), ((WebLogParserW3C) parser).getTimeFormat());
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
	ServerPort(new String[]{"%p", "%{local}p", "%{canonical}p", "s-port"}, Port.class, "[0-9]+") {
		@Override
		public LogField parse(Scanner scanner, WebLogParser parser) throws ParseException {
			try {
				return parser.getTokenInstance(classType, scanner.next(pattern), true);
			} catch (InputMismatchException e) {
				throw new ParseException("Bad port!!!", parser.getPos());
			}
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
	ClientPort(new String[]{"%{remote}p", "c-port"}, Port.class, "[0-9]+") {
		@Override
		public LogField parse(Scanner scanner, WebLogParser parser) throws ParseException {
			try {
				return parser.getTokenInstance(classType, scanner.next(pattern), false);
			} catch (InputMismatchException e) {
				throw new ParseException("Bad port!!!", parser.getPos());
			}
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
	ServerIP(new String[]{"%A", "s-ip"}, Address.class, null) {
		@Override
		public Class[] getDependencies() {
			return new Class[]{
					InetAddressConverter.class
			};
		}

		@Override
		public LogField parse(Scanner scanner, WebLogParser parser) throws ParseException {
			try {
				InetAddress address = InetAddress.getByName(scanner.next());
				return parser.getTokenInstance(classType, address, true);
			} catch (UnknownHostException e) {
				throw new ParseException("Bad server IP!!!", parser.getPos());
			}
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
	ClientIP(new String[]{"%a", "c-ip"}, Address.class, null) {
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
		public LogField parse(Scanner scanner, WebLogParser parser) throws ParseException {
			try {
				InetAddress address = InetAddress.getByName(scanner.next());
				return parser.getTokenInstance(classType, address, false);
			} catch (UnknownHostException e) {
				throw new ParseException("Bad client IP!!!", parser.getPos());
			}
		}
	},
	/**
	 * NCSA:
	 * The time taken to serve the request, in microseconds.
	 *
	 * Format string: <code>%D</code>, <code>%{us}T</code>
	 */
	TimeTaken(new String[]{"%D", "%{us}T"}, org.sessionization.parser.fields.TimeTaken.class, "[0-9]+"),
	/**
	 * NCSA:
	 * The time taken to serve the request in millisecondsl
	 *
	 * Format string: <code>%{ms}T</code>
	 *
	 * W3C:
	 * The time taken to serve the request, in miliseconds.
	 *
	 * Format string: <code>time-taken</code>
	 */
	TimeTakenM(new String[]{"%{ms}T", "time-taken"}, org.sessionization.parser.fields.TimeTaken.class, "[0-9]+") {
		@Override
		public LogField parse(Scanner scanner, WebLogParser parser) throws ParseException {
			try {
				long val = TimeUnit.Milliseconds.getMicroSeconds(Integer.valueOf(scanner.next(this.pattern)));
				return parser.getTokenInstance(classType, val);
			} catch (InputMismatchException e) {
				throw new ParseException("Bad time taken!!!", parser.getPos());
			}
		}
	},
	/**
	 * NCSA:
	 * The time taken to serve the request in seconds.
	 *
	 * Format string; <code>%{s}T</code>, <code>%T</code>
	 */
	TiemTakenS(new String[]{"%{s}T", "%T"}, org.sessionization.parser.fields.TimeTaken.class, "[0-9]+") {
		@Override
		public LogField parse(Scanner scanner, WebLogParser parser) throws ParseException {
			try {
				long val = TimeUnit.Seconds.getMicroSeconds(Integer.valueOf(scanner.next(this.pattern)));
				return parser.getTokenInstance(classType, val);
			} catch (InputMismatchException e) {
				throw new ParseException("Bad time taken!!!", parser.getPos());
			}
		}
	},
	/**
	 * W3C:
	 *
	 * Format string: <code>sc-substatus</code>
	 */
	SubStatus(new String[]{"sc-substatus"}, org.sessionization.parser.fields.w3c.SubStatus.class, "[0-9]+"),
	/**
	 * W3C:
	 *
	 * Format string: <code>sc-win32-status</code>
	 */
	Win32Status(new String[]{"sc-win32-status"}, org.sessionization.parser.fields.w3c.Win32Status.class, "[0-9]+"),
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
	Host(new String[]{"%v", "cs-host"}, org.sessionization.parser.fields.Host.class, null),
	/**
	 * W3C:
	 *
	 * Format string: <code>cs-version</code>
	 */
	ProtocolVersion(new String[]{"cs-version"}, Protocol.class, null),
	/**
	 * W3C:
	 *
	 * Format string: <code>s-sitename</code>
	 */
	SiteName(new String[]{"s-sitename"}, org.sessionization.parser.fields.w3c.SiteName.class, null),
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
	ComputerName(new String[]{"%V", "s-computername"}, org.sessionization.parser.fields.w3c.ComputerName.class, null),
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
	UriQuery(new String[]{"%q", "cs-uri-query"}, org.sessionization.parser.fields.UriQuery.class, null) {
		@Override
		public Class[] getDependencies() {
			return new Class[]{
					UriQueryPair.class,
					UriQueryKey.class
			};
		}

		@Override
		public Map<Class, ObjectPool.ObjectCreator> getCreators() {
			return ObjectCreators.UriQuesy.getCreator();
		}
	},
	/**
	 * NCSA:
	 * Filename.
	 *
	 * Format string: <code>%f</code>, <code>%U</code>
	 *
	 * W3C:
	 *
	 * Format string: <code>cs-uri-stem</code>
	 */
	UriSteam(new String[]{"%f", "%U", "cs-uri-stem"}, UriSteam.class, null),
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
	SizeOfTransfer(new String[]{"%S"}, org.sessionization.parser.fields.ncsa.SizeOfTransfer.class, "[0-9]+"),
	/**
	 * NCSA:
	 * Number of keepalive requests handled on this connection. Interesting if KeepAlive is
	 * being used, so that, for example, a '1' means the first keepalive request after the
	 * initial one, '2' the second, etc...; otherwise this is always 0 (indicating the
	 * initial request). Available in versions 2.2.11 and later.
	 *
	 * Format string: <code>%k</code>
	 */
	KeepAliveNumber(new String[]{"%k"}, org.sessionization.parser.fields.ncsa.KeepAliveNumber.class, "[0-9]+"),
	/**
	 * NCSA:
	 * Connection status when response is completed:
	 * X = connection aborted before the response completed.
	 * + =	connection may be kept alive after the response is sent.
	 * - =	connection will be closed after the response is sent.
	 *
	 * Format string: <code>%X</code>
	 */
	ConnectionStatus(new String[]{"%X"}, org.sessionization.parser.fields.ncsa.ConnectionStatus.class, "(X|\\+|\\-)") {
		@Override
		public Class[] getDependencies() {
			return new Class[]{
					ConnectionStatusConverter.class
			};
		}

		@Override
		public LogField parse(Scanner scanner, WebLogParser parser) throws ParseException {
			try {
				return org.sessionization.parser.fields.ncsa.ConnectionStatus.getConnectionStatus(scanner.next(pattern));
			} catch (InputMismatchException e) {
				throw new ParseException("Bad connection status!!!", parser.getPos());
			}
		}
	},
	/**
	 * NCSA:
	 * The process ID of the child that serviced the request.
	 *
	 * Format string: <code>%P</code>
	 */
	ProcessID(new String[]{"%P"}, org.sessionization.parser.fields.ncsa.ProcessID.class, "[0-9]+"),
	/**
	 * W3C:
	 * The directives Version and Fields are required and should precede all entries in the log.
	 * The Fields directive specifies the data recorded in the fields of each entry.
	 *
	 * Format string: <code>#Version</code>, <code>#Fields</code>, <code>#Software</code>, <code>#Start-Date</code>, <code>#End-Date</code>, <code>#Date</code>, <code>#Remark</code>
	 */
	MetaData(new String[]{"#Version:", "#Fields:", "#Software:", "#Start-Date:", "#End-Date:", "#Date:", "#Remark:"}, null, "(#)(.+?)") {
		@Override
		public LogField parse(Scanner scanner, WebLogParser parser) throws ParseException {
			try {
				String data = scanner.next(pattern);
				List<String> list = new LinkedList<>();
				while (scanner.hasNext()) {
					list.add(scanner.next());
				}
				return new MetaData(data, list);
			} catch (InputMismatchException e) {
				throw new ParseException("Bad meta data!!!", parser.getPos());
			}
		}
	},
	Unknown(new String[]{""}, null, null);

	final Class classType;
	private final String[] format;
	Pattern pattern;

	LogFieldType(String[] format, Class classType, String pattern) {
		this.format = format;
		this.classType = classType;
		if (pattern != null) {
			this.pattern = Pattern.compile(pattern);
		} else {
			this.pattern = null;
		}
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
		String s = classType.getSimpleName();
		return Character.toLowerCase(s.charAt(0)) + s.substring(1);
	}

	/**
	 * @return
	 */
	public String getSetterName() {
		return "set" + classType.getSimpleName();
	}

	/**
	 * @return
	 */
	public String getGetterName() {
		return "get" + classType.getSimpleName();
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
	public LogField parse(final Scanner scanner, final WebLogParser parser) throws ParseException {
		try {
			if (pattern != null) {
				return parser.getTokenInstance(classType, scanner.next(pattern));
			} else {
				return parser.getTokenInstance(classType, scanner.next());
			}
		} catch (InputMismatchException e) {
			System.out.println(getClassE().getName());
			throw new ParseException(e.getMessage(), parser.getPos());
		}
	}

	/**
	 * @return
	 */
	public String[] getFormatString() {
		return format;
	}

	/**
	 * @return
	 */
	public Pattern getPattern() {
		return pattern;
	}

	public Map<Class, ObjectPool.ObjectCreator> getCreators() {
		return null;
	}
}

enum TimeUnit {
	Seconds {
		@Override
		public long getMicroSeconds(int value) {
			return value * 1000000;
		}
	},
	Milliseconds {
		@Override
		public long getMicroSeconds(int value) {
			return value * 1000;
		}
	};

	public abstract long getMicroSeconds(int value);
}

enum ObjectCreators {
	UriQuesy {
		@Override
		public Map<Class, ObjectPool.ObjectCreator> getCreator() {
			Map creators = new HashMap<>(3);
			ObjectPool.ObjectCreator creator;

			creator = (pool, args) -> {
				if (args.length < 1) {
					return null;
				}
				UriQuery query = new UriQuery();
				if (!((String) args[0]).equals("-")) {
					UriQueryPair pair;
					List list = new ArrayList<>();
					String[] tab = ((String) args[0]).split("&");
					for (String s : tab) {
						tab = s.split("=");
						if (tab.length == 2) {
							pair = pool.getObject(UriQueryPair.class, tab[0], tab[1]);
						} else {
							pair = pool.getObject(UriQueryPair.class, tab[0], "-");
						}
						list.add(pair);
					}
					query.setPairs(list);
				}
				return query;
			};
			creators.put(UriQuery.class, creator);

			creator = (pool, args) -> {
				UriQueryPair pair = new UriQueryPair();
				UriQueryKey key = pool.getObject(UriQueryKey.class, args[0]);
				pair.setKey(key);
				pair.setValue((String) args[1]);
				return pair;
			};
			creators.put(UriQueryPair.class, creator);

			return creators;
		}
	},
	Cookie {
		@Override
		public Map<Class, ObjectPool.ObjectCreator> getCreator() {
			Map creators = new HashMap<>();
			ObjectPool.ObjectCreator creator;

			creator = (pool, args) -> {
				if (args.length < 2) {
					return null;
				}
				LogType parser = (LogType) args[1];
				String line = (String) args[0];
				Cookie cookie = new Cookie();
				if (!line.equals("-")) {
					List list = new ArrayList<>();
					CookiePair pair;
					for (String s : parser.parseCooki(line).split(" ")) {
						int mid = s.indexOf("=");
						String sKey, value;
						if (mid == s.length() - 1) {
							value = "-";
							sKey = s.substring(0, mid);
						} else {
							value = s.substring(mid + 1);
							sKey = s.substring(0, mid);
						}
						pair = pool.getObject(CookiePair.class, sKey, value);
						list.add(pair);
					}
					cookie.setPairs(list);
				}
				return cookie;
			};
			creators.put(org.sessionization.parser.fields.Cookie.class, creator);

			creator = (pool, args) -> {
				CookiePair pair = new CookiePair();
				CookieKey key = pool.getObject(CookieKey.class, args[0]);
				pair.setKey(key);
				pair.setValue((String) args[1]);
				return pair;
			};
			creators.put(CookiePair.class, creator);

			return creators;
		}
	},
	UriSteamQuery {
		@Override
		public Map<Class, ObjectPool.ObjectCreator> getCreator() {
			Map creators = new HashMap<>();
			creators.putAll(UriQuesy.getCreator());
			ObjectPool.ObjectCreator creator;

			creator = (pool, args) -> {
				URI uri = null;
				try {
					uri = new URI(args[0].toString());
				} catch (URISyntaxException e) {
					e.printStackTrace();
				}
				org.sessionization.parser.fields.UriSteamQuery steamQuery = new UriSteamQuery();
				String query = uri.getQuery();
				UriQuery uriQuery = pool.getObject(UriQuery.class, query != null ? query : "-");
				UriSteam uriSteam = pool.getObject(UriSteam.class, uri.getRawPath());
				steamQuery.setUriSteam(uriSteam);
				steamQuery.setQuery(uriQuery);
				return steamQuery;
			};
			creators.put(org.sessionization.parser.fields.UriSteamQuery.class, creator);

			return creators;
		}
	},
	RequestLine {
		@Override
		public Map<Class, ObjectPool.ObjectCreator> getCreator() {
			Map creators = new HashMap<>();
			creators.putAll(UriSteamQuery.getCreator());
			ObjectPool.ObjectCreator creator;

			creator = (pool, args) -> {
				if (args.length < 1) {
					return null;
				}
				String[] tab = ((String) args[0]).split(" ");
				if (tab.length < 3) {
					return null;
				}
				org.sessionization.parser.fields.ncsa.RequestLine requestLine = new RequestLine();
				requestLine.setMethod(Method.setMethod(tab[0]));
				org.sessionization.parser.fields.UriSteamQuery steamQuery = pool.getObject(org.sessionization.parser.fields.UriSteamQuery.class, tab[1]);
				requestLine.setSteamQuery(steamQuery);
				Protocol protocol = pool.getObject(Protocol.class, tab[2]);
				requestLine.setProtocol(protocol);
				return requestLine;
			};
			creators.put(org.sessionization.parser.fields.ncsa.RequestLine.class, creator);

			return creators;
		}
	},
	Referer {
		@Override
		public Map<Class, ObjectPool.ObjectCreator> getCreator() {
			Map creators = new HashMap<>();
			creators.putAll(UriSteamQuery.getCreator());
			ObjectPool.ObjectCreator creator;

			creator = (pool, args) -> {
				if (args.length < 1) {
					return null;
				}
				URI uri = (URI) args[0];
				org.sessionization.parser.fields.Referer referer = new Referer();
				org.sessionization.parser.fields.UriSteamQuery query = pool.getObject(org.sessionization.parser.fields.UriSteamQuery.class, uri);
				String hostName = uri.getAuthority();
				Host host = pool.getObject(Host.class, hostName != null ? hostName : "-");
				referer.setSteamQuery(query);
				referer.setHost(host);
				return referer;
			};
			creators.put(org.sessionization.parser.fields.Referer.class, creator);

			return creators;
		}
	};

	/**
	 * @return
	 */
	public abstract Map<Class, ObjectPool.ObjectCreator> getCreator();
}
