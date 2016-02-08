package org.sessionization.parser.fields.ncsa;

import org.hibernate.Session;
import org.sessionization.database.HibernateTable;
import org.sessionization.parser.LogField;
import org.sessionization.parser.fields.Method;
import org.sessionization.parser.fields.Protocol;
import org.sessionization.parser.fields.Resource;
import org.sessionization.parser.fields.UriSteamQuery;

import javax.persistence.Cacheable;
import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToOne;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;

@Embeddable
@Cacheable
public class RequestLine implements LogField, HibernateTable, Resource {

	private Method method;

	@OneToOne(cascade = CascadeType.ALL)
	private UriSteamQuery steamQuery;

	@OneToOne(cascade = CascadeType.ALL)
	private Protocol protocol;

	public RequestLine() {
		method = null;
		steamQuery = null;
		protocol = null;
	}

	/**
	 *
	 * @param line
	 * @throws URISyntaxException
	 */
	public RequestLine(String line) {
		String[] tab = line.split(" ");
		if (tab.length < 3) {
			throw new IllegalArgumentException();
		} else {
			method = Method.setMethod(tab[0]);
			steamQuery = new UriSteamQuery(URI.create(tab[1]));
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
		this.steamQuery = new UriSteamQuery(new URI(uri));
		this.protocol = new Protocol(protocol);
		this.method = Method.setMethod(method);
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
	public Object setDbId(Session session) {
		Integer steamQueryId = (Integer) getSteamQuery().setDbId(session);
		Integer protocolId = (Integer) getProtocol().setDbId(session);
		return null;
	}

	@Override
	public boolean equals(Object o) {
		if (o == null || !(o instanceof RequestLine)) return false;
		if (this == o) return true;
		RequestLine that = (RequestLine) o;
		return getMethod() != null ? getMethod().equals(that.getMethod()) : that.getMethod() == null
				&& getSteamQuery() != null ? getSteamQuery().equals(that.getSteamQuery()) : that.getSteamQuery() == null
				&& getProtocol() != null ? getProtocol().equals(that.getProtocol()) : that.getProtocol() == null;
	}

	@Override
	public int hashCode() {
		int result = getMethod() != null ? getMethod().hashCode() : 0;
		result = 31 * result + (getSteamQuery() != null ? getSteamQuery().hashCode() : 0);
		result = 31 * result + (getProtocol() != null ? getProtocol().hashCode() : 0);
		return result;
	}

	@Override
	public String toString() {
		return "RequestLine{" +
				"method=" + method +
				", steamQuery=" + steamQuery +
				", protocol=" + protocol +
				'}';
	}
}
