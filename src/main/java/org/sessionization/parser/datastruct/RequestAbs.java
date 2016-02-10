package org.sessionization.parser.datastruct;

import org.sessionization.TimePoint;
import org.sessionization.database.HibernateTable;
import org.sessionization.parser.fields.*;
import org.sessionization.parser.fields.ncsa.*;
import org.sessionization.parser.fields.w3c.*;

import javax.persistence.*;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "type", discriminatorType = DiscriminatorType.INTEGER)
@Cacheable
@Table(name = "Request")
public abstract class RequestAbs implements TimePoint, HibernateTable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	public RequestAbs() {
		this.id = null;
	}

	public RequestAbs(ParsedLine line) {
		this();
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public RequestLine getRequestLine() {
		return null;
	}

	public StatusCode getStatusCode() {
		return null;
	}

	public SizeOfResponse getSizeOfResponse() {
		return null;
	}

	public SizeOfRequest getSizeOfRequest() {
		return null;
	}

	public SizeOfTransfer getSizeOfTransfer() {
		return null;
	}

	public Referer getReferer() {
		return null;
	}

	public Method getMethod() {
		return null;
	}

	public DateTime getDateTime() {
		return null;
	}

	public Date getDate() {
		return null;
	}

	public Time getTime() {
		return null;
	}

	public Port getPort() {
		return null;
	}

	public ServerAddress getAddress() {
		return null;
	}

	public TimeTaken getTimeTaken() {
		return null;
	}

	public SubStatus getSubStatus() {
		return null;
	}

	public Win32Status getWin32Status() {
		return null;
	}

	public Host getHost() {
		return null;
	}

	public Protocol getProtocol() {
		return null;
	}

	public SiteName getSiteName() {
		return null;
	}

	public ComputerName getComputerName() {
		return null;
	}

	public UriQuery getUriQuery() {
		return null;
	}

	public UriSteam getUriSteam() {
		return null;
	}

	public KeepAliveNumber getKeepAliveNumber() {
		return null;
	}

	public ConnectionStatus getConnectionStatus() {
		return null;
	}

	public ProcessID getProcessID() {
		return null;
	}

	@Override
	public boolean equals(Object o) {
		if (o == null || !(o instanceof RequestAbs)) return false;
		return true;
	}

	@Override
	public int hashCode() {
		return getId() != null ? getId().hashCode() : 0;
	}
}
