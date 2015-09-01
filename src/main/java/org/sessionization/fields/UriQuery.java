package org.sessionization.fields;

import javax.persistence.*;
import java.util.HashMap;
import java.util.Map;

@Entity
@Table(name = "url_query")
public class UriQuery implements Field {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Colunm(name = "query_id")
	private int id;

	@ElementCollection
	@CollectionTable(name = "query_key_value", joinColumns = @JoinColumn(name = "uri_query_id"))
	@MapKeyJoinColumn(name = "query_key_id")
	@Column(name = "value")
	private HashMap<UriQueryKey, String> map;

	public UriQuery() {
		id = 0;
		map = null;
	}

	public UriQuery(String niz) {
		if (niz != null && niz.charAt(0) != '-') {
			if (niz.charAt(0) == '?') map = setupMap(niz.substring(1));
			else map = setupMap(niz);
		} else {
			map = null;
		}
	}

	private HashMap<UriQueryKey, UriQueryValue> setupMap(String niz) {
		HashMap<UriQueryKey, UriQueryValue> tMap = new HashMap<>();
		for (String s : niz.split("&")) {
			String[] tab = s.split("=");
			if (tab.length == 2) tMap.put(new UriQueryKey(tab[0]), new UriQueryValue(tab[1]));
			else tMap.put(new UriQueryKey(tab[0]), new UriQueryValue("-"));
		}
		return tMap;
	}

	public Map<UriQueryKey, String> getMap() {
		return map;
	}

	public void setMap(Map<UriQueryKey, String> map) {
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
