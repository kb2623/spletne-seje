package org.sessionization.fields.ncsa;

import java.net.MalformedURLException;
import java.net.URL;

import org.sessionization.fields.*;
import org.oosql.annotation.Column;
import org.sessionization.fields.Method;

public class RequestLine implements Field {

	@Column 
	private Method method;
	@Column 
	private FileQuery uri;
	@Column 
	private Protocol protocol;
	/**
	 * Konstruktor
	 *
	 * @param method Uporabljena metoda
	 * @param url Uporabljen URL naslov
	 * @param protocol Uporabljen protokol
	 * @throws MalformedURLException Podan nepravilen URL naslov
	 */
	public RequestLine(String method, String uri, String protocol) throws MalformedURLException {
		this.uri = new FileQuery(new URL(protocol.split("/")[0], null, uri));
		this.protocol = new Protocol(protocol);
		this.method = Method.setMethod(method);
	}
	/**
	 * Getter za resurs
	 *
	 * @return
	 * @see File
	 */
	public FileQuery getFile() {
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
	public FieldType getFieldType() {
		return FieldType.RequestLine;
	}

	@Override
	public String toString() {
		return method.toString() + " " + uri.toString() + " " + uri.toString() + " " + protocol.toString();
	}

	@Override
	public boolean equals(Object o) {
		return o instanceof RequestLine && (o == this || method == ((RequestLine) o).getMethod() && protocol.equals(((RequestLine) o).getProtocol()) && uri.equals(((RequestLine) o).getFile()));
	}
}
