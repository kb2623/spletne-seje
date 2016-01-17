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

	@Override
	public void run() {
		UserSessionAbs session = null;
		try {
			session = queue.take();
			db.execute(operation, session);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
