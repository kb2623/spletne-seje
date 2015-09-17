package org.sessionization.fields.ncsa;

import org.sessionization.fields.*;

import javax.persistence.*;
import java.net.MalformedURLException;
import java.net.URL;

@Entity
public class RequestLine implements Field {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@OneToOne(cascade = CascadeType.ALL)
	private Method method;

	@OneToOne(cascade = CascadeType.ALL)
	private UriSteamQuery uri;

	@OneToOne(cascade = CascadeType.ALL)
	private Protocol protocol;

	public RequestLine() {
		id = null;
		method = null;
		uri = null;
		protocol = null;
	}
	/**
	 * Konstruktor
	 *
	 * @param method Uporabljena metoda
	 * @param url Uporabljen URL naslov
	 * @param protocol Uporabljen protokol
	 * @throws MalformedURLException Podan nepravilen URL naslov
	 */
	public RequestLine(String method, String uri, String protocol) throws MalformedURLException {
		id = null;
		this.uri = new UriSteamQuery(new URL(protocol.split("/")[0], null, uri));
		this.protocol = new Protocol(protocol);
		this.method = Method.setMethod(method);
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public void setMethod(Method method) {
		this.method = method;
	}

	public UriSteamQuery getUri() {
		return uri;
	}

	public void setUri(UriSteamQuery uri) {
		this.uri = uri;
	}

	public void setProtocol(Protocol protocol) {
		this.protocol = protocol;
	}
	/**
	 * Getter za resurs
	 *
	 * @return
	 * @see UriSteam
	 */
	public UriSteamQuery getFile() {
		return uri;
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
	/**
	 * Getter za protokol
	 *
	 * @return protokol
	 */
	public Protocol getProtocol() {
		return protocol;
	}

	@Override
	public String izpis() {
		return method.izpis() + " " + uri.izpis() + " " + uri.izpis() + " " + protocol.izpis();
	}

	@Override
	public String toString() {
		return method.toString() + " " + uri.toString() + " " + uri.toString() + " " + protocol.toString();
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		RequestLine that = (RequestLine) o;
		if (getId() != null ? !getId().equals(that.getId()) : that.getId() != null) return false;
		if (getMethod() != that.getMethod()) return false;
		if (getUri() != null ? !getUri().equals(that.getUri()) : that.getUri() != null) return false;
		if (getProtocol() != null ? !getProtocol().equals(that.getProtocol()) : that.getProtocol() != null) return false;
		return true;
	}

	@Override
	public int hashCode() {
		int result = getId() != null ? getId().hashCode() : 0;
		result = 31 * result + (getMethod() != null ? getMethod().hashCode() : 0);
		result = 31 * result + (getUri() != null ? getUri().hashCode() : 0);
		result = 31 * result + (getProtocol() != null ? getProtocol().hashCode() : 0);
		return result;
	}
}
