package org.sessionization.parser.fields;

import org.hibernate.Query;
import org.hibernate.Session;
import org.sessionization.database.HibernateUtil;
import org.sessionization.parser.LogField;

import javax.persistence.*;
import java.net.URI;

@Entity
@Cacheable
public class UriSteamQuery implements LogField, HibernateUtil.HibernateTable, Resource {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@OneToOne(cascade = CascadeType.ALL)
	private UriSteam uriSteam;

	@OneToOne(cascade = CascadeType.ALL)
	private UriQuery query;

	public UriSteamQuery() {
		id = null;
		uriSteam = null;
		query = null;
	}

	public UriSteamQuery(URI uri) {
		uriSteam = new UriSteam(uri.getRawPath());
		String query = uri.getQuery();
		this.query = new UriQuery(query != null ? query : "-");
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public UriSteam getUriSteam() {
		return uriSteam;
	}

	public void setUriSteam(UriSteam uriSteam) {
		this.uriSteam = uriSteam;
	}

	public UriQuery getQuery() {
		return query;
	}

	public void setQuery(UriQuery query) {
		this.query = query;
	}

	@Override
	public String getFile() {
		return uriSteam.getFile();
	}

	@Override
	public String izpis() {
		return uriSteam.izpis() + " " + (query.getPairs() != null ? query.izpis() : "");
	}

	@Override
	public Object setDbId(Session session) {
		Integer uriSteamId = (Integer) getUriSteam().setDbId(session);
		Integer queryId = (Integer) getQuery().setDbId(session);
		if (getId() != null) {
			return getId();
		}
		if (uriSteamId != null && queryId != null) {
			Query query = session.createQuery("select sq.id form " + getClass().getSimpleName() + " as sq where sq.uriSteam = " + uriSteamId + " and sq.query = " + queryId);
			for (Object o : query.list()) {
				if (equals(session.load(getClass(), (Integer) o))) {
					setId((Integer) o);
					return o;
				}
			}
		}
		return null;
	}

	@Override
	public boolean equals(Object o) {
		if (o == null || !(o instanceof UriSteamQuery)) return false;
		if (this == o) return true;
		UriSteamQuery that = (UriSteamQuery) o;
		return getUriSteam() != null ? getUriSteam().equals(that.getUriSteam()) : that.getUriSteam() == null
				&& getQuery() != null ? getQuery().equals(that.getQuery()) : that.getQuery() == null;
	}

	@Override
	public int hashCode() {
		int result = id != null ? id.hashCode() : 0;
		result = 31 * result + (uriSteam != null ? uriSteam.hashCode() : 0);
		result = 31 * result + (getQuery() != null ? getQuery().hashCode() : 0);
		return result;
	}

	@Override
	public String toString() {
		return "UriSteamQuery{" +
				"id=" + id +
				", uriSteam=" + uriSteam +
				", query=" + query +
				'}';
	}
}
