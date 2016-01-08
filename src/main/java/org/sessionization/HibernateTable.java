package org.sessionization;

public interface HibernateTable {

	default String getIdQuery() {
		return null;
	}

}
