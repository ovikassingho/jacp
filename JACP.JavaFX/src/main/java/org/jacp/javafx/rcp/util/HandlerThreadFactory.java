/************************************************************************
 * 
 * Copyright (C) 2010 - 2012
 *
 * [HandlerThreadFactory.java]
 * AHCP Project (http://jacp.googlecode.com/)
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

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;
/**
 * Thread factory for all executors. Defines name of threads.
 * @author Andy Moncsek
 *
 */
public class HandlerThreadFactory implements ThreadFactory {
	public static String PREFIX="JacpFX-Worker-";
	final String name;
	private static final AtomicInteger counter = new AtomicInteger(0);
	public HandlerThreadFactory(String name) {
		this.name = name;
	}
	public Thread newThread(Runnable r) {
		return new Thread(r,PREFIX.concat(name.concat(Integer.toString(counter.incrementAndGet()))));
	}

}
