package org.sessionization.fields;

import org.oosqljet.SqlMapping;
import org.oosqljet.annotation.Table;

/**
 * Razred sharanjuje podatke o uporabljeni metodi
 * 
 * @author klemen
 */
@Table public enum Method implements Field, SqlMapping<Method, Integer> {

	// Vse HTTP metode
	GET {
        @Override
        public Integer inMaping(Method method) {
            // TODO
            return null;
        }

        @Override
		public String getMethod() {
			return "GET";
		}
	},
	HEAD() {
        @Override
        public Integer inMaping(Method method) {
            // TODO
            return null;
        }

        @Override
		public String getMethod() {
			return "HEAD";
		}
	},
	POST() {
        @Override
        public Integer inMaping(Method method) {
            // TODO
            return null;
        }

        @Override
		public String getMethod() {
			return "POST";
		}
	},
	PUT() {
        @Override
        public Integer inMaping(Method method) {
            // TODO
            return null;
        }

        @Override
		public String getMethod() {
			return "PUT";
		}
	},
	DELETE() {
        @Override
        public Integer inMaping(Method method) {
            // TODO
            return null;
        }

        @Override
		public String getMethod() {
			return "DELETE";
		}
	},
	TRACE() {
        @Override
        public Integer inMaping(Method method) {
            // TODO
            return null;
        }

        @Override
		public String getMethod() {
			return "TRACE";
		}
	},
	OPTIONS() {
        @Override
        public Integer inMaping(Method method) {
            // TODO
            return null;
        }

        @Override
		public String getMethod() {
			return "OPTIONS";
		}
	},
	CONNECT() {
        @Override
        public Integer inMaping(Method method) {
            // TODO
            return null;
        }

        @Override
		public String getMethod() {
			return "CONNECT";
		}
	},
	PATCH() {
        @Override
        public Integer inMaping(Method method) {
            // TODO
            return null;
        }

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

    @Override
    public Method outMaping(Integer in) {
        // TODO
        return null;
    }
}
