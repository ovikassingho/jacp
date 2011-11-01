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
import java.util.logging.Level;
import java.util.logging.Logger;

import org.jacp.api.action.IAction;
import org.jacp.api.component.IRootComponent;
import org.jacp.api.componentLayout.IPerspectiveLayout;
import org.jacp.api.componentLayout.IWorkbenchLayout;
import org.jacp.api.coordinator.IPerspectiveCoordinator;
import org.jacp.api.launcher.Launcher;
import org.jacp.api.perspective.IPerspective;
import org.jacp.api.workbench.IWorkbench;

import org.jacp.api.componentLayout.Layout;
import org.jacp.javafx2.rcp.action.FX2Action;
import org.jacp.javafx2.rcp.componentLayout.FX2WorkbenchLayout;
import org.jacp.javafx2.rcp.coordinator.FX2PerspectiveCoordinator;
import org.jacp.javafx2.rcp.perspective.AFX2Perspective;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.MenuBar;
import javafx.scene.layout.Region;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 * represents the basic JavaFX2 workbench instance; handles perspectives and
 * components;
 * 
 * @author Andy Moncsek
 */
public abstract class AFX2Workbench
		implements
		IWorkbench<Region, Node, EventHandler<ActionEvent>, ActionEvent, Object, StageStyle>,
		IRootComponent<IPerspective<EventHandler<ActionEvent>, ActionEvent, Object>, IAction<ActionEvent, Object>> {

	private List<IPerspective<EventHandler<ActionEvent>, ActionEvent, Object>> perspectives;
	private final IPerspectiveCoordinator<EventHandler<ActionEvent>, ActionEvent, Object> perspectiveHandler = new FX2PerspectiveCoordinator(
			this);
	private final int inset = 50;
	private final IWorkbenchLayout<Region, Node, StageStyle> layout = new FX2WorkbenchLayout();
	private final Logger logger = Logger.getLogger(this.getClass().getName());
	private Launcher<?> launcher;
	private MenuBar menu;
	private Stage stage;
	private Group root;

	public AFX2Workbench(final String name) {

	}

	@Override
	public void init(Launcher<?> launcher) {
		this.launcher = launcher;

	}

	/**
	 * handles sequence for workbench size, menu bar, tool bar and perspective
	 * initialisation
	 */
	private void handleInitialisationSequence() {
		Platform.runLater(new Runnable() {

			@Override
			public void run() {
				stage.show();
				// start perspective Observer worker thread
				// TODO create status daemon which observes
				// thread component on
				// failure and restarts if needed!!
				((FX2PerspectiveCoordinator) perspectiveHandler).start();
				// init menu instance#
				log("3.1: workbench menu");
				initMenuBar();
				// init toolbar instance
				log("3.2: workbench tool bars");

				// initToolBars();
				// handle perspectives
				log("3.3: workbench init perspectives");
				initComponents(null);
				// handle workspce bar entries
				log("3.4: workbench handle bar entries");
				handleBarEntries(layout.getToolBars());
				// handle default and defined workspace menu
				// entries
				log("3.5: workbench init menu");
				initWorkbenchMenu();
			}

		});
	}

	public void start(Stage stage) throws Exception {
		this.stage = stage;
		log("1: init workbench");
		// init user defined workspace
		this.handleInitialLayout(new FX2Action("TODO", "init"), layout);
		setBasicLayout(stage);
		log("3: handle initialisation sequence");
		handleInitialisationSequence();
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
	public IWorkbenchLayout<Region, Node, StageStyle> getWorkbenchLayout() {
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
		List<IPerspective<EventHandler<ActionEvent>, ActionEvent, Object>> perspectivesTmp = getPerspectives();
		for (int i = 0; i < perspectivesTmp.size(); i++) {
			final IPerspective<EventHandler<ActionEvent>, ActionEvent, Object> perspective = perspectivesTmp
					.get(i);
			log("3.4.1: register component: " + perspective.getName());
			registerComponent(perspective);
			// TODO what if component removed an initialized later
			// again?
			log("3.4.2: create perspective menu");
			createPerspectiveMenue(perspective);
			if (perspective.isActive()) {
				Platform.runLater(new Runnable() {
					@Override
					public void run() {
						initComponent(new FX2Action(perspective.getId(),
								perspective.getId(), "init"), perspective);
						refreshBarEntries();

					}
				}); // FX2 UTILS END
			}

		}
	}

	/**
	 * refresh bar entries after perspective initialization
	 */
	private void refreshBarEntries() {
		// TODO implement refresh bar entries
	}

	/**
	 * creates basic menu entry for perspective
	 * 
	 * @param perspective
	 */
	private void createPerspectiveMenue(
			final IPerspective<EventHandler<ActionEvent>, ActionEvent, Object> perspective) {
		// TODO implement missing "create perspective menu" functionality
	}

	@Override
	public void initComponent(
			IAction<ActionEvent, Object> action,
			IPerspective<EventHandler<ActionEvent>, ActionEvent, Object> perspective) {
		final IPerspectiveLayout<? extends Node, Node> perspectiveLayout = ((AFX2Perspective) perspective)
				.getIPerspectiveLayout();
		log("3.4.3: perspective handle init");
		handlePerspectiveInitMethod(action, perspective);
		log("3.4.4: perspective init subcomponents");
		perspective.initComponents(action);
		log("3.4.5: perspective init bar entries");
		addPerspectiveBarEntries(perspective);
		initPerspectiveUI(perspective, perspectiveLayout);

	}

	/**
	 * handles initialization of custom tool/bottom- bar entries
	 * 
	 * @param perspective
	 */
	private void addPerspectiveBarEntries(
			final IPerspective<EventHandler<ActionEvent>, ActionEvent, Object> perspective) {
		synchronized (layout) { // TODO remove synchronized block
			if (perspective instanceof AFX2Perspective) {
				((AFX2Perspective) perspective).handleBarEntries(layout
						.getToolBars());
			}
		}
	}

	/**
	 * add perspective UI to workbench root component
	 * 
	 * @param perspective
	 * @param perspectiveLayout
	 */
	private void initPerspectiveUI(
			final IPerspective<EventHandler<ActionEvent>, ActionEvent, Object> perspective,
			final IPerspectiveLayout<? extends Node, Node> perspectiveLayout) {
		log("3.4.6: perspective init SINGLE_PANE");
		initPerspectiveInStackMode(perspectiveLayout);
	}

	/**
	 * initialize perspective in stacked mode; creates an panel an add
	 * perspective
	 * 
	 * @param layout
	 */
	private void initPerspectiveInStackMode(
			final IPerspectiveLayout<? extends Node, Node> layout) {
		disableComponents();
		final Node comp = layout.getRootComponent();
		comp.setVisible(true);
		synchronized (root) { // TODO avoid synchronized block!!
			this.root.getChildren().add(comp);
		}
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
			IAction<ActionEvent, Object> action,
			IPerspective<EventHandler<ActionEvent>, ActionEvent, Object> perspective) {
		if (getTargetPerspectiveId(action.getTargetId()).equals(
				perspective.getId())) {
			log("3.4.3.1: perspective handle with custom action");
			perspective.handlePerspective(action);
		} else {
			log("3.4.3.1: perspective handle with default >>init<< action");
			perspective.handlePerspective(new FX2Action(perspective.getId(),
					perspective.getId(), "init"));
		}
	}

	@Override
	public void handleAndReplaceComponent(
			IAction<ActionEvent, Object> action,
			IPerspective<EventHandler<ActionEvent>, ActionEvent, Object> component) {
		// TODO Auto-generated method stub

	}

	/**
	 * set basic layout manager for workspace
	 * 
	 * @param stage
	 *            javafx.stage.Stage
	 */
	private void setBasicLayout(final Stage stage) {
		int x = layout.getWorkbenchSize().getX();
		int y = layout.getWorkbenchSize().getY();
		this.root = new Group();
		stage.initStyle(layout.getStyle());
		// TODO check handling of layout.getLayoutManager();
		stage.setScene(new Scene(root, x, y));
	}

	public Parent getRoot() {
		return root;
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

	private void log(final String message) {
		if (logger.isLoggable(Level.FINE)) {
			logger.fine(">> " + message);
		}
	}
}
