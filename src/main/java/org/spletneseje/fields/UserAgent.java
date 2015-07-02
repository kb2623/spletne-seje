package org.spletneseje.fields;

import org.spletneseje.database.annotation.Entry;
import org.spletneseje.database.annotation.Table;

@Table public class UserAgent implements Field {

	@Entry private String userAgentString;
	
	public UserAgent(String info, LogType type) {
		userAgentString = type.parseUserAgent(info);
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
