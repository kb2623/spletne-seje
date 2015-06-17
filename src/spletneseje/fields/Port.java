package spletneseje.fields;

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
	public String toString() {
		return (isSrever ? "Server" : "Client") + " port " + portNumber;
	}

	@Override
	public String getKey() {
		if(!isSrever) {
			return String.valueOf(portNumber);
		} else {
			return null;
		}
	}

}
