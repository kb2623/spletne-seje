package org.sessionization.fields;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "user_agent")
public class UserAgent implements Field {

	@Column(name = "user_agent_string")
	private String userAgentString;

	public UserAgent() {
		userAgentString = null;
	}

	public UserAgent(String info, LogType type) {
		userAgentString = type.parseUserAgent(info);
	}

	public void setUserAgentString(String userAgentString) {
		this.userAgentString = userAgentString;
	}

	public String getUserAgentString() {
		return userAgentString;
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
		return userAgentString;
	}

	@Override
	public boolean equals(Object o) {
		return o instanceof UserAgent && userAgentString.equals(((UserAgent) o).getUserAgentString());
	}

	@Override
	public FieldType getFieldType() {
		return FieldType.UserAgent;
	}

}
