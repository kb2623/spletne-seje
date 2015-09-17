package org.sessionization.parser.datastruct;

import org.sessionization.fields.Field;
import org.sessionization.fields.StatusCode;
import org.sessionization.fields.ncsa.DateTime;
import org.sessionization.fields.ncsa.RequestLine;
import org.sessionization.fields.ncsa.SizeOfTransfer;

import javax.persistence.*;

@Entity
public class Request {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Embedded
	private DateTime dateTime;

	@OneToOne(cascade = CascadeType.ALL)
	private RequestLine requestLine;

	@Embedded
	private StatusCode statusCode;

	@Embedded
	private SizeOfTransfer sizeOfTransfer;

	public Request() {
		id = null;
		dateTime = null;
		requestLine = null;
		statusCode = null;
		sizeOfTransfer = null;
	}

	public Request(ParsedLine line) {
		id = null;
		for (Field f : line) {
			if (f instanceof DateTime) {
				dateTime = (DateTime) f;
			} else if (f instanceof RequestLine) {
				requestLine = (RequestLine) f;
			} else if (f instanceof StatusCode) {
				statusCode = (StatusCode) f;
			} else if (f instanceof SizeOfTransfer) {
				sizeOfTransfer = (SizeOfTransfer) f;
			}
		}
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public DateTime getDateTime() {
		return dateTime;
	}

	public void setDateTime(DateTime dateTime) {
		this.dateTime = dateTime;
	}

	public RequestLine getRequestLine() {
		return requestLine;
	}

	public void setRequestLine(RequestLine requestLine) {
		this.requestLine = requestLine;
	}

	public StatusCode getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(StatusCode statusCode) {
		this.statusCode = statusCode;
	}

	public SizeOfTransfer getSizeOfTransfer() {
		return sizeOfTransfer;
	}

	public void setSizeOfTransfer(SizeOfTransfer sizeOfTransfer) {
		this.sizeOfTransfer = sizeOfTransfer;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Request request = (Request) o;
		if (getId() != null ? !getId().equals(request.getId()) : request.getId() != null) return false;
		if (getDateTime() != null ? !getDateTime().equals(request.getDateTime()) : request.getDateTime() != null)
			return false;
		if (getRequestLine() != null ? !getRequestLine().equals(request.getRequestLine()) : request.getRequestLine() != null)
			return false;
		if (getStatusCode() != null ? !getStatusCode().equals(request.getStatusCode()) : request.getStatusCode() != null)
			return false;
		if (getSizeOfTransfer() != null ? !getSizeOfTransfer().equals(request.getSizeOfTransfer()) : request.getSizeOfTransfer() != null)
			return false;
		return true;
	}

	@Override
	public int hashCode() {
		int result = getId() != null ? getId().hashCode() : 0;
		result = 31 * result + (getDateTime() != null ? getDateTime().hashCode() : 0);
		result = 31 * result + (getRequestLine() != null ? getRequestLine().hashCode() : 0);
		result = 31 * result + (getStatusCode() != null ? getStatusCode().hashCode() : 0);
		result = 31 * result + (getSizeOfTransfer() != null ? getSizeOfTransfer().hashCode() : 0);
		return result;
	}
}
