package org.jacp.javafx.rcp.util;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

public class HandlerThreadFactory implements ThreadFactory {
	final String name;
	private static final AtomicInteger counter = new AtomicInteger(0);
	public HandlerThreadFactory(String name) {
		this.name = name;
	}
	public Thread newThread(Runnable r) {
		return new Thread(r,name.concat(Integer.toString(counter.incrementAndGet())));
	}

}
