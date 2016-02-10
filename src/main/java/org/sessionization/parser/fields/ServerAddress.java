package org.sessionization.parser.fields;

import javax.persistence.Cacheable;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import java.net.Inet4Address;
import java.net.Inet6Address;
import java.net.InetAddress;

@Entity
@Cacheable
@DiscriminatorValue("1")
public class ServerAddress extends Address {

	public ServerAddress() {
		super();
	}

	public ServerAddress(InetAddress address) {
		super(address);
	}

	public ServerAddress(Inet4Address address) {
		super(address);
	}

	public ServerAddress(Inet6Address address) {
		super(address);
	}

	@Override
	public String izpis() {
		return "Server " + getAddress().getHostAddress();
	}

	@Override
	public boolean equals(Object o) {
		if (o == null || !(o instanceof ServerAddress)) return false;
		if (this == o) return true;
		return super.equals(o);
	}

	@Override
	public int hashCode() {
		return super.hashCode() + 31;
	}

	@Override
	public String toString() {
		return "Address{" +
				super.toString() +
				"serverAddress=true}";
	}
}
