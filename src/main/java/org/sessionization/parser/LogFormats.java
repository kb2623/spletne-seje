package org.sessionization.parser;

import java.util.ArrayList;
import java.util.List;

public enum LogFormats {

	CommonLogFormat {
		@Override
		public List<LogFieldType> create(String[] args) {
			List<LogFieldType> list = new ArrayList<>();
			list.add(LogFieldType.RemoteHost);
			list.add(LogFieldType.RemoteLogname);
			list.add(LogFieldType.RemoteUser);
			list.add(LogFieldType.DateTime);
			list.add(LogFieldType.RequestLine);
			list.add(LogFieldType.StatusCode);
			list.add(LogFieldType.SizeOfResponse);
			return list;
		}
	},
	CombinedLogFormat {
		@Override
		public List<LogFieldType> create(String[] args) {
			List<LogFieldType> list = LogFormats.CommonLogFormat.create(null);
			list.add(LogFieldType.Referer);
			list.add(LogFieldType.UserAgent);
			return list;
		}
	},
	CustomLogFormat {
		@Override
		public List<LogFieldType> create(String[] args) {
			List<LogFieldType> list = new ArrayList<>();
			for (String symbol : args) {
				// Fixme: Dolocena polja nimajo tipa, kar privede do napake pri obdelavi datoeke
				switch (symbol) {
					case "%%":
						list.add(LogFieldType.Unknown);
						break;
					case "%a":
						list.add(LogFieldType.ClientIP);
						break;
					case "%A":
						list.add(LogFieldType.ServerIP);
						break;
					case "%B":
						list.add(LogFieldType.SizeOfResponse);
						break;
					case "%b":
						list.add(LogFieldType.SizeOfResponse);
						break;
					case "%C":
						list.add(LogFieldType.Cookie);
						break;
					case "%D":
						list.add(LogFieldType.TimeTaken);
						break;
					case "%e":
						list.add(LogFieldType.Unknown);
						break;
					case "%f":
						list.add(LogFieldType.UriSteam);
						break;
					case "%h":
						list.add(LogFieldType.RemoteHost);
						break;
					case "%H":
						list.add(LogFieldType.ProtocolVersion);
						break;
					case "%k":
						list.add(LogFieldType.KeepAliveNumber);
						break;
					case "%l":
						list.add(LogFieldType.RemoteLogname);
						break;
					case "%m":
						list.add(LogFieldType.Method);
						break;
					case "%P":
						list.add(LogFieldType.ProcessID);
						break;
					case "%q":
						list.add(LogFieldType.UriQuery);
						break;
					case "%r":
						list.add(LogFieldType.RequestLine);
						break;
					case "%s":
					case "%>s":
						list.add(LogFieldType.StatusCode);
						break;
					case "%t":
						list.add(LogFieldType.DateTime);
						break;
					case "%T":
						list.add(LogFieldType.TimeTaken);
						break;
					case "%u":
						list.add(LogFieldType.RemoteUser);
						break;
					case "%U":
						list.add(LogFieldType.UriSteam);
						break;
					case "%v":
						list.add(LogFieldType.ComputerName);
						break;
					case "%V":
						list.add(LogFieldType.ComputerName);
						break;
					case "%X":
						list.add(LogFieldType.Unknown);
						break;
					case "%I":
						list.add(LogFieldType.SizeOfRequest);
						break;
					case "%O":
						list.add(LogFieldType.SizeOfResponse);
						break;
					case "%S":
//						list.add(LogFieldType.SizeOfTransfer);
						break;
					case "%^ti":
						list.add(LogFieldType.Unknown);
						break;
					case "%^to":
						list.add(LogFieldType.Unknown);
						break;
					case "%p":
					case "%{local}p":
					case "%{canonical}p":
						list.add(LogFieldType.ServerPort);
						break;
					case "%{remote}p":
						list.add(LogFieldType.ClientPort);
						break;
					case "%{Referer}i":
						list.add(LogFieldType.Referer);
						break;
					case "%{User-agent}i":
						list.add(LogFieldType.UserAgent);
						break;
					case "CUSTOM":
						break;
					default:
						list.add(LogFieldType.Unknown);
				}
			}
			return list;
		}
	},
	ExtendedLogFormat {
		@Override
		public List<LogFieldType> create(String[] args) {
			List<LogFieldType> list = new ArrayList<>();
			for (String fieldName : args) {
				switch (fieldName) {
					case "cs(Referer)":
						list.add(LogFieldType.Referer);
						break;
					case "cs(Cookie)":
						list.add(LogFieldType.Cookie);
						break;
					case "cs(User-Agent)":
						list.add(LogFieldType.UserAgent);
						break;
					case "cs-method":
						list.add(LogFieldType.Method);
						break;
					case "date":
						list.add(LogFieldType.Date);
						break;
					case "time":
						list.add(LogFieldType.Time);
						break;
					case "s-sitename":
						list.add(LogFieldType.SiteName);
						break;
					case "s-computername":
						list.add(LogFieldType.ComputerName);
						break;
					case "s-ip":
						list.add(LogFieldType.ServerIP);
						break;
					case "c-ip":
						list.add(LogFieldType.ClientIP);
						break;
					case "cs-uri-stem":
						list.add(LogFieldType.UriSteam);
						break;
					case "cs-uri-query":
						list.add(LogFieldType.UriQuery);
						break;
					case "s-port":
						list.add(LogFieldType.ServerPort);
						break;
					case "c-port":
						list.add(LogFieldType.ClientPort);
						break;
					case "cs-username":
						list.add(LogFieldType.RemoteUser);
						break;
					case "cs-version":
						list.add(LogFieldType.ProtocolVersion);
						break;
					case "cs-host":
						list.add(LogFieldType.Host);
						break;
					case "sc-status":
						list.add(LogFieldType.StatusCode);
						break;
					case "sc-substatus":
						list.add(LogFieldType.SubStatus);
						break;
					case "sc-win32-status":
						list.add(LogFieldType.Win32Status);
						break;
					case "sc-bytes":
						list.add(LogFieldType.SizeOfResponse);
						break;
					case "cs-bytes":
						list.add(LogFieldType.SizeOfRequest);
						break;
					case "time-taken":
						list.add(LogFieldType.TimeTaken);
						break;
					case "#Fields:":
						break;
					case "IIS":
						break;
					default:
						list.add(LogFieldType.Unknown);
						break;
				}
			}
			return list;
		}
	},
	IISLogFormat {
		@Override
		public List<LogFieldType> create(String[] args) {
			return ExtendedLogFormat.create(args);
		}
	};

	public abstract List<LogFieldType> create(String[] args);

}
