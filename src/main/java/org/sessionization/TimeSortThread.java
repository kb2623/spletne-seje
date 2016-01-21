package org.sessionization;

import org.datastruct.RadixTreeMap;
import org.sessionization.parser.datastruct.ParsedLine;
import org.sessionization.parser.datastruct.UserSessionAbs;

import java.util.Map;
import java.util.concurrent.BlockingQueue;

public class TimeSortThread extends Thread {

	private static volatile int ThreadNumber = -1;
	private static volatile int sessionLength = 0;

	private BlockingQueue<ParsedLine> qParser;
	private BlockingQueue<UserSessionAbs> qSession;
	private Map<String, UserSessionAbs> map;

	public TimeSortThread(ThreadGroup group, BlockingQueue qParser, BlockingQueue qSession, Map map, int sessionLength) {
		super(group, "TimeSortingThread-" + ThreadNumber++);
		this.qParser = qParser;
		this.qSession = qSession;
		this.map = map;
		if (TimeSortThread.sessionLength != 0) {
			TimeSortThread.sessionLength = sessionLength;
		}
	}

	public TimeSortThread(BlockingQueue qParser, BlockingQueue qSession, int sessionLength) {
		this(null, qParser, qSession, new RadixTreeMap<>(), sessionLength);
	}

	@Override
	public void run() {
		ParsedLine line = null;
		while (true) {
			line = qParser.poll();
			if (line != null) {
				// TODO: 1/20/16 glavna logika ustvari mapo ter dodaj
			} else {
				break;
			}
		}
	}
}