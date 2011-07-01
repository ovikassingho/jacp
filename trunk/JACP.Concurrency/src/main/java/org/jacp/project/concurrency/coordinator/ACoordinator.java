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
package org.jacp.project.concurrency.coordinator;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.logging.Logger;

import org.jacp.api.action.IAction;
import org.jacp.api.component.IComponent;
import org.jacp.api.coordinator.ICoordinator;

/**
 * Observer handles message notification and notifies correct components
 * 
 * @author Andy Moncsek
 * 
 */
public abstract class ACoordinator extends Thread implements
		ICoordinator<ActionListener, ActionEvent, Object> {

	private final Logger logger = Logger.getLogger(this.getClass().getName());

	private volatile BlockingQueue<IAction<ActionEvent, Object>> messages = new ArrayBlockingQueue<IAction<ActionEvent, Object>>(
			100000);

	@Override
	public final void run() {
		while (!Thread.interrupted()) {
			log(" observer thread size" + messages.size());
			IAction<ActionEvent, Object> action = null;
			try {
				action = messages.take();
			} catch (final InterruptedException e) {
				e.printStackTrace();
			}
			final Map<String, Object> messages = action.getMessageList();
			for (final String targetId : messages.keySet()) {
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
	protected IAction<ActionEvent, Object> getValidAction(
			final IAction<ActionEvent, Object> action, final String target,
			final Object message) {
		final IAction<ActionEvent, Object> actionClone = action.clone();
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
	protected boolean isLocalMessage(final String messageId) {
		return !messageId.contains(".");
	}

	/**
	 * returns target message with perspective and component name // TODO remove
	 * code duplication
	 * 
	 * @param messageId
	 * @return
	 */
	protected String[] getTargetId(final String messageId) {
		return messageId.split("\\.");
	}

	/**
	 * returns the message target perspective id
	 * 
	 * @param messageId
	 * @return
	 */
	protected String getTargetPerspectiveId(final String messageId) {
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
	protected String getTargetComponentId(final String messageId) {
		final String[] targetId = getTargetId(messageId);
		if (!isLocalMessage(messageId)) {
			return targetId[1];
		}
		return messageId;
	}

	@Override
	public final <M extends IComponent<ActionListener, ActionEvent, Object>> M getObserveableById(
			final String id, final List<M> components) {
		for (int i = 0; i < components.size(); i++) {
			final M p = components.get(i);
			if (p.getId().equals(id)) {
				return p;
			}
		}
		return null;
	}

	@Override
	public final void handle(final IAction<ActionEvent, Object> action) {
		messages.add(action);

	}

	protected void log(final String message) {
		logger.fine(message);
	}

}
