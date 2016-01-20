package org.sessionization;

import org.sessionization.database.HibernateUtil;
import org.sessionization.parser.datastruct.UserSessionAbs;

import java.util.concurrent.BlockingQueue;

public class TSaveDb extends Thread {

	private BlockingQueue<UserSessionAbs> queue;
	private HibernateUtil db;
	private HibernateUtil.Operation operation;

	public TSaveDb(BlockingQueue queue, HibernateUtil db, HibernateUtil.Operation operation) {
		super();
		this.queue = queue;
		this.db = db;
		this.operation = operation;
	}

	public TSaveDb(BlockingQueue queue, HibernateUtil db) {
		this(queue, db, (session, table) -> {
			Integer ret = (Integer) table.setDbId(session);
			try {
				session.getTransaction().begin();
				session.saveOrUpdate(table);
				session.getTransaction().commit();
			} catch (Exception e) {
				session.getTransaction().rollback();
				throw e;
			}
			return ret;
		});
	}

	@Override
	public void run() {
		UserSessionAbs session = null;
		try {
			while (true) {
				session = queue.take();
				if (session != null) {
					db.execute(operation, session);
				} else {
					break;
				}
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
