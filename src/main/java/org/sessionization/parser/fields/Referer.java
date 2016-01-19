package org.sessionization.parser.fields;

import org.hibernate.Query;
import org.hibernate.Session;
import org.sessionization.database.HibernateUtil;
import org.sessionization.parser.LogField;

import javax.persistence.*;
import java.net.URI;

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
		if (o == null || !(o instanceof Referer)) return false;
		if (this == o) return true;
		Referer that = (Referer) o;
		return getSteamQuery() != null ? getSteamQuery().equals(that.getSteamQuery()) : that.getSteamQuery() == null
				&& getHost() != null ? getHost().equals(that.getHost()) : that.getHost() == null;
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
		Integer steamQueryId = (Integer) getSteamQuery().setDbId(session);
		Integer hostId = (Integer) getHost().setDbId(session);
		if (getId() != null) {
			return getId();
		}
		if (steamQueryId != null && hostId != null) {
			Query query = session.createQuery("select r form " + getClass().getSimpleName() + " as r where r.steamQuery = " + steamQueryId + " and r.host = " + hostId);
			for (Object o : query.list()) {
				if (equals(session.load(getClass(), (Integer) o))) {
					setId((Integer) o);
					return o;
				}
			}
		}
		return null;
	}
}
