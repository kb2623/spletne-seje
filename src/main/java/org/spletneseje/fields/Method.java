package org.spletneseje.fields;
/**
 * Razred sharanjuje podatke o uporabljeni metodi
 * 
 * @author klemen
 */
public enum Method implements Field {

	// Vse HTTP metode
	GET {
		@Override
		public String getMethod() {
			return "GET";
		}
	},
	HEAD() {
		@Override
		public String getMethod() {
			return "HEAD";
		}
	},
	POST() {
		@Override
		public String getMethod() {
			return "POST";
		}
	},
	PUT() {
		@Override
		public String getMethod() {
			return "PUT";
		}
	},
	DELETE() {
		@Override
		public String getMethod() {
			return "DELETE";
		}
	},
	TRACE() {
		@Override
		public String getMethod() {
			return "TRACE";
		}
	},
	OPTIONS() {
		@Override
		public String getMethod() {
			return "OPTIONS";
		}
	},
	CONNECT() {
		@Override
		public String getMethod() {
			return "CONNECT";
		}
	},
	PATCH() {
		@Override
		public String getMethod() {
			return "PATCH";
		}
	};

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
	public abstract String getMethod();
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
		return getMethod();
	}

	@Override
	public String toString() {
		return getMethod();
	}

	@Override
	public FieldType getFieldType() {
		return FieldType.Method;
	}
}
