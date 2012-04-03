/************************************************************************
 * 
 * Copyright (C) 2010 - 2012
 *
 * [FX2WorkbenchHandler.java]
 * AHCP Project (http://jacp.googlecode.com)
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
 *
 *
 ************************************************************************/
package org.jacp.javafx.rcp.handler;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;

import org.jacp.api.action.IAction;
import org.jacp.api.component.IExtendedComponent;
import org.jacp.api.component.ILayoutAbleComponent;
import org.jacp.api.component.ISubComponent;
import org.jacp.api.component.IVComponent;
import org.jacp.api.componentLayout.IPerspectiveLayout;
import org.jacp.api.componentLayout.IWorkbenchLayout;
import org.jacp.api.handler.IComponentHandler;
import org.jacp.api.launcher.Launcher;
import org.jacp.api.perspective.IPerspective;
import org.jacp.javafx.rcp.action.FXAction;
import org.jacp.javafx.rcp.component.AFXComponent;
import org.jacp.javafx.rcp.componentLayout.FX2ComponentLayout;
import org.jacp.javafx.rcp.componentLayout.FX2WorkbenchLayout;
import org.jacp.javafx.rcp.perspective.AFX2Perspective;
import org.jacp.javafx.rcp.util.FX2Util;

/**
 * Handles initialization and re assignment of perspectives in workbench
 * @author Andy Moncsek
 *
 */
public class FX2WorkbenchHandler
		implements
		IComponentHandler<IPerspective<EventHandler<Event>, Event, Object>, IAction<Event, Object>> {
	private final Logger logger = Logger.getLogger(this.getClass().getName());
	private final IWorkbenchLayout<Node> workbenchLayout;
	private final Launcher<?> launcher;
	private final List<IPerspective<EventHandler<Event>, Event, Object>> perspectives;
	private final GridPane root;

	public FX2WorkbenchHandler(
			final Launcher<?> launcher,
			final IWorkbenchLayout<Node> workbenchLayout,
			final GridPane root,
			final List<IPerspective<EventHandler<Event>, Event, Object>> perspectives) {
		this.workbenchLayout = workbenchLayout;
		this.root = root;
		this.perspectives = perspectives;
		this.launcher = launcher;
	}

	@Override
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

	@Override
	public final void initComponent(final IAction<Event, Object> action,
			final IPerspective<EventHandler<Event>, Event, Object> perspective) {
		final IPerspectiveLayout<? extends Node, Node> perspectiveLayout = ((AFX2Perspective) perspective)
				.getIPerspectiveLayout();
		this.log("3.4.3: perspective handle init");
		this.handlePerspectiveInitMethod(action, perspective);
		this.log("3.4.4: perspective init subcomponents");
		perspective.initComponents(action);
		this.log("3.4.5: perspective init bar entries");
		this.initPerspectiveUI(perspective, perspectiveLayout);

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
			if (subComp instanceof AFXComponent && subComp.isActive()) {
				final Node editorComponent = ((AFXComponent) subComp)
						.getRoot();
				if (editorComponent != null) {
					editorComponent.setVisible(true);
					editorComponent.setDisable(false);
					this.addComponentByType(((AFXComponent) subComp), layout);
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
		final Node root = component.getRoot();
		GridPane.setHgrow(root, Priority.ALWAYS);
		GridPane.setVgrow(root, Priority.ALWAYS);
		children.add(root);
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
	 * add perspective UI to workbench root component
	 * 
	 * @param perspective
	 * @param perspectiveLayout
	 */
	private void initPerspectiveUI(
			final IPerspective<EventHandler<Event>, Event, Object> perspective,
			final IPerspectiveLayout<? extends Node, Node> perspectiveLayout) {
		this.log("3.4.6: perspective init SINGLE_PANE");
		final Node comp = perspectiveLayout.getRootComponent();
		comp.setVisible(true);
		synchronized (this.root) {
			final ObservableList<Node> children = this.root.getChildren();
			hideChildren(children);
			GridPane.setConstraints(comp, 0, 0);
			children.add(comp);
		}
	}

	@SuppressWarnings("unchecked")
	private void handlePerspectiveInitMethod(
			final IAction<Event, Object> action,
			final IPerspective<EventHandler<Event>, Event, Object> perspective) {
		if (perspective instanceof IExtendedComponent) {
			final FX2ComponentLayout tmpLayout = new FX2ComponentLayout(this
					.getWorkbenchLayout().getMenu(), this.getWorkbenchLayout()
					.getRegisteredToolbars(), this.getWorkbenchLayout()
					.getGlassPane());
			((IExtendedComponent<Node>) perspective)
					.onStart(new FX2ComponentLayout(this.getWorkbenchLayout()
							.getMenu(), this.getWorkbenchLayout()
							.getRegisteredToolbars(), this.getWorkbenchLayout()
							.getGlassPane()));
			if (perspective instanceof ILayoutAbleComponent) {
				final IPerspectiveLayout<Node, Node> perspectiveLayout = (IPerspectiveLayout<Node, Node>) ((ILayoutAbleComponent<Node>) perspective)
						.getIPerspectiveLayout();
				perspective.postInit(new FX2PerspectiveHandler(this.launcher,
						tmpLayout, perspectiveLayout, perspective
								.getComponentDelegateQueue()));
			} else {

			}

		}
		if (FX2Util.getTargetPerspectiveId(action.getTargetId()).equals(
				perspective.getId())) {
			this.log("3.4.3.1: perspective handle with custom action");
			perspective.handlePerspective(action);
		} // End if
		else {
			this.log("3.4.3.1: perspective handle with default >>init<< action");
			perspective.handlePerspective(new FXAction(perspective.getId(),
					perspective.getId(), "init"));
		} // End else
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
	 * set all child components to invisible
	 * 
	 * @param children
	 */
	private void hideChildren(final ObservableList<Node> children) {
		for (final Node child : children) {
			child.setVisible(false);
		}
	}

	private void log(final String message) {
		if (this.logger.isLoggable(Level.FINE)) {
			this.logger.fine(">> " + message);
		}
	}

	public final FX2WorkbenchLayout getWorkbenchLayout() {
		return (FX2WorkbenchLayout) this.workbenchLayout;
	}

	public final List<IPerspective<EventHandler<Event>, Event, Object>> getPerspectives() {
		return this.perspectives;
	}
}
