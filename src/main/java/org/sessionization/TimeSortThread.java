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

public class TimeSortThread extends Thread {

	private static volatile int ThreadNumber = -1;
	private static volatile int sessionLength = 0;
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
	public TimeSortThread(ThreadGroup group, BlockingQueue qParser, BlockingQueue qSession, Map map, int sessionLength) {
		super(group, "TimeSortingThread-" + ThreadNumber++);
		this.qParser = qParser;
		this.qSession = qSession;
		this.map = map;
		if (TimeSortThread.sessionLength != 0) {
			TimeSortThread.sessionLength = sessionLength;
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
	public TimeSortThread(BlockingQueue qParser, BlockingQueue qSession, int sessionLength) {
		this(null, qParser, qSession, new RadixTreeMap<>(), sessionLength);
	}

	@Override
	public void run() {
		ParsedLine line = null;
		try {
			while (true) {
				line = qParser.poll();
				if (line != null) {
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
							// TODO: 1/22/16 Tuja imamo problem pri vecnitnem delovanju saj ne vem kaj ima katera nit
							session.addParsedLine(line);
						} else {
							session = makeSession(line);
							qSession.put(map.put(session.getKey(), session));
						}
						queue.offer(e);
					}
					for (e = queue.peek(); e != null && line.minus(e) > sessionLength; e = queue.peek()) {
						session = map.get(e.getKey());
						if (session == null) {
							queue.poll();
						} else if (session.getLocalDateTime().equals(e.getDateTime())) {
							qSession.put(map.remove(e.getKey()));
							queue.poll();
						}
					}
				} else {
					for (UserSessionAbs s : map.values()) {
						qSession.put(s);
					}
					break;
				}
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	private UserSessionAbs makeSession(ParsedLine line) {
		try {
			Class aClass = ClassLoader.getSystemClassLoader().loadClass(UserSessionDump.getName());
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
	}
}
