package org.sessionization;

import org.sessionization.parser.datastruct.AbsUserId;

import java.util.Map;
import java.util.concurrent.BlockingQueue;

public class LearnThread extends Thread {

	private BlockingQueue<Map<String, AbsUserId>> queueParser;

	public LearnThread(BlockingQueue queue) {
		super();
		queueParser = queue;
	}

	@Override
	public void run() {
		// TODO
	}
}
