package org.sessionization.fields;

import org.sessionization.fields.query.UriQueryPair;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class UriQuery implements Field {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@OneToMany(cascade = CascadeType.ALL)
	private List<UriQueryPair> pairs;

	public UriQuery() {
		id = null;
		pairs = null;
	}

	public UriQuery(String niz) {
		id = null;
		if (niz != null && niz.charAt(0) != '-') {
			if (niz.charAt(0) == '?') setupMap(niz.substring(1));
			else setupMap(niz);
		} else {
			pairs = null;
		}
	}

	private void setupMap(String niz) {
		String[] tab = niz.split("&");
		pairs = new ArrayList<>(tab.length);
		for (String s : tab) {
			String[] pair = s.split("=");
			if (tab.length == 2) pairs.add(new UriQueryPair(pair[0], pair[1]));
			else pairs.add(new UriQueryPair(pair[0], "-"));
		}
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public List<UriQueryPair> getPairs() {
		return pairs;
	}

	public void setPairs(List<UriQueryPair> pairs) {
		this.pairs = pairs;
	}

	@Override
	public String izpis() {
		StringBuilder builder = new StringBuilder();
		builder.append('[');
		if (pairs == null) builder.append('-');
		else pairs.forEach(e -> builder.append('[').append(e.toString()).append(']'));
		return builder.append(']').toString();
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append('[');
		if (pairs != null)
			pairs.forEach(e -> builder.append('[').append(e.toString()).append(']'));
		return builder.append(']').toString();
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		UriQuery uriQuery = (UriQuery) o;
		if (getId() != null ? !getId().equals(uriQuery.getId()) : uriQuery.getId() != null) return false;
		if (getPairs() != null ? !getPairs().equals(uriQuery.getPairs()) : uriQuery.getPairs() != null) return false;
		return true;
	}

	@Override
	public int hashCode() {
		int result = getId() != null ? getId().hashCode() : 0;
		result = 31 * result + (getPairs() != null ? getPairs().hashCode() : 0);
		return result;
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
