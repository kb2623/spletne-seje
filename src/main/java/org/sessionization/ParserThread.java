package org.sessionization;

import org.datastruct.RadixTree;
import org.sessionization.parser.datastruct.PageViewAbs;
import org.sessionization.parser.AbsParser;
import org.sessionization.parser.datastruct.PageViewDump;
import org.sessionization.parser.datastruct.ParsedLine;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;
import java.util.concurrent.BlockingQueue;

public class ParserThread extends Thread {

	private BlockingQueue<Map<String, PageViewAbs>> queue;
	private AbsParser parser;
	private HibernateUtil util;

	public ParserThread(BlockingQueue queue, AbsParser parser, ClassLoader loader) {
		super();
		this.queue = queue;
		this.parser = parser;
		this.setContextClassLoader(loader);
	}

	@Override
	public void run() {
		Map<String, PageViewAbs> map = new RadixTree<>();
		for (ParsedLine line : parser) {
			if (!line.isResource()) {
				System.out.println(line.toString());
			}
		}
	}
}
