/************************************************************************
 * 
 * Copyright (C) 2010 - 2012
 *
 * [AFXSubComponent.java]
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
package org.jacp.javafx.rcp.component;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Semaphore;
import java.util.logging.Logger;

import javafx.event.Event;
import javafx.event.EventHandler;

import org.jacp.api.action.IAction;
import org.jacp.api.component.ISubComponent;

/**
 * the AFXSubComponent is the basic component for all components
 * 
 * @author Andy Moncsek
 * 
 */
public abstract class ASubComponent extends AComponent implements
		ISubComponent<EventHandler<Event>, Event, Object> {

	private volatile String executionTarget = "";

	private volatile String parentId;

	private final Semaphore lock = new Semaphore(1);
	
	private final Logger logger = Logger.getLogger(this.getClass().getName());

	protected volatile BlockingQueue<IAction<Event, Object>> incomingMessage = new ArrayBlockingQueue<IAction<Event, Object>>(
			1000);

	@Override
	public final void initEnv(final String parentId,
			final BlockingQueue<IAction<Event, Object>> messageQueue) {
		this.parentId = parentId;
		this.globalMessageQueue = messageQueue;

	}

	@Override
	public final String getExecutionTarget() {
		return this.executionTarget;
	}

	@Override
	public final void setExecutionTarget(final String target) {
		this.executionTarget = target;

	}

	@Override
	public final boolean hasIncomingMessage() {
		return !this.incomingMessage.isEmpty();
	}

	@Override
	public final void putIncomingMessage(final IAction<Event, Object> action) {
		try {
			this.incomingMessage.put(action);
		} catch (final InterruptedException e) {
			logger.info("massage put failed:");
		}

	}

	@Override
	public final IAction<Event, Object> getNextIncomingMessage() {
		if (this.hasIncomingMessage()) {
			try {
				return this.incomingMessage.take();
			} catch (final InterruptedException e) {
				logger.info("massage take failed:");
			}
		}
		return null;
	}

	@Override
	public final boolean isBlocked() {
		return lock.availablePermits() == 0;
	}

	@Override
	public final void lock() {
		try {
			lock.acquire();
		} catch (InterruptedException e) {
			logger.info("lock interrupted.");
		}
	}

	@Override
	public final void release() {
		lock.release();
	}

	@Override
	public final String getParentId() {
		return this.parentId;
	}

}
