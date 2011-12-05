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
package org.jacp.javafx2.rcp.component;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;

import javafx.event.Event;
import javafx.event.EventHandler;

import org.jacp.api.action.IAction;
import org.jacp.api.action.IActionListener;
import org.jacp.api.component.ICallbackComponent;
import org.jacp.api.component.IStateLessCallabackComponent;
import org.jacp.api.coordinator.IStatelessComponentCoordinator;
import org.jacp.api.launcher.Launcher;
import org.jacp.javafx2.rcp.action.FX2Action;
import org.jacp.javafx2.rcp.action.FX2ActionListener;
import org.jacp.javafx2.rcp.coordinator.StatelessCallbackCoordinator;

/**
 * represents a abstract stateless background component
 * 
 * @author Andy Moncsek
 * 
 */

public abstract class AStatelessCallbackComponent implements
		IStateLessCallabackComponent<EventHandler<Event>, Event, Object> {
	private String id;
	private String target = "";
	private String name;
	private volatile String handleComponentTarget;
	private volatile boolean active = true;
	private boolean isActivated = false;
	private volatile AtomicBoolean blocked = new AtomicBoolean(false);
	private BlockingQueue<IAction<Event, Object>> globalMessageQueue;
	private final BlockingQueue<IAction<Event, Object>> incomingActions = new ArrayBlockingQueue<IAction<Event, Object>>(
			500);
	private IStatelessComponentCoordinator<EventHandler<Event>, Event, Object> coordinator;

	private Launcher<?> launcher;

	@Override
	public final String getHandleTargetAndClear() {
		return this.handleComponentTarget;
	}

	@Override
	public final void setHandleTarget(final String componentTargetId) {
		this.handleComponentTarget = componentTargetId;
	}

	@Override
	public final String getExecutionTarget() {
		return this.target;
	}

	@Override
	public final void setExecutionTarget(final String target) {
		this.target = target;
	}

	@Override
	public boolean hasIncomingMessage() {
		return !this.incomingActions.isEmpty();
	}

	// TODO add double check idiom
	private final synchronized IStatelessComponentCoordinator<EventHandler<Event>, Event, Object> getCooridinator() {
		if (this.coordinator == null) {
			if (this.launcher == null) {
				throw new UnsupportedOperationException("no di launcher set");
			}
			this.coordinator = new StatelessCallbackCoordinator(this,
					this.launcher);
		}
		return this.coordinator;
	}

	@Override
	public void putIncomingMessage(IAction<Event, Object> action) {
		try {
			this.incomingActions.put(action);
		} catch (final InterruptedException e) {
			e.printStackTrace();
		}
	}

	@Override
	public IAction<Event, Object> getNextIncomingMessage() {
		if (this.hasIncomingMessage()) {
			try {
				return this.incomingActions.take();
			} catch (final InterruptedException e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	@Override
	public final boolean isBlocked() {
		return this.blocked.get();
	}

	@Override
	public final void setBlocked(final boolean blocked) {
		this.blocked.set(blocked);
	}

	@Override
	public IActionListener<EventHandler<Event>, Event, Object> getActionListener() {
		return new FX2ActionListener(new FX2Action(this.id),
				this.globalMessageQueue);
	}

	@Override
	public String getId() {
		return this.id;
	}

	@Override
	public void setId(String id) {
		this.id = id;
	}

	@Override
	public boolean isActive() {
		return this.active;
	}

	@Override
	public void setActive(boolean active) {
		this.active = active;
	}

	@Override
	public void setStarted(boolean isActive) {
		this.isActivated = isActive;

	}

	@Override
	public boolean isStarted() {
		return this.isActivated;
	}

	@Override
	public String getName() {
		return this.name;
	}

	@Override
	public void setName(String name) {
		this.name = name;

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final void setMessageQueue(BlockingQueue<IAction<Event, Object>> messageQueue){
		this.globalMessageQueue = messageQueue;
	}
	

	@SuppressWarnings("unchecked")
	@Override
	public final <C> C handle(final IAction<Event, Object> action) {
		return (C) this.handleAction(action);
	}

	public abstract Object handleAction(IAction<Event, Object> action);

	public final void addMessage(final IAction<Event, Object> message) {
		this.getCooridinator().incomingMessage(message);
	}

	@Override
	public synchronized void setLauncher(Launcher<?> launcher) {
		this.launcher = launcher;
	}

	@Override
	protected final Object clone() {
		try {
			final AStatelessCallbackComponent comp = (AStatelessCallbackComponent) super
					.clone();
			comp.setId(this.id);
			comp.setActive(this.active);
			comp.setName(this.name);
			comp.setExecutionTarget(this.target);
			comp.setHandleTarget(this.handleComponentTarget);
			comp.setMessageQueue(this.globalMessageQueue);
			return comp;
		} catch (final CloneNotSupportedException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * init cloned instance with values of blueprint
	 * 
	 * @param comp
	 * @return
	 */
	public final synchronized ICallbackComponent<EventHandler<Event>, Event, Object> init(
			final ICallbackComponent<EventHandler<Event>, Event, Object> comp) {
		comp.setId(this.id);
		comp.setActive(this.active);
		comp.setName(this.name);
		comp.setExecutionTarget(this.target);
		comp.setHandleTarget(this.handleComponentTarget);
		comp.setMessageQueue(this.globalMessageQueue);
		return comp;
	}

}
