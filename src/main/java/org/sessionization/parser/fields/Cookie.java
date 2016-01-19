package org.sessionization.parser.fields;

import org.hibernate.Query;
import org.hibernate.Session;
import org.sessionization.database.HibernateUtil;
import org.sessionization.parser.LogField;
import org.sessionization.parser.LogType;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Cacheable
public class Cookie implements LogField, HibernateUtil.HibernateTable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@ManyToMany(cascade = CascadeType.ALL)
	private List<CookiePair> pairs;

	public Cookie() {
		id = null;
		pairs = null;
	}

	public Cookie(String line, LogType type) throws IllegalArgumentException {
		id = null;
		if (!line.equals("-")) {
			String[] tab = type.parseCooki(line).split(" ");
			pairs = new ArrayList<>(tab.length);
			for (String s : tab) {
				int indexOf = s.indexOf('=');
				CookiePair tmp = null;
				if (indexOf == s.length() - 1) {
					tmp = new CookiePair(s.substring(0, indexOf), "-");
				} else {
					tmp = new CookiePair(s.substring(0, indexOf), s.substring(indexOf + 1));
				}
				pairs.add(tmp);
			}
		} else {
			pairs = null;
		}
	}

	public static String patteren() {
		return "([^ \\\"\\[\\{\\(\\]\\}\\)<>/\\\\?=@,;:]+=[\\x21\\x23-\\x2B\\x2D-\\x3A\\x3C-\\x5B\\x5D-\\x7E]*;)*([^ \\\"\\[\\{\\(\\]\\}\\)<>/\\\\?=@,;:]+=[\\x21\\x23-\\x2B\\x2D-\\x3A\\x3C-\\x5B\\x5D-\\x7E]*){1}";
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public List<CookiePair> getPairs() {
		return pairs;
	}

	public void setPairs(List<CookiePair> pairs) {
		this.pairs = pairs;
	}

	@Override
	public String izpis() {
		StringBuilder builder = new StringBuilder();
		builder.append('[');
		if (pairs == null) {
			builder.append('-');
		} else {
			pairs.forEach(e -> builder.append('[').append(e.toString()).append(']'));
		}
		return builder.append(']').toString();
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append('[');
		if (pairs == null) {
			builder.append('-');
		} else {
			pairs.forEach(e -> builder.append('[').append(e.toString()).append(']'));
		}
		return builder.append(']').toString();
	}

	@Override
	public String getKey() {
		if (pairs == null) return "-";
		else {
			StringBuilder builder = new StringBuilder();
			pairs.forEach(e -> builder.append(e.toString()));
			return builder.toString();
		}
	}

	@Override
	public boolean equals(Object o) {
		if (o == null || !(o instanceof Cookie)) return false;
		if (this == o) return true;
		Cookie that = (Cookie) o;
		if (getPairs() == null ? that.getPairs() == null : false) {
			return true;
		} else if (getPairs().size() == that.getPairs().size()) {
			return getPairs().containsAll(that.getPairs());
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
	public Object setDbId(Session session) {
		List<Integer> ids = new ArrayList<>(pairs.size());
		for (CookiePair c : pairs) {
			Integer tmp = (Integer) c.setDbId(session);
			if (tmp != null) {
				ids.add(tmp);
			}
		}
		if (getId() != null) {
			return getId();
		}
		if (ids.size() == pairs.size()) {
			Query query = session.createQuery("select distinct c.id from " + getClass().getSimpleName() + " as c join c.pairs as cp where cp.id in (:list)");
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
}
