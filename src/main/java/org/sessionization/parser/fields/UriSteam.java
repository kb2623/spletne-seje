package org.sessionization.parser.fields;

import org.sessionization.HibernateTable;
import org.sessionization.parser.LogField;

import javax.persistence.*;

@Entity
@Cacheable
public class UriSteam implements LogField, HibernateTable, Resource {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(unique = true, nullable = false)
	private String file;

	public UriSteam() {
		id = null;
		file = null;
	}

	public UriSteam(String file) {
		id = null;
		setFile(file);
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@Override
	public String getFile() {
		return file != null ? file : "";
	}

	public void setFile(String file) {
		if (file.equals("-")) {
			this.file = null;
		} else {
			this.file = file;
		}
	}



	public String izpis() {
		return getFile();
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		UriSteam uriSteam = (UriSteam) o;
		if (getId() != null ? !getId().equals(uriSteam.getId()) : uriSteam.getId() != null) return false;
		if (!getFile().equals(uriSteam.getFile())) return false;
		return true;
	}

	@Override
	public int hashCode() {
		int result = getId() != null ? getId().hashCode() : 0;
		result = 31 * result + getFile().hashCode();
		return result;
	}

	@Override
	public String toString() {
		return getFile();
	}

	@Override
	public String getIdQuery() {
		return "select u.id form " + getClass().getSimpleName() + " u where u.file = '" + file + "'";
	}
}
