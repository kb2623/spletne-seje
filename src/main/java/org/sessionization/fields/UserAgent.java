package org.sessionization.fields;

import javax.persistence.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

@Entity
@Cacheable
public class UserAgent implements Field {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(name = "string")
	private String userAgentString;

	public UserAgent() {
		id = null;
		userAgentString = null;
	}

	public UserAgent(String info, LogType type) {
		id = null;
		userAgentString = type.parseUserAgent(info);
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public void setUserAgentString(String userAgentString) {
		this.userAgentString = userAgentString;
	}

	public String getUserAgentString() {
		return userAgentString != null ? userAgentString : "";
	}

	public boolean isCrawler() {
		// todo
		return false;
	}

	@Override
	public String izpis() {
		return userAgentString;
	}

	@Override
	public String toString() {
		return userAgentString;
	}

	@Override
	public String getKey() {
		return userAgentString != null ? userAgentString : "-";
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		UserAgent userAgent = (UserAgent) o;
		if (getId() != null ? !getId().equals(userAgent.getId()) : userAgent.getId() != null) return false;
		if (!getUserAgentString().equals(userAgent.getUserAgentString())) return false;
		return true;
	}

	@Override
	public int hashCode() {
		int result = getId() != null ? getId().hashCode() : 0;
		result = 31 * result + getUserAgentString().hashCode();
		return result;
	}
}
