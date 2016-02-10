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
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "is_server", discriminatorType = DiscriminatorType.INTEGER)
@Cacheable
public abstract class Address implements LogField, HibernateTable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(nullable = false)
	private InetAddress address;

	public Address() {
		id = null;
		address = null;
	}

	public Address(String address) throws UnknownHostException {
		id = null;
		this.address = InetAddress.getByName(address);
	}

	public Address(InetAddress address) {
		id = null;
		this.address = address;
	}

	public Address(Inet4Address address) {
		id = null;
		this.address = address;
	}

	public Address(Inet6Address address) {
		id = null;
		this.address = address;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public InetAddress getAddress() {
		return address;
	}

	public void setAddress(InetAddress address) {
		this.address = address;
	}

	@Override
	public String izpis() {
		return address.getHostAddress();
	}

	@Override
	public Object setDbId(Session session) {
		if (getId() != null) {
			return getId();
		}
		Query query = session.createQuery("select a.id from " + getClass().getSimpleName() + " as a where a.address = :address");
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
	public boolean equals(Object o) {
		if (o == null || !(o instanceof Address)) return false;
		if (this == o) return true;
		Address that = (Address) o;
		return getAddress() != null ? getAddress().equals(that.getAddress()) : that.getAddress() == null;
	}

	@Override
	public int hashCode() {
		int result = getId() != null ? getId().hashCode() : 0;
		result = 31 * result + (getAddress() != null ? getAddress().hashCode() : 0);
		return result;
	}

	@Override
	public String toString() {
		return "id=" + id +
				", address=" + address;
	}
}
