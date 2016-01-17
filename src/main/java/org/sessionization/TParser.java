package org.sessionization;

import org.sessionization.parser.WebLogParser;
import org.sessionization.parser.datastruct.ParsedLine;

import java.io.IOException;
import java.text.ParseException;
import java.util.concurrent.BlockingQueue;

public class TParser extends Thread {

	private BlockingQueue<ParsedLine> queue;
	private WebLogParser parser;

	public TParser(BlockingQueue queue, WebLogParser parser) {
		super();
		this.queue = queue;
		this.parser = parser;
	}

	@Override
	public void run() {
		ParsedLine line;
		do {
			try {
				line = parser.parseLine();
				queue.put(line);
			} catch (ParseException | IOException | InterruptedException e) {
				line = null;
			}
		} while (line != null);
	}
}
