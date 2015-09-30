package org.sessionization;

import java.net.URL;
import java.net.URLClassLoader;

public class UrlLoader extends URLClassLoader {

	public UrlLoader(URL[] urls) {
		super(urls);
	}

	public synchronized void defineClass(String name, byte[] bytes) {
		super.defineClass(name, bytes, 0, bytes.length);
	}

}
