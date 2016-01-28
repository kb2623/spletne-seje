package org.sessionization;

import org.datastruct.RadixTreeMap;
import org.datastruct.concurrent.SharedBinomialQueue;
import org.sessionization.parser.datastruct.ParsedLine;
import org.sessionization.parser.datastruct.UserSessionAbs;
import org.sessionization.parser.datastruct.UserSessionDump;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

public class TimeSortThread extends Thread {

	private static volatile boolean run = true;
	private static volatile int ThreadNumber = 0;
	private static volatile int sessionLength = 0;

	private static ClassLoader loader = null;
	private static Queue<QueueElement> queue = new SharedBinomialQueue<>(
			(o1, o2) -> {
				return (int) o1.minus(o2);
			}
	);

	private BlockingQueue<ParsedLine> qParser;
	private BlockingQueue<UserSessionAbs> qSession;
	private Map<String, UserSessionAbs> map;

	/**
	 * Konstruktor za ustvarjanje niti, ki sortira seje po casu.
	 * Uporabi ta konstruktor, ce zelis izvajati casovno sortiranje na vecih nitih
	 *
	 * @param group
	 * @param qParser
	 * @param qSession
	 * @param map
	 * @param sessionLength
	 */
	public TimeSortThread(ThreadGroup group, ClassLoader loader, BlockingQueue qParser, BlockingQueue qSession, Map map, int sessionLength) {
		super(group, "TimeSortingThread-" + ThreadNumber++);
		this.qParser = qParser;
		this.qSession = qSession;
		this.map = map;
		if (TimeSortThread.sessionLength == 0) {
			TimeSortThread.sessionLength = sessionLength;
		}
		if (TimeSortThread.loader == null) {
			TimeSortThread.loader = loader;
		}
	}

	/**
	 * Kostruktor za ustarjanje niti, ki sortira seje po casu.
	 * Ta konsruktor uporabi ce izvajas casovno urejanje samo na eni niti.
	 *
	 * @param qParser
	 * @param qSession
	 * @param sessionLength
	 */
	public TimeSortThread(ClassLoader loader, BlockingQueue qParser, BlockingQueue qSession, int sessionLength) {
		this(null, loader, qParser, qSession, new RadixTreeMap<>(), sessionLength);
	}

	public static void end() {
		run = false;
	}

	@Override
	public void run() {
		try {
			while (run) {
				ParsedLine line = qParser.poll(1L, TimeUnit.SECONDS);
				if (line != null) {
					consume(line);
				}
			}
			while (!qParser.isEmpty()) {
				consume(qParser.poll());
			}
			for (UserSessionAbs u : map.values()) {
				qSession.put(u);
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
			return;
		}
	}

	private void consume(ParsedLine line) throws InterruptedException {
		if (!line.isMetaData()) {
			QueueElement e = new QueueElement(line.getKey(), line.getLocalDateTime());
			UserSessionAbs session = map.get(e.getKey());
			if (session == null) {
				if (!line.isWebPageResource()) {
					session = makeSession(line);
					map.put(session.getKey(), session);
					queue.offer(e);
				}
			} else {
				if (line.minus(session) <= sessionLength) {
					session.addParsedLine(line);
				} else {
					session = makeSession(line);
					qSession.put(map.put(session.getKey(), session));
				}
				queue.offer(e);
			}
			for (e = queue.peek(); e != null && line.minus(e) > sessionLength; e = queue.peek()) {
				session = map.get(e.getKey());
				if (session != null && session.getLocalDateTime().equals(e.getDateTime())) {
					qSession.put(map.remove(e.getKey()));
				}
				queue.poll();
			}
		}
	}

	private UserSessionAbs makeSession(ParsedLine line) {
		try {
			Class aClass = loader.loadClass(UserSessionDump.getName());
			Constructor init = aClass.getConstructor(ParsedLine.class);
			return (UserSessionAbs) init.newInstance(line);
		} catch (InstantiationException | InvocationTargetException | NoSuchMethodException | IllegalAccessException | ClassNotFoundException e) {
			e.printStackTrace();
			return null;
		}
	}

	private class QueueElement implements TimePoint {

		private LocalDateTime dateTime;
		private String key;

		public QueueElement(String key, LocalDateTime dateTime) {
			this.dateTime = dateTime;
			this.key = key;
		}

		public QueueElement() {
			this(null, null);
		}

		public LocalDateTime getDateTime() {
			return dateTime;
		}

		public void setDateTime(LocalDateTime dateTime) {
			this.dateTime = dateTime;
		}

		public String getKey() {
			return key;
		}

		public void setKey(String key) {
			this.key = key;
		}

		@Override
		public LocalDateTime getLocalDateTime() {
			return getDateTime();
		}

		@Override
		public boolean equals(Object o) {
			if (o == null || !(o instanceof QueueElement)) return false;
			if (this == o) return true;
			QueueElement that = (QueueElement) o;
			return getDateTime().equals(that.getDateTime()) &&
					getKey().equals(that.getKey());
		}

		@Override
		public int hashCode() {
			int result = getDateTime() != null ? getDateTime().hashCode() : 0;
			result = 31 * result + (getKey() != null ? getKey().hashCode() : 0);
			return result;
		}

		@Override
		public String toString() {
			return "QueueElement{" +
					"dateTime=" + printDate() +
					", key='" + key + '\'' +
					'}';
		}
	}
}
