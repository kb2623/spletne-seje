package org.sessionization.parser.fields.ncsa;

import org.hibernate.Query;
import org.hibernate.Session;
import org.sessionization.database.HibernateUtil;
import org.sessionization.database.MethodConverter;
import org.sessionization.parser.LogField;
import org.sessionization.parser.fields.Method;
import org.sessionization.parser.fields.Protocol;
import org.sessionization.parser.fields.Resource;
import org.sessionization.parser.fields.UriSteamQuery;

import javax.persistence.*;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

@Entity
@Cacheable
public class RequestLine implements LogField, HibernateUtil.HibernateTable, Resource {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	private Method method;

	@OneToOne(cascade = CascadeType.ALL)
	private UriSteamQuery steamQuery;

	@OneToOne(cascade = CascadeType.ALL)
	private Protocol protocol;

	public RequestLine() {
		id = null;
		method = null;
		steamQuery = null;
		protocol = null;
	}

	/**
	 *
	 * @param line
	 * @throws URISyntaxException
	 */
	public RequestLine(String line) throws URISyntaxException {
		String[] tab = line.split(" ");
		if (tab.length < 3) {
			throw new IllegalArgumentException();
		} else {
			method = Method.setMethod(tab[0]);
			steamQuery = new UriSteamQuery(new URI(tab[1]));
			protocol = new Protocol(tab[2]);
		}
	}

	/**
	 * Konstruktor
	 *
	 * @param method   Uporabljena metoda
	 * @param url      Uporabljen URL naslov
	 * @param protocol Uporabljen protokol
	 * @throws MalformedURLException Podan nepravilen URL naslov
	 */
	public RequestLine(String method, String uri, String protocol) throws URISyntaxException {
		id = null;
		this.steamQuery = new UriSteamQuery(new URI(uri));
		this.protocol = new Protocol(protocol);
		this.method = Method.setMethod(method);
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

	public String getFile() {
		return steamQuery.getFile();
	}

	/**
	 * Getter metoda za method
	 *
	 * @return Uporabljena metoda pri zahtevi
	 * @see Method
	 */
	public Method getMethod() {
		return this.method;
	}

	public void setMethod(Method method) {
		this.method = method;
	}

	/**
	 * Getter za protokol
	 *
	 * @return protokol
	 */
	public Protocol getProtocol() {
		return protocol;
	}

	public void setProtocol(Protocol protocol) {
		this.protocol = protocol;
	}

	@Override
	public String izpis() {
		return method.izpis() + " " + steamQuery.izpis() + " " + protocol.izpis();
	}

	@Override
	public String toString() {
		return method.toString() + " " + steamQuery.toString() + " " + protocol.toString();
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		RequestLine that = (RequestLine) o;
		if (getId() != null ? !getId().equals(that.getId()) : that.getId() != null) return false;
		if (getMethod() != that.getMethod()) return false;
		if (getSteamQuery() != null ? !getSteamQuery().equals(that.getSteamQuery()) : that.getSteamQuery() != null)
			return false;
		if (getProtocol() != null ? !getProtocol().equals(that.getProtocol()) : that.getProtocol() != null) return false;
		return true;
	}

	@Override
	public int hashCode() {
		int result = getId() != null ? getId().hashCode() : 0;
		result = 31 * result + (getMethod() != null ? getMethod().hashCode() : 0);
		result = 31 * result + (getSteamQuery() != null ? getSteamQuery().hashCode() : 0);
		result = 31 * result + (getProtocol() != null ? getProtocol().hashCode() : 0);
		return result;
	}

	@Override
	public Object setDbId(Session session) {
		Integer id = getId();
		Integer steamQueryId = (Integer) getSteamQuery().setDbId(session);
		Integer protocolId = (Integer) getProtocol().setDbId(session);
		if (id == null && steamQueryId != null && protocolId != null) {
			Query query = session.createQuery("select l.id from " + getClass().getSimpleName() + " as l where l.method = '" + MethodConverter.getMethodString(getMethod()) + "' and l.steamQuery = " + steamQueryId + " and l.protocol = " + protocolId);
			List list = query.list();
			if (!list.isEmpty()) {
				id = (Integer) list.get(0);
				setId(id);
			}
		}
		return id;
	}
}
