package org.jacp.project.JACP.Util.util;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

// TODO: Auto-generated Javadoc
/**
 * The Class ExternalCounter.
 */
public class ExternalCounter {

	/** The instance. */
	private static ExternalCounter instance;

	/** The counters. */
	private Map<String, AtomicInteger> counters;

	/**
	 * Instantiates a new external counter.
	 */
	private ExternalCounter() {
		this.counters = new HashMap<String, AtomicInteger>();
	}

	/**
	 * Gets the single instance of ExternalCounter.
	 * 
	 * @return single instance of ExternalCounter
	 */
	public static ExternalCounter getInstance() {
		if (instance == null) {
			instance = new ExternalCounter();
		}
		return instance;
	}

	/**
	 * Register counter.
	 * 
	 * @param counterId
	 *            the counter id
	 * @return true, if successful
	 */
	public AtomicInteger registerCounter(String counterId) {
		if (counters.containsKey(counterId)) {
			return null;
		}
		AtomicInteger integer = new AtomicInteger(0);
		counters.put(counterId, integer);
		return integer;
	}

	/**
	 * Increment counter.
	 * 
	 * @param counterId
	 *            the counter id
	 * @return the int
	 */
	public int incrementCounter(String counterId) {
		AtomicInteger i = counters.get(counterId);
		if (i == null) {
			i = this.registerCounter(counterId);
		}
		return i.getAndIncrement();

	}

	public void resetCounter(String counterId) {
		if (counters.containsKey(counterId)) {
			counters.get(counterId).set(0);
		}
	}
}
