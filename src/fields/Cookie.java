package fields;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

public class Cookie implements Field {

	private HashMap<String, String> map;

	public Cookie(String line, Cookie.Type type) throws IllegalArgumentException {
		if(!line.equals("-")) {
			map = new HashMap<>();
			switch (type) {
			case NCSA:
				line = line.replace(';', ' ');
				break;
			case W3C:
				line = line.replace(';', ' ');
				line = line.replaceAll("\\+", "");
				break;
			}
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
		if(map == null) {
			builder.append('-');
		} else {
			for(Entry<String, String> entry : map.entrySet()) {
				builder.append(entry.getKey()).append(" = ").append(entry.getValue()).append('\n');
			}
		}
		return builder.toString();
	}

	@Override
	public String getKey() {
		StringBuilder builder = new StringBuilder();
		if(map == null) {
			builder.append('-');
		} else {
			map.entrySet().stream().forEach((entry) -> builder.append(entry.getKey()).append(entry.getValue()));
		}
		return builder.toString();
	}

	@Override
	public boolean equals(Object obj) {
		if(!(obj instanceof Cookie)) {
			return false;
		} else {
			Iterator<Entry<String, String>> itThis = map.entrySet().iterator(), itObj = ((Cookie) obj).getMap().entrySet().iterator();
			while(itThis.hasNext() && itObj.hasNext()) {
				Entry<String, String> entityObj = itObj.next(),	entityThis = itThis.next();
				if(!entityObj.getKey().equals(entityThis.getKey()) || !entityObj.getValue().equals(entityThis.getValue())) {
					return false;
				}
			}
			return !itObj.hasNext() && !itThis.hasNext();
		}
	}

	public static String patteren() {
		return "([^ \\\"\\[\\{\\(\\]\\}\\)<>/\\\\?=@,;:]+=[\\x21\\x23-\\x2B\\x2D-\\x3A\\x3C-\\x5B\\x5D-\\x7E]*;)*([^ \\\"\\[\\{\\(\\]\\}\\)<>/\\\\?=@,;:]+=[\\x21\\x23-\\x2B\\x2D-\\x3A\\x3C-\\x5B\\x5D-\\x7E]*){1}";
	}

	public enum Type {
		NCSA,
		W3C
	}

}
