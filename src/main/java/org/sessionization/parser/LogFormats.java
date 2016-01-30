package org.sessionization.parser;

import org.datastruct.RadixTreeMap;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Map;

public enum LogFormats {

	CommonLogFormat(0) {
		@Override
		public List<LogFieldTypeImp> make(String... args) {
			return create("%h", "%l", "%u", "%t", "%r", "%s", "%b");
		}
	},
	CombinedLogFormat(0) {
		public List<LogFieldTypeImp> make(String... args) {
			return create("%h", "%l", "%u", "%t", "%r", "%s", "%b", "%{Referer}i", "%{User-agent}i");
		}
	},
	ParseCmdArgs(2) {
		@Override
		public List<LogFieldTypeImp> make(String... args) throws NullPointerException {
			if (args == null) {
				throw new NullPointerException();
			} else {
				return create(args);
			}
		}
	};

	private Map<String, LogFieldTypeImp> enumMaper;

	LogFormats(int n) {
		Map<String, LogFieldTypeImp> map = new RadixTreeMap<>();
		for (LogFieldTypeImp type : EnumSet.allOf(LogFieldTypeImp.class)) {
			for (String s : type.getFormatString()) {
				switch (s.isEmpty() ? 3 : n) {
					case 0:
						if (s.charAt(0) == '%') {
							map.put(s, type);
						}
						break;
					case 1:
						if (s.charAt(0) != '%') {
							map.put(s, type);
						}
						break;
					case 2:
						map.put(s, type);
						break;
				}
			}
		}
		enumMaper = map;
	}

	List<LogFieldTypeImp> create(String... args) {
		List<LogFieldTypeImp> list = new ArrayList<>(args.length);
		for (String s : args) {
			LogFieldTypeImp type = enumMaper.get(s);
			if (type == null) {
				list.add(LogFieldTypeImp.Unknown);
			} else if (type != LogFieldTypeImp.MetaData) {
				list.add(type);
			}
		}
		return list;
	}

	public abstract List<LogFieldTypeImp> make(String... args) throws NullPointerException;
}
