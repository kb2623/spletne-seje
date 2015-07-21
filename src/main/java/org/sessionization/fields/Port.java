package org.sessionization.fields;

import org.oosqljet.annotation.Column;
import org.oosqljet.annotation.Table;

@Table public class Port implements Field {

	@Column(unique = true) private int portNumber;
	@Column private boolean isServer;
	
	public Port(String number, boolean isServer) {
		this.isServer = isServer;
		portNumber = Integer.valueOf(number);
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
