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
public class Referer implements LogField, HibernateUtil.HibernateTable, Resource {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@OneToOne(cascade = CascadeType.ALL)
	private UriSteamQuery steamQuery;

	@OneToOne(cascade = CascadeType.ALL)
	private Host host;

	public Referer() {
		id = null;
		steamQuery = null;
		host = null;
	}

	public Referer(URI url) {
		String tmp;
		steamQuery = new UriSteamQuery(url);
		tmp = url.getAuthority();
		host = new Host(tmp != null ? tmp : "-");
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public UriSteamQuery getSteamQuery() {
		return steamQuery;
	}

	public void setSteamQuery(UriSteamQuery steamQuery) {
		this.steamQuery = steamQuery;
	}

	public Host getHost() {
		return host;
	}

	public void setHost(Host host) {
		this.host = host;
	}

	@Override
	public String izpis() {
		return host.izpis() + " " + steamQuery.izpis();
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof Referer)) return false;
		Referer referer = (Referer) o;
		if (steamQuery != null ? !steamQuery.equals(referer.steamQuery) : referer.steamQuery != null) return false;
		if (getHost() != null ? !getHost().equals(referer.getHost()) : referer.getHost() != null) return false;
		return true;
	}

	@Override
	public int hashCode() {
		int result = id != null ? id.hashCode() : 0;
		result = 31 * result + (steamQuery != null ? steamQuery.hashCode() : 0);
		result = 31 * result + (getHost() != null ? getHost().hashCode() : 0);
		return result;
	}

	@Override
	public String toString() {
		return steamQuery.toString() + " " + host.toString();
	}

	@Override
	public String getFile() {
		return steamQuery.getFile();
	}

	@Override
	public Object setDbId(Session session) {
		Integer id = getId();
		Integer steamQueryId = (Integer) getSteamQuery().setDbId(session);
		Integer hostId = (Integer) getHost().setDbId(session);
		if (id == null && steamQueryId != null && hostId != null) {
			Query query = session.createQuery(
					"select r form " + getClass().getSimpleName() + " as r where r.steamQuery = " + steamQueryId + " and r.host = " + hostId
			);
			List list = query.list();
			if (!list.isEmpty()) {
				id = (Integer) list.get(0);
				setId(id);
			}
		}
		return id;
	}
}
