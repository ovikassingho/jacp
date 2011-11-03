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

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.jacp.api.action.IAction;
import org.jacp.api.component.IRootComponent;
import org.jacp.api.component.ISubComponent;
import org.jacp.api.component.IVComponent;
import org.jacp.api.componentLayout.IPerspectiveLayout;
import org.jacp.api.componentLayout.IWorkbenchLayout;
import org.jacp.api.coordinator.IPerspectiveCoordinator;
import org.jacp.api.launcher.Launcher;
import org.jacp.api.perspective.IPerspective;
import org.jacp.api.workbench.IWorkbench;

import org.jacp.api.componentLayout.Layout;
import org.jacp.javafx2.rcp.action.FX2Action;
import org.jacp.javafx2.rcp.component.AFX2Component;
import org.jacp.javafx2.rcp.componentLayout.FX2WorkbenchLayout;
import org.jacp.javafx2.rcp.coordinator.FX2PerspectiveCoordinator;
import org.jacp.javafx2.rcp.perspective.AFX2Perspective;
import org.jacp.javafx2.rcp.util.AFX2ComponentWorker;
import org.jacp.javafx2.rcp.util.FX2Util;

import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.event.Event;
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
		IWorkbench<Region, Node, EventHandler<Event>, Event, Object, StageStyle>,
		IRootComponent<IPerspective<EventHandler<Event>, Event, Object>, IAction<Event, Object>> {

	private List<IPerspective<EventHandler<Event>, Event, Object>> perspectives;
	private final IPerspectiveCoordinator<EventHandler<Event>, Event, Object> perspectiveHandler = new FX2PerspectiveCoordinator(
			this);
	private final int inset = 50;
	private final IWorkbenchLayout<Region, Node, StageStyle> workbenchLayout = new FX2WorkbenchLayout();
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
				handleBarEntries(getWorkbenchLayout().getToolBars());
				// handle default and defined workspace menu
				// entries
				log("3.5: workbench init menu");
				initWorkbenchMenu();
			}

		});
	}

	public final void start(final Stage stage) throws Exception {
		this.stage = stage;
		log("1: init workbench");
		// init user defined workspace
		this.handleInitialLayout(new FX2Action("TODO", "init"),
				getWorkbenchLayout());
		setBasicLayout(stage);
		log("3: handle initialisation sequence");
		handleInitialisationSequence();
	}

	@Override
	public final void setPerspectives(
			final List<IPerspective<EventHandler<Event>, Event, Object>> perspectives) {
		this.perspectives = perspectives;

	}

	@Override
	public final List<IPerspective<EventHandler<Event>, Event, Object>> getPerspectives() {
		return this.perspectives;
	}

	@Override
	public final void initWorkbenchMenu() {
		// TODO Auto-generated method stub

	}

	@Override
	public final void initMenuBar() {
		// TODO Auto-generated method stub

	}

	@Override
	public final Node getDefaultMenu() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public final Node handleMenuEntries(Node meuBar) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public final void handleBarEntries(Map<Layout, Node> bars) {
		// TODO Auto-generated method stub

	}

	@Override
	public final IWorkbenchLayout<Region, Node, StageStyle> getWorkbenchLayout() {
		return workbenchLayout;
	}

	@Override
	public final void registerComponent(
			final IPerspective<EventHandler<Event>, Event, Object> component) {
		component.init(launcher);
		perspectiveHandler.addPerspective(component);
	}

	@Override
	public final void unregisterComponent(
			final IPerspective<EventHandler<Event>, Event, Object> component) {
		perspectiveHandler.removePerspective(component);
	}

	@Override
	public final void initComponents(final IAction<Event, Object> action) {
		final List<IPerspective<EventHandler<Event>, Event, Object>> perspectivesTmp = getPerspectives();
		for (int i = 0; i < perspectivesTmp.size(); i++) {
			final IPerspective<EventHandler<Event>, Event, Object> perspective = perspectivesTmp
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
					public final void run() {
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
			final IPerspective<EventHandler<Event>, Event, Object> perspective) {
		// TODO implement missing "create perspective menu" functionality
	}

	@Override
	public final void initComponent(final IAction<Event, Object> action,
			final IPerspective<EventHandler<Event>, Event, Object> perspective) {
		final IPerspectiveLayout<? extends Node, Node> perspectiveLayout = ((AFX2Perspective) perspective)
				.getIPerspectiveLayout();
		log("3.4.3: perspective handle init");
		handlePerspectiveInitMethod(action, perspective);
		log("3.4.4: perspective init subcomponents");
		perspective.initComponents(action);
		log("3.4.5: perspective init bar entries");
		addPerspectiveBarEntries(perspective, getToolBarMap());
		initPerspectiveUI(perspective, perspectiveLayout);

	}

	/**
	 * return map with toolbars
	 * 
	 * @return
	 */
	private Map<Layout, Node> getToolBarMap() {
		synchronized (getWorkbenchLayout()) {
			return getWorkbenchLayout().getToolBars();
		}
	}

	/**
	 * handles initialization of custom tool/bottom- bar entries
	 * 
	 * @param perspective
	 */
	private void addPerspectiveBarEntries(
			final IPerspective<EventHandler<Event>, Event, Object> perspective,
			final Map<Layout, Node> toolBars) {
		if (toolBars != null && perspective instanceof AFX2Perspective) {
			((AFX2Perspective) perspective).handleBarEntries(toolBars);
		}
	}

	/**
	 * add perspective UI to workbench root component
	 * 
	 * @param perspective
	 * @param perspectiveLayout
	 */
	private void initPerspectiveUI(
			final IPerspective<EventHandler<Event>, Event, Object> perspective,
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
			final IAction<Event, Object> action,
			final IPerspective<EventHandler<Event>, Event, Object> perspective) {
		if (getTargetPerspectiveId(action.getTargetId()).equals(
				perspective.getId())) {
			log("3.4.3.1: perspective handle with custom action");
			perspective.handlePerspective(action);
		} // End if
		else {
			log("3.4.3.1: perspective handle with default >>init<< action");
			perspective.handlePerspective(new FX2Action(perspective.getId(),
					perspective.getId(), "init"));
		} // End else
	}

	@Override
	public final void handleAndReplaceComponent(
			final IAction<Event, Object> action,
			final IPerspective<EventHandler<Event>, Event, Object> perspective) {
		final IPerspectiveLayout<? extends Node, Node> perspectiveLayout = ((AFX2Perspective) perspective)
				.getIPerspectiveLayout();
		// backup old component
		final Node componentOld = getLayoutComponentFromPerspectiveLayout(perspectiveLayout);
		perspective.handlePerspective(action);
		if (componentOld != null) {
			if (!Platform.isFxApplicationThread()) {
				Platform.runLater(new Runnable() {
					@Override
					public final void run() {
						handlePerspectiveReassignment(perspective,
								perspectiveLayout, componentOld);

					} // End run
				} // End runnable
				); // End runlater
			} // End if
			else {
				handlePerspectiveReassignment(perspective, perspectiveLayout,
						componentOld);
			} // End else

		} // End outer if
		else {
			initPerspectiveUI(perspective, perspectiveLayout);
		} // End else

	}

	/**
	 * reassignment can only be done in FX main thread; TODO remove synchronized
	 */
	private void handlePerspectiveReassignment(
			final IPerspective<EventHandler<Event>, Event, Object> perspective,
			final IPerspectiveLayout<? extends Node, Node> perspectiveLayout,
			final Node componentOld) {
		final Node componentNew = getLayoutComponentFromPerspectiveLayout(perspectiveLayout);
		reassignChild(componentOld.getParent(), componentOld, componentNew);
		// set already active editors to new component
		reassignSubcomponents(perspective, perspectiveLayout);
	}

	/**
	 * handle reassignment of components in perspective ui
	 * 
	 * @param parent
	 * @param oldComp
	 * @param newComp
	 */
	private void reassignChild(final Node parent, final Node oldComp,
			final Node newComp) {
		final ObservableList<Node> children = FX2Util.getChildren(parent);
		if (children.remove(oldComp)) {
			children.add(newComp);
			parent.setVisible(true);
		}
	}

	/**
	 * add all active subcomponents to replaced perspective
	 * 
	 * @param layout
	 * @param perspective
	 */
	private void reassignSubcomponents(
			final IPerspective<EventHandler<Event>, Event, Object> perspective,
			final IPerspectiveLayout<? extends Node, Node> layout) {
		runReassign(perspective, layout);

	}

	/**
	 * call reassign of all components in perspective
	 * 
	 * @param layout
	 * @param perspective
	 */
	private void runReassign(
			final IPerspective<EventHandler<Event>, Event, Object> perspective,
			final IPerspectiveLayout<? extends Node, Node> layout) {
		final List<ISubComponent<EventHandler<Event>, Event, Object>> subcomponents = perspective
				.getSubcomponents();
		for (int i = 0; i < subcomponents.size(); i++) {
			final ISubComponent<EventHandler<Event>, Event, Object> subComp = subcomponents
					.get(i);
			if (subComp instanceof AFX2Component) {
				final Node editorComponent = ((AFX2Component) subComp)
						.getRoot();
				if (editorComponent != null) {
					editorComponent.setVisible(true);
					addComponentByType(((AFX2Component) subComp), layout);
				} // End if
			} // End outer if
		} // End for
	}

	/**
	 * find valid target and add type specific new ui component
	 * 
	 * @param layout
	 * @param editor
	 */
	private void addComponentByType(
			final IVComponent<Node, EventHandler<Event>, Event, Object> component,
			final IPerspectiveLayout<? extends Node, Node> layout) {
		final Node validContainer = layout.getTargetLayoutComponents().get(
				component.getExecutionTarget());
		final ObservableList<Node> children = FX2Util.getChildren(validContainer);
		children.add(component.getRoot());
	}



	// TODO move to util class!! same code located in AFX2Component

	/**
	 * get perspectives ui root container
	 * 
	 * @param layout
	 * @param dimension
	 * @return
	 */
	private Node getLayoutComponentFromPerspectiveLayout(
			final IPerspectiveLayout<? extends Node, Node> layout) {
		final Node comp = layout.getRootComponent();
		comp.setVisible(true);
		return comp;
	}

	/**
	 * set basic layout manager for workspace
	 * 
	 * @param stage
	 *            javafx.stage.Stage
	 */
	private void setBasicLayout(final Stage stage) {
		int x = getWorkbenchLayout().getWorkbenchSize().getX();
		int y = getWorkbenchLayout().getWorkbenchSize().getY();
		this.root = new Group();
		stage.initStyle(getWorkbenchLayout().getStyle());
		// TODO check handling of layout.getLayoutManager();
		stage.setScene(new Scene(root, x, y));
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
	/**
	 * returns id of target component
	 * @param messageId
	 * @return
	 */
	private String[] getTargetId(final String messageId) {
		return messageId.split("\\.");
	}
	/**
	 * returns true if id is valid
	 * @param targetId
	 * @return
	 */
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
