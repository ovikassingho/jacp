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
package org.jacp.javafx2.rcp.coordinator;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import javafx.event.Event;
import javafx.event.EventHandler;

import org.jacp.api.action.IAction;
import org.jacp.api.component.IComponent;
import org.jacp.api.component.ISubComponent;
import org.jacp.api.coordinator.IComponentCoordinator;
import org.jacp.api.coordinator.IDalegator;
import org.jacp.api.perspective.IPerspective;

/**
 * observe component actions and delegates to correct component
 * 
 * @author Andy Moncsek
 */
public class FX2ComponentCoordinator extends AFX2Coordinator implements
		IComponentCoordinator<EventHandler<Event>, Event, Object>, IDalegator<EventHandler<Event>, Event, Object> {

	private List<ISubComponent<EventHandler<Event>, Event, Object>> components = new CopyOnWriteArrayList<ISubComponent<EventHandler<Event>, Event, Object>>();
	private final IPerspective<EventHandler<Event>, Event, Object> perspective;

	public FX2ComponentCoordinator(
			final IPerspective<EventHandler<Event>, Event, Object> perspective) {
		this.setDaemon(true);
		this.perspective = perspective;
	}


	@Override
	public void addComponent(
			ISubComponent<EventHandler<Event>, Event, Object> component) {
		component.setMessageQueue(this.getMessages()); 
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
			final ISubComponent<EventHandler<Event>, Event, Object> component = this
					.getObserveableById(this.getTargetComponentId(targetId),
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
		final IAction<Event, Object> actionClone = this.getValidAction(action,
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
		final boolean local = this.isLocalMessage(targetId);
		if (!local) {
			final String targetPerspectiveId = this
					.getTargetPerspectiveId(targetId);
			if (this.perspective.getId().equals(targetPerspectiveId)) {
				// TODO target is in same perspective but component was not
				// found previously
				throw new UnsupportedOperationException(
						"invalid component id handling not supported yet.");
			}
			// delegate to parent perspective, then find responsible
			// perspective
			this.perspective.delegateMassege(targetId, action);

		} else {
			// possible message to perspective
			this.perspective.delegateMassege(targetId, action);
		}
	}

	@Override
	public void delegateMessage(String target, IAction<Event, Object> action) {
		this.handleMessage(target, action);
	}

	@Override
	public final <P extends IComponent<EventHandler<Event>, Event, Object>> void handleActive(
			final P component, final IAction<Event, Object> action) {
		this.log(" //1.1.1.1.1// component " + action.getTargetId()
				+ " delegate to perspective: " + this.perspective.getId());
		this.perspective.handleAndReplaceComponent(action,
				(ISubComponent<EventHandler<Event>, Event, Object>) component);

	}

	@Override
	public final <P extends IComponent<EventHandler<Event>, Event, Object>> void handleInActive(
			final P component, final IAction<Event, Object> action) {
		component.setActive(true);
		this.perspective.initComponent(action,
				(ISubComponent<EventHandler<Event>, Event, Object>) component);

	}

	@Override
	public void delegateTargetChange(String target,
			ISubComponent<EventHandler<Event>, Event, Object> component) {
		throw new UnsupportedOperationException("Not supported yet.");
	}
	
	
	/**
	 * returns associated perspective
	 * @return
	 */
	@Override
	public final IPerspective<EventHandler<Event>, Event, Object> getParentPerspective(){
		return this.perspective;
	}
}
