package org.spletneseje.fields;

import java.net.MalformedURLException;
import java.net.URL;

public class Referer implements Field {
	
	private URL uri;
	
	public Referer(String url) throws MalformedURLException {
		if(!url.equals("-")) {
			uri = new URL(url);
		} else {
			uri = null;
		}
	}

	public URL getUrl() {
		return uri;
	}

	public UriQuery getUrlQuerys() {
		if (uri == null) return null;
		return new UriQuery(uri.getQuery());
	}

	@Override
	public String izpis() {
		return (uri == null) ? "-" : uri.getPath() + uri.getQuery();
	}

	@Override
	public String toString() {
		return (uri == null) ? "-" : uri.getPath() + uri.getQuery();
	}

	@Override
	public boolean equals(Object o) {
		return o instanceof Referer && (uri == null) ? ((Referer) o).getUrl() == null : uri.equals(((Referer) o).getUrl());
	}

	@Override
	public String getKey() {
		return "";
	}

	@Override
	public FieldType getFieldType() {
		return FieldType.Referer;
	}
}
