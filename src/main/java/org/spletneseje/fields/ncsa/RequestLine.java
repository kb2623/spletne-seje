package org.spletneseje.fields.ncsa;

import java.net.MalformedURLException;
import java.net.URL;

import org.spletneseje.database.annotation.Entry;
import org.spletneseje.fields.*;

public class RequestLine extends File {

	@Entry	private Method method;
	private URL uri;
	@Entry	private Protocol protocol;
	/**
	 * Konstruktor
	 *
	 * @param method Uporabljena metoda
	 * @param url Uporabljen URL naslov
	 * @param protocol Uporabljen protokol
	 * @throws MalformedURLException Podan nepravilen URL naslov
	 */
	public RequestLine(String method, String uri, String protocol) throws MalformedURLException {
		String[] tab = protocol.split("/");
		this.uri = new URL(tab[0], "", uri);
		this.protocol = new Protocol(protocol);
		this.method = Method.setMethod(method);
	}
	/**
	 * Getter metoda za URL
	 *
	 * @return Url naslov
	 * @see URL
	 */
	public URL getUri() {
		return this.uri;
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
		return method.izpis() + " " + uri.getPath() + " " + uri.getQuery() + " " + protocol.izpis();
	}

	@Override
	public FieldType getFieldType() {
		return FieldType.RequestLine;
	}

	@Override
	public String getExtension() {
		int indexOfExtension = uri.getPath().lastIndexOf('.');
		int indexOfLastSeparator = uri.getPath().lastIndexOf('/');
		return (indexOfExtension < indexOfLastSeparator) ? null : uri.getPath().substring(indexOfExtension+1);
	}

	@Override
	public UriQuery getQuery() {
		return new UriQuery(uri.getQuery());
	}

	/**
	 * @return
	 */
	@Override
	@Entry public String file() {
		return uri.getFile();
	}

	@Override
	public String toString() {
		return method.toString() + " " + uri.getPath() + " " + uri.getQuery() + " " + protocol.toString();
	}

	@Override
	public boolean equals(Object o) {
		return o instanceof RequestLine && (o == this || method == ((RequestLine) o).getMethod() && protocol.equals(((RequestLine) o).getProtocol()) && uri.equals(((RequestLine) o).getUri()));
	}

}