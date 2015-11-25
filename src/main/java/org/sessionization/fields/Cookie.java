package org.sessionization.fields;

import org.datastruct.ClassPool;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Cacheable
public class Cookie implements LogField {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@OneToMany(cascade = CascadeType.ALL)
	private List<CookiePair> pairs;

	public Cookie(String line, LogType type) throws IllegalArgumentException {
		id = null;
		if (!line.equals("-")) {
			String[] tab = type.parseCooki(line).split(" ");
			pairs = new ArrayList<>(tab.length);
			for (String s : tab) {
				int indexOf = s.indexOf('=');
				CookiePair tmp = null;
				if (indexOf == s.length() - 1) {
					tmp = ClassPool.getObject(CookiePair.class, s.substring(0, indexOf), "-");
				} else {
					tmp = ClassPool.getObject(CookiePair.class, s.substring(0, indexOf), s.substring(indexOf + 1));
				}
				pairs.add(tmp);
			}
		} else {
			pairs = null;
		}
	}

	public static String patteren() {
		return "([^ \\\"\\[\\{\\(\\]\\}\\)<>/\\\\?=@,;:]+=[\\x21\\x23-\\x2B\\x2D-\\x3A\\x3C-\\x5B\\x5D-\\x7E]*;)*([^ \\\"\\[\\{\\(\\]\\}\\)<>/\\\\?=@,;:]+=[\\x21\\x23-\\x2B\\x2D-\\x3A\\x3C-\\x5B\\x5D-\\x7E]*){1}";
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public List<CookiePair> getPairs() {
		return pairs;
	}

	public void setPairs(List<CookiePair> pairs) {
		this.pairs = pairs;
	}

	@Override
	public String izpis() {
		StringBuilder builder = new StringBuilder();
		builder.append('[');
		if (pairs == null) {
			builder.append('-');
		} else {
			pairs.forEach(e -> builder.append('[').append(e.toString()).append(']'));
		}
		return builder.append(']').toString();
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append('[');
		if (pairs == null) {
			builder.append('-');
		} else {
			pairs.forEach(e -> builder.append('[').append(e.toString()).append(']'));
		}
		return builder.append(']').toString();
	}

	@Override
	public String getKey() {
		if (pairs == null) return "-";
		else {
			StringBuilder builder = new StringBuilder();
			pairs.forEach(e -> builder.append(e.toString()));
			return builder.toString();
		}
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Cookie cookie = (Cookie) o;
		if (getId() != null ? !getId().equals(cookie.getId()) : cookie.getId() != null) return false;
		if (getPairs() != null ? !getPairs().equals(cookie.getPairs()) : cookie.getPairs() != null) return false;
		return true;
	}

	@Override
	public int hashCode() {
		int result = getId() != null ? getId().hashCode() : 0;
		result = 31 * result + (getPairs() != null ? getPairs().hashCode() : 0);
		return result;
	}
}

@Entity
@Cacheable
class CookiePair {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	private String value;

	@OneToOne(cascade = CascadeType.ALL)
	private CookieKey key;

	public CookiePair() {
		id = null;
		value = null;
		key = null;
	}

	public CookiePair(String key, String value) {
		id = null;
		this.value = value;
		this.key = ClassPool.getObject(CookieKey.class, key);
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public CookieKey getKey() {
		return key;
	}

	public void setKey(CookieKey key) {
		this.key = key;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		CookiePair that = (CookiePair) o;

		if (getId() != null ? !getId().equals(that.getId()) : that.getId() != null) return false;
		if (getValue() != null ? !getValue().equals(that.getValue()) : that.getValue() != null) return false;
		if (getKey() != null ? !getKey().equals(that.getKey()) : that.getKey() != null) return false;

		return true;
	}

	@Override
	public int hashCode() {
		int result = getId() != null ? getId().hashCode() : 0;
		result = 31 * result + (getValue() != null ? getValue().hashCode() : 0);
		result = 31 * result + (getKey() != null ? getKey().hashCode() : 0);
		return result;
	}

	@Override
	public String toString() {
		return key.toString() + " = " + value;
	}
}

@Entity
@Cacheable
class CookieKey {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	private String name;

	public CookieKey() {
		id = null;
		name = null;
	}

	public CookieKey(String name) {
		this.name = name;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		CookieKey cookieKey = (CookieKey) o;

		if (getId() != null ? !getId().equals(cookieKey.getId()) : cookieKey.getId() != null) return false;
		if (getName() != null ? !getName().equals(cookieKey.getName()) : cookieKey.getName() != null) return false;

		return true;
	}

	@Override
	public int hashCode() {
		int result = getId() != null ? getId().hashCode() : 0;
		result = 31 * result + (getName() != null ? getName().hashCode() : 0);
		return result;
	}

	@Override
	public String toString() {
		return name;
	}
}