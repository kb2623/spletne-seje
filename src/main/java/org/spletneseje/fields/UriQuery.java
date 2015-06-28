package org.spletneseje.fields;

import java.util.HashMap;
import java.util.Map;

public class UriQuery implements Field {
	
	private HashMap<String, String> map;
	
	public UriQuery(String niz) {
		if(niz.charAt(0) != '-') {
			map = new HashMap<>();
			for(String s : niz.split("&")) {
				String[] tab = s.split("=");
				if(tab.length == 2) {
					map.put(tab[0], tab[1]);
				} else {
					map.put(tab[0], "-");
				}
			}
		} else {
			map = null;
		}
	}

	public Map<String, String> getMap() {
		return map;
	}

	@Override
	public String izpis() {		
		StringBuilder builder = new StringBuilder();
		builder.append('[');
		if (map == null) builder.append('-');
		else map.entrySet().forEach(e -> builder.append('[').append(e.getKey()).append(" = ").append(e.getValue()).append(']'));
		return builder.append(']').toString();
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append('[');
		if (map == null) builder.append('-');
		else map.entrySet().forEach(e -> builder.append('[').append(e.getKey()).append(" = ").append(e.getValue()).append(']'));
		return builder.append(']').toString();
	}

	@Override
	public boolean equals(Object o) {
		return o instanceof UriQuery && (map == null) ? ((UriQuery) o).getMap() == null : map.equals(((UriQuery) o).getMap());
	}

	@Override
	public FieldType getFieldType() {
		return FieldType.UriQuery;
	}
}
