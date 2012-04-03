/************************************************************************
 * 
 * Copyright (C) 2010 - 2012
 *
 * [AFX2Coordinator.java]
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
package org.jacp.javafx.rcp.coordinator;


import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.logging.Logger;

import javafx.event.Event;
import javafx.event.EventHandler;

import org.jacp.api.action.IAction;
import org.jacp.api.action.IDelegateDTO;
import org.jacp.api.coordinator.ICoordinator;

/**
 * Observer handles messages and notifies correct components, the observer is
 * running in an own thread so that message handling can be done in background
 * 
 * @author Andy Moncsek
 */
public abstract class AFXCoordinator extends Thread implements
		ICoordinator<EventHandler<Event>, Event, Object> {

	private final Logger logger = Logger.getLogger(this.getClass().getName());
	private volatile BlockingQueue<IAction<Event, Object>> messages = new ArrayBlockingQueue<IAction<Event, Object>>(
			100000);

	@Override
	public final void run() {
		while (!Thread.interrupted()) {
			this.log(" observer thread size" + this.messages.size());
			
			IAction<Event, Object> action = null;
			try {
				action = this.messages.take();
			} catch (final InterruptedException e) {
				e.printStackTrace();
			}
			final Map<String, Object> myMessages = action.getMessageList();
			for (final String targetId : myMessages.keySet()) {
				this.log(" handle message to: " + targetId);
				this.handleMessage(targetId, action);
			}
			this.log(" observer thread DONE");
		}
	}
	
	/**
	 * add message to delegate queue
	 * @param target
	 * @param action
	 * @param queue
	 */
	protected final void delegateMessageToCorrectPerspective(
			String target,
			IAction<Event, Object> action,
			BlockingQueue<IDelegateDTO<Event, Object>> queue) {
		queue.add(new DelegateDTO(target, action));
	}


	@Override
	public BlockingQueue<IAction<Event, Object>> getMessageQueue() {
		return messages;
	}
	
	
	
	protected void log(final String message) {
		this.logger.fine(message);
	}
}
