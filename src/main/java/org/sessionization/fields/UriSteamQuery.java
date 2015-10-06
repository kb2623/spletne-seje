package org.sessionization.fields;

import javax.persistence.Cacheable;
import javax.persistence.Entity;
import javax.persistence.OneToOne;
import java.net.URI;

@Entity
@Cacheable
public class UriSteamQuery extends UriSteam {

	@OneToOne
	private UriQuery query;

	public UriSteamQuery() {
		super();
		query = null;
	}

	public UriSteamQuery(URI uri) {
		super(uri.getRawPath());
		query = new UriQuery(uri.getQuery());
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
		return super.izpis() + (query.getPairs() != null ? query.izpis() : "");
	}

	@Override
	public String toString() {
		return super.toString() + (query.getPairs() != null ? query.izpis() : "");
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		if (!super.equals(o)) return false;
		UriSteamQuery that = (UriSteamQuery) o;
		if (getQuery() != null ? !getQuery().equals(that.getQuery()) : that.getQuery() != null) return false;
		return true;
	}

	@Override
	public int hashCode() {
		int result = super.hashCode();
		result = 31 * result + (getQuery() != null ? getQuery().hashCode() : 0);
		return result;
	}
}
