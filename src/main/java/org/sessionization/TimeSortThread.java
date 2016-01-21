package org.sessionization;

import org.datastruct.RadixTree;
import org.datastruct.concurrent.SharedMap;
import org.sessionization.parser.datastruct.ParsedLine;
import org.sessionization.parser.datastruct.UserSessionAbs;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentMap;

public class TimeSortThread extends Thread {

	private static volatile int ThreadNumber = -1;
	private static volatile int sessionLength = 0;

	private BlockingQueue<ParsedLine> qParser;
	private BlockingQueue<UserSessionAbs> qSession;
	private ConcurrentMap<String, UserSessionAbs> map;

	public TimeSortThread(ThreadGroup group, BlockingQueue qParser, BlockingQueue qSession, ConcurrentMap map, int sessionLength) {
		super(group, "TimeSortingThread-" + ThreadNumber++);
		this.qParser = qParser;
		this.qSession = qSession;
		this.map = map;
		if (TimeSortThread.sessionLength != 0) {
			TimeSortThread.sessionLength = sessionLength;
		}
	}

	public TimeSortThread(BlockingQueue qParser, BlockingQueue qSession, int sessionLength) {
		this(null, qParser, qSession, new SharedMap<>(new RadixTree<>()), sessionLength);
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
