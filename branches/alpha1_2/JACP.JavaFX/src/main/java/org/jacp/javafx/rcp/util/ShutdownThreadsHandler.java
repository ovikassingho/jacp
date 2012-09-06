/************************************************************************
 * 
 * Copyright (C) 2010 - 2012
 *
 * [FX2Util.java]
 * AHCP Project (http://jacp.googlecode.com)
 * All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at 
 *
 *     http://www.apache.org/licenses/LICENSE-2.0 
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an "AS IS"
 * BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either 
 * express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 *
 *
 ************************************************************************/
package org.jacp.javafx.rcp.util;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;

/**
 * Util class to register all Thread and executors in JACPFX, needed to shutdown all Threads and Executors on application close.
 * @author Andy Moncsek
 *
 */
public final class ShutdownThreadsHandler{
	private static  List<Thread> registeredThreads = new CopyOnWriteArrayList<Thread>();
	private static  List<ExecutorService> registeredExecutors = new CopyOnWriteArrayList<ExecutorService>();
	
	/**
	 * Register a Thread.
	 * @param t
	 */
	public static final <T extends Thread> void registerThread(T t) {
		registeredThreads.add(t);
	}
	/**
	 * Register an Executor service.
	 * @param t
	 */
	public static final <E extends ExecutorService> void registerexecutor(E t) {
		registeredExecutors.add(t);
	}
	/**
	 * Shutdown all registered Threads.
	 */
	public static final void shutdownThreads() {
		for(Thread t:registeredThreads) {
			t.interrupt();
		}
	}
	/**
	 * Shutdown all registered Executors.
	 */
	public static final void shutDownExecutors() {
		for(ExecutorService e: registeredExecutors) {
			e.shutdown();
		}
	}
	
	/**
	 * Shutdown registered Threads and Executors.
	 */
	public static final void shutdowAll() {
		for(Thread t:registeredThreads) {
			t.interrupt();
		}
		for(ExecutorService e: registeredExecutors) {
			e.shutdown();
		}
	}
}
