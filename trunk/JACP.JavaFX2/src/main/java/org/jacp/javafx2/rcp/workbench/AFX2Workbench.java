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
package org.jacp.javafx2.rcp.workbench;

import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import org.jacp.api.action.IAction;
import org.jacp.api.component.IRootComponent;
import org.jacp.api.componentLayout.IWorkbenchLayout;
import org.jacp.api.componentLayout.Layout;
import org.jacp.api.coordinator.IPerspectiveCoordinator;
import org.jacp.api.launcher.Launcher;
import org.jacp.api.perspective.IPerspective;
import org.jacp.api.workbench.IWorkbench;
import org.jacp.javafx2.rcp.componentLayout.FX2WorkbenchLayout;
import org.jacp.javafx2.rcp.coordinator.FX2PerspectiveCoordinator;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.MenuBar;
import javafx.scene.layout.Region;
import javafx.stage.Stage;

/**
 * represents the basic JavaFX2 workbench instance; handles perspectives and
 * components;
 * 
 * @author Andy Moncsek
 */
public class AFX2Workbench extends Application
		implements
		IWorkbench<Region, Node, EventHandler<ActionEvent>, ActionEvent, Object>,
		IRootComponent<IPerspective<EventHandler<ActionEvent>, ActionEvent, Object>, IAction<ActionEvent, Object>> {

	private List<IPerspective<EventHandler<ActionEvent>, ActionEvent, Object>> perspectives;
	private final IPerspectiveCoordinator<EventHandler<ActionEvent>, ActionEvent, Object> perspectiveHandler = new FX2PerspectiveCoordinator(this);
	private final int inset = 50;
	private final IWorkbenchLayout<Region, Node> layout = new FX2WorkbenchLayout();
	private final Logger logger = Logger.getLogger(this.getClass().getName());
	private Launcher<?> launcher;
	private MenuBar menu;

	@Override
	public void start(Stage arg0) throws Exception {

	}

	@Override
	public void setPerspectives(
			List<IPerspective<EventHandler<ActionEvent>, ActionEvent, Object>> perspectives) {
		this.perspectives = perspectives;

	}

	@Override
	public List<IPerspective<EventHandler<ActionEvent>, ActionEvent, Object>> getPerspectives() {
		return this.perspectives;
	}

	@Override
	public void init(Launcher<?> launcher) {
		// TODO Auto-generated method stub

	}

	@Override
	public void initWorkbenchMenu() {
		// TODO Auto-generated method stub

	}

	@Override
	public void initMenuBar() {
		// TODO Auto-generated method stub

	}

	@Override
	public Node getDefaultMenu() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Node handleMenuEntries(Node meuBar) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void handleBarEntries(Map<Layout, Node> bars) {
		// TODO Auto-generated method stub

	}

	@Override
	public void disableComponents() {
		// TODO Auto-generated method stub

	}

	@Override
	public void enableComponents() {
		// TODO Auto-generated method stub

	}

	@Override
	public void handleInitialLayout(IAction<ActionEvent, Object> action,
			IWorkbenchLayout<Region, Node> layout) {
		// TODO move to concrete workbench!!!

	}

	@Override
	public IWorkbenchLayout<Region, Node> getWorkbenchLayout() {
		return null;
	}

	@Override
	public void registerComponent(
			IPerspective<EventHandler<ActionEvent>, ActionEvent, Object> component) {
		// TODO Auto-generated method stub

	}

	@Override
	public void unregisterComponent(
			IPerspective<EventHandler<ActionEvent>, ActionEvent, Object> component) {
		// TODO Auto-generated method stub

	}

	@Override
	public void initComponents(IAction<ActionEvent, Object> action) {
		// TODO Auto-generated method stub

	}

	@Override
	public void initComponent(
			IAction<ActionEvent, Object> action,
			IPerspective<EventHandler<ActionEvent>, ActionEvent, Object> component) {
		// TODO Auto-generated method stub

	}

	@Override
	public void handleAndReplaceComponent(
			IAction<ActionEvent, Object> action,
			IPerspective<EventHandler<ActionEvent>, ActionEvent, Object> component) {
		// TODO Auto-generated method stub

	}

	
}
