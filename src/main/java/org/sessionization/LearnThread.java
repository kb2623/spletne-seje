package org.sessionization;

import org.sessionization.parser.datastruct.UserIdAbs;

import java.util.Map;
import java.util.concurrent.BlockingQueue;

public class LearnThread extends Thread {

	private BlockingQueue<Map<String, UserIdAbs>> queueParser;

	public LearnThread(BlockingQueue queue) {
		super();
		queueParser = queue;
	}

	@Override
	public void run() {
		// TODO
	}
}
