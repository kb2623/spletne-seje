package org.sessionization.fields;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "port")
public class Port implements Field {

	@Column(name = "port_number")
	private int portNumber;

	@Column(name = "is_server")
	private boolean isServer;

	public Port() {
		portNumber = 0;
		isServer = false;
	}

	public Port(String number, boolean isServer) {
		this.isServer = isServer;
		portNumber = Integer.valueOf(number);
	}

	public void setPortNumber(int portNumber) {
		this.portNumber = portNumber;
	}

	public void setIsServer(boolean isServer) {
		this.isServer = isServer;
	}

	public boolean isServer() {
        return isServer;
    }

	public int getPortNumber() {
		return portNumber;
	}

	@Override
	public String izpis() {
		return (isServer ? "Server" : "Client") + " port " + portNumber;
	}

	@Override
	public String toString() {
		return (isServer ? "Server" : "Client") + " port " + portNumber;
	}

	@Override
	public String getKey() {
		if(!isServer()) {
			return String.valueOf(portNumber);
		} else {
			return "";
		}
	}

	@Override
	public boolean equals(Object o) {
		return o instanceof Port && isServer() == ((Port) o).isServer() && portNumber == ((Port) o).getPortNumber();
	}

	@Override
	public FieldType getFieldType() {
		return (isServer) ? FieldType.ServerPort : FieldType.ClientPort;
	}
}
