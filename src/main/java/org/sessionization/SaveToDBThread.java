package org.sessionization;

import java.util.concurrent.BlockingQueue;

public class SaveToDBThread extends Thread {

	private BlockingQueue queue;

	public SaveToDBThread(BlockingQueue queue) {
		this.queue = queue;
	}

	@Override
	public void run() {
		// TODO: 12/8/15
	}
}
