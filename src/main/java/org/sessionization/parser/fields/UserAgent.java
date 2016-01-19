package org.sessionization.parser.fields;

import org.hibernate.Query;
import org.hibernate.Session;
import org.sessionization.database.HibernateUtil;
import org.sessionization.parser.LogField;
import org.sessionization.parser.LogType;

import javax.persistence.*;

@Entity
@Cacheable
public class UserAgent implements LogField, HibernateUtil.HibernateTable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(name = "name", unique = true, nullable = false)
	private String userAgentString;

	public UserAgent() {
		id = null;
		userAgentString = null;
	}

	public UserAgent(String info, LogType type) {
		id = null;
		userAgentString = type.parseUserAgent(info);
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getUserAgentString() {
		return userAgentString != null ? userAgentString : "";
	}

	public void setUserAgentString(String userAgentString) {
		this.userAgentString = userAgentString;
	}

	public boolean isCrawler() {
		// todo Najdi primerno metodo za prepoznavanje spletnih robotov
		return false;
	}

	@Override
	public String izpis() {
		return userAgentString;
	}

	@Override
	public String toString() {
		return userAgentString;
	}

	@Override
	public String getKey() {
		return userAgentString != null ? userAgentString : "-";
	}

	@Override
	public boolean equals(Object o) {
		if (o == null || !(o instanceof UserAgent)) return false;
		if (this == o) return true;
		UserAgent that = (UserAgent) o;
		return getUserAgentString().equals(that.getUserAgentString());
	}

	@Override
	public int hashCode() {
		int result = getId() != null ? getId().hashCode() : 0;
		result = 31 * result + getUserAgentString().hashCode();
		return result;
	}

	@Override
	public Object setDbId(Session session) {
		if (getId() != null) {
			return getId();
		}
		Query query = session.createQuery("select u.id form " + getClass().getSimpleName() + " as u where u.userAgentString = '" + getUserAgentString() + "'");
		for (Object o : query.list()) {
			if (equals(session.load(getClass(), (Integer) o))) {
				setId((Integer) o);
				return o;
			}
		}
		return null;
	}
}
