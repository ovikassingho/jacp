/************************************************************************
 * 
 * Copyright (C) 2010 - 2012
 *
 * [AFX2Perspective.java]
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

package org.jacp.javafx.rcp.perspective;

import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.Node;

import org.jacp.api.action.IAction;
import org.jacp.api.action.IActionListener;
import org.jacp.api.action.IDelegateDTO;
import org.jacp.api.component.IExtendedComponent;
import org.jacp.api.component.ILayoutAbleComponent;
import org.jacp.api.component.ISubComponent;
import org.jacp.api.componentLayout.IBaseLayout;
import org.jacp.api.componentLayout.IPerspectiveLayout;
import org.jacp.api.coordinator.IComponentCoordinator;
import org.jacp.api.handler.IComponentHandler;
import org.jacp.api.perspective.IPerspective;
import org.jacp.javafx.rcp.action.FXAction;
import org.jacp.javafx.rcp.action.FXActionListener;
import org.jacp.javafx.rcp.componentLayout.FXComponentLayout;
import org.jacp.javafx.rcp.componentLayout.FXPerspectiveLayout;
import org.jacp.javafx.rcp.coordinator.FXComponentCoordinator;
import org.jacp.javafx.rcp.util.FXUtil;

/**
 * represents a basic javafx2 perspective that handles subcomponents,
 * perspectives are not handled in thread so avoid long running tasks in
 * perspectives.
 * 
 * @author Andy Moncsek
 */
