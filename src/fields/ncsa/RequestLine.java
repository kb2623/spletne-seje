package fields.ncsa;

import java.net.MalformedURLException;
import java.net.URL;

import fields.Field;
import fields.Method;

public class RequestLine implements Field {
	
	private Method method;
	private URL url;
	private float version;
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
		if (tab.length > 1) {
			version = Float.valueOf(tab[1]);
		}
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
	 * @see fields.Method
	 */
	public Method getMethod() {
		return this.method;
	}
	/**
	 * Getter za verzijo protokola
	 *
	 * @return Verzija protokola
	 */
	public float getProtocolVersion() {
		return version;
	}
    /**
     * Metoda, ki vrne koncnico zahtevanega resursa
     *
     * @return OK -> Koncnica zahtevanega resursa, ERROR -> <code>null</code>
     */
	public String getExtension() {
		int indexOfExtension = url.getPath().lastIndexOf('.');
		int indexOfLastSeparator = url.getPath().lastIndexOf('/');
		return (indexOfExtension < indexOfLastSeparator) ? null : url.getPath().substring(indexOfExtension+1);
	}
	
	@Override
	public String izpis() {
		return method.izpis() + " " + url.getPath() + " " + url.getQuery();
	}
	
	@Override
	public String getKey() {
		return null;
	}

}
