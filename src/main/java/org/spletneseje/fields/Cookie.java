package org.spletneseje.fields;

import org.spletneseje.database.annotation.Table;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

@Table(noRowId = true) public class Cookie implements Field {

	@org.spletneseje.database.annotation.Entry
	private HashMap<String, String> map;

	public Cookie(String line, LogType type) throws IllegalArgumentException {
		if(!line.equals("-")) {
			map = new HashMap<>();
			line = type.parseCooki(line);
			for(String s : line.split(" ")) {
				int indexOf = s.indexOf('=');
				if(indexOf == s.length()-1) {
					map.put(s.substring(0, indexOf), "-");
				} else {
					map.put(s.substring(0, indexOf), s.substring(indexOf+1));
				}
			}
		} else {
			map = null;
		}
	}

	public HashMap<String, String> getMap() {
		return map;
	}

	@Override
	public String izpis() {
		StringBuilder builder = new StringBuilder();
		builder.append('[');
		if(map == null) {
			builder.append('-');
		} else {
			for(Entry<String, String> entry : map.entrySet())
				builder.append('[').append(entry.getKey()).append(" = ").append(entry.getValue()).append(']');
		}
		return builder.append(']').toString();
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append('[');
		if(map == null) {
			builder.append('-');
		} else {
			for(Entry<String, String> entry : map.entrySet())
				builder.append('[').append(entry.getKey()).append(" = ").append(entry.getValue()).append(']');
		}
		return builder.append(']').toString();
	}

	@Override
	public String getKey() {
		if (map == null) return "";
		StringBuilder builder = new StringBuilder();
		map.entrySet().stream().forEach((entry) -> builder.append(entry.getKey()).append(entry.getValue()));
		return builder.toString();
	}

	@Override
	public FieldType getFieldType() {
		return FieldType.Cookie;
	}

	@Override
	public boolean equals(Object obj) {
		if(!(obj instanceof Cookie)) {
			return false;
		} else {
			Iterator<Entry<String, String>> itThis = map.entrySet().iterator(), itObj = ((Cookie) obj).getMap().entrySet().iterator();
			while(itThis.hasNext() && itObj.hasNext()) {
				Entry<String, String> entityObj = itObj.next(),	entityThis = itThis.next();
				if(!entityObj.getKey().equals(entityThis.getKey()) || !entityObj.getValue().equals(entityThis.getValue())) return false;
			}
			return !itObj.hasNext() && !itThis.hasNext();
		}
	}

	public static String patteren() {
		return "([^ \\\"\\[\\{\\(\\]\\}\\)<>/\\\\?=@,;:]+=[\\x21\\x23-\\x2B\\x2D-\\x3A\\x3C-\\x5B\\x5D-\\x7E]*;)*([^ \\\"\\[\\{\\(\\]\\}\\)<>/\\\\?=@,;:]+=[\\x21\\x23-\\x2B\\x2D-\\x3A\\x3C-\\x5B\\x5D-\\x7E]*){1}";
	}

}
