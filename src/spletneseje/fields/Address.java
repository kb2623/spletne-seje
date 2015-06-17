package spletneseje.fields;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class Address implements Field {
	
	private boolean serverAddress;
	
	private InetAddress address;
	
	public Address(String address, boolean isServerAdderess) throws UnknownHostException {
		this.serverAddress = isServerAdderess;
		this.address = InetAddress.getByName(address);
	}

	@Override
	public String izpis() {
		return (serverAddress ? "Server" : "Client") + " " + address.getHostAddress();
	}

	@Override
	public String getKey() {
		return !serverAddress ? address.getHostAddress() : null;
	}

}