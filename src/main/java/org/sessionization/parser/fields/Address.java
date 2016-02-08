package org.sessionization.parser.fields;

import org.hibernate.Query;
import org.hibernate.Session;
import org.sessionization.database.HibernateTable;
import org.sessionization.parser.LogField;

import javax.persistence.*;
import java.net.Inet4Address;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.UnknownHostException;

@Entity
@Cacheable
public class Address implements LogField, HibernateTable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(name = "is_server")
	private boolean serverAddress;

	@Column(nullable = false)
	private InetAddress address;

	public Address() {
		id = null;
		serverAddress = false;
		address = null;
	}

	public Address(String address, Boolean isServerAdderess) throws UnknownHostException {
		id = null;
		this.serverAddress = isServerAdderess;
		this.address = InetAddress.getByName(address);
	}

	public Address(Inet4Address address, Boolean isServerAddress) {
		id = null;
		this.serverAddress = isServerAddress;
		this.address = address;
	}

	public Address(Inet6Address address, Boolean isServerAddress) {
		id = null;
		this.address = address;
		this.serverAddress = isServerAddress;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public boolean isServerAddress() {
		return serverAddress;
	}

	public void setServerAddress(boolean serverAddress) {
		this.serverAddress = serverAddress;
	}

	public InetAddress getAddress() {
		return address;
	}

	public void setAddress(InetAddress address) {
		this.address = address;
	}

	@Override
	public String izpis() {
		return (serverAddress ? "Server" : "Client") + " " + address.getHostAddress();
	}

	@Override
	public Object setDbId(Session session) {
		if (getId() != null) {
			return getId();
		}
		Query query = session.createQuery("select a.id from " + getClass().getSimpleName() + " as a where a.serverAddress = " + isServerAddress() + " and a.address = :address");
		query.setParameter("address", getAddress());
		for (Object o : query.list()) {
			if (equals(session.load(getClass(), (Integer) o))) {
				setId((Integer) o);
				return o;
			}
		}
		return null;
	}

	@Override
	public String getKey() {
		return !serverAddress ? address.getHostAddress() : "";
	}

	@Override
	public boolean equals(Object o) {
		if (o == null || !(o instanceof Address)) return false;
		if (this == o) return true;
		Address that = (Address) o;
		return isServerAddress() == that.isServerAddress()
				&& getAddress() != null ? getAddress().equals(that.getAddress()) : that.getAddress() == null;
	}

	@Override
	public int hashCode() {
		int result = getId() != null ? getId().hashCode() : 0;
		result = 31 * result + (isServerAddress() ? 1 : 0);
		result = 31 * result + (getAddress() != null ? getAddress().hashCode() : 0);
		return result;
	}

	@Override
	public String toString() {
		return "Address{" +
				"id=" + id +
				", serverAddress=" + serverAddress +
				", address=" + address +
				'}';
	}
}
