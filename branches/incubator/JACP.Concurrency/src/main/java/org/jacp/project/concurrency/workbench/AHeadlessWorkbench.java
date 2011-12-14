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

package org.jacp.project.concurrency.workbench;



import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.jacp.api.action.IAction;
import org.jacp.api.component.IRootComponent;
import org.jacp.api.coordinator.IPerspectiveCoordinator;
import org.jacp.api.launcher.Launcher;
import org.jacp.api.perspective.IPerspective;
import org.jacp.api.workbench.IBase;
import org.jacp.project.concurrency.action.Action;
import org.jacp.project.concurrency.action.ActionListener;
import org.jacp.project.concurrency.action.Event;
import org.jacp.project.concurrency.coordinator.PerspectiveCoordinator;


/**
 * This class defines a headless workbench for JACP run time
 * 
 * @author Andy Moncsek
 * 
 */
public abstract class AHeadlessWorkbench
		implements
		IBase<ActionListener, Event, Object>,
		IRootComponent<IPerspective<ActionListener, Event, Object>, IAction<Event, Object>> {
	private List<IPerspective<ActionListener, Event, Object>> perspectives;
	private final IPerspectiveCoordinator<ActionListener, Event, Object> perspectiveCoordinator = new PerspectiveCoordinator(this);
	private Launcher<?> launcher;
	private final Logger logger = Logger.getLogger(this.getClass().getName());

	@Override
	public void setPerspectives(List<IPerspective<ActionListener, Event, Object>> perspectives) {
		this.perspectives = perspectives;

	}

	@Override
	public List<IPerspective<ActionListener, Event, Object>> getPerspectives() {
		return this.perspectives;
	}

	@Override
	public void init(Launcher<?> launcher) {
		this.launcher = launcher;
		log("1: init workbench");
		log("3: handle initialisation sequence");
		handleInitialisationSequence();

	}
	
	/**
	 * handles sequence for workbench size, menu bar, tool bar and perspective
	 * initialisation
	 */
	private void handleInitialisationSequence() {
		//TODO is necessary? 
	}

	@Override
	public void registerComponent(
			IPerspective<ActionListener, Event, Object> component) {
		component.init(launcher);
		perspectiveCoordinator.addPerspective(component);

	}

	@Override
	public void unregisterComponent(
			IPerspective<ActionListener, Event, Object> component) {
		perspectiveCoordinator.removePerspective(component);

	}

	@Override
	public void initComponents(IAction<Event, Object> action) {
		final List<IPerspective<ActionListener, Event, Object>> perspectivesTmp = getPerspectives();
		for (int i = 0; i < perspectivesTmp.size(); i++) {
			final IPerspective<ActionListener, Event, Object> perspective = perspectivesTmp
					.get(i);
			log("3.4.1: register component: " + perspective.getName());
			registerComponent(perspective);
		}

	}

	@Override
	public void initComponent(IAction<Event, Object> action,
			IPerspective<ActionListener, Event, Object> component) {
		log("3.4.3: perspective handle init");
		handlePerspectiveInitMethod(action, component);
		log("3.4.4: perspective init subcomponents");
		component.initComponents(action);

	}
	
	/**
	 * handles initialization with correct action; perspective can be
	 * initialized at application start or when called by an other component
	 * 
	 * @param action
	 * @param perspectiveLayout
	 * @param perspective
	 */
	private void handlePerspectiveInitMethod(
			final IAction<Event, Object> action,
			final IPerspective<ActionListener, Event, Object> perspective) {
		if (getTargetPerspectiveId(action.getTargetId()).equals(
				perspective.getId())) {
			log("3.4.3.1: perspective handle with custom action");
			perspective.handlePerspective(action);
		} else {
			log("3.4.3.1: perspective handle with default >>init<< action");
			perspective.handlePerspective(new Action(perspective.getId(),
					perspective.getId(), "init"));
		}
	}

	@Override
	public void handleAndReplaceComponent(IAction<Event, Object> action,
			IPerspective<ActionListener, Event, Object> component) {
		component.handlePerspective(action);

	}

	private void log(final String message) {
		if (logger.isLoggable(Level.FINE)) {
			logger.fine(">> " + message);
		}
	}
	/**
	 * returns the message target id
	 * 
	 * @param messageId
	 * @return
	 */
	private String getTargetPerspectiveId(final String messageId) {
		final String[] targetId = getTargetId(messageId);
		if (checkValidComponentId(targetId)) {
			return targetId[0];
		}
		return messageId;
	}

	private String[] getTargetId(final String messageId) {
		return messageId.split("\\.");
	}

	private boolean checkValidComponentId(final String[] targetId) {
		if (targetId != null && targetId.length == 2) {
			return true;
		}

		return false;
	}

	
}
