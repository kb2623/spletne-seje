package org.spletneseje.fields;

import org.spletneseje.database.annotation.Entry;

import java.net.MalformedURLException;
import java.net.URL;

public class Referer extends File {
	
	private URL uri;
	
	public Referer(String url) throws MalformedURLException {
		if(!url.equals("-")) {
			uri = new URL(url);
		} else {
			uri = null;
		}
	}

	public URL getUri() {
		return uri;
	}

	@Override
	public String izpis() {
		return (uri == null) ? "-" : uri.getPath() + (uri.getQuery() != null ? uri.getQuery() : "");
	}

	@Override
	public String getKey() {
		return uri != null ? uri.getFile() : "";
	}

	@Override
	public FieldType getFieldType() {
		return FieldType.Referer;
	}

	@Override
	public String getExtension() {
		int indexOfExtension = uri.getPath().lastIndexOf('.');
		int indexOfLastSeparator = uri.getPath().lastIndexOf('/');
		return (indexOfExtension < indexOfLastSeparator) ? null : uri.getPath().substring(indexOfExtension+1);
	}

	@Override
	@Entry public UriQuery getQuery() {
		if (uri == null) return null;
		return new UriQuery(uri.getQuery());
	}

	@Override
	@Entry public String file() {
		return uri != null ? uri.getFile() : "";
	}

	@Override
	public boolean isResource() {
		return false;
	}

	@Override
	public String toString() {
		return (uri == null) ? "-" : uri.getPath() + uri.getQuery();
	}

	@Override
	public boolean equals(Object o) {
		return o instanceof Referer && (uri == null) ? ((Referer) o).getUri() == null : uri.equals(((Referer) o).getUri());
	}

}
