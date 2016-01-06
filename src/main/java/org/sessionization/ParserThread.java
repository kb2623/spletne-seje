package org.sessionization;

import org.sessionization.parser.WebLogParser;
import org.sessionization.parser.datastruct.ParsedLine;

import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;

public class ParserThread extends Thread {

	private BlockingQueue<Map<String, List<List<ParsedLine>>>> queue;
	private WebLogParser parser;
	private int segmentSize;

	public ParserThread(BlockingQueue queue, WebLogParser parser, int segmentSize) {
		super();
		this.queue = queue;
		this.parser = parser;
		this.segmentSize = segmentSize;
	}

	@Override
	public void run() {

	}
}
