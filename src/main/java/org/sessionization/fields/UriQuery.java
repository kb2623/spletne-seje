package org.sessionization.fields;

import org.datastruct.ClassPool;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Cacheable
public class UriQuery implements LogField {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@OneToMany(cascade = CascadeType.ALL)
	private List<UriQueryPair> pairs;

	public UriQuery() {
		id = null;
		pairs = null;
	}

	public UriQuery(String niz) {
		id = null;
		if (niz != null && niz.charAt(0) != '-') {
			setupMap(niz);
		} else {
			pairs = null;
		}
	}

	private void setupMap(String niz) {
		String[] tab = niz.split("&");
		pairs = new ArrayList<>(tab.length);
		for (String s : tab) {
			String[] pair = s.split("=");
			UriQueryPair tmp = null;
			if (pair.length == 2) {
				tmp = ClassPool.getObject(UriQueryPair.class, pair[0], pair[1]);
			} else {
				tmp = ClassPool.getObject(UriQueryPair.class, pair[0], "-");
			}
			pairs.add(tmp);
		}
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public List<UriQueryPair> getPairs() {
		return pairs;
	}

	public void setPairs(List<UriQueryPair> pairs) {
		this.pairs = pairs;
	}

	@Override
	public String izpis() {
		StringBuilder builder = new StringBuilder();
		builder.append('[');
		if (pairs == null) builder.append('-');
		else pairs.forEach(e -> builder.append('[').append(e.toString()).append(']'));
		return builder.append(']').toString();
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append('[');
		if (pairs != null)
			pairs.forEach(e -> builder.append('[').append(e.toString()).append(']'));
		return builder.append(']').toString();
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		UriQuery uriQuery = (UriQuery) o;
		if (getId() != null ? !getId().equals(uriQuery.getId()) : uriQuery.getId() != null) return false;
		if (getPairs() != null ? !getPairs().equals(uriQuery.getPairs()) : uriQuery.getPairs() != null) return false;
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
class UriQueryPair {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	private String value;

	@OneToOne(cascade = CascadeType.ALL)
	private UriQueryKey key;

	public UriQueryPair() {
		id = null;
		value = null;
		key = null;
	}

	public UriQueryPair(String key, String value) {
		id = null;
		this.value = value;
		this.key = ClassPool.getObject(UriQueryKey.class, key);
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

	public UriQueryKey getKey() {
		return key;
	}

	public void setKey(UriQueryKey key) {
		this.key = key;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		UriQueryPair that = (UriQueryPair) o;
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
class UriQueryKey {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	private String name;

	public UriQueryKey() {
		id = null;
		name = null;
	}

	public UriQueryKey(String name) {
		this.name = name;
		id = null;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		UriQueryKey that = (UriQueryKey) o;
		if (getId() != null ? !getId().equals(that.getId()) : that.getId() != null) return false;
		if (getName() != null ? !getName().equals(that.getName()) : that.getName() != null) return false;
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
