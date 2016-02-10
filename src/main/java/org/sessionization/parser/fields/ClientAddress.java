package org.sessionization.parser.fields;

import javax.persistence.Cacheable;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import java.net.Inet4Address;
import java.net.Inet6Address;
import java.net.InetAddress;

@Entity
@Cacheable
@DiscriminatorValue("0")
public class ClientAddress extends Address {

	public ClientAddress() {
		super();
	}

	public ClientAddress(InetAddress address) {
		super(address);
	}

	public ClientAddress(Inet4Address address) {
		super(address);
	}

	public ClientAddress(Inet6Address address) {
		super(address);
	}

	@Override
	public String getKey() {
		return getAddress().getHostAddress();
	}

	@Override
	public String izpis() {
		return "Client " + getAddress().getHostAddress();
	}

	@Override
	public boolean equals(Object o) {
		if (o == null || !(o instanceof ClientAddress)) return false;
		if (this == o) return true;
		return super.equals(o);
	}

	@Override
	public String toString() {
		return "Address{" +
				super.toString() +
				"serverAddress=false}";
	}
}
