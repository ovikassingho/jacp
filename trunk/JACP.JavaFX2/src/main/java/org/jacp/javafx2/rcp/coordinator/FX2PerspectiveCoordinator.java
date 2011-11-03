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
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.stage.StageStyle;

import org.jacp.api.action.IAction;
import org.jacp.api.component.IComponent;
import org.jacp.api.component.ISubComponent;
import org.jacp.api.componentLayout.Layout;
import org.jacp.api.coordinator.IPerspectiveCoordinator;
import org.jacp.api.perspective.IPerspective;
import org.jacp.api.workbench.IWorkbench;
import org.jacp.javafx2.rcp.action.FX2Action;
import org.jacp.javafx2.rcp.workbench.AFX2Workbench;

/**
 * Observe perspectives and delegates message to correct component
 * 
 * @author Andy Moncsek
 */
public class FX2PerspectiveCoordinator extends AFX2Coordinator implements
		IPerspectiveCoordinator<EventHandler<Event>, Event, Object> {

	private final IWorkbench<?, Node, EventHandler<Event>, Event, Object, StageStyle> workbench;
	private List<IPerspective<EventHandler<Event>, Event, Object>> perspectives = new CopyOnWriteArrayList<IPerspective<EventHandler<Event>, Event, Object>>();

	public FX2PerspectiveCoordinator(
			final IWorkbench<?, Node, EventHandler<Event>, Event, Object, StageStyle> workbench) {
		setDaemon(true);
		this.workbench = workbench;
	}

	public final Map<Layout, Node> getBars() {
		return workbench.getWorkbenchLayout().getToolBars();
	}

	/**
	 * returns default workbench menu
	 * 
	 */
	public final Node getMenu() {
		return workbench.getDefaultMenu();
	}

	@Override
	public void handleMessage(final String target,
			final IAction<Event, Object> action) {
		final IPerspective<EventHandler<Event>, Event, Object> perspective = getObserveableById(
				getTargetPerspectiveId(target), perspectives);
		if (perspective != null) {
			final IAction<Event, Object> actionClone = getValidAction(action,
					target, action.getMessageList().get(target));
			handleComponentHit(target, actionClone, perspective);
		} // End if
		else {
			// TODO implement missing perspective handling!!
			throw new UnsupportedOperationException(
					"No responsible perspective found. Handling not implemented yet.");
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
			handleMessageToActivePerspective(target, action, perspective);
		} // End if
		else {
			// perspective was not active and will be initialized
			log(" //1.1.1.1// perspective HIT handle IN-ACTIVE: "
					+ action.getTargetId());
			handleInActive(perspective, action);
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
		log(" //1.1.1.1// perspective HIT handle ACTIVE: "
				+ action.getTargetId());
		if (isLocalMessage(target)) {
			// message is addressing perspective
			handleActive(perspective, action);
		} // End if
		else {
			// delegate to addressed component
			perspective.delegateComponentMassege(target, action);
		} // End else
	}

	@Override
	public void delegateMessage(final String target, final IAction<Event, Object> action) {
		// Find local Target; if target is perspective handle target or
		// delegate
		// message to responsible component observer
		if (isLocalMessage(target)) {
			handleMessage(target, action);
		} // End if
		else {
			callComponentDelegate(target, action);
		} // End else
	}

	/**
	 * delegate to responsible componentObserver in correct perspective
	 * 
	 * @param target
	 * @param action
	 */
	private void callComponentDelegate(final String target,
			final IAction<Event, Object> action) {
		final IPerspective<EventHandler<Event>, Event, Object> perspective = getObserveableById(
				getTargetPerspectiveId(target), perspectives);
		// TODO REMOVE null handling... use DUMMY instead (maybe like
		// Collections.EMPTY...)
		if (perspective != null) {
			if (!perspective.isActive()) {
				handleInActive(perspective, action);
			} // End inner if
			else {
				perspective.delegateComponentMassege(target, action);
			} // End else

		} // End if

	}

	@Override
	public <P extends IComponent<EventHandler<Event>, Event, Object>> void handleActive(
			final P component, final IAction<Event, Object> action) {
		((AFX2Workbench) workbench).handleAndReplaceComponent(action,
				(IPerspective<EventHandler<Event>, Event, Object>) component);
	}

	@Override
	public <P extends IComponent<EventHandler<Event>, Event, Object>> void handleInActive(
			final P component, final IAction<Event, Object> action) {
		component.setActive(true);
		((AFX2Workbench) workbench).initComponent(action,
				(IPerspective<EventHandler<Event>, Event, Object>) component);
	}

	@Override
	public void delegateTargetChange(final String target,
			final ISubComponent<EventHandler<Event>, Event, Object> component) {
		// find responsible perspective
		final IPerspective<EventHandler<Event>, Event, Object> responsiblePerspective = getObserveableById(
				getTargetPerspectiveId(target), perspectives);
		// find correct target in perspective
		if (responsiblePerspective != null) {
			handleTargetHit(responsiblePerspective, component);

		} // End if
		else {
			handleTargetMiss();
		} // End else
	}

	/**
	 * handle component delegate when target was found
	 * 
	 * @param responsiblePerspective
	 * @param component
	 */
	private void handleTargetHit(
			final IPerspective<EventHandler<Event>, Event, Object> responsiblePerspective,
			final ISubComponent<EventHandler<Event>, Event, Object> component) {
		if (!responsiblePerspective.isActive()) {
			// 1. init perspective (do not register component before perspective
			// is active, otherwise component will be handled once again)
			handleInActive(responsiblePerspective,
					new FX2Action(responsiblePerspective.getId(),
							responsiblePerspective.getId(), "init"));
		} // End if
		addToActivePerspective(responsiblePerspective, component);
	}

	private void addToActivePerspective(
			final IPerspective<EventHandler<Event>, Event, Object> responsiblePerspective,
			final ISubComponent<EventHandler<Event>, Event, Object> component) {
		responsiblePerspective.addActiveComponent(component);
	}

	/**
	 * handle component delegate when no target found
	 */
	private void handleTargetMiss() {
		throw new UnsupportedOperationException(
				"No responsible perspective found. Handling not implemented yet.");
	}

	@Override
	public void addPerspective(
			final IPerspective<EventHandler<Event>, Event, Object> perspective) {
		perspective.setObserver(this);
		this.perspectives.add(perspective);
	}

	@Override
	public void removePerspective(
			final IPerspective<EventHandler<Event>, Event, Object> perspective) {
		perspective.setObserver(null);
		this.perspectives.remove(perspective);
	}

}
