package org.sessionization.fields;

import javax.persistence.Cacheable;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToOne;
import java.net.URI;
import java.net.URISyntaxException;

@Entity
@Cacheable
public class Referer extends UriSteamQuery implements Field {

	@OneToOne(cascade = CascadeType.ALL)
	private Host host;

	public Referer() {
		super();
		host = null;
	}

	public Referer(URI url) {
		super(url);
		if (url.getRawAuthority() != null) {
			host = new Host(url.getAuthority());
		} else {
			host = new Host("-");
		}
	}

	public void setHost(Host host) {
		this.host = host;
	}

	public Host getHost() {
		return host;
	}

	@Override
	public String izpis() {
		return host.izpis() + super.izpis();
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof Referer)) return false;
		if (!super.equals(o)) return false;
		Referer referer = (Referer) o;
		if (getHost() != null ? !getHost().equals(referer.getHost()) : referer.getHost() != null) return false;
		return true;
	}

	@Override
	public int hashCode() {
		int result = super.hashCode();
		result = 31 * result + (getHost() != null ? getHost().hashCode() : 0);
		return result;
	}

	@Override
	public String toString() {
		return host.toString() + super.toString();
	}
}
