package be.sonck.xml;

import java.util.Iterator;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class BlockingQueueXmlHandlerIterator<T> implements XmlHandlerListener<T>, Iterator<T> {

	private static final int SLEEP_TIME = 100;
	
	private boolean isParsingDone;
	private BlockingQueue<T> queue;
	private T lastTakenEntry;
	
	
	public BlockingQueueXmlHandlerIterator(int capacity) {
		queue = new ArrayBlockingQueue<T>(capacity);
	}
	

	@Override
	public void start() {
	}
	
	@Override
	public final void end() {
		isParsingDone = true;
	}

	@Override
	public final void newElement(T element) {
		try {
			queue.put(element);
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public final boolean hasNext() {
		T peekedObject = queue.peek();
		while (peekedObject == null && !isParsingDone) {
			try {
				Thread.sleep(SLEEP_TIME);
			} catch (InterruptedException e) {
				throw new RuntimeException(e);
			}
			
			peekedObject = queue.peek();
		}
		
		return (peekedObject != null);
	}

	@Override
	public final T next() {
		try {
			lastTakenEntry = queue.take();
			return lastTakenEntry;
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public void remove() {
		if (lastTakenEntry != null) {
			queue.remove(lastTakenEntry);
		}
	}
}
