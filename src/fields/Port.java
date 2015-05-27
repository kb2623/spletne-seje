package fields;

public class Port implements Field {
	
	private int portNumber;
	private boolean isSrever;
	
	public Port(String number, boolean isServer) {
		this.isSrever = isServer;
		portNumber = Integer.valueOf(number);
	}

	@Override
	public String izpis() {
		return (isSrever ? "Server" : "Client") + " port " + portNumber;
	}

	@Override
	public String getKey() {
		if(!isSrever) {
			return ""+portNumber;
		} else {
			return null;
		}
	}

}
