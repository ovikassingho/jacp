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
import org.jacp.api.launcher.Launcher;
import org.jacp.api.perspective.IPerspective;
import org.jacp.api.workbench.IBase;
import org.jacp.project.concurrency.action.ActionListener;
import org.jacp.project.concurrency.action.Event;

/**
 * This class defines a headless workbench for JACP run time
 * 
 * @author Andy Moncsek
 * 
 */
public class AHeadlessWorkbench
		implements
		IBase<ActionListener, Event, Object>,
		IRootComponent<IPerspective<ActionListener, Event, Object>, IAction<Event, Object>> {
	private List<IPerspective<ActionListener, Event, Object>> perspectives;
	private Launcher<?> launcher;
	private final Logger logger = Logger.getLogger(this.getClass().getName());

	@Override
	public void setPerspectives(
			List<IPerspective<ActionListener, Event, Object>> perspectives) {
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

	}

	@Override
	public void registerComponent(
			IPerspective<ActionListener, Event, Object> component) {
		component.init(launcher);
		//perspectiveHandler.addPerspective(component);

	}

	@Override
	public void unregisterComponent(
			IPerspective<ActionListener, Event, Object> component) {
		// TODO Auto-generated method stub

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
		// TODO Auto-generated method stub

	}

	@Override
	public void handleAndReplaceComponent(IAction<Event, Object> action,
			IPerspective<ActionListener, Event, Object> component) {
		// TODO Auto-generated method stub

	}

	private void log(final String message) {
		if (logger.isLoggable(Level.FINE)) {
			logger.fine(">> " + message);
		}
	}

}
