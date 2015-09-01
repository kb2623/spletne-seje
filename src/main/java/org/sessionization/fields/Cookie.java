package org.sessionization.fields;

import javax.persistence.*;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

@Entity
@Table(name = "cookie")
public class Cookie implements Field {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "cookie_id")
	private int id;

	@ElementCollection
	@CollectionTable(name = "cookie_key_value", joinColumns = @JoinColumn(name = "cookie_id"))
	@MapKeyJoinColumn(name = "cookie_key_id")
	@Column(name = "value")
	private Map<CookieKey, String> map;

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

	public Map<CookieKey, String> getMap() {
		return map;
	}

	public void setMap(Map<CookieKey, String> map) {
		this.map = map;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getId() {
		return id;
	}

	@Override
	public String izpis() {
		StringBuilder builder = new StringBuilder();
		builder.append('[');
		if(map == null) {
			builder.append('-');
		} else {
			for(Map.Entry<String, String> Column : map.entrySet())
				builder.append('[').append(Column.getKey()).append(" = ").append(Column.getValue()).append(']');
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
			for(Map.Entry<String, String> Column : map.entrySet())
				builder.append('[').append(Column.getKey()).append(" = ").append(Column.getValue()).append(']');
		}
		return builder.append(']').toString();
	}

	@Override
	public String getKey() {
		if (map == null) return "";
		StringBuilder builder = new StringBuilder();
		map.entrySet().stream().forEach((Column) -> builder.append(Column.getKey()).append(Column.getValue()));
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
			Iterator<Map.Entry<String, String>> itThis = map.entrySet().iterator(), itObj = ((Cookie) obj).getMap().entrySet().iterator();
			while(itThis.hasNext() && itObj.hasNext()) {
				Map.Entry<String, String> entityObj = itObj.next(),	entityThis = itThis.next();
				if(!entityObj.getKey().equals(entityThis.getKey()) || !entityObj.getValue().equals(entityThis.getValue())) return false;
			}
			return !itObj.hasNext() && !itThis.hasNext();
		}
	}

	public static String patteren() {
		return "([^ \\\"\\[\\{\\(\\]\\}\\)<>/\\\\?=@,;:]+=[\\x21\\x23-\\x2B\\x2D-\\x3A\\x3C-\\x5B\\x5D-\\x7E]*;)*([^ \\\"\\[\\{\\(\\]\\}\\)<>/\\\\?=@,;:]+=[\\x21\\x23-\\x2B\\x2D-\\x3A\\x3C-\\x5B\\x5D-\\x7E]*){1}";
	}

}
