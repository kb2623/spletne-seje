package org.sessionization;

import org.datastruct.RadixTree;
import org.sessionization.parser.AbsParser;
import org.sessionization.parser.datastruct.ParsedLine;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;

public class ParserThread extends Thread {

	private BlockingQueue<Map<String, List<List<ParsedLine>>>> queue;
	private AbsParser parser;
	private int segmentSize;

	public ParserThread(BlockingQueue queue, AbsParser parser, int segmentSize) {
		super();
		this.queue = queue;
		this.parser = parser;
		this.segmentSize = segmentSize;
	}

	@Override
	public void run() {
		int cSize = 0;
		Map<String, List<List<ParsedLine>>> map = null;
		List<List<ParsedLine>> oList;
		List<ParsedLine> iList;
		for (ParsedLine line : parser) {
			if (cSize == 0) {
				map = new RadixTree<>();
			}
			oList = map.get(line.getKey());
			if (oList == null) {
				oList = new LinkedList<>();
				iList = new LinkedList<>();
				iList.add(line);
				oList.add(iList);
				map.put(line.getKey(), oList);
				cSize++;
			} else {
				if (!line.isResource()) {
					iList = new LinkedList<>();
					iList.add(line);
					oList.add(iList);
				} else {
					iList = oList.get(oList.size() - 1);
					iList.add(line);
				}
			}
			if (cSize == segmentSize) {
				cSize = 0;
				try {
					queue.put(map);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
		if (cSize > 0) {
			try {
				queue.put(map);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
