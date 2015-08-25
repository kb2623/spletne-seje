package org.sessionization.fields;

import javax.persistence.Column;
import java.net.URL;

// TODO
public class FileQuery extends File {

	@Column(name = "query")
	private UriQuery query;

	public FileQuery() {
		super();
		query = null;
	}

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

	public void setQuery(UriQuery query) {
		this.query = query;
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
