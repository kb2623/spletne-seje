package org.sessionization.parser.datastruct;

import org.sessionization.fields.Address;
import org.sessionization.fields.Field;
import org.sessionization.fields.RemoteUser;
import org.sessionization.fields.ncsa.RemoteHost;
import org.sessionization.fields.ncsa.RemoteLogname;

import javax.persistence.*;
import java.util.LinkedList;
import java.util.List;

@Entity
public class CLFclass extends WebPageRequestAbs {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@OneToOne(cascade = CascadeType.ALL)
	private RemoteHost remoteHost;

	@OneToOne(cascade = CascadeType.ALL)
	private RemoteLogname remoteLogname;

	@OneToOne(cascade = CascadeType.ALL)
	private RemoteUser remoteUser;

	@OneToMany(cascade = CascadeType.ALL)
	private List<Request> requests;

	public CLFclass() {
		id = null;
		remoteHost = null;
		remoteLogname = null;
		remoteUser = null;
		requests = null;
	}

	public CLFclass(ParsedLine line) {
		id = null;
		for (Field f : line) {
			if (f instanceof RemoteHost) {
				remoteHost = (RemoteHost) f;
			} else if (f instanceof RemoteLogname) {
				remoteLogname = (RemoteLogname) f;
			} else if (f instanceof RemoteUser) {
				remoteUser = (RemoteUser) f;
			}
		}
		requests = new LinkedList<>();
		requests.add(new Request(line));
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public RemoteHost getRemoteHost() {
		return remoteHost;
	}

	public void setRemoteHost(RemoteHost remoteHost) {
		this.remoteHost = remoteHost;
	}

	public RemoteLogname getRemoteLogname() {
		return remoteLogname;
	}

	public void setRemoteLogname(RemoteLogname remoteLogname) {
		this.remoteLogname = remoteLogname;
	}

	public RemoteUser getRemoteUser() {
		return remoteUser;
	}

	public void setRemoteUser(RemoteUser remoteUser) {
		this.remoteUser = remoteUser;
	}

	public List<Request> getRequests() {
		return requests;
	}

	public void setRequests(List<Request> requests) {
		this.requests = requests;
	}

	@Override
	public boolean add(ParsedLine line) {
		if (line.isResource()) {
			return requests.add(new Request(line));
		} else {
			return false;
		}
	}

	@Override
	public String getKey() {
		return remoteHost.getKey() + remoteLogname.getKey() + remoteUser.getKey();
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		CLFclass clFclass = (CLFclass) o;
		if (getId() != null ? !getId().equals(clFclass.getId()) : clFclass.getId() != null) return false;
		if (getRemoteHost() != null ? !getRemoteHost().equals(clFclass.getRemoteHost()) : clFclass.getRemoteHost() != null)
			return false;
		if (getRemoteLogname() != null ? !getRemoteLogname().equals(clFclass.getRemoteLogname()) : clFclass.getRemoteLogname() != null)
			return false;
		if (getRemoteUser() != null ? !getRemoteUser().equals(clFclass.getRemoteUser()) : clFclass.getRemoteUser() != null)
			return false;
		if (getRequests() != null ? !getRequests().equals(clFclass.getRequests()) : clFclass.getRequests() != null)
			return false;
		return true;
	}

	@Override
	public int hashCode() {
		int result = getId() != null ? getId().hashCode() : 0;
		result = 31 * result + (getRemoteHost() != null ? getRemoteHost().hashCode() : 0);
		result = 31 * result + (getRemoteLogname() != null ? getRemoteLogname().hashCode() : 0);
		result = 31 * result + (getRemoteUser() != null ? getRemoteUser().hashCode() : 0);
		result = 31 * result + (getRequests() != null ? getRequests().hashCode() : 0);
		return result;
	}
}
