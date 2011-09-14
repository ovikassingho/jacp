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

import javafx.event.ActionEvent;
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
		IStateLessBGComponent<EventHandler<ActionEvent>, ActionEvent, Object> {
	private String id;
	private String target = "";
	private String name;
	private volatile String handleComponentTarget;
	private volatile boolean active = true;
	private boolean isActivated = false;
	private volatile AtomicBoolean blocked = new AtomicBoolean(false);
	private ICoordinator<EventHandler<ActionEvent>, ActionEvent, Object> componentObserver;
	private IPerspective<EventHandler<ActionEvent>, ActionEvent, Object> parentPerspective;
	private final BlockingQueue<IAction<ActionEvent, Object>> incomingActions = new ArrayBlockingQueue<IAction<ActionEvent, Object>>(
			500);
	private IStatelessComponentCoordinator<EventHandler<ActionEvent>, ActionEvent, Object> coordinator;
	private Launcher<?> launcher;

	@Override
	public final String getHandleTargetAndClear() {
		return handleComponentTarget;
	}

	@Override
	public final void setHandleTarget(final String componentTargetId) {
		handleComponentTarget = componentTargetId;
	}

	@Override
	public final String getExecutionTarget() {
		return target;
	}

	@Override
	public final void setExecutionTarget(final String target) {
		this.target = target;
	}

	@Override
	public void setParentPerspective(
			IPerspective<EventHandler<ActionEvent>, ActionEvent, Object> perspective) {
		this.parentPerspective = perspective;
	}

	@Override
	public IPerspective<EventHandler<ActionEvent>, ActionEvent, Object> getParentPerspective() {
		return this.parentPerspective;
	}

	@Override
	public boolean hasIncomingMessage() {
		return !incomingActions.isEmpty();
	}

	private final synchronized IStatelessComponentCoordinator<EventHandler<ActionEvent>, ActionEvent, Object> getCooridinator() {
		if (coordinator == null) {
			if (launcher == null) {
				throw new UnsupportedOperationException("no di launcher set");
			}
			coordinator = new StatelessComponentCoordinator(this, launcher);
		}
		return coordinator;
	}

	@Override
	public void putIncomingMessage(IAction<ActionEvent, Object> action) {
		try {
			incomingActions.put(action);
		} catch (final InterruptedException e) {
			e.printStackTrace();
		}
	}

	@Override
	public IAction<ActionEvent, Object> getNextIncomingMessage() {
		if (hasIncomingMessage()) {
			try {
				return incomingActions.take();
			} catch (final InterruptedException e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	@Override
	public final boolean isBlocked() {
		return blocked.get();
	}

	@Override
	public final void setBlocked(final boolean blocked) {
		this.blocked.set(blocked);
	}

	@Override
	public IActionListener<EventHandler<ActionEvent>, ActionEvent, Object> getActionListener() {
		return new FX2ActionListener(new FX2Action(id), componentObserver);
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
			ICoordinator<EventHandler<ActionEvent>, ActionEvent, Object> observer) {
		this.componentObserver = observer;
	}

	@SuppressWarnings("unchecked")
	@Override
	public final <C> C handle(final IAction<ActionEvent, Object> action) {
		return (C) handleAction(action);
	}

	public abstract Object handleAction(IAction<ActionEvent, Object> action);

	@Override
	public void setLauncher(Launcher<?> launcher) {
		this.launcher = launcher;
	}

	@Override
	protected final Object clone() {
		try {
			final AStatelessComponent comp = (AStatelessComponent) super
					.clone();
			comp.setId(id);
			comp.setActive(active);
			comp.setName(name);
			comp.setExecutionTarget(target);
			comp.setHandleTarget(handleComponentTarget);
			comp.setObserver(componentObserver);
			comp.setParentPerspective(parentPerspective);
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
	public final synchronized IBGComponent<EventHandler<ActionEvent>, ActionEvent, Object> init(
			final IBGComponent<EventHandler<ActionEvent>, ActionEvent, Object> comp) {
		comp.setId(id);
		comp.setActive(active);
		comp.setName(name);
		comp.setExecutionTarget(target);
		comp.setHandleTarget(handleComponentTarget);
		comp.setObserver(componentObserver);
		comp.setParentPerspective(parentPerspective);
		return comp;
	}

}
