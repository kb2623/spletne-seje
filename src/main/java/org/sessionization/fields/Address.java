package org.sessionization.fields;

import org.oosqljet.annotation.Column;
import org.oosqljet.annotation.Table;

import java.net.InetAddress;
import java.net.UnknownHostException;

@Table public class Address implements Field {

	@Column private boolean serverAddress;
	@Column private InetAddress address;
	
	public Address(String address, boolean isServerAdderess) throws UnknownHostException {
		this.serverAddress = isServerAdderess;
		this.address = InetAddress.getByName(address);
	}

    public boolean isServer() {
        return serverAddress;
    }

	public InetAddress getAddress() {
		return address;
	}

	@Override
	public String izpis() {
		return (serverAddress ? "Server" : "Client") + " " + address.getHostAddress();
	}

	@Override
	public String toString() {
		return (serverAddress ? "Server" : "Client") + " " + address.getHostAddress();
	}

	@Override
	public String getKey() {
		return !serverAddress ? address.getHostAddress() : "";
	}

	@Override
	public boolean equals(Object o) {
		return o instanceof Address && serverAddress == ((Address) o).isServer() && address.equals(((Address) o).getAddress());
	}

	@Override
	public FieldType getFieldType() {
		return (serverAddress) ? FieldType.ServerIP : FieldType.ClientIP;
	}
}