public abstract class AFXPerspective implements
		IPerspective<EventHandler<Event>, Event, Object>,
		IExtendedComponent<Node>, ILayoutAbleComponent<Node> {
	private String id;
	private String name;
	private boolean active;
	private boolean isActivated = false;
	private final Logger logger = Logger.getLogger(this.getClass().getName());
	private List<ISubComponent<EventHandler<Event>, Event, Object>> subcomponents;
	private IComponentHandler<ISubComponent<EventHandler<Event>, Event, Object>, IAction<Event, Object>> componentHandler;
	private BlockingQueue<ISubComponent<EventHandler<Event>, Event, Object>> componentDelegateQueue;
	private BlockingQueue<IDelegateDTO<Event, Object>> messageDelegateQueue;
	private IComponentCoordinator<EventHandler<Event>, Event, Object> componentCoordinator;
	private FXComponentLayout layout;
	private BlockingQueue<IAction<Event, Object>> globalMessageQueue;
	private final IPerspectiveLayout<Node, Node> perspectiveLayout = new FXPerspectiveLayout();
	

	@Override
	public final void init(
			final BlockingQueue<ISubComponent<EventHandler<Event>, Event, Object>> componentDelegateQueue, final BlockingQueue<IDelegateDTO<Event, Object>> messageDelegateQueue) {
		this.componentDelegateQueue = componentDelegateQueue;
		this.messageDelegateQueue = messageDelegateQueue;

	}

	@Override
	public final <C> C handle(IAction<Event, Object> action) {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final void onStart(final IBaseLayout<Node> layout) {
		this.layout = (FXComponentLayout) layout;
		onStartPerspective(this.layout);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void postInit(
			IComponentHandler<ISubComponent<EventHandler<Event>, Event, Object>, IAction<Event, Object>> componentHandler) {
		// init component handler
		this.componentHandler = componentHandler;
		componentCoordinator = new FXComponentCoordinator();
		((FXComponentCoordinator) this.componentCoordinator).start();
		componentCoordinator.setComponentHandler(this.componentHandler);
		componentCoordinator.setMessageDelegateQueue(this.messageDelegateQueue);
		componentCoordinator.setParentId(this.getId());
		registerSubcomponents(subcomponents);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onTearDown(final IBaseLayout<Node> layout) {
		onTearDownPerspective(this.layout);
		this.layout = null;
	}

	/**
	 * Handle menu, bars and other UI components on component start.
	 * 
	 * @param menuBar
	 * @param bars
	 */
	public abstract void onStartPerspective(final FXComponentLayout layout);

	/**
	 * Clean up menu, bars and other components on component teardown.
	 * 
	 * @param menuBar
	 * @param bars
	 */
	public abstract void onTearDownPerspective(final FXComponentLayout layout);
	/**
	 * handle perspective method to initialize the perspective and the layout
	 * @param action
	 * @param perspectiveLayout
	 */
	public abstract void handlePerspective(IAction<Event, Object> action,
			final FXPerspectiveLayout perspectiveLayout);

	@Override
	public void handlePerspective(IAction<Event, Object> action) {
		this.handlePerspective(action,
				(FXPerspectiveLayout) this.perspectiveLayout);

	}

	@Override
	public final void registerComponent(
			ISubComponent<EventHandler<Event>, Event, Object> component) {
		this.log("register component: " + component.getId());
		component.setParentId(this.getId());
		this.componentCoordinator.addComponent(component);
		if (!this.subcomponents.contains(component))
			this.subcomponents.add(component);

	}

	@Override
	public final void unregisterComponent(
			ISubComponent<EventHandler<Event>, Event, Object> component) {
		this.log("unregister component: " + component.getId());
		component.setParentId(null);
		this.componentCoordinator.removeComponent(component);
		if (this.subcomponents.contains(component))
			this.subcomponents.remove(component);
	}

	@Override
	public final void initComponents(IAction<Event, Object> action) {
		final String targetId = FXUtil.getTargetComponentId(action
				.getTargetId());
		this.log("3.4.4.1: subcomponent targetId: " + targetId);
		final List<ISubComponent<EventHandler<Event>, Event, Object>> components = this
				.getSubcomponents();
		for (int i = 0; i < components.size(); i++) {
			final ISubComponent<EventHandler<Event>, Event, Object> component = components
					.get(i);
			if (component.getId().equals(targetId)) {
				this.log("3.4.4.2: subcomponent init with custom action");
				componentHandler.initComponent(action, component);
			} // else END
			else if (component.isActive() && !component.isStarted()) {
				this.log("3.4.4.2: subcomponent init with default action");
				componentHandler.initComponent(new FXAction(component.getId(),
						component.getId(), "init"), component);
			} // if END

		} // for END
	}


	@Override
	public boolean isActive() {
		return this.active;
	}

	@Override
	public boolean isStarted() {
		return this.isActivated;
	}

	@Override
	public final void setName(String name) {
		this.name = name;
	}

	@Override
	public final void setId(String id) {
		this.id = id;
	}

	@Override
	public final void setActive(boolean active) {
		this.active = active;
	}

	@Override
	public final void setStarted(boolean isActive) {
		this.isActivated = isActive;
	}

	@Override
	public final void setSubcomponents(
			List<ISubComponent<EventHandler<Event>, Event, Object>> subComponents) {
		this.subcomponents = subComponents;

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final void setMessageQueue(
			BlockingQueue<IAction<Event, Object>> messageQueue) {
		this.globalMessageQueue = messageQueue;
	}

	
	private void log(final String message) {
		if (this.logger.isLoggable(Level.FINE)) {
			this.logger.fine(">> " + message);
		}
	}

	/**
	 * register components at componentHandler
	 * 
	 * @param <M>
	 * @param components
	 */
	private <M extends ISubComponent<EventHandler<Event>, Event, Object>> void registerSubcomponents(
			final List<M> components) {
		for (int i = 0; i < components.size(); i++) {
			final M component = components.get(i);
			this.registerComponent(component);
		}
	}

	@Override
	public List<ISubComponent<EventHandler<Event>, Event, Object>> getSubcomponents() {
		return this.subcomponents;
	}

	@Override
	public final IPerspectiveLayout<? extends Node, Node> getIPerspectiveLayout() {
		return this.perspectiveLayout;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final IActionListener<EventHandler<Event>, Event, Object> getActionListener(
			Object message) {
		final FXAction action = new FXAction(this.id);
		action.setMessage(message);
		return new FXActionListener(action, this.globalMessageQueue);
	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	public final IActionListener<EventHandler<Event>, Event, Object> getActionListener(
			String targetId, Object message) {
		final FXAction action = new FXAction(this.id);
		action.addMessage(targetId, message);
		return new FXActionListener(action, this.globalMessageQueue);
	}

	@Override
	public String getName() {
		return this.name;
	}

	@Override
	public String getId() {
		return this.id;
	}

	@Override
	public final BlockingQueue<ISubComponent<EventHandler<Event>, Event, Object>> getComponentDelegateQueue() {
		return this.componentDelegateQueue;
	}
	
	@Override
	public final BlockingQueue<IDelegateDTO<Event, Object>> getMessageDelegateQueue() {
		return this.messageDelegateQueue;
	}

	@Override
	public final BlockingQueue<IAction<Event, Object>> getComponentsMessageQueue() {
		return this.componentCoordinator.getMessageQueue();

	}
	
	

	@Override
	public IComponentHandler<ISubComponent<EventHandler<Event>, Event, Object>, IAction<Event, Object>> getComponentHandler() {
		return componentHandler;
	}
}
