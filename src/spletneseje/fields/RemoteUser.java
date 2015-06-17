package spletneseje.fields;

public class RemoteUser implements Field {
	
	private String user;
	
	public RemoteUser(String user) {
		if(!user.equalsIgnoreCase("-")) {
			this.user = user;
		}
	}

	@Override
	public String izpis() {
		return (user != null) ? user : "-";
	}

	@Override
	public String getKey() {
		return user;
	}

}
