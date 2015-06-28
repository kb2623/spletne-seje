package org.spletneseje.fields.ncsa;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.spletneseje.fields.Field;
import org.spletneseje.fields.FieldType;
import org.spletneseje.fields.Method;
import org.spletneseje.fields.Protocol;

public class RequestLine implements Field {

	private Method method;
	private URL url;
	private Protocol protocol;
	/**
	 * Konstruktor
	 *
	 * @param method Uporabljena metoda
	 * @param url Uporabljen URL naslov
	 * @param protocol Uporabljen protokol
	 * @throws MalformedURLException Podan nepravilen URL naslov
	 */
	public RequestLine(String method, String url, String protocol) throws MalformedURLException {
		String[] tab = protocol.split("/");
		this.url = new URL(tab[0], "", url);
		this.protocol = new Protocol(protocol);
		this.method = Method.setMethod(method);
	}
	/**
	 * Getter metoda za URL
	 *
	 * @return Url naslov
	 * @see URL
	 */
	public URL getUrl() {
		return this.url;
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
	/**
	 * Metoda, ki vrne koncnico zahtevanega resursa
	 *
	 * @return
	 *      OK -> Koncnica zahtevanega resursa
	 *      ERROR -> <code>null</code>
	 */
	public String getExtension() {
		int indexOfExtension = url.getPath().lastIndexOf('.');
		int indexOfLastSeparator = url.getPath().lastIndexOf('/');
		return (indexOfExtension < indexOfLastSeparator) ? null : url.getPath().substring(indexOfExtension+1);
	}
	/**
	 * Metoda vrne query string
	 *
	 * @return query niz
	 */
	public String getQueryToString() {
		StringBuilder builder = new StringBuilder();
		builder.append('[');
		getQuery().entrySet().forEach(e -> builder.append('[').append(e.getKey()).append(" = ").append(e.getValue()).append(']'));
		return builder.append(']').toString();
	}
	/**
	 * Metoda vrne query niz v sodatkovni strukturi slovar
	 *
	 * @return slovar, ki vsebuje kot
	 *      ključ -> ključ qury niza
	 *      vrednost -> vresnost query niza
	 */
	public Map<String, String> getQuery() {
		HashMap<String, String> map = new HashMap<>();
		if (url.getQuery() == null) return map;
		for (String s : url.getQuery().split("&")) {
			String[] tmp = s.split("=");
			map.put(tmp[0], tmp[1]);
		}
		return map;
	}

	@Override
	public String izpis() {
		return method.izpis() + " " + url.getPath() + " " + url.getQuery() + " " + protocol.izpis();
	}

	@Override
	public String toString() {
		return method.toString() + " " + url.getPath() + " " + url.getQuery() + " " + protocol.toString();
	}

	@Override
	public boolean equals(Object o) {
		return o instanceof RequestLine && (o == this || method == ((RequestLine) o).getMethod() && protocol.equals(((RequestLine) o).getProtocol()) && url.equals(((RequestLine) o).getUrl()));
	}

	@Override
	public FieldType getFieldType() {
		return FieldType.RequestLine;
	}

}