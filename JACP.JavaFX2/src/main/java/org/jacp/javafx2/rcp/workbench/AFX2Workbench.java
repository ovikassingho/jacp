/*
# * Copyright (C) 2010,2011.
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

import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.MenuBar;
import javafx.scene.control.ToolBar;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import org.jacp.api.action.IAction;
import org.jacp.api.component.IExtendedComponent;
import org.jacp.api.component.IRootComponent;
import org.jacp.api.component.ISubComponent;
import org.jacp.api.component.IVComponent;
import org.jacp.api.componentLayout.IPerspectiveLayout;
import org.jacp.api.componentLayout.IWorkbenchLayout;
import org.jacp.api.coordinator.IPerspectiveCoordinator;
import org.jacp.api.launcher.Launcher;
import org.jacp.api.perspective.IPerspective;
import org.jacp.api.util.ToolbarPosition;
import org.jacp.api.workbench.IWorkbench;
import org.jacp.javafx2.rcp.action.FX2Action;
import org.jacp.javafx2.rcp.component.AFX2Component;
import org.jacp.javafx2.rcp.componentLayout.FX2ComponentLayout;
import org.jacp.javafx2.rcp.componentLayout.FX2WorkbenchLayout;
import org.jacp.javafx2.rcp.coordinator.FX2PerspectiveCoordinator;
import org.jacp.javafx2.rcp.perspective.AFX2Perspective;
import org.jacp.javafx2.rcp.util.FX2Util;

/**
 * represents the basic JavaFX2 workbench instance; handles perspectives and
 * components;
 * 
 * @author Andy Moncsek
 */
