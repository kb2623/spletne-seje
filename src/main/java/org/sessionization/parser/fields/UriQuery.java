package org.sessionization.parser.fields;

import org.hibernate.Session;
import org.sessionization.database.HibernateTable;
import org.sessionization.parser.LogField;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Cacheable
public class UriQuery implements LogField, HibernateTable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@ManyToMany(cascade = CascadeType.ALL)
	private Set<UriQueryPair> pairs;

	public UriQuery() {
		id = null;
		pairs = null;
	}

	public UriQuery(String niz) {
		id = null;
		if (niz != null && !niz.equals("-")) {
			setupMap(niz);
		} else {
			pairs = new HashSet<>();
			pairs.add(new UriQueryPair(" ", " "));
		}
	}

	private void setupMap(String niz) {
		String[] tab = niz.split("&");
		UriQueryPair tmp;
		pairs = new HashSet<>(tab.length);
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

	public Set<UriQueryPair> getPairs() {
		return pairs;
	}

	public void setPairs(Set<UriQueryPair> pairs) {
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
	public Object setDbId(Session session) {
		List<Integer> ids = new ArrayList<>(pairs.size());
		for (UriQueryPair p : pairs) {
			Integer tmp = (Integer) p.setDbId(session);
			if (tmp != null) {
				ids.add(tmp);
			}
		}
		if (getId() != null) {
			return getId();
		}
		if (ids.size() == pairs.size()) {
			org.hibernate.Query query = session.createQuery("select distinct q.id from " + getClass().getSimpleName() + " as q join q.pairs as p where p.id in (:list)");
			query.setParameterList("list", ids);
			ids = query.list();
			for (Integer i : ids) {
				if (equals(session.load(getClass(), i))) {
					setId(i);
					return i;
				}
			}
		}
		return null;
	}

	@Override
	public boolean equals(Object o) {
		if (o == null || !(o instanceof UriQuery)) return false;
		if (this == o) return true;
		UriQuery taht = (UriQuery) o;
		if (getPairs() == null ? taht.getPairs() == null : false) {
			return true;
		} else if (getPairs().size() == taht.getPairs().size()) {
			return getPairs().containsAll(taht.getPairs());
		} else {
			return false;
		}
	}

	@Override
	public int hashCode() {
		int result = getId() != null ? getId().hashCode() : 0;
		result = 31 * result + (getPairs() != null ? getPairs().hashCode() : 0);
		return result;
	}

	@Override
	public String toString() {
		return "UriQuery{" +
				"id=" + id +
				", pairs=" + pairs +
				'}';
	}
}

