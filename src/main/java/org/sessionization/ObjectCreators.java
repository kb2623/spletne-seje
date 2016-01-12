package org.sessionization;

import org.datastruct.ObjectPool;
import org.sessionization.parser.LogType;
import org.sessionization.parser.fields.*;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class ObjectCreators {

	public ObjectPool.ObjectCreator<Cookie> getCookieCretor() {
		return (pool, args) -> {
			if (args.length < 2) {
				return null;
			}
			LogType parser = (LogType) args[1];
			String line = (String) args[0];
			Cookie cookie = new Cookie();
			String tab[] = parser.parseCooki(line).split(" ");
			List list = new ArrayList<>();
			CookiePair pair;
			for (String s : tab) {
				int mid = s.indexOf("=");
				String sKey;
				String value;
				if (mid == s.length() - 1) {
					value = "-";
					sKey = s.substring(0, mid);
				} else {
					value = s.substring(mid + 1);
					sKey = s.substring(0, mid);
				}
				pair = pool.getObject(CookiePair.class, sKey, value);
				list.add(pair);
			}
			cookie.setPairs(list);
			return cookie;
		};
	}

	public ObjectPool.ObjectCreator<CookiePair> getCookieParirCreator() {
		return (pool, args) -> {
			CookiePair pair = new CookiePair();
			CookieKey key = pool.getObject(CookieKey.class, args[0]);
			pair.setKey(key);
			pair.setValue((String) args[1]);
			return pair;
		};
	}

	public ObjectPool.ObjectCreator<CookieKey> getCookieKeyCreator() {
		return (pool, args) -> {
			CookieKey key = new CookieKey();
			key.setName((String) args[0]);
			return key;
		};
	}

	public ObjectPool.ObjectCreator<UriQuery> getUriQueryCreator() {
		return (pool, args) -> {
			if (args.length < 2) {
				return null;
			}
			LogType parser = (LogType) args[1];
			String line = (String) args[0];
			UriQuery query = new UriQuery();
			List list = new ArrayList<>();
			// TODO: 1/13/16
			return null;
		};
	}

	public ObjectPool.ObjectCreator<UriQueryPair> getUriQueryPairCreator() {
		return (pool, args) -> {
			UriQueryPair pair = new UriQueryPair();
			UriQueryKey key = pool.getObject(UriQueryKey.class, args[0]);
			pair.setKey(key);
			pair.setValue((String) args[1]);
			return pair;
		};
	}

	public ObjectPool.ObjectCreator<UriQueryKey> getUriQueryKeyCreator() {
		return (pool, args) -> {
			UriQueryKey key = new UriQueryKey();
			key.setName((String) args[0]);
			return key;
		};
	}

}
