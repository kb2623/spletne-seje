package org.sessionization.parser;

import org.sessionization.fields.FieldType;

import java.util.ArrayList;
import java.util.List;

public enum LogFormats {

	CommonLogFormat {
		@Override
		public List<FieldType> create(List<String> args) {
			List<FieldType> list = new ArrayList<>();
			list.add(FieldType.RemoteHost);
			list.add(FieldType.RemoteLogname);
			list.add(FieldType.RemoteUser);
			list.add(FieldType.DateTime);
			list.add(FieldType.RequestLine);
			list.add(FieldType.StatusCode);
			list.add(FieldType.SizeOfResponse);
			if (args != null && args.contains("%C")) list.add(FieldType.Cookie);
			return list;
		}
	},
	CombinedLogFormat {
		@Override
		public List<FieldType> create(List<String> args) {
			List<FieldType> list = LogFormats.CommonLogFormat.create(null);
			list.add(FieldType.Referer);
			list.add(FieldType.UserAgent);
			if (args != null && args.contains("%C")) list.add(FieldType.Cookie);
			return list;
		}
	},
	CustomLogFormat {
		@Override
		public List<FieldType> create(List<String> args) {
			List<FieldType> list = new ArrayList<>();
			args.forEach(symbol -> {
				switch (symbol) {
				case "%%": list.add(FieldType.Unknown);	                break;
				case "%a": list.add(FieldType.ClientIP); 		        break;
				case "%A": list.add(FieldType.ServerIP);                break;
				case "%B": list.add(FieldType.SizeOfResponse);          break;
				case "%b": list.add(FieldType.SizeOfResponse);          break;
				case "%C": list.add(FieldType.Cookie);			        break;
				case "%D": list.add(FieldType.TimeTaken);               break;
				case "%e": list.add(FieldType.Unknown);                 break;
				case "%f": list.add(FieldType.UriSteam);	                break;
				case "%h": list.add(FieldType.RemoteHost);		        break;
				case "%H": list.add(FieldType.ProtocolVersion);	        break;
				case "%k": list.add(FieldType.KeepAliveNumber);         break;
				case "%l": list.add(FieldType.RemoteLogname);	        break;
				case "%m": list.add(FieldType.Method);			        break;
				case "%P": list.add(FieldType.ProcessID);                break;
				case "%q": list.add(FieldType.UriQuery);		        break;
				case "%r": list.add(FieldType.RequestLine);		        break;
				case "%s": list.add(FieldType.StatusCode);		        break;
				case "%t": list.add(FieldType.DateTime);		        break;
				case "%T": list.add(FieldType.TimeTaken);		        break;
				case "%u": list.add(FieldType.RemoteUser);		        break;
				case "%U": list.add(FieldType.UriSteam);			        break;
				case "%v": list.add(FieldType.ComputerName);            break;
				case "%V": list.add(FieldType.ComputerName);            break;
				case "%X": list.add(null);				                break;
				case "%I": list.add(FieldType.SizeOfRequest);	        break;
				case "%O": list.add(FieldType.SizeOfResponse);          break;
				case "%S": list.add(FieldType.SizeOfTransfer);          break;
				case "%^ti": list.add(null);			                break;
				case "%^to": list.add(null);			                break;
				case "%p":
				case "%{local}p":
				case "%{canonical}p": list.add(FieldType.ServerPort);   break;
				case "%{remote}p": list.add(FieldType.ClientPort);      break;
				default: list.add(FieldType.Unknown);
				}
			});
			return list;
		}
	},
	ExtendedLogFormat {
		@Override
		public List<FieldType> create(List<String> args) {
			List<FieldType> list = new ArrayList<>();
			args.forEach(fieldName -> {
				switch (fieldName) {
				case "cs(Referer)": 	list.add(FieldType.Referer);		break;
				case "cs(Cookie)":		list.add(FieldType.Cookie); 		break;
				case "cs(User-Agent)": 	list.add(FieldType.UserAgent);		break;
				case "cs-method":		list.add(FieldType.Method);			break;
				case "date":			list.add(FieldType.Date);			break;
				case "time":			list.add(FieldType.Time);			break;
				case "s-sitename":		list.add(FieldType.SiteName);		break;
				case "s-computername":	list.add(FieldType.ComputerName);	break;
				case "s-ip":			list.add(FieldType.ServerIP);		break;
				case "c-ip":			list.add(FieldType.ClientIP);		break;
				case "cs-uri-stem":		list.add(FieldType.UriSteam);		break;
				case "cs-uri-query":	list.add(FieldType.UriQuery);		break;
				case "s-port":			list.add(FieldType.ServerPort);		break;
				case "c-port":			list.add(FieldType.ClientPort);		break;
				case "cs-username":		list.add(FieldType.RemoteUser);		break;
				case "cs-version":		list.add(FieldType.ProtocolVersion);break;
				case "cs-host":			list.add(FieldType.Host);			break;
				case "sc-status":		list.add(FieldType.StatusCode);		break;
				case "sc-substatus":	list.add(FieldType.SubStatus);		break;
				case "sc-win32-status":	list.add(FieldType.Win32Status);	break;
				case "sc-bytes":		list.add(FieldType.SizeOfResponse);	break;
				case "cs-bytes":		list.add(FieldType.SizeOfRequest);	break;
				case "time-taken":		list.add(FieldType.TimeTaken);		break;
				case "#Fields:":                                            break;
				default:				list.add(FieldType.Unknown);		break;
				}
			});
			return list;
		}
	};

	public abstract List<FieldType> create(List<String> args);

}
