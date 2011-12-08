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
import javafx.scene.Node;

import org.jacp.api.action.IAction;
import org.jacp.api.action.IActionListener;
import org.jacp.api.component.IVComponent;
import org.jacp.api.componentLayout.IBaseLayout;
import org.jacp.javafx2.rcp.action.FX2Action;
import org.jacp.javafx2.rcp.action.FX2ActionListener;
import org.jacp.javafx2.rcp.componentLayout.FX2ComponentLayout;

/**
 * represents a basic FX2 component to extend from, uses this abstract class to
 * create UI components
 * 
 * @author Andy Moncsek
 */
public abstract class AFX2Component implements
		IVComponent<Node, EventHandler<Event>, Event, Object> {

	private String id;
	private String target;
	private String name;
	private String parentId;
	private Node root;
	private boolean active;
	private boolean isActived = false;
	private volatile AtomicBoolean blocked = new AtomicBoolean(false);
	private final BlockingQueue<IAction<Event, Object>> incomingActions = new ArrayBlockingQueue<IAction<Event, Object>>(
			1000);
	private BlockingQueue<IAction<Event, Object>> globalMessageQueue;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final void setRoot(final Node root) {
		this.root = root;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final Node getRoot() {
		return this.root;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final String getExecutionTarget() {
		return this.target;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final void setExecutionTarget(final String target) {
		this.target = target;
	}

	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public final boolean hasIncomingMessage() {
		return !this.incomingActions.isEmpty();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final void putIncomingMessage(final IAction<Event, Object> action) {
		try {
			this.incomingActions.put(action);
		} catch (final InterruptedException e) {
			e.printStackTrace();
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final IAction<Event, Object> getNextIncomingMessage() {
		if (this.hasIncomingMessage()) {
			try {
				return this.incomingActions.take();
			} catch (final InterruptedException e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final boolean isBlocked() {
		return this.blocked.get();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final void setBlocked(final boolean blocked) {
		this.blocked.set(blocked);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final IActionListener<EventHandler<Event>, Event, Object> getActionListener() {
		return new FX2ActionListener(new FX2Action(this.id),
				this.globalMessageQueue);
	}
	

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final String getId() {
		if (this.id == null) {
			throw new UnsupportedOperationException("No id set");
		}
		return this.id;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final void setId(final String id) {
		this.id = id;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final boolean isActive() {
		return this.active;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final void setActive(final boolean active) {
		this.active = active;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final void setStarted(final boolean isActive) {
		this.isActived = isActive;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final boolean isStarted() {
		return this.isActived;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final String getName() {
		if (this.name == null) {
			throw new UnsupportedOperationException("No name set");
		}
		return this.name;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final void setName(final String name) {
		this.name = name;
	}

	@Override
	public String getParentId() {
		return parentId;
	}

	@Override
	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final void setMessageQueue(BlockingQueue<IAction<Event, Object>> messageQueue){
		this.globalMessageQueue = messageQueue;
	}
	
	

	@Override
	@SuppressWarnings("unchecked")
	/**
	 * {@inheritDoc}
	 */
	public final <C> C handle(final IAction<Event, Object> action) {
		return (C) this.handleAction(action);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final Node postHandle(final Node node,
			final IAction<Event, Object> action) {
		return this.postHandleAction(node, action);
	}

	/**
	 * @see org.jacp.api.component.IHandleable#handle(IAction)
	 * @param action
	 * @return a node
	 */
	public abstract Node handleAction(final IAction<Event, Object> action);

	/**
	 * @see org.jacp.api.component.IVComponent#postHandle(Object, IAction)
	 *      {@inheritDoc}
	 * @param node
	 * @param action
	 * @return a node
	 */
	public abstract Node postHandleAction(final Node node,
			final IAction<Event, Object> action);

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final void onStart(final IBaseLayout<Node> layout) {
		this.onStartComponent((FX2ComponentLayout) layout);

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onTearDown(final IBaseLayout<Node> layout) {
		this.onTearDownComponent((FX2ComponentLayout) layout);

	}

	/**
	 * Handle menu, bars and other UI components on component start.
	 * 
	 * @param menuBar
	 * @param bars
	 */
	public abstract void onStartComponent(final FX2ComponentLayout layout);

	/**
	 * Clean up menu, bars and other components on component teardown.
	 * 
	 * @param menuBar
	 * @param bars
	 */
	public abstract void onTearDownComponent(final FX2ComponentLayout layout);

}
