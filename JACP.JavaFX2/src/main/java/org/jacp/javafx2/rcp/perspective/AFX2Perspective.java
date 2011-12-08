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

package org.jacp.javafx2.rcp.perspective;

import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.Node;

import org.jacp.api.action.IAction;
import org.jacp.api.action.IActionListener;
import org.jacp.api.component.IDelegateDTO;
import org.jacp.api.component.IExtendedComponent;
import org.jacp.api.component.ILayoutAbleComponent;
import org.jacp.api.component.ISubComponent;
import org.jacp.api.componentLayout.IBaseLayout;
import org.jacp.api.componentLayout.IPerspectiveLayout;
import org.jacp.api.coordinator.IComponentCoordinator;
import org.jacp.api.handler.IComponentHandler;
import org.jacp.api.launcher.Launcher;
import org.jacp.api.perspective.IPerspective;
import org.jacp.javafx2.rcp.action.FX2Action;
import org.jacp.javafx2.rcp.action.FX2ActionListener;
import org.jacp.javafx2.rcp.component.AFX2Component;
import org.jacp.javafx2.rcp.componentLayout.FX2ComponentLayout;
import org.jacp.javafx2.rcp.componentLayout.FX2PerspectiveLayout;
import org.jacp.javafx2.rcp.coordinator.FX2ComponentCoordinator;
import org.jacp.javafx2.rcp.util.FX2ComponentAddWorker;
import org.jacp.javafx2.rcp.util.FX2Util;

/**
 * represents a basic javafx2 perspective that handles subcomponents,
 * perspectives are not handled in thread so avoid long running tasks in
 * perspectives.
 * 
 * @author Andy Moncsek
 */
public abstract class AFX2Perspective implements
		IPerspective<EventHandler<Event>, Event, Object>,
		IExtendedComponent<Node>, ILayoutAbleComponent<Node> {
	private String id;
	private String name;
	private boolean active;
	private boolean isActivated = false;
	private final Logger logger = Logger.getLogger(this.getClass().getName());
	private Launcher<?> launcher;
	private List<ISubComponent<EventHandler<Event>, Event, Object>> subcomponents;
	private IComponentHandler<ISubComponent<EventHandler<Event>, Event, Object>, IAction<Event, Object>> componentHandler;

	private final FX2ComponentAddWorker addWorker = new FX2ComponentAddWorker();
	private BlockingQueue<IDelegateDTO<EventHandler<Event>, Event, Object>> delegateQueue;
	private IComponentCoordinator<EventHandler<Event>, Event, Object> componentCoordinator;
	private FX2ComponentLayout layout;
	private BlockingQueue<IAction<Event, Object>> globalMessageQueue;
	private final IPerspectiveLayout<Node, Node> perspectiveLayout = new FX2PerspectiveLayout();

	@Override
	public final void init(
			Launcher<?> launcher,
			final BlockingQueue<IDelegateDTO<EventHandler<Event>, Event, Object>> delegateQueue) {
		this.launcher = launcher;
		this.delegateQueue = delegateQueue;

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
		this.layout = (FX2ComponentLayout) layout;
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
		componentCoordinator = new FX2ComponentCoordinator();
		((FX2ComponentCoordinator) this.componentCoordinator).start();
		componentCoordinator.setComponentHandler(this.componentHandler);
		componentCoordinator.setDelegateQueue(this.delegateQueue);
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
	public abstract void onStartPerspective(final FX2ComponentLayout layout);

	/**
	 * Clean up menu, bars and other components on component teardown.
	 * 
	 * @param menuBar
	 * @param bars
	 */
	public abstract void onTearDownPerspective(final FX2ComponentLayout layout);
	/**
	 * handle perspective method to initialize the perspective and the layout
	 * @param action
	 * @param perspectiveLayout
	 */
	public abstract void handlePerspective(IAction<Event, Object> action,
			final FX2PerspectiveLayout perspectiveLayout);

	@Override
	public void handlePerspective(IAction<Event, Object> action) {
		this.handlePerspective(action,
				(FX2PerspectiveLayout) this.perspectiveLayout);

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
		final String targetId = FX2Util.getTargetComponentId(action
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
				componentHandler.initComponent(new FX2Action(component.getId(),
						component.getId(), "init"), component);
			} // if END

		} // for END
	}

	/**
	 * run at startup method in perspective
	 * 
	 * @param component
	 */
	private void runComponentOnStartupSequence(final AFX2Component component) {
		component.onStartComponent(this.layout);
	}

	@Override
	public final void addActiveComponent(
			ISubComponent<EventHandler<Event>, Event, Object> component) {
		// register new component at perspective
		this.registerComponent(component);
		if (component instanceof AFX2Component) {
			runComponentOnStartupSequence((AFX2Component) component);
			// add component ui root to correct target
			this.addComponentUIValue(this.getIPerspectiveLayout()
					.getTargetLayoutComponents(), component);
		}

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
	public String getName() {
		return this.name;
	}

	@Override
	public IPerspectiveLayout<? extends Node, Node> getIPerspectiveLayout() {
		return this.perspectiveLayout;
	}

	@Override
	public List<ISubComponent<EventHandler<Event>, Event, Object>> getSubcomponents() {
		return this.subcomponents;
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

	/**
	 * handles ui return value and it to perspective TODO check correctness of
	 * implementation
	 * 
	 * @param targetComponents
	 * @param component
	 */
	private void addComponentUIValue(final Map<String, Node> targetComponents,
			final ISubComponent<EventHandler<Event>, Event, Object> component) {
		addWorker.handleInApplicationThread(targetComponents,
				this.getDelegateQueue(), ((AFX2Component) component));
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
	public final BlockingQueue<IDelegateDTO<EventHandler<Event>, Event, Object>> getDelegateQueue() {
		return this.delegateQueue;
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
