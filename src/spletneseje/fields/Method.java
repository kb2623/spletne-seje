package spletneseje.fields;
/**
 * Razred sharanjuje podatke o uporabljeni metodi
 * 
 * @author klemen
 */
public enum Method implements Field {

	// Vse HTTP metode
	GET("GET"),
	HEAD("HEAD"),
	POST("POST"),
	PUT("PUT"),
	DELETE("DELETE"),
	TRACE("TRACE"),
	OPTIONS("OPTIONS"),
	CONNECT("CONNECT"),
	PATCH("PATCH");
	
	private String method;
	
	private Method(String method) {
		this.method = method;
	}
	/**
	 * Staticna metoda, ki ustvari nov objekt tipa Method.
	 *
	 * @param protocol Niz, ki vsebuje protokol
	 * @param method Niz, ki predstavlja uporabljeno metodo protokola
	 * @return Objekt, ki predstavlja metodo
	 * @throws IllegalArgumentException
	 * @see Method
	 */
	public static Method setMethod(String method) throws IllegalArgumentException {
		switch (method.toUpperCase()) {
		case "GET":		return GET;
		case "HEAD":	return HEAD;
		case "POST":	return POST;
		case "PUT": 	return PUT;
		case "DELETE":	return DELETE;
		case "TRACE": 	return TRACE;
		case "OPTIONS": return OPTIONS;
		case "CONNECT": return CONNECT;
		case "PATCH": 	return PATCH;
		default: 		throw new IllegalArgumentException("Neznana metoda");
		}
	}
	/**
	 * Getter za uporabljeno metodo HTTP protokola.
	 *
	 * @return Niz, ki predstavlja uporabljeno metodo HTTP protokola
	 */
	public String getMethod() {
		return method;
	}
	/**
	 * Getter za protokol metode
	 *
	 * @return Protokol metode
	 */
	public String getProtocol() {
		return "HTTP";
	}
	
	@Override
	public String izpis() {
		return method;
	}

	@Override
	public String getKey() {
		return null;
	}
}
