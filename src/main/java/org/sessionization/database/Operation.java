package org.sessionization.database;

import org.hibernate.Session;

@FunctionalInterface
public interface Operation {
	/**
	 * @param session
	 * @param o
	 * @return
	 */
	Object run(Session session, HibernateTable table);
}
