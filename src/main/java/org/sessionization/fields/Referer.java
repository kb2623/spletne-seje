package org.sessionization.fields;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.net.URL;

@Entity
@Table(name = "referer")
public class Referer extends FileQuery implements Field {

	@Column(name = "host")
	Host host;

	public Referer() {
		super();
		host = null;
	}

	public Referer(URL url) {
		super(url);
		host = new Host(url.getAuthority());
	}

	public void setHost(Host host) {
		this.host = host;
	}

	public Host getHost() {
		return host;
	}

	@Override
	public String getKey() {
		return "";
	}

	@Override
	public String izpis() {
		return host.izpis() + super.izpis();
	}

	@Override
	public FieldType getFieldType() {
		return FieldType.Referer;
	}

	@Override
	public boolean equals(Object o) {
		return o instanceof Referer && super.equals(o) && host.equals(((Referer) o).getHost());
	}

	@Override
	public String toString() {
		return host.toString() + super.toString();
	}
}
