package spletneseje.fields;

public class UserAgent implements Field {
	
	private String userAgentString;
	
	public UserAgent(String info, UserAgent.Type type) {
		switch (type) {
		case NCSA:
			userAgentString = info;
			break;
		case W3C:
			StringBuilder builder = new StringBuilder();
			for (int i = 0; i < info.length(); i++) {
				if (info.charAt(i) == '+') {
					if (i > 0 && info.charAt(i - 1) != '+') builder.append(' ');
					else builder.append('+');
				}
				else builder.append(info.charAt(i));
			}
			userAgentString = builder.toString();
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
	public String toString() {
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
