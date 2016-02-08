package org.sessionization.database;

import org.hibernate.Session;

public interface HibernateTable {
	/**
	 * @param session
	 * @return
	 */
	Object setDbId(Session session);
}
