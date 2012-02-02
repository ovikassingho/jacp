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

package org.jacp.swing.rcp.coordinator;

import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

import org.jacp.api.action.IAction;
import org.jacp.api.component.IComponent;
import org.jacp.api.component.ISubComponent;
import org.jacp.api.componentLayout.Layout;
import org.jacp.api.coordinator.IPerspectiveCoordinator;
import org.jacp.api.perspective.IPerspective;
import org.jacp.api.workbench.IWorkbench;
import org.jacp.swing.rcp.action.SwingAction;
import org.jacp.swing.rcp.workbench.ASwingWorkbench;

/**
 * Observe perspective actions and delegates message to correct component
 * 
 * @author Andy Moncsek
 */
public class SwingPerspectiveCoordinator extends ACoordinator implements
		IPerspectiveCoordinator<ActionListener, ActionEvent, Object> {

	private final List<IPerspective<ActionListener, ActionEvent, Object>> perspectives = new CopyOnWriteArrayList<IPerspective<ActionListener, ActionEvent, Object>>();
	private final IWorkbench<?, Container, ActionListener, ActionEvent, Object> workbench;

	public SwingPerspectiveCoordinator(
			final IWorkbench<?, Container, ActionListener, ActionEvent, Object> workbench) {
		setDaemon(true);
		this.workbench = workbench;
	}

	@Override
	public final void addPerspective(
			final IPerspective<ActionListener, ActionEvent, Object> perspective) {
		perspective.setObserver(this);
		perspectives.add(perspective);
	}

	@Override
	public final void removePerspective(
			final IPerspective<ActionListener, ActionEvent, Object> perspective) {
		perspective.setObserver(null);
		perspectives.remove(perspective);
	}

	public final Map<Layout, Container> getBars() {
		return workbench.getWorkbenchLayout().getToolBars();
	}

	public final Container getMenu() {
		return workbench.getDefaultMenu();
	}

	/**
	 * handles message to perspective be perspective or subcomponent !! watch
	 * out
	 * 
	 * @param message
	 * @param action
	 */
	@Override
	public final void handleMessage(final String target,
			final IAction<ActionEvent, Object> action) {
		final IPerspective<ActionListener, ActionEvent, Object> perspective = getObserveableById(
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
	private void handleComponentHit(final String target,
			final IAction<ActionEvent, Object> action,
			final IPerspective<ActionListener, ActionEvent, Object> perspective) {
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
	private void handleMessageToActivePerspective(final String target,
			final IAction<ActionEvent, Object> action,
			final IPerspective<ActionListener, ActionEvent, Object> perspective) {
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

	@Override
	public final void delegateMessage(final String target,
			final IAction<ActionEvent, Object> action) {
		// Find local Target; if target is perspective handle target or
		// delegate
		// message to responsible component observer
		if (isLocalMessage(target)) {
			handleMessage(target, action);
		} else {
			callComponentDelegate(target, action);
		}

	}

	@Override
	public final void delegateTargetChange(final String target,
			final ISubComponent<ActionListener, ActionEvent, Object> component) {
		// find responsible perspective
		final IPerspective<ActionListener, ActionEvent, Object> responsiblePerspective = getObserveableById(
				getTargetPerspectiveId(target), perspectives);
		// find correct target in perspective
		if (responsiblePerspective != null) {
			handleTargetHit(responsiblePerspective, component);

		} else {
			handleTargetMiss();
		}

	}

	/**
	 * handle component delegate when target was found
	 * 
	 * @param responsiblePerspective
	 * @param component
	 */
	private void handleTargetHit(
			final IPerspective<ActionListener, ActionEvent, Object> responsiblePerspective,
			final ISubComponent<ActionListener, ActionEvent, Object> component) {
		if (!responsiblePerspective.isActive()) {
			// 1. init perspective (do not register component before perspective
			// is active, otherwise component will be handled once again)
			handleInActive(responsiblePerspective,
					new SwingAction(responsiblePerspective.getId(),
							responsiblePerspective.getId(), "init"));
		}
		addToActivePerspective(responsiblePerspective, component);
	}

	/**
	 * add active component to perspective
	 * 
	 * @param responsiblePerspective
	 * @param component
	 */
	private void addToActivePerspective(
			final IPerspective<ActionListener, ActionEvent, Object> responsiblePerspective,
			final ISubComponent<ActionListener, ActionEvent, Object> component) {
		responsiblePerspective.addActiveComponent(component);
	}

	/**
	 * handle component delegate when no target found
	 */
	private void handleTargetMiss() {
		throw new UnsupportedOperationException(
				"No responsible perspective found. Handling not implemented yet.");
	}

	/**
	 * delegate to responsible componentObserver in correct perspective
	 * 
	 * @param target
	 * @param action
	 */
	private void callComponentDelegate(final String target,
			final IAction<ActionEvent, Object> action) {
		final IPerspective<ActionListener, ActionEvent, Object> perspective = getObserveableById(
				getTargetPerspectiveId(target), perspectives);
		// TODO REMOVE null handling... use DUMMY instead (maybe like
		// Collections.EMPTY...)
		if (perspective != null) {
			if (!perspective.isActive()) {
				handleInActive(perspective, action);
			} else {
				perspective.delegateComponentMassege(target, action);
			}

		}

	}

	@Override
	public final <M extends IComponent<ActionListener, ActionEvent, Object>> void handleActive(
			final M component, final IAction<ActionEvent, Object> action) {
		((ASwingWorkbench) workbench).handleAndReplaceComponent(action,
				(IPerspective<ActionListener, ActionEvent, Object>) component);

	}

	@Override
	public final <M extends IComponent<ActionListener, ActionEvent, Object>> void handleInActive(
			final M component, final IAction<ActionEvent, Object> action) {
		component.setActive(true);
		((ASwingWorkbench) workbench).initComponent(action,
				(IPerspective<ActionListener, ActionEvent, Object>) component);

	}

}
