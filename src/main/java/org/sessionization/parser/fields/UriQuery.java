package org.sessionization.parser.fields;

import org.hibernate.Session;
import org.sessionization.database.HibernateUtil;
import org.sessionization.parser.LogField;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Cacheable
public class UriQuery implements LogField, HibernateUtil.HibernateTable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@ManyToMany(cascade = CascadeType.ALL)
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
	public Object setDbId(Session session) {
		List<Integer> ids = new ArrayList<>(pairs.size());
		for (UriQueryPair p : pairs) {
			Integer tmp = (Integer) p.setDbId(session);
			if (tmp != null) {
				ids.add(tmp);
			}
		}
		Integer id = getId();
		if (id == null && ids.size() == pairs.size()) {
			org.hibernate.Query query = session.createQuery("select distinct q.id from " + getClass().getSimpleName() + " as q join q.pairs as p where p.id in (:list)");
			query.setParameterList("list", ids);
			ids = query.list();
			for (Integer i : ids) {
				UriQuery q = session.load(UriQuery.class, i);
				if (equals(q)) {
					setId(i);
					return i;
				}
			}
		}
		return id;
	}
}

