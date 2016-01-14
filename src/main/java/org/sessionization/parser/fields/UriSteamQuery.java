package org.sessionization.parser.fields;

import org.hibernate.Query;
import org.hibernate.Session;
import org.sessionization.database.HibernateUtil;
import org.sessionization.parser.LogField;

import javax.persistence.*;
import java.net.URI;
import java.util.List;

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
	public String toString() {
		return uriSteam.toString() + " " + (query.getPairs() != null ? query.izpis() : "");
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof UriSteamQuery)) return false;
		UriSteamQuery that = (UriSteamQuery) o;
		if (id != null ? !id.equals(that.id) : that.id != null) return false;
		if (uriSteam != null ? !uriSteam.equals(that.uriSteam) : that.uriSteam != null) return false;
		if (getQuery() != null ? !getQuery().equals(that.getQuery()) : that.getQuery() != null) return false;
		return true;
	}

	@Override
	public int hashCode() {
		int result = id != null ? id.hashCode() : 0;
		result = 31 * result + (uriSteam != null ? uriSteam.hashCode() : 0);
		result = 31 * result + (getQuery() != null ? getQuery().hashCode() : 0);
		return result;
	}

	@Override
	public Object setDbId(Session session) {
		getUriSteam().setDbId(session);
		getQuery().setDbId(session);
		Query query = session.createQuery(
				"select sq.id form " + getClass().getSimpleName() + " as sq where sq.uriSteam = " + getUriSteam().getId() + " and sq.query = " + getQuery().getId()
		);
		List list = query.list();
		Integer id = null;
		if (!list.isEmpty()) {
			id = (Integer) list.get(0);
			setId(id);
		}
		return id;
	}
}
