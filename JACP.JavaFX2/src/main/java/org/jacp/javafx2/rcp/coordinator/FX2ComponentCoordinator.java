/************************************************************************
 * 
 * Copyright (C) 2010 - 2012
 *
 * [FX2ComponentCoordinator.java]
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
package org.jacp.javafx2.rcp.coordinator;

import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CopyOnWriteArrayList;

import javafx.event.Event;
import javafx.event.EventHandler;

import org.jacp.api.action.IAction;
import org.jacp.api.action.IDelegateDTO;
import org.jacp.api.component.IComponent;
import org.jacp.api.component.ISubComponent;
import org.jacp.api.coordinator.IComponentCoordinator;
import org.jacp.api.handler.IComponentHandler;
import org.jacp.javafx2.rcp.util.FX2Util;

/**
 * observe component actions and delegates to correct component
 * 
 * @author Andy Moncsek
 */
public class FX2ComponentCoordinator extends AFX2Coordinator implements
		IComponentCoordinator<EventHandler<Event>, Event, Object> {

	private List<ISubComponent<EventHandler<Event>, Event, Object>> components = new CopyOnWriteArrayList<ISubComponent<EventHandler<Event>, Event, Object>>();
	private IComponentHandler<ISubComponent<EventHandler<Event>, Event, Object>, IAction<Event, Object>> componentHandler;
	private BlockingQueue<IDelegateDTO<Event, Object>> delegateQueue;
	private String parentId;

	@Override
	public void addComponent(
			ISubComponent<EventHandler<Event>, Event, Object> component) {
		component.setMessageQueue(this.getMessageQueue());
		this.components.add(component);
	}

	@Override
	public void removeComponent(
			ISubComponent<EventHandler<Event>, Event, Object> component) {
		component.setMessageQueue(null);
		this.components.remove(component);

	}

	@Override
	public void handleMessage(String targetId, IAction<Event, Object> action) {
		synchronized (action) {
			final ISubComponent<EventHandler<Event>, Event, Object> component = FX2Util
					.getObserveableById(FX2Util.getTargetComponentId(targetId),
							this.components);
			this.log(" //1.1// component message to: " + action.getTargetId());
			if (component != null) {
				this.log(" //1.1.1// component HIT: " + action.getTargetId());
				this.handleComponentHit(targetId, action, component);
			} // End if
			else {
				// delegate message to parent perspective
				this.log(" //1.1.1// component MISS: " + action.getTargetId());
				this.handleComponentMiss(targetId, action);
			} // End else
		} // End synchronized
	}

	/**
	 * handle method if component was found in local context
	 * 
	 * @param targetId
	 * @param action
	 * @param component
	 */
	private void handleComponentHit(final String targetId,
			final IAction<Event, Object> action,
			final ISubComponent<EventHandler<Event>, Event, Object> component) {
		final IAction<Event, Object> actionClone = FX2Util.getValidAction(action,
				targetId, action.getMessageList().get(targetId));
		if (component.isActive()) {
			this.log(" //1.1.1.1// component HIT handle ACTIVE: "
					+ action.getTargetId());
			this.handleActive(component, actionClone);
		} // End if
		else {
			this.log(" //1.1.1.1// component HIT handle IN-ACTIVE: "
					+ action.getTargetId());
			this.handleInActive(component, actionClone);
		} // End else
	}

	/**
	 * handle method if no valid component found; delegate to responsible
	 * perspective
	 * 
	 * @param targetId
	 * @param action
	 */
	private void handleComponentMiss(final String targetId,
			final IAction<Event, Object> action) {
		final boolean local = FX2Util.isLocalMessage(targetId);
		if (!local) {
			final String targetPerspectiveId = FX2Util
					.getTargetPerspectiveId(targetId);
			if (parentId.equals(targetPerspectiveId)) {
				// TODO target is in same perspective but component was not
				// found previously
				throw new UnsupportedOperationException(
						"invalid component id handling not supported yet.");
			}
		}
		// possible message to perspective
		delegateMessageToCorrectPerspective(targetId, action,
				this.delegateQueue);

	}

	

	@Override
	public final <P extends IComponent<EventHandler<Event>, Event, Object>> void handleActive(
			final P component, final IAction<Event, Object> action) {
		this.log(" //1.1.1.1.1// component " + action.getTargetId()
				+ " delegate to perspective: " + parentId);
		this.componentHandler.handleAndReplaceComponent(action,
				(ISubComponent<EventHandler<Event>, Event, Object>) component);

	}

	@Override
	public final <P extends IComponent<EventHandler<Event>, Event, Object>> void handleInActive(
			final P component, final IAction<Event, Object> action) {
		component.setActive(true);
		this.componentHandler.initComponent(action,
				(ISubComponent<EventHandler<Event>, Event, Object>) component);

	}

	@SuppressWarnings("unchecked")
	@Override
	public IComponentHandler<ISubComponent<EventHandler<Event>, Event, Object>, IAction<Event, Object>> getComponentHandler() {
		return componentHandler;
	}

	@SuppressWarnings("unchecked")
	@Override
	public <P extends IComponent<EventHandler<Event>, Event, Object>> void setComponentHandler(
			IComponentHandler<P, IAction<Event, Object>> handler) {
		this.componentHandler = (IComponentHandler<ISubComponent<EventHandler<Event>, Event, Object>, IAction<Event, Object>>) handler;

	}

	@Override
	public void setMessageDelegateQueue(
			BlockingQueue<IDelegateDTO<Event, Object>> delegateQueue) {
		this.delegateQueue = delegateQueue;
		
	}

	@Override
	public void setParentId(String parentId) {
		this.parentId = parentId;
		
	}
}
