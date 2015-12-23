package org.sessionization.parser;

import org.datastruct.RadixTree;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Map;

public enum LogFormats {

	CommonLogFormat {
		@Override
		public List<LogFieldType> make(String... args) {
			return create("%h", "%l", "%u", "%t", "%r", "%s", "%b");
		}
	},
	CombinedLogFormat {
		public List<LogFieldType> make(String... args) {
			return create("%h", "%l", "%u", "%t", "%r", "%s", "%b", "%{Referrer}i", "%{User-agent}i");
		}
	},
	ParseCmdArgs {
		@Override
		public List<LogFieldType> make(String... args) throws NullPointerException {
			if (args == null) {
				throw new NullPointerException();
			} else {
				return create(args);
			}
		}
	};

	private static final Map<String, LogFieldType> enumMaper = initMap();

	private static Map<String, LogFieldType> initMap() {
		Map<String, LogFieldType> map = new RadixTree<>();
		for (LogFieldType type : EnumSet.allOf(LogFieldType.class)) {
			for (String s : type.getFormatString()) {
				map.put(s, type);
			}
		}
		return map;
	}

	List<LogFieldType> create(String... args) {
		List<LogFieldType> list = new ArrayList<>(args.length);
		for (String s : args) {
			LogFieldType type = enumMaper.get(s);
			if (type == null) {
				list.add(LogFieldType.Unknown);
			} else if (type != LogFieldType.MetaData) {
				list.add(type);
			}
		}
		return list;
	}

	public abstract List<LogFieldType> make(String... args) throws NullPointerException;
}
