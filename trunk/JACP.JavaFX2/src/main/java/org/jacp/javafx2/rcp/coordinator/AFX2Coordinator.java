/*
 * Copyright (C) 2010,2011.
 * AHCP Project
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
 */
package org.jacp.javafx2.rcp.coordinator;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.logging.Logger;
import javafx.event.Event;
import javafx.event.EventHandler;
import org.jacp.api.action.IAction;
import org.jacp.api.component.IComponent;
import org.jacp.api.coordinator.ICoordinator;

/**
 * Observer handles messages and notifies correct components, the observer is
 * running in an own thread so that message handling can be done in background
 * 
 * @author Andy Moncsek
 */
public abstract class AFX2Coordinator extends Thread implements
		ICoordinator<EventHandler<Event>, Event, Object> {

	private final Logger logger = Logger.getLogger(this.getClass().getName());
	private volatile BlockingQueue<IAction<Event, Object>> messages = new ArrayBlockingQueue<IAction<Event, Object>>(
			100000);

	@Override
	public final void run() {
		while (!Thread.interrupted()) {
			log(" observer thread size" + messages.size());
			IAction<Event, Object> action = null;
			try {
				action = messages.take();
			} catch (final InterruptedException e) {
				e.printStackTrace();
			}
			final Map<String, Object> myMessages = action.getMessageList();
			for (final String targetId : myMessages.keySet()) {
				log(" handle message to: " + targetId);
				handleMessage(targetId, action);
			}
			log(" observer thread DONE");
		}
	}

	/**
	 * returns cloned action with valid message TODO add to interface
	 * 
	 * @param action
	 * @param message
	 * @return
	 */
	protected final IAction<Event, Object> getValidAction(
			final IAction<Event, Object> action, final String target,
			final Object message) {
		final IAction<Event, Object> actionClone = action.clone();
		actionClone.addMessage(target, message);
		return actionClone;
	}

	/**
	 * when id has no separator it is a local message // TODO remove code
	 * duplication
	 * 
	 * @param messageId
	 * @return
	 */
	protected final boolean isLocalMessage(final String messageId) {
		return !messageId.contains(".");
	}

	/**
	 * returns target message with perspective and component name // TODO remove
	 * code duplication
	 * 
	 * @param messageId
	 * @return
	 */
	protected final String[] getTargetId(final String messageId) {
		return messageId.split("\\.");
	}

	/**
	 * returns the message target perspective id
	 * 
	 * @param messageId
	 * @return
	 */
	protected final String getTargetPerspectiveId(final String messageId) {
		final String[] targetId = getTargetId(messageId);
		if (!isLocalMessage(messageId)) {
			return targetId[0];
		}
		return messageId;
	}

	/**
	 * returns the message target component id
	 * 
	 * @param messageId
	 * @return
	 */
	protected final String getTargetComponentId(final String messageId) {
		final String[] targetId = getTargetId(messageId);
		if (!isLocalMessage(messageId)) {
			return targetId[1];
		}
		return messageId;
	}

	@Override
	public void handle(final IAction<Event, Object> action) {
		messages.add(action);
	}

	@Override
	public <P extends IComponent<EventHandler<Event>, Event, Object>> P getObserveableById(
			final String id, final List<P> components) {
		for (int i = 0; i < components.size(); i++) {
			final P p = components.get(i);
			if (p.getId().equals(id)) {
				return p;
			}
		}
		return null;
	}

	protected void log(final String message) {
		logger.fine(message);
	}
}
