package org.sessionization.parser.fields;

import org.hibernate.Session;
import org.sessionization.database.HibernateUtil;
import org.sessionization.parser.LogField;

import javax.persistence.*;

@Entity
@Cacheable
public class Port implements LogField, HibernateUtil.HibernateTable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(name = "port_number", nullable = false)
	private int portNumber;

	@Column(name = "server_port", nullable = false)
	private boolean isServer;

	public Port() {
		id = null;
		portNumber = 0;
		isServer = false;
	}

	public Port(String number, Boolean isServer) {
		id = null;
		this.isServer = isServer;
		portNumber = Integer.valueOf(number);
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
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

	public void setPortNumber(int portNumber) {
		this.portNumber = portNumber;
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
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Port port = (Port) o;
		if (getPortNumber() != port.getPortNumber()) return false;
		if (isServer() != port.isServer()) return false;
		if (getId() != null ? !getId().equals(port.getId()) : port.getId() != null) return false;
		return true;
	}

	@Override
	public int hashCode() {
		int result = getId() != null ? getId().hashCode() : 0;
		result = 31 * result + getPortNumber();
		result = 31 * result + (isServer() ? 1 : 0);
		return result;
	}

	//	return "selest p.id from " + getClass().getSimpleName() + " p where p.portNumber = " + portNumber + " and p.isServer = " + String.valueOf(isServer);
	@Override
	public Object setDbId(Session session) {
		// TODO: 1/14/16
		return null;
	}
}
