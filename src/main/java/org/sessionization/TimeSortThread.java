package org.sessionization;

import org.datastruct.RadixTreeMap;
import org.datastruct.concurrent.SharedBinomialQueue;
import org.sessionization.parser.datastruct.ParsedLine;
import org.sessionization.parser.datastruct.UserSessionAbs;

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
		while (true) {
			line = qParser.poll();
			if (line != null) {
				QueueElement e = new QueueElement(line.getKey(), line.getLocalDateTime());
				UserSessionAbs session = map.get(e.getKey());
				if (session == null) {
					/** Imamo novo sejo */
					// TODO: 1/22/16 dodaje e na prioritetno vrsto, ter izdelaj sejo ce je to potrebno
				} else {
					/** Seja ze obstaja */
					// TODO: 1/22/16 Preveri case med zahtevo in pridobljeno sejo
				}
				// TODO: 1/22/16 Preveri privi element na prioritetni vrsti, ter ga primerjaj z e elementom, ki si ga izdelal. Ce pridobljena vrednost presega sessionLength potem prenesi ta sejo na vrsto za shranjevaneje v podatkovno bazo
			} else {
				/** Prdobili smo zadnjo vrstico */
				try {
					for (UserSessionAbs s : map.values()) {
						qSession.put(s);
					}
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				break;
			}
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
			return getDateTime() != null ? getDateTime().equals(that.getDateTime()) : that.getDateTime() == null &&
					getKey() != null ? getKey().equals(that.getKey()) : that.getKey() == null;
		}

		@Override
		public int hashCode() {
			int result = getDateTime() != null ? getDateTime().hashCode() : 0;
			result = 31 * result + (getKey() != null ? getKey().hashCode() : 0);
			return result;
		}
	}
}
