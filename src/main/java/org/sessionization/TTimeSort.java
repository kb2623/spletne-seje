package org.sessionization;

import org.datastruct.RadixTree;
import org.sessionization.parser.datastruct.ParsedLine;
import org.sessionization.parser.datastruct.UserSessionAbs;

import java.util.Map;
import java.util.concurrent.BlockingQueue;

public class TTimeSort extends Thread {

	private BlockingQueue<ParsedLine> qParser;
	private BlockingQueue<UserSessionAbs> qMap;
	private Map<String, UserSessionAbs> map;

	public TTimeSort(BlockingQueue qParser, BlockingQueue qMap) {
		super();
		this.qParser = qParser;
		this.qMap = qMap;
		this.map = new RadixTree<>();
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
