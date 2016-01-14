package org.sessionization.parser.fields;

import org.hibernate.Session;
import org.sessionization.database.HibernateUtil;
import org.sessionization.parser.LogField;

import javax.persistence.*;
import java.net.Inet4Address;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.UnknownHostException;

@Entity
@Cacheable
public class Address implements LogField, HibernateUtil.HibernateTable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(name = "is_server")
	private boolean serverAddress;

	@Column(unique = true, nullable = false)
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
	public String toString() {
		return (serverAddress ? "Server" : "Client") + " " + address.getHostAddress();
	}

	@Override
	public String getKey() {
		return !serverAddress ? address.getHostAddress() : "";
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Address address1 = (Address) o;
		if (isServerAddress() != address1.isServerAddress()) return false;
		if (getId() != null ? !getId().equals(address1.getId()) : address1.getId() != null) return false;
		if (getAddress() != null ? !getAddress().equals(address1.getAddress()) : address1.getAddress() != null)
			return false;
		return true;
	}

	@Override
	public int hashCode() {
		int result = getId() != null ? getId().hashCode() : 0;
		result = 31 * result + (isServerAddress() ? 1 : 0);
		result = 31 * result + (getAddress() != null ? getAddress().hashCode() : 0);
		return result;
	}

	//	return "select a.id from " + getClass().getSimpleName() + " where a.serverAddress = '" + InetAddressConverter.getInetAddressString(address) + "' and s.serverAddress = " + String.valueOf(serverAddress);
	@Override
	public Object setDbId(Session session) {
		return null;
	}
}
