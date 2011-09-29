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

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;

import org.jacp.api.action.IAction;
import org.jacp.api.component.IComponent;
import org.jacp.api.component.ISubComponent;
import org.jacp.api.coordinator.IPerspectiveCoordinator;
import org.jacp.api.perspective.IPerspective;
import org.jacp.api.workbench.IWorkbench;

/**
 * Observe perspective actions and delegates message to correct component
 * 
 * @author Andy Moncsek
 */
public class FX2PerspectiveCoordinator extends AFX2Coordinator implements
		IPerspectiveCoordinator<EventHandler<ActionEvent>, ActionEvent, Object> {

	private final IWorkbench<?, Node, EventHandler<ActionEvent>, ActionEvent, Object> workbench;
	private List<IPerspective<EventHandler<ActionEvent>, ActionEvent, Object>> perspectives = new CopyOnWriteArrayList<IPerspective<EventHandler<ActionEvent>, ActionEvent, Object>>();

	public FX2PerspectiveCoordinator(
			final IWorkbench<?, Node, EventHandler<ActionEvent>, ActionEvent, Object> workbench) {
		setDaemon(true);
		this.workbench = workbench;
	}

	public void handleMessage(String target, IAction<ActionEvent, Object> action) {
		final IPerspective<EventHandler<ActionEvent>, ActionEvent, Object> perspective = getObserveableById(
				getTargetPerspectiveId(target), perspectives);
		if (perspective != null) {
			final IAction<ActionEvent, Object> actionClone = getValidAction(
					action, target, action.getMessageList().get(target));
			handleComponentHit(target, actionClone, perspective);
		} else {
			// TODO implement missing perspective handling!!
			throw new UnsupportedOperationException(
					"No responsible perspective found. Handling not implemented yet.");
		}
	}

	/**
	 * handle message target hit
	 * 
	 * @param target
	 * @param action
	 */
	private void handleComponentHit(
			final String target,
			final IAction<ActionEvent, Object> action,
			final IPerspective<EventHandler<ActionEvent>, ActionEvent, Object> perspective) {
		if (perspective.isActive()) {
			handleMessageToActivePerspective(target, action, perspective);
		} else {
			// perspective was not active and will be initialized
			log(" //1.1.1.1// perspective HIT handle IN-ACTIVE: "
					+ action.getTargetId());
			handleWorkspaceModeSpecific();
			handleInActive(perspective, action);
		}
	}

	/**
	 * handle message to active perspective; check if target is perspective or
	 * component
	 * 
	 * @param target
	 * @param action
	 * @param perspective
	 */
	private void handleMessageToActivePerspective(
			final String target,
			final IAction<ActionEvent, Object> action,
			final IPerspective<EventHandler<ActionEvent>, ActionEvent, Object> perspective) {
		// if perspective already active handle perspective and replace
		// with newly created layout component in workbench
		log(" //1.1.1.1// perspective HIT handle ACTIVE: "
				+ action.getTargetId());
		if (isLocalMessage(target)) {
			// message is addressing perspective
			handleWorkspaceModeSpecific();
			handleActive(perspective, action);
		} else {
			// delegate to addressed component
			perspective.delegateComponentMassege(target, action);
		}
	}

	/**
	 * handle workspace mode specific actions. When working in single pain mode
	 * all components are deactivated and only one (the active) will be
	 * activated; when working in window mode all components must be visible
	 */
	private void handleWorkspaceModeSpecific() {
		switch (workbench.getWorkbenchLayout().getWorkspaceMode()) {
		case WINDOWED_PANE:
			workbench.enableComponents();
			break;
		default:
			workbench.disableComponents();
		}
	}

	public void delegateMessage(String target,
			IAction<ActionEvent, Object> action) {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	public <P extends IComponent<EventHandler<ActionEvent>, ActionEvent, Object>> void handleActive(
			P component, IAction<ActionEvent, Object> action) {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	public <P extends IComponent<EventHandler<ActionEvent>, ActionEvent, Object>> void handleInActive(
			P component, IAction<ActionEvent, Object> action) {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	public void delegateTargetChange(
			String target,
			ISubComponent<EventHandler<ActionEvent>, ActionEvent, Object> component) {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	public void addPerspective(
			IPerspective<EventHandler<ActionEvent>, ActionEvent, Object> perspective) {
		perspective.setObserver(this);
		this.perspectives.add(perspective);
	}

	public void removePerspective(
			IPerspective<EventHandler<ActionEvent>, ActionEvent, Object> perspective) {
		perspective.setObserver(null);
		this.perspectives.remove(perspective);
	}

}
