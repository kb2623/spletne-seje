package org.sessionization.fields;

import org.oosql.annotation.Column;

import java.net.URL;

public class Referer extends FileQuery implements Field {

	@Column 
	Host host;

	public Referer(URL url) {
		super(url);
		host = new Host(url.getAuthority());
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
