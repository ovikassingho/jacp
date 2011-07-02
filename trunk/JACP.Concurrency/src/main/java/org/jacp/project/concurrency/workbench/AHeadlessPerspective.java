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

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.jacp.api.action.IAction;
import org.jacp.api.action.IActionListener;
import org.jacp.api.component.ISubComponent;
import org.jacp.api.coordinator.ICoordinator;
import org.jacp.api.launcher.Launcher;
import org.jacp.api.perspective.IPerspective;

public class AHeadlessPerspective implements
		IPerspective<ActionListener, ActionEvent, Object> {
	private String id;
	private String name;
	private boolean active;
	private boolean isActived = false;
	private final Logger logger = Logger.getLogger(this.getClass().getName());
	private Launcher<?> launcher;

	private final List<ISubComponent<ActionListener, ActionEvent, Object>> subcomponents = new CopyOnWriteArrayList<ISubComponent<ActionListener, ActionEvent, Object>>();
    private ICoordinator<ActionListener, ActionEvent, Object> perspectiveObserver;
    
	@Override
	public IActionListener<ActionListener, ActionEvent, Object> getActionListener() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getId() {
		return id;
	}

	@Override
	public void setId(String id) {
		this.id = id;

	}

	@Override
	public boolean isActive() {
		return active;
	}

	@Override
	public void setActive(boolean active) {
		this.active = active;

	}

	@Override
	public void setActived(boolean isActive) {
		this.isActived = isActive;

	}

	@Override
	public boolean isActived() {
		return this.isActived;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public void setName(String name) {
		this.name = name;

	}

	@Override
	public void setObserver(
			ICoordinator<ActionListener, ActionEvent, Object> observer) {
		this.perspectiveObserver = observer;

	}

	@Override
	public void registerComponent(
			ISubComponent<ActionListener, ActionEvent, Object> component) {
		log("register component: " + component.getId());
		// componentHandler.addComponent(component);
		subcomponents.add(component);
		component.setParentPerspective(this);

	}

	@Override
	public void unregisterComponent(
			ISubComponent<ActionListener, ActionEvent, Object> component) {
		log("unregister component: " + component.getId());
		// componentHandler.removeComponent(component);
		subcomponents.remove(component);
		component.setParentPerspective(null);

	}

	@Override
	public void initComponents(IAction<ActionEvent, Object> action) {
		// TODO Auto-generated method stub

	}

	@Override
	public void initComponent(IAction<ActionEvent, Object> action,
			ISubComponent<ActionListener, ActionEvent, Object> component) {
		// TODO Auto-generated method stub

	}

	@Override
	public void handleAndReplaceComponent(IAction<ActionEvent, Object> action,
			ISubComponent<ActionListener, ActionEvent, Object> component) {
		// TODO Auto-generated method stub

	}

	@Override
	public <C> C handle(IAction<ActionEvent, Object> action) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void init(Launcher<?> launcher) {
		this.launcher = launcher;

	}

	@Override
	public List<ISubComponent<ActionListener, ActionEvent, Object>> getSubcomponents() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setSubcomponents(
			List<ISubComponent<ActionListener, ActionEvent, Object>> subComponents) {
		// TODO Auto-generated method stub

	}

	@Override
	public void handlePerspective(IAction<ActionEvent, Object> action) {
		// TODO Auto-generated method stub

	}

	@Override
	public void addActiveComponent(
			ISubComponent<ActionListener, ActionEvent, Object> component) {
		// TODO Auto-generated method stub

	}

	@Override
	public void delegateTargetChange(String target,
			ISubComponent<ActionListener, ActionEvent, Object> component) {
		// TODO Auto-generated method stub

	}

	@Override
	public void delegateComponentMassege(String target,
			IAction<ActionEvent, Object> action) {
		// TODO Auto-generated method stub

	}

	@Override
	public void delegateMassege(String target,
			IAction<ActionEvent, Object> action) {
		// TODO Auto-generated method stub

	}

	private void log(final String message) {
		if (logger.isLoggable(Level.FINE)) {
			logger.fine(">> " + message);
		}
	}
}
