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

package org.jacp.javafx2.rcp.perspective;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;

import org.jacp.api.action.IAction;
import org.jacp.api.action.IActionListener;
import org.jacp.api.component.IExtendedComponent;
import org.jacp.api.component.ILayoutAbleComponent;
import org.jacp.api.component.ISubComponent;
import org.jacp.api.componentLayout.IPerspectiveLayout;
import org.jacp.api.componentLayout.Layout;
import org.jacp.api.coordinator.IComponentCoordinator;
import org.jacp.api.coordinator.ICoordinator;
import org.jacp.api.launcher.Launcher;
import org.jacp.api.perspective.IPerspective;
import org.jacp.javafx2.rcp.action.FX2Action;
import org.jacp.javafx2.rcp.action.FX2ActionListener;
import org.jacp.javafx2.rcp.coordinator.FX2ComponentCoordinator;

/**
 * represents a basic javafx2 perspective that handles subcomponents,
 * perspectives are not handled in thread so avoid long running tasks in
 * perspectives.
 * 
 * @author Andy Moncsek
 */
public class AFX2Perspective implements
		IPerspective<EventHandler<ActionEvent>, ActionEvent, Object>,
		IExtendedComponent<Node>, ILayoutAbleComponent<Node> {
	private String id;
	private String name;
	private boolean active;
	private boolean isActivated = false;
	private final Logger logger = Logger.getLogger(this.getClass().getName());
	private Launcher<?> launcher;
	private final List<ISubComponent<EventHandler<ActionEvent>, ActionEvent, Object>> subcomponents = new CopyOnWriteArrayList<ISubComponent<EventHandler<ActionEvent>, ActionEvent, Object>>();
	private final IComponentCoordinator<EventHandler<ActionEvent>, ActionEvent, Object> componentHandler = new FX2ComponentCoordinator(
			this);
	private ICoordinator<EventHandler<ActionEvent>, ActionEvent, Object> perspectiveObserver;

	@Override
	public IActionListener<EventHandler<ActionEvent>, ActionEvent, Object> getActionListener() {
		return new FX2ActionListener(new FX2Action(id), perspectiveObserver);
	}

	@Override
	public String getId() {
		return this.id;
	}

	@Override
	public void setId(String id) {
		this.id = id;
	}

	@Override
	public boolean isActive() {
		return this.active;
	}

	@Override
	public void setActive(boolean active) {
		this.active = active;
	}

	@Override
	public void setActivated(boolean isActive) {
		this.isActivated = isActive;
	}

	@Override
	public boolean isActivated() {
		return this.isActivated;
	}

	@Override
	public String getName() {
		return this.name;
	}

	@Override
	public void setName(String name) {
		this.name = name;
	}

	@Override
	public void setObserver(
			ICoordinator<EventHandler<ActionEvent>, ActionEvent, Object> observer) {
		this.perspectiveObserver = observer;
	}

	@Override
	public void registerComponent(
			ISubComponent<EventHandler<ActionEvent>, ActionEvent, Object> component) {
		log("register component: " + component.getId());
		componentHandler.addComponent(component);
		subcomponents.add(component);
		component.setParentPerspective(this);

	}

	@Override
	public void unregisterComponent(
			ISubComponent<EventHandler<ActionEvent>, ActionEvent, Object> component) {
		// TODO Auto-generated method stub

	}

	@Override
	public void initComponents(IAction<ActionEvent, Object> action) {
		// TODO Auto-generated method stub

	}

	@Override
	public void initComponent(
			IAction<ActionEvent, Object> action,
			ISubComponent<EventHandler<ActionEvent>, ActionEvent, Object> component) {
		// TODO Auto-generated method stub

	}

	@Override
	public void handleAndReplaceComponent(
			IAction<ActionEvent, Object> action,
			ISubComponent<EventHandler<ActionEvent>, ActionEvent, Object> component) {
		// TODO Auto-generated method stub

	}

	@Override
	public <C> C handle(IAction<ActionEvent, Object> action) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IPerspectiveLayout<? extends Node, Node> getIPerspectiveLayout() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void handleMenuEntries(Node menuBar) {
		// TODO Auto-generated method stub

	}

	@Override
	public void handleBarEntries(Map<Layout, Node> bars) {
		// TODO Auto-generated method stub

	}

	@Override
	public void init(Launcher<?> launcher) {
		this.launcher = launcher;
		((FX2ComponentCoordinator) componentHandler).start();

	}

	@Override
	public List<ISubComponent<EventHandler<ActionEvent>, ActionEvent, Object>> getSubcomponents() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setSubcomponents(
			List<ISubComponent<EventHandler<ActionEvent>, ActionEvent, Object>> subComponents) {
		// TODO Auto-generated method stub

	}

	@Override
	public void handlePerspective(IAction<ActionEvent, Object> action) {
		// TODO Auto-generated method stub

	}

	@Override
	public void addActiveComponent(
			ISubComponent<EventHandler<ActionEvent>, ActionEvent, Object> component) {
		// TODO Auto-generated method stub

	}

	@Override
	public void delegateTargetChange(
			String target,
			ISubComponent<EventHandler<ActionEvent>, ActionEvent, Object> component) {
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
