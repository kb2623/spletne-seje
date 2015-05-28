package fields.ncsa;

import java.net.MalformedURLException;
import java.net.URL;

import fields.Field;
import fields.Method;

public class RequestLine implements Field {
	
	private Method method;
	private URL url;
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
		this.method = Method.setMethod(this.url.getProtocol(), method);
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
	 * @see fields.Method
	 */
	public Method getMethod() {
		return this.method;
	}
	
	public String getExtension() {
		int indexOfExtension = url.getPath().lastIndexOf('.');
		int indexOfLastSeparator = url.getPath().lastIndexOf('/');
		return (indexOfExtension < indexOfLastSeparator) ? 
				null : url.getPath().substring(indexOfExtension+1);
	}
	
	@Override
	public String izpis() {
		return method.izpis()+" "+url.getPath()+" "+url.getQuery();
	}
	
	@Override
	public String getKey() {
		return null;
	}

}
