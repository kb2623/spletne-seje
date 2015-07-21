package org.sessionization.fields;

import org.oosqljet.annotation.Column;
import org.oosqljet.annotation.Table;

import java.util.HashMap;
import java.util.Map;

@Table public class UriQuery implements Field {

	@Column private HashMap<String, String> map;

	public UriQuery(String niz) {
		if (niz != null && niz.charAt(0) != '-') {
			if (niz.charAt(0) == '?') map = setupMap(niz.substring(1));
			else map = setupMap(niz);
		} else {
			map = null;
		}
	}

	private HashMap<String, String> setupMap(String niz) {
		HashMap<String, String> tMap = new HashMap<>();
		for (String s : niz.split("&")) {
			String[] tab = s.split("=");
			if (tab.length == 2) tMap.put(tab[0], tab[1]);
			else tMap.put(tab[0], "-");
		}
		return tMap;
	}

	public Map<String, String> getMap() {
		return map;
	}

	@Override
	public String izpis() {
		StringBuilder builder = new StringBuilder();
		builder.append('[');
		if (map == null) builder.append('-');
		else
			map.entrySet().forEach(e -> builder.append('[').append(e.getKey()).append(" = ").append(e.getValue()).append(']'));
		return builder.append(']').toString();
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append('[');
		if (map != null)
			map.entrySet().forEach(e -> builder.append('[').append(e.getKey()).append(" = ").append(e.getValue()).append(']'));
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

	@Override
	public String getKey() {
		return "";
	}
}
