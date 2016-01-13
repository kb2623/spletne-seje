package org.sessionization.parser.fields;

import org.sessionization.HibernateTable;
import org.sessionization.parser.LogField;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Cacheable
public class UriQuery implements LogField, HibernateTable {

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
		if (niz != null && !niz.equals("-")) {
			setupMap(niz);
		} else {
			pairs = null;
		}
	}

	private void setupMap(String niz) {
		String[] tab = niz.split("&");
		UriQueryPair tmp;
		pairs = new ArrayList<>(tab.length);
		for (String s : tab) {
			tab = s.split("=");
			if (tab.length == 2) {
				tmp = new UriQueryPair(tab[0], tab[1]);
			} else {
				tmp = new UriQueryPair(tab[0], "-");
			}
			pairs.add(tmp);
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
	public String getIdQuery() {
		// TODO: 1/8/16
		return null;
	}
}

