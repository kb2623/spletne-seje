package fields;

public class UserAgent implements Field {
	
	private String userAgentString;
	
	public UserAgent(String info, UserAgent.Type type) {
		switch (type) {
		case NCSA:
			userAgentString = info;
			break;
		case W3C:
			userAgentString = info.replace('+', ' ');
			break;
		}
	}
	
	public String getUserAgentString() {
		return userAgentString;
	}

	@Override
	public String izpis() {
		return userAgentString;
	}
	
	@Override
	public String getKey() {
		return userAgentString;
	}
	
	public enum Type {
		NCSA,
		W3C
	}

}
