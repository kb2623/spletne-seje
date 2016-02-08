package org.sessionization.parser.fields;

import org.hibernate.Query;
import org.hibernate.Session;
import org.sessionization.database.HibernateTable;
import org.sessionization.parser.LogField;

import javax.persistence.*;

@Entity
@Cacheable
public class Port implements LogField, HibernateTable {

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
	public Object setDbId(Session session) {
		if (getId() != null) {
			return getId();
		}
		Query query = session.createQuery("select p.id from " + getClass().getSimpleName() + " as p where p.portNumber = " + getPortNumber() + " and p.isServer = " + isServer());
		for (Object o : query.list()) {
			if (equals(session.load(getClass(), (Integer) o))) {
				setId((Integer) o);
				return o;
			}
		}
		return null;
	}

	@Override
	public boolean equals(Object o) {
		if (o == null || !(o instanceof Port)) return false;
		if (this == o) return true;
		Port that = (Port) o;
		return getPortNumber() == that.getPortNumber()
				&& isServer() == that.isServer();
	}

	@Override
	public int hashCode() {
		int result = getId() != null ? getId().hashCode() : 0;
		result = 31 * result + getPortNumber();
		result = 31 * result + (isServer() ? 1 : 0);
		return result;
	}

	@Override
	public String toString() {
		return "Port{" +
				"id=" + id +
				", portNumber=" + portNumber +
				", isServer=" + isServer +
				'}';
	}
}
