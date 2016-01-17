package org.sessionization;

import com.sun.org.apache.xpath.internal.operations.String;
import org.sessionization.parser.datastruct.UserIdAbs;
import org.sessionization.parser.datastruct.UserSessionAbs;

import java.util.Map;
import java.util.concurrent.BlockingQueue;

public class TTimeSort extends Thread {

	private BlockingQueue<Map<String, UserIdAbs>> qParser;
	private BlockingQueue<Map<String, UserSessionAbs>> qMap;

	public TTimeSort(BlockingQueue qParser, BlockingQueue qMap) {
		super();
		this.qParser = qParser;
		this.qMap = qMap;
	}

	@Override
	public void run() {
		// TODO: 1/18/16 Tukaj uprorabi podatek o dolzini deje ter uporabi dinamicne razredi in bmesnik TimePoint 
	}
}
