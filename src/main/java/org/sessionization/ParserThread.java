package org.sessionization;

import org.sessionization.parser.AbsParser;
import org.sessionization.parser.datastruct.PageViewAbs;

import java.util.Map;
import java.util.concurrent.BlockingQueue;

public class ParserThread extends Thread {

	private BlockingQueue<Map<String, PageViewAbs>> queue;
	private AbsParser parser;

	public ParserThread(BlockingQueue queue, AbsParser parser, ClassLoader loader) {
		super();
		this.queue = queue;
		this.parser = parser;
	}

	@Override
	public void run() {
	}
}
