package org.sessionization;

import org.hibernate.Transaction;
import org.sessionization.database.HibernateUtil;
import org.sessionization.parser.datastruct.UserSessionAbs;

import java.util.concurrent.BlockingQueue;

public class SaveDataBaseThread extends Thread {

	private static volatile int ThreadNumber = 0;

	private BlockingQueue<UserSessionAbs> queue;
	private HibernateUtil db;
	private HibernateUtil.Operation operation;

	/**
	 * @param group
	 * @param queue
	 * @param db
	 * @param operation
	 */
	public SaveDataBaseThread(ThreadGroup group, BlockingQueue queue, HibernateUtil db, HibernateUtil.Operation operation) {
		super(group, "SaveToDataBaseThread-" + ThreadNumber++);
		this.queue = queue;
		this.db = db;
		this.operation = operation;
	}

	/**
	 *
	 * @param queue
	 * @param db
	 * @param operation
	 */
	public SaveDataBaseThread(BlockingQueue queue, HibernateUtil db, HibernateUtil.Operation operation) {
		this(null, queue, db, operation);
	}

	public SaveDataBaseThread(BlockingQueue queue, HibernateUtil db) {
		this(queue, db, (session, table) -> {
			Integer ret = (Integer) table.setDbId(session);
			Transaction transaction = session.getTransaction();
			try {
				transaction.begin();
				session.saveOrUpdate(table);
				transaction.commit();
			} catch (Exception e) {
				if (transaction != null) {
					transaction.rollback();
				}
				throw e;
			}
			return ret;
		});
	}

	@Override
	public void run() {
		try {
			while (true) {
				consume(queue.take());
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	private void consume(UserSessionAbs session) {
		db.execute(operation, session);
	}
}
