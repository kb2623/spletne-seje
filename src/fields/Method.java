package fields;
/**
 * Razred sharanjuje podatke o uporabljeni metodi
 * 
 * @author klemen
 */
public enum Method implements Field {
	
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
	 * 
	 * @param protocol
	 * @param method
	 * @return
	 * @throws IllegalArgumentException
	 * @see fields.Method
	 */
	public static Method setMethod(String protocol, String method) throws IllegalArgumentException {
		if(protocol.equalsIgnoreCase("HTTP")) {
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
		} else if(protocol.equals("FTP")) {
			//TODO Dodaj metode za FTP protokol
			throw new IllegalArgumentException("Ni še implementirano");
		} else {
			throw new IllegalArgumentException("Neznan protokol");
		}
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