public abstract class AFX2Workbench
		implements
		IWorkbench<Node, EventHandler<Event>, Event, Object>,
		IRootComponent<IPerspective<EventHandler<Event>, Event, Object>, IAction<Event, Object>> {

	private List<IPerspective<EventHandler<Event>, Event, Object>> perspectives;
	private final IPerspectiveCoordinator<EventHandler<Event>, Event, Object> perspectiveHandler = new FX2PerspectiveCoordinator(
			this);
	private final IWorkbenchLayout<Node> workbenchLayout = new FX2WorkbenchLayout();
	private final Logger logger = Logger.getLogger(this.getClass().getName());
	private Launcher<?> launcher;
	private Stage stage;
	private GridPane root;

	public AFX2Workbench(final String name) {

	}

	@Override
	/**
	 * {@inheritDoc}
	 */
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
				AFX2Workbench.this.stage.show();
				// start perspective Observer worker thread
				// TODO create status daemon which observes
				// thread component on
				// failure and restarts if needed!!
				((FX2PerspectiveCoordinator) AFX2Workbench.this.perspectiveHandler)
						.start();
				// init toolbar instance
				AFX2Workbench.this.log("3.2: workbench tool bars");
				// initToolBars();
				// handle perspectives
				AFX2Workbench.this.log("3.3: workbench init perspectives");
				AFX2Workbench.this.initComponents(null);

			}

		});
	}

	/**
	 * JavaFX2 specific start sequence
	 * 
	 * @param stage
	 * @throws Exception
	 */
	public final void start(final Stage stage) throws Exception {
		this.stage = stage;
		this.log("1: init workbench");
		// init user defined workspace
		this.handleInitialLayout(new FX2Action("TODO", "init"),
				this.getWorkbenchLayout());
		this.setBasicLayout(stage);
		postHandle(new FX2ComponentLayout(this.getWorkbenchLayout().getMenu(),
				this.getWorkbenchLayout().getRegisteredToolbars()));
		this.log("3: handle initialisation sequence");
		this.handleInitialisationSequence();
	}

	/**
	 * Handle menu and bar entries created in @see
	 * {@link org.jacp.javafx2.rcp.workbench.AFX2Workbench#handleInitialLayout(IAction, IWorkbenchLayout, Stage)}
	 * 
	 * @param layout
	 */
	public abstract void postHandle(final FX2ComponentLayout layout);

	@Override
	/**
	 * {@inheritDoc}
	 */
	public void handleInitialLayout(final IAction<Event, Object> action,
			final IWorkbenchLayout<Node> layout) {
		this.handleInitialLayout(action, layout, this.stage);
	}

	/**
	 * JavaFX2 specific initialization method to create a workbench instance
	 * 
	 * @param action
	 * @param layout
	 * @param stage
	 */
	public abstract void handleInitialLayout(
			final IAction<Event, Object> action,
			final IWorkbenchLayout<Node> layout, final Stage stage);

	@Override
	/**
	 * {@inheritDoc}
	 */
	public final void setPerspectives(
			final List<IPerspective<EventHandler<Event>, Event, Object>> perspectives) {
		this.perspectives = perspectives;

	}

	@Override
	/**
	 * {@inheritDoc}
	 */
	public final List<IPerspective<EventHandler<Event>, Event, Object>> getPerspectives() {
		return this.perspectives;
	}

	@Override
	/**
	 * {@inheritDoc}
	 */
	public final FX2WorkbenchLayout getWorkbenchLayout() {
		return (FX2WorkbenchLayout) this.workbenchLayout;
	}

	@Override
	/**
	 * {@inheritDoc}
	 */
	public final void registerComponent(
			final IPerspective<EventHandler<Event>, Event, Object> component) {
		component.init(this.launcher);
		this.perspectiveHandler.addPerspective(component);
	}

	@Override
	/**
	 * {@inheritDoc}
	 */
	public final void unregisterComponent(
			final IPerspective<EventHandler<Event>, Event, Object> component) {
		this.perspectiveHandler.removePerspective(component);
	}

	@Override
	/**
	 * {@inheritDoc}
	 */
	public final void initComponents(final IAction<Event, Object> action) {
		final List<IPerspective<EventHandler<Event>, Event, Object>> perspectivesTmp = this
				.getPerspectives();
		for (int i = 0; i < perspectivesTmp.size(); i++) {
			final IPerspective<EventHandler<Event>, Event, Object> perspective = perspectivesTmp
					.get(i);
			this.log("3.4.1: register component: " + perspective.getName());
			this.registerComponent(perspective);
			// TODO what if component removed an initialized later
			// again?
			this.log("3.4.2: create perspective menu");
			if (perspective.isActive()) {
				Platform.runLater(new Runnable() {
					@Override
					public final void run() {
						AFX2Workbench.this.initComponent(new FX2Action(
								perspective.getId(), perspective.getId(),
								"init"), perspective);

					}
				}); // FX2 UTILS END
			}

		}
	}

	@Override
	/**
	 * {@inheritDoc}
	 */
	public final void initComponent(final IAction<Event, Object> action,
			final IPerspective<EventHandler<Event>, Event, Object> perspective) {
		final IPerspectiveLayout<? extends Node, Node> perspectiveLayout = ((AFX2Perspective) perspective)
				.getIPerspectiveLayout();
		this.log("3.4.3: perspective handle init");
		this.handlePerspectiveInitMethod(action, perspective);
		this.log("3.4.4: perspective init subcomponents");
		perspective.initComponents(action);
		this.log("3.4.5: perspective init bar entries");
		// addPerspectiveBarEntries(perspective, getToolBarMap());
		this.initPerspectiveUI(perspective, perspectiveLayout);

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
		this.log("3.4.6: perspective init SINGLE_PANE");
		this.initPerspectiveInStackMode(perspectiveLayout);
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
		synchronized (this.root) {
			final ObservableList<Node> children = this.root.getChildren();
			hideChildren(children);
			GridPane.setConstraints(comp, 0, 0);
			GridPane.setHgrow(comp, Priority.ALWAYS);
			GridPane.setVgrow(comp, Priority.ALWAYS);
			children.add(comp);
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
	@SuppressWarnings("unchecked")
	private void handlePerspectiveInitMethod(
			final IAction<Event, Object> action,
			final IPerspective<EventHandler<Event>, Event, Object> perspective) {
		if (perspective instanceof IExtendedComponent) {
			((IExtendedComponent<Node>) perspective)
					.onStart(new FX2ComponentLayout(this.getWorkbenchLayout()
							.getMenu(), this.getWorkbenchLayout()
							.getRegisteredToolbars()));
		}
		if (this.getTargetPerspectiveId(action.getTargetId()).equals(
				perspective.getId())) {
			this.log("3.4.3.1: perspective handle with custom action");
			perspective.handlePerspective(action);
		} // End if
		else {
			this.log("3.4.3.1: perspective handle with default >>init<< action");
			perspective.handlePerspective(new FX2Action(perspective.getId(),
					perspective.getId(), "init"));
		} // End else
	}

	@Override
	/**
	 * {@inheritDoc}
	 */
	public final void handleAndReplaceComponent(
			final IAction<Event, Object> action,
			final IPerspective<EventHandler<Event>, Event, Object> perspective) {
		final IPerspectiveLayout<? extends Node, Node> perspectiveLayout = ((AFX2Perspective) perspective)
				.getIPerspectiveLayout();
		// backup old component
		final Node componentOld = this
				.getLayoutComponentFromPerspectiveLayout(perspectiveLayout);
		perspective.handlePerspective(action);
		if (componentOld != null) {
			this.handlePerspectiveReassignment(perspective, perspectiveLayout,
					componentOld);

		} // End outer if
		else {
			this.initPerspectiveUI(perspective, perspectiveLayout);
		} // End else

	}

	/**
	 * reassignment can only be done in FX main thread;
	 */
	private void handlePerspectiveReassignment(
			final IPerspective<EventHandler<Event>, Event, Object> perspective,
			final IPerspectiveLayout<? extends Node, Node> perspectiveLayout,
			final Node componentOld) {
		final Node componentNew = this
				.getLayoutComponentFromPerspectiveLayout(perspectiveLayout);
		this.reassignChild(componentOld.getParent(), componentOld, componentNew);
		// set already active editors to new component
		this.reassignSubcomponents(perspective, perspectiveLayout);
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
		// set all other components in in workbench to invisible
		hideChildren(children);
		oldComp.setVisible(false);
		newComp.setVisible(true);
		if (children.remove(oldComp)) {
			children.add(newComp);
			parent.setVisible(true);
		}
	}

	/**
	 * set all child components to invisible
	 * 
	 * @param children
	 */
	private void hideChildren(final ObservableList<Node> children) {
		for (final Node child : children) {
			child.setVisible(false);
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
		this.runReassign(perspective, layout);

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
			if (subComp instanceof AFX2Component && subComp.isActive()) {
				final Node editorComponent = ((AFX2Component) subComp)
						.getRoot();
				if (editorComponent != null) {
					editorComponent.setVisible(true);
					editorComponent.setDisable(false);
					this.addComponentByType(((AFX2Component) subComp), layout);
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
		final ObservableList<Node> children = FX2Util
				.getChildren(validContainer);
		Node root = component.getRoot();
		GridPane.setHgrow(root, Priority.ALWAYS);
		GridPane.setVgrow(root, Priority.ALWAYS);
		children.add(root);
	}

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
		int x = this.getWorkbenchLayout().getWorkbenchSize().getX();
		int y = this.getWorkbenchLayout().getWorkbenchSize().getY();
		// the main content Pane, will be "centered"
		this.root = new GridPane();
		root.setId("root-pane");

		stage.initStyle((StageStyle) this.getWorkbenchLayout().getStyle());
		//
		// add the toolbars in a specific order
		if (!this.getWorkbenchLayout().getRegisteredToolbars().isEmpty()) {
			// holds only the decorator (if set) and the menu bar.
			final BorderPane baseLayoutPane = new BorderPane();
			// TODO: handle the custom decorator

			// add the menu if needed
			if (this.getWorkbenchLayout().isMenuEnabled())
				baseLayoutPane.setTop(this.getWorkbenchLayout().getMenu());

			final BorderPane toolbarPane = new BorderPane();
			baseLayoutPane.setCenter(toolbarPane);

			final Map<ToolbarPosition, ToolBar> registeredToolbars = this
					.getWorkbenchLayout().getRegisteredToolbars();

			for (final ToolbarPosition position : registeredToolbars.keySet()) {
				this.assignCorrectToolBarLayout(position,
						registeredToolbars.get(position), toolbarPane);
			}

			// add root to the center
			toolbarPane.setCenter(this.root);

			stage.setScene(new Scene(baseLayoutPane, x, y));

		} else {
			stage.setScene(new Scene(this.root, x, y));
		}
		// if (!this.getWorkbenchLayout().getToolBarMap().isEmpty()) {
		//
		// final BorderPane pane = new BorderPane();
		// final Iterator<Entry<Layout, ToolBar>> it = this
		// .getWorkbenchLayout().getToolBarMap().entrySet().iterator();
		// while (it.hasNext()) {
		// final Entry<Layout, ToolBar> entry = it.next();
		// this.assignCorrectBarLayout(entry.getKey(), entry.getValue(),
		// pane, x, y, this.getWorkbenchLayout().isMenuEnabled());
		// }
		// pane.setCenter(this.root);
		// if (this.getWorkbenchLayout().getMenu() != null) {
		// this.getWorkbenchLayout().getMenu().setMaxHeight(y * .025);
		// y = y + Double.valueOf((y * .08) + "").intValue();
		//
		// }
		// stage.setScene(new Scene(pane, x, y));
		// } else {
		// stage.setScene(new Scene(this.root, x, y));
		// }

	}

	/**
	 * set toolBars to correct position
	 * 
	 * @param layout
	 * @param bar
	 * @param pane
	 * @param x
	 * @param y
	 */
	private void assignCorrectToolBarLayout(ToolbarPosition position,
			ToolBar bar, BorderPane pane) {
		switch (position) {
		case NORTH:
			pane.setTop(bar);
			break;
		case SOUTH:
			pane.setBottom(bar);
			break;
		case EAST:
			pane.setRight(envelopToolBar(bar));
			break;
		case WEST:
			pane.setLeft(envelopToolBar(bar));
			break;
		}
	}

	private HBox envelopToolBar(ToolBar bar) {
		HBox box = new HBox();
		bar.setMaxHeight(Double.MAX_VALUE);
		box.getChildren().add(bar);
		return box;
	}

	/**
	 * returns the message target id
	 * 
	 * @param messageId
	 * @return
	 */
	private String getTargetPerspectiveId(final String messageId) {
		final String[] targetId = this.getTargetId(messageId);
		if (this.checkValidComponentId(targetId)) {
			return targetId[0];
		}
		return messageId;
	}

	/**
	 * returns id of target component
	 * 
	 * @param messageId
	 * @return
	 */
	private String[] getTargetId(final String messageId) {
		return messageId.split("\\.");
	}

	/**
	 * returns true if id is valid
	 * 
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
		if (this.logger.isLoggable(Level.FINE)) {
			this.logger.fine(">> " + message);
		}
	}
}
