package org.sessionization.fields;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.net.InetAddress;
import java.net.UnknownHostException;

@Entity
@Table(name = "address")
public class Address implements Field {

	@Column(name = "is_server")
	private boolean serverAddress;

	@Column(name = "address")
	private InetAddress address;

	public Address() {
		serverAddress = false;
		address = null;
	}
	
	public Address(String address, boolean isServerAdderess) throws UnknownHostException {
		this.serverAddress = isServerAdderess;
		this.address = InetAddress.getByName(address);
	}

	public boolean isServerAddress() {
		return serverAddress;
	}

	public void setServerAddress(boolean serverAddress) {
		this.serverAddress = serverAddress;
	}

	public void setAddress(InetAddress address) {
		this.address = address;
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
		return o instanceof Address && serverAddress == ((Address) o).isServerAddress() && address.equals(((Address) o).getAddress());
	}

	@Override
	public FieldType getFieldType() {
		return (serverAddress) ? FieldType.ServerIP : FieldType.ClientIP;
	}
}
