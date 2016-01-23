package org.sessionization;

import org.sessionization.parser.WebLogParser;
import org.sessionization.parser.datastruct.ParsedLine;

import java.io.IOException;
import java.text.ParseException;
import java.util.concurrent.BlockingQueue;

public class ParserThread extends Thread {

	private static volatile int ThreadNumber = 0;

	private BlockingQueue<ParsedLine> queue;
	private WebLogParser parser;

	/**
	 * @param group
	 * @param queue
	 * @param parser
	 */
	public ParserThread(ThreadGroup group, BlockingQueue queue, WebLogParser parser) {
		super(group, "ParserThread-" + ThreadNumber++);
		this.queue = queue;
		this.parser = parser;
	}

	/**
	 *
	 * @param queue
	 * @param parser
	 */
	public ParserThread(BlockingQueue queue, WebLogParser parser) {
		this(null, queue, parser);
	}

	@Override
	public void run() {
		ParsedLine line;
		do {
			try {
				line = parser.parseLine();
				queue.put(line);
			} catch (ParseException | IOException e) {
				e.printStackTrace();
				line = null;
			} catch (InterruptedException e) {
				e.printStackTrace();
				line = null;
			}
		} while (line != null);
	}
}
