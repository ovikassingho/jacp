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

import javafx.application.Platform;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.Node;

import org.jacp.api.action.IAction;
import org.jacp.api.component.IComponent;
import org.jacp.api.coordinator.IPerspectiveCoordinator;
import org.jacp.api.perspective.IPerspective;
import org.jacp.api.workbench.IWorkbench;
import org.jacp.javafx2.rcp.workbench.AFX2Workbench;

/**
 * Observe perspectives and delegates message to correct component
 * 
 * @author Andy Moncsek
 */
public class FX2PerspectiveCoordinator extends AFX2Coordinator implements
		IPerspectiveCoordinator<EventHandler<Event>, Event, Object> {

	private final IWorkbench<Node, EventHandler<Event>, Event, Object> workbench;
	private final List<IPerspective<EventHandler<Event>, Event, Object>> perspectives = new CopyOnWriteArrayList<IPerspective<EventHandler<Event>, Event, Object>>();

	public FX2PerspectiveCoordinator(
			final IWorkbench<Node, EventHandler<Event>, Event, Object> workbench) {
		this.setDaemon(true);
		this.workbench = workbench;
	}

	@Override
	public void handleMessage(final String target,
			final IAction<Event, Object> action) {
		final IPerspective<EventHandler<Event>, Event, Object> perspective = this
				.getObserveableById(this.getTargetPerspectiveId(target),
						this.perspectives);
		if (perspective != null) {
			final IAction<Event, Object> actionClone = this.getValidAction(
					action, target, action.getMessageList().get(target));
			this.handleComponentHit(target, actionClone, perspective);
		} // End if
		else {
			// TODO implement missing perspective handling!!
			throw new UnsupportedOperationException(
					"No responsible perspective found. Handling not implemented yet. target: "+target+" perspectives: "+perspectives);
		} // End else
	}

	/**
	 * handle message target hit
	 * 
	 * @param target
	 * @param action
	 */
	private void handleComponentHit(final String target,
			final IAction<Event, Object> action,
			final IPerspective<EventHandler<Event>, Event, Object> perspective) {
		if (perspective.isActive()) {
			this.handleMessageToActivePerspective(target, action, perspective);
		} // End if
		else {
			// perspective was not active and will be initialized
			this.log(" //1.1.1.1// perspective HIT handle IN-ACTIVE: "
					+ action.getTargetId());
			this.handleInActive(perspective, action);
		} // End else
	}

	/**
	 * handle message to active perspective; check if target is perspective or
	 * component
	 * 
	 * @param target
	 * @param action
	 * @param perspective
	 */
	private void handleMessageToActivePerspective(final String target,
			final IAction<Event, Object> action,
			final IPerspective<EventHandler<Event>, Event, Object> perspective) {
		// if perspective already active handle perspective and replace
		// with newly created layout component in workbench
		this.log(" //1.1.1.1// perspective HIT handle ACTIVE: "
				+ action.getTargetId());
		if (this.isLocalMessage(target)) {
			// message is addressing perspective
			this.handleActive(perspective, action);
		} // End if
		else {
			// delegate to addressed component
			perspective.delegateMassege(target, action);
		} // End else
	}

	

	@Override
	public <P extends IComponent<EventHandler<Event>, Event, Object>> void handleActive(
			final P component, final IAction<Event, Object> action) {
		Platform.runLater(new Runnable() {
			@Override
			public final void run() {
				((AFX2Workbench) FX2PerspectiveCoordinator.this.workbench)
						.handleAndReplaceComponent(
								action,
								(IPerspective<EventHandler<Event>, Event, Object>) component);
			} // End run
		} // End runnable
		); // End runlater
	}

	@Override
	public <P extends IComponent<EventHandler<Event>, Event, Object>> void handleInActive(
			final P component, final IAction<Event, Object> action) {
		component.setActive(true);
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				((AFX2Workbench) FX2PerspectiveCoordinator.this.workbench)
						.initComponent(
								action,
								(IPerspective<EventHandler<Event>, Event, Object>) component);
			}
		});
	}

	

	@Override
	public void addPerspective(
			final IPerspective<EventHandler<Event>, Event, Object> perspective) {
		perspective.setMessageQueue(this.getMessages());
		this.perspectives.add(perspective);
	}

	@Override
	public void removePerspective(
			final IPerspective<EventHandler<Event>, Event, Object> perspective) {
		perspective.setMessageQueue(null);
		this.perspectives.remove(perspective);
	}

}
