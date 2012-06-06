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

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.util.Callback;

import org.jacp.api.action.IAction;
import org.jacp.api.annotations.OnStart;
import org.jacp.api.component.IComponentView;
import org.jacp.api.component.IPerspective;
import org.jacp.api.component.IPerspectiveView;
import org.jacp.api.component.ISubComponent;
import org.jacp.api.componentLayout.IPerspectiveLayout;
import org.jacp.api.componentLayout.IWorkbenchLayout;
import org.jacp.api.handler.IComponentHandler;
import org.jacp.api.launcher.Launcher;
import org.jacp.api.util.UIType;
import org.jacp.javafx.rcp.action.FXAction;
import org.jacp.javafx.rcp.component.AFXComponent;
import org.jacp.javafx.rcp.componentLayout.FXComponentLayout;
import org.jacp.javafx.rcp.componentLayout.FXMLPerspectiveLayout;
import org.jacp.javafx.rcp.componentLayout.FXPerspectiveLayout;
import org.jacp.javafx.rcp.componentLayout.FXWorkbenchLayout;
import org.jacp.javafx.rcp.perspective.AFXPerspective;
import org.jacp.javafx.rcp.util.FXUtil;

/**
 * Handles initialization and re assignment of perspectives in workbench.
 * 
 * @author Andy Moncsek
 * 
 */
public class FXWorkbenchHandler
		implements
		IComponentHandler<IPerspective<EventHandler<Event>, Event, Object>, IAction<Event, Object>> {
	private final Logger logger = Logger.getLogger(this.getClass().getName());
	private final IWorkbenchLayout<Node> workbenchLayout;
	private final Launcher<?> launcher;
	private final List<IPerspective<EventHandler<Event>, Event, Object>> perspectives;
	private final GridPane root;

	public FXWorkbenchHandler(
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
		final IPerspectiveLayout<? extends Node, Node> perspectiveLayout = ((AFXPerspective) perspective)
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

		this.log("3.4.3: perspective handle init");
		this.handlePerspectiveInitMethod(action, perspective);
		this.log("3.4.4: perspective init subcomponents");
		perspective.initComponents(action);
		final IPerspectiveLayout<? extends Node, Node> perspectiveLayout = ((AFXPerspective) perspective)
				.getIPerspectiveLayout();
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
				final AFXComponent subComponent = (AFXComponent) subComp;
				final Node editorComponent = subComponent.getRoot();
				// TODO set cache hint to perform faster reassignment
				if (editorComponent != null) {
					editorComponent.setVisible(true);
					editorComponent.setDisable(false);
					this.addComponentByType(subComponent, layout);
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
			final IComponentView<Node, EventHandler<Event>, Event, Object> component,
			final IPerspectiveLayout<? extends Node, Node> layout) {
		final Node validContainer = layout.getTargetLayoutComponents().get(
				component.getExecutionTarget());
		final ObservableList<Node> children = FXUtil
				.getChildren(validContainer);
		final Node root = component.getRoot();
		if (!children.contains(root)) {
			GridPane.setHgrow(root, Priority.ALWAYS);
			GridPane.setVgrow(root, Priority.ALWAYS);
			children.add(root);
		}

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
		final ObservableList<Node> children = FXUtil.getChildren(parent);
		// set all other components in in workbench to invisible
		this.hideChildren(children);
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
			this.hideChildren(children);
			GridPane.setConstraints(comp, 0, 0);
			children.add(comp);
		}
	}

	@SuppressWarnings("unchecked")
	private void handlePerspectiveInitMethod(
			final IAction<Event, Object> action,
			final IPerspective<EventHandler<Event>, Event, Object> perspective) {
		if (perspective instanceof IPerspectiveView) {
			final FXComponentLayout layout = new FXComponentLayout(
					this.getWorkbenchLayout());
			

			
			final IPerspectiveView<Node, EventHandler<Event>, Event, Object> perspectiveView = ((IPerspectiveView<Node, EventHandler<Event>, Event, Object>)perspective);			
			
			if(perspectiveView.getType().equals(UIType.DECLARATIVE)) {
				// init IPerspectiveLayout for FXML 
				FXUtil.setPrivateMemberValue(AFXPerspective.class, perspective, FXUtil.AFXPERSPECTIVE_PERSPECTIVE_LAYOUT, new FXMLPerspectiveLayout(loadFXMLandSetController(perspectiveView)));
				FXUtil.invokeHandleMethodsByAnnotation(OnStart.class, perspective,layout, perspectiveView.getDocumentURL(),perspectiveView.getResourceBundle());
			} else {
				// init default IPerspectiveLayout
				FXUtil.setPrivateMemberValue(AFXPerspective.class, perspective, FXUtil.AFXPERSPECTIVE_PERSPECTIVE_LAYOUT, new FXPerspectiveLayout());
				FXUtil.invokeHandleMethodsByAnnotation(OnStart.class, perspective,
						layout);
			}
			
			
			final IPerspectiveLayout<Node, Node> perspectiveLayout = (IPerspectiveLayout<Node, Node>) perspectiveView.getIPerspectiveLayout();
			perspective.postInit(new FXPerspectiveHandler(this.launcher,
					layout, perspectiveLayout, perspective
							.getComponentDelegateQueue()));
		} else {
			// TODO handle non UI Perspectives (not present 10.04.2012)
		}

		if (FXUtil.getTargetPerspectiveId(action.getTargetId()).equals(
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
	
	private Node loadFXMLandSetController(
			final IPerspectiveView<Node, EventHandler<Event>, Event, Object> perspectiveView) {
		final String bundleLocation = perspectiveView.getResourceBundleLocation();
		final String localeID = perspectiveView.getLocaleID();
		final URL url = getClass().getResource(perspectiveView.getViewLocation());
		final FXMLLoader fxmlLoader = new FXMLLoader(url);
		if(bundleLocation!=null && bundleLocation.length()>1) {
			fxmlLoader.setResources(ResourceBundle.getBundle(bundleLocation, getCorrectLocale(localeID)));
		}
		fxmlLoader.setControllerFactory(new Callback<Class<?>, Object>() {
			@Override
			public Object call(Class<?> paramClass) {
				log("set FXML controller" + perspectiveView.getName());
				return perspectiveView;
			}
		});

		try {
			return (Node) fxmlLoader.load();
		} catch (IOException e) {
			throw new MissingResourceException(
					"fxml file not found --  place in resource folder and reference like this: uiDescriptionFile = \"/myUIFile.fxml\"",
					perspectiveView.getViewLocation(), "");
		}
	}
	
	private Locale getCorrectLocale(final String localeID) {
		Locale locale = Locale.getDefault();
		if(localeID!=null && localeID.length()>1){
			if(localeID.contains("_")) {
				String[] loc = localeID.split("_");
				locale = new Locale(loc[0],loc[1]);
			} else {
				locale = new Locale(localeID);
			}
			
		}
		return locale;
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

	public final FXWorkbenchLayout getWorkbenchLayout() {
		return (FXWorkbenchLayout) this.workbenchLayout;
	}

	public final List<IPerspective<EventHandler<Event>, Event, Object>> getPerspectives() {
		return this.perspectives;
	}
}
