package org.sessionization.fields;

import org.oosqljet.annotation.Column;

import java.net.URL;

public class FileQuery extends File {

	@Column private UriQuery query;

	public FileQuery(URL url) {
		super(url.getFile());
		query = new UriQuery(url.getQuery());
	}
	/**
	 * Metoda vrne query
	 *
	 * @return vrne <code>UriQuery</code>
	 * @see UriQuery
	 */
	public UriQuery getQuery() {
		return query;
	}

	@Override
	public String izpis() {
		return super.izpis() + query.izpis();
	}

	@Override
	public String toString() {
		return super.toString() + query.toString();
	}

	@Override
	public boolean equals(Object o) {
		return o instanceof FileQuery && super.equals(o) && query.equals(((FileQuery) o).getQuery());
	}
}
