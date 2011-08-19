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


import java.util.EventListener;

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
import org.jacp.project.concurrency.action.Action;
import org.jacp.project.concurrency.action.ActionListener;
import org.jacp.project.concurrency.action.Event;

public abstract class AHeadlessPerspective implements
		IPerspective<EventListener, Event, Object> {
	private String id;
	private String name;
	private boolean active;
	private boolean isActived = false;
	private final Logger logger = Logger.getLogger(this.getClass().getName());
	private Launcher<?> launcher;

	private final List<ISubComponent<EventListener, Event, Object>> subcomponents = new CopyOnWriteArrayList<ISubComponent<EventListener, Event, Object>>();
    private ICoordinator<EventListener, Event, Object> perspectiveObserver;
    


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
	public void setActivated(boolean isActive) {
		this.isActived = isActive;
		
	}

	@Override
	public boolean isActivated() {
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
			ICoordinator<EventListener, Event, Object> observer) {
		this.perspectiveObserver = observer;

	}
	
	@Override
	public <C> C handle(IAction<Event, Object> action) {
		// TODO Auto-generated method stub
		handlePerspective(action);
		return null;
	}
	


	@Override
	public void registerComponent(
			ISubComponent<EventListener, Event, Object> component) {
		log("register component: " + component.getId());
		// componentHandler.addComponent(component);
		subcomponents.add(component);
		component.setParentPerspective(this);

	}

	@Override
	public void unregisterComponent(
			ISubComponent<EventListener, Event, Object> component) {
		log("unregister component: " + component.getId());
		// componentHandler.removeComponent(component);
		subcomponents.remove(component);
		component.setParentPerspective(null);

	}

	@Override
	public void initComponents(IAction<Event, Object> action) {
		// TODO Auto-generated method stub

	}

	@Override
	public void initComponent(IAction<Event, Object> action,
			ISubComponent<EventListener, Event, Object> component) {
		// TODO Auto-generated method stub

	}

	@Override
	public void handleAndReplaceComponent(IAction<Event, Object> action,
			ISubComponent<EventListener, Event, Object> component) {
		// TODO Auto-generated method stub

	}


	@Override
	public void init(Launcher<?> launcher) {
		this.launcher = launcher;

	}

	@Override
	public List<ISubComponent<EventListener, Event, Object>> getSubcomponents() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setSubcomponents(
			List<ISubComponent<EventListener, Event, Object>> subComponents) {
		// TODO Auto-generated method stub

	}

	
	@Override
	public void addActiveComponent(
			ISubComponent<EventListener, Event, Object> component) {
		// TODO Auto-generated method stub

	}

	@Override
	public void delegateTargetChange(String target,
			ISubComponent<EventListener, Event, Object> component) {
		// TODO Auto-generated method stub

	}

	@Override
	public void delegateComponentMassege(String target,
			IAction<Event, Object> action) {
		// TODO Auto-generated method stub

	}

	@Override
	public void delegateMassege(String target,
			IAction<Event, Object> action) {
		// TODO Auto-generated method stub

	}

	private void log(final String message) {
		if (logger.isLoggable(Level.FINE)) {
			logger.fine(">> " + message);
		}
	}

	@Override
	public IActionListener<EventListener, Event, Object> getActionListener() {
		// TODO Auto-generated method stub
		return new ActionListener(new Action(this.id), null);
	}
}
