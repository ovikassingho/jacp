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
import org.jacp.api.action.IDelegateDTO;
import org.jacp.api.annotations.CallbackComponent;
import org.jacp.api.annotations.Component;
import org.jacp.api.component.IPerspectiveView;
import org.jacp.api.component.ISubComponent;
import org.jacp.api.componentLayout.IPerspectiveLayout;
import org.jacp.api.coordinator.IComponentCoordinator;
import org.jacp.api.handler.IComponentHandler;
import org.jacp.javafx.rcp.action.FXAction;
import org.jacp.javafx.rcp.component.AComponent;
import org.jacp.javafx.rcp.component.ASubComponent;
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
public abstract class AFXPerspective extends AComponent implements
		IPerspectiveView<Node, EventHandler<Event>, Event, Object> {
	private final Logger logger = Logger.getLogger(this.getClass().getName());
	private List<ISubComponent<EventHandler<Event>, Event, Object>> subcomponents;
	private IComponentHandler<ISubComponent<EventHandler<Event>, Event, Object>, IAction<Event, Object>> componentHandler;
	private BlockingQueue<ISubComponent<EventHandler<Event>, Event, Object>> componentDelegateQueue;
	private BlockingQueue<IDelegateDTO<Event, Object>> messageDelegateQueue;
	private IComponentCoordinator<EventHandler<Event>, Event, Object> componentCoordinator;
	private final IPerspectiveLayout<Node, Node> perspectiveLayout = new FXPerspectiveLayout();

	@Override
	public final void init(
			final BlockingQueue<ISubComponent<EventHandler<Event>, Event, Object>> componentDelegateQueue,
			final BlockingQueue<IDelegateDTO<Event, Object>> messageDelegateQueue,
			final BlockingQueue<IAction<Event, Object>> globalMessageQueue) {
		this.componentDelegateQueue = componentDelegateQueue;
		this.messageDelegateQueue = messageDelegateQueue;
		this.globalMessageQueue = globalMessageQueue;

	}

	@Override
	public final <C> C handle(final IAction<Event, Object> action) {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void postInit(
			final IComponentHandler<ISubComponent<EventHandler<Event>, Event, Object>, IAction<Event, Object>> componentHandler) {
		// init component handler
		this.componentHandler = componentHandler;
		this.componentCoordinator = new FXComponentCoordinator();
		((FXComponentCoordinator) this.componentCoordinator).start();
		this.componentCoordinator.setComponentHandler(this.componentHandler);
		this.componentCoordinator
				.setMessageDelegateQueue(this.messageDelegateQueue);
		this.componentCoordinator.setParentId(this.getId());
		this.registerSubcomponents(this.subcomponents);
	}

	/**
	 * handle perspective method to initialize the perspective and the layout
	 * 
	 * @param action
	 * @param perspectiveLayout
	 */
	public abstract void handlePerspective(IAction<Event, Object> action,
			final FXPerspectiveLayout perspectiveLayout);

	@Override
	public void handlePerspective(final IAction<Event, Object> action) {
		this.handlePerspective(action,
				(FXPerspectiveLayout) this.perspectiveLayout);

	}

	@Override
	public final void registerComponent(
			final ISubComponent<EventHandler<Event>, Event, Object> component) {

		this.handleMetaAnnotation(component);
		this.log("register component: " + component.getId());

		component.initEnv(this.getId(),
				this.componentCoordinator.getMessageQueue());
		this.componentCoordinator.addComponent(component);
		if (!this.subcomponents.contains(component)) {
			this.subcomponents.add(component);
		}

	}

	/**
	 * set meta attributes defined in annotations
	 * 
	 * @param component
	 */
	private void handleMetaAnnotation(
			final ISubComponent<EventHandler<Event>, Event, Object> component) {
		final Component componentAnnotation = component.getClass()
				.getAnnotation(Component.class);
		if (componentAnnotation != null) {
			FXUtil.setPrivateMemberValue(AComponent.class, component,
					FXUtil.ACOMPONENT_ID, componentAnnotation.id());
			FXUtil.setPrivateMemberValue(AComponent.class, component,
					FXUtil.ACOMPONENT_ACTIVE, componentAnnotation.active());
			FXUtil.setPrivateMemberValue(AComponent.class, component,
					FXUtil.ACOMPONENT_NAME, componentAnnotation.name());
			FXUtil.setPrivateMemberValue(ASubComponent.class, component,
					FXUtil.ACOMPONENT_EXTARGET,
					componentAnnotation.defaultExecutionTarget());
			this.log("register component with annotations : "
					+ componentAnnotation.id());
		} else {
			final CallbackComponent callbackAnnotation = component.getClass()
					.getAnnotation(CallbackComponent.class);
			if (callbackAnnotation != null) {
				FXUtil.setPrivateMemberValue(AComponent.class, component,
						FXUtil.ACOMPONENT_ID, callbackAnnotation.id());
				FXUtil.setPrivateMemberValue(AComponent.class, component,
						FXUtil.ACOMPONENT_ACTIVE, callbackAnnotation.active());
				FXUtil.setPrivateMemberValue(AComponent.class, component,
						FXUtil.ACOMPONENT_NAME, callbackAnnotation.name());
				this.log("register CallbackComponent with annotations : "
						+ callbackAnnotation.id());
			}
		}
	}

	@Override
	public final void unregisterComponent(
			final ISubComponent<EventHandler<Event>, Event, Object> component) {
		this.log("unregister component: " + component.getId());
		component.initEnv(null, null);
		this.componentCoordinator.removeComponent(component);
		if (this.subcomponents.contains(component)) {
			this.subcomponents.remove(component);
		}
	}

	@Override
	public final void initComponents(final IAction<Event, Object> action) {
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
				this.componentHandler.initComponent(action, component);
			} // else END
			else if (component.isActive() && !component.isStarted()) {
				this.log("3.4.4.2: subcomponent init with default action");
				this.componentHandler.initComponent(
						new FXAction(component.getId(), component.getId(),
								"init"), component);
			} // if END

		} // for END
	}

	@Override
	public final void setSubcomponents(
			final List<ISubComponent<EventHandler<Event>, Event, Object>> subComponents) {
		this.subcomponents = subComponents;

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
		return this.componentHandler;
	}
}
