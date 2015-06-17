package spletneseje.fields;

import java.net.MalformedURLException;
import java.net.URL;

public class Referer implements Field {
	
	private URL referer;
	
	public Referer(String url) throws MalformedURLException {
		if(!url.equals("-")) {
			this.referer = new URL(url);
		} else {
			this.referer = null;
		}
	}

	@Override
	public String izpis() {
		return referer != null ? this.referer.getPath() : "-";
	}

	@Override
	public String getKey() {
		return null;
	}

}
