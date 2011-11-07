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
import org.jacp.api.component.IBGComponent;
import org.jacp.api.component.IStateLessBGComponent;
import org.jacp.api.coordinator.ICoordinator;
import org.jacp.api.coordinator.IStatelessComponentCoordinator;
import org.jacp.api.launcher.Launcher;
import org.jacp.api.perspective.IPerspective;
import org.jacp.javafx2.rcp.action.FX2Action;
import org.jacp.javafx2.rcp.action.FX2ActionListener;
import org.jacp.javafx2.rcp.coordinator.StatelessComponentCoordinator;
/**
 * represents a abstract stateless background component
 * 
 * @author Andy Moncsek
 * 
 */
public abstract class AStatelessComponent implements
		IStateLessBGComponent<EventHandler<Event>, Event, Object> {
	private String id;
	private String target = "";
	private String name;
	private volatile String handleComponentTarget;
	private volatile boolean active = true;
	private boolean isActivated = false;
	private volatile AtomicBoolean blocked = new AtomicBoolean(false);
	private ICoordinator<EventHandler<Event>, Event, Object> componentObserver;
	private IPerspective<EventHandler<Event>, Event, Object> parentPerspective;
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
	public void setParentPerspective(
			IPerspective<EventHandler<Event>, Event, Object> perspective) {
		this.parentPerspective = perspective;
	}

	@Override
	public IPerspective<EventHandler<Event>, Event, Object> getParentPerspective() {
		return this.parentPerspective;
	}

	@Override
	public boolean hasIncomingMessage() {
		return !this.incomingActions.isEmpty();
	}
	//TODO add double check idiom
	private final synchronized IStatelessComponentCoordinator<EventHandler<Event>, Event, Object> getCooridinator() {
		if (this.coordinator == null) {
			if (this.launcher == null) {
				throw new UnsupportedOperationException("no di launcher set");
			}
			this.coordinator = new StatelessComponentCoordinator(this, launcher);
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
		if (hasIncomingMessage()) {
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
		return new FX2ActionListener(new FX2Action(this.id), this.componentObserver);
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
	public void setActivated(boolean isActive) {
		this.isActivated = isActive;

	}

	@Override
	public boolean isActivated() {
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

	@Override
	public void setObserver(
			ICoordinator<EventHandler<Event>, Event, Object> observer) {
		this.componentObserver = observer;
	}

	@SuppressWarnings("unchecked")
	@Override
	public final <C> C handle(final IAction<Event, Object> action) {
		return (C) handleAction(action);
	}

	public abstract Object handleAction(IAction<Event, Object> action);
	
	public final void addMessage(final IAction<Event, Object> message) {
		getCooridinator().incomingMessage(message);
	    }


	@Override
	public synchronized void setLauncher(Launcher<?> launcher) {
		this.launcher = launcher;
	}

	@Override
	protected final Object clone() {
		try {
			final AStatelessComponent comp = (AStatelessComponent) super
					.clone();
			comp.setId(this.id);
			comp.setActive(this.active);
			comp.setName(this.name);
			comp.setExecutionTarget(this.target);
			comp.setHandleTarget(this.handleComponentTarget);
			comp.setObserver(this.componentObserver);
			comp.setParentPerspective(this.parentPerspective);
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
	public final synchronized IBGComponent<EventHandler<Event>, Event, Object> init(
			final IBGComponent<EventHandler<Event>, Event, Object> comp) {
		comp.setId(this.id);
		comp.setActive(this.active);
		comp.setName(this.name);
		comp.setExecutionTarget(this.target);
		comp.setHandleTarget(this.handleComponentTarget);
		comp.setObserver(this.componentObserver);
		comp.setParentPerspective(this.parentPerspective);
		return comp;
	}

}
