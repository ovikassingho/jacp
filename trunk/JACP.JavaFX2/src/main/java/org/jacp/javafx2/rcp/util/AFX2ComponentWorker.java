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

package org.jacp.javafx2.rcp.util;

import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.Node;

import org.jacp.api.action.IAction;
import org.jacp.api.component.ISubComponent;
import org.jacp.api.component.IVComponent;
import org.jacp.javafx2.rcp.component.AFX2Component;
import org.jacp.javafx2.rcp.componentLayout.FX2ComponentLayout;

/**
 * handles component methods in own thread;
 * 
 * @author Andy Moncsek
 */
public abstract class AFX2ComponentWorker<T> extends Task<T> {
	/**
	 * find valid target component in perspective
	 * 
	 * @param targetComponents
	 * @param id
	 * @return
	 */
	protected final Node getValidContainerById(
			final Map<String, Node> targetComponents, final String id) {
		return targetComponents.get(id);
	}

	/**
	 * find valid target and add type specific new component. Handles Container,
	 * ScrollPanes, Menus and Bar Entries from user
	 * 
	 * @param layout
	 * @param editor
	 */
	protected final void addComponentByType(
			final Node validContainer,
			final IVComponent<Node, EventHandler<Event>, Event, Object> component) {
		this.handleAdd(validContainer, component.getRoot(), component.getName());
		validContainer.setVisible(true);

	}

	/**
	 * enables component an add to container
	 * 
	 * @param validContainer
	 * @param uiComponent
	 * @param name
	 */
	private void handleAdd(final Node validContainer, final Node uiComponent,
			final String name) {
		if (validContainer != null && uiComponent != null) {
			uiComponent.setDisable(false);
			uiComponent.setVisible(true);
			final ObservableList<Node> children = FX2Util
					.getChildren(validContainer);
			children.add(uiComponent);
		}

	}

	/**
	 * removes old ui component of subcomponent form parent ui component
	 * 
	 * @param parent
	 * @param currentContainer
	 */
	protected void handleOldComponentRemove(final Node parent,
			final Node currentContainer) {
		currentContainer.setVisible(false);
		currentContainer.setDisable(true);
		final ObservableList<Node> children = FX2Util.getChildren(parent);
		children.remove(currentContainer);
	}

	/**
	 * set new ui component to parent ui component, be careful! call this method
	 * only in EDT... never run from separate thread
	 * 
	 * @param component
	 * @param parent
	 * @param currentTaget
	 */
	protected void handleNewComponentValue(
			final IVComponent<Node, EventHandler<Event>, Event, Object> component,
			final Map<String, Node> targetComponents, final Node parent,
			final String currentTaget) {
		if (parent == null) {
			final String validId = this.getValidTargetId(currentTaget,
					component.getExecutionTarget());
			this.handleTargetChange(component, targetComponents, validId);
		} else if (currentTaget.equals(component.getExecutionTarget())) {
			this.addComponentByType(parent, component);
		} else {
			final String validId = this.getValidTargetId(currentTaget,
					component.getExecutionTarget());
			this.handleTargetChange(component, targetComponents, validId);
		}
	}

	/**
	 * currentTarget.length < 2 Happens when component changed target from one
	 * perspective to an other
	 * 
	 * @param currentTaget
	 * @param futureTarget
	 * @return
	 */
	protected String getValidTargetId(final String currentTaget,
			final String futureTarget) {
		return currentTaget.length() < 2 ? this
				.getTargetComponentId(futureTarget) : futureTarget;
	}

	/**
	 * Handle component when target has changed
	 * 
	 * @param component
	 * @param targetComponents
	 */
	protected void handleTargetChange(
			final IVComponent<Node, EventHandler<Event>, Event, Object> component,
			final Map<String, Node> targetComponents, final String target) {
		final Node validContainer = this.getValidContainerById(
				targetComponents, target);
		if (validContainer != null) {
			handleLocalTargetChange(component, targetComponents, validContainer);
		} else {
			// handle target outside current perspective
			this.changeComponentTarget(component);
		}
	}

	/**
	 * Handle target change inside perspective.
	 * @param component
	 * @param targetComponents
	 * @param validContainer
	 */
	protected void handleLocalTargetChange(
			final IVComponent<Node, EventHandler<Event>, Event, Object> component,
			final Map<String, Node> targetComponents, final Node validContainer) {
		this.addComponentByType(validContainer, component);
	}

	/**
	 * Handle target change to an other perspective.
	 * @param component
	 * @param targetComponents
	 * @param target
	 * @param layout
	 */
	protected void handlePerspectiveChange(
			final IVComponent<Node, EventHandler<Event>, Event, Object> component,
			final Map<String, Node> targetComponents, final String target,
			final FX2ComponentLayout layout) {
		// target component not found in current perspective, move to an other
		// perspective
		// run teardown
		if (component instanceof AFX2Component) {
			((AFX2Component) component).onTearDownComponent(layout);
		}
		// handle target outside current perspective
		this.changeComponentTarget(component);
	}

	/**
	 * move component to new target
	 * 
	 * @param component
	 */
	protected final void changeComponentTarget(
			final ISubComponent<EventHandler<Event>, Event, Object> component) {
		component.getParentPerspective().delegateTargetChange(
				component.getExecutionTarget(), component);
	}

	/**
	 * runs subcomponents handle method
	 * 
	 * @param component
	 * @param action
	 * @return
	 */
	protected final Node prepareAndHandleComponent(
			final IVComponent<Node, EventHandler<Event>, Event, Object> component,
			final IAction<Event, Object> action) {
		final Node editorComponent = component.handle(action);
		if (editorComponent != null)
			component.setRoot(editorComponent);
		return editorComponent;
	}

	/**
	 * executes post handle method in application main thread
	 * 
	 * @param component
	 * @param action
	 */
	protected void executePostHandle(
			final IVComponent<Node, EventHandler<Event>, Event, Object> component,
			final IAction<Event, Object> action) {
		final Node root = component.getRoot();
		final Node tmp = component.postHandle(root, action);
		if (tmp != null) {
			tmp.setVisible(true);
			component.setRoot(tmp);
		}
	}

	/**
	 * returns the message target component id
	 * 
	 * @param messageId
	 * @return
	 */
	protected final String getTargetComponentId(final String messageId) {
		final String[] targetId = this.getTargetId(messageId);
		if (!this.isLocalMessage(messageId)) {
			return targetId[1];
		}
		return messageId;
	}

	/**
	 * when id has no separator it is a local message
	 * 
	 * @param messageId
	 * @return
	 */
	protected final boolean isLocalMessage(final String messageId) {
		return !messageId.contains(".");
	}

	/**
	 * returns target message with perspective and component name as array
	 * 
	 * @param messageId
	 * @return
	 */
	protected final String[] getTargetId(final String messageId) {
		return messageId.split("\\.");
	}

	protected void log(final String message) {
		if (Logger.getLogger(AFX2ComponentWorker.class.getName()).isLoggable(
				Level.FINE)) {
			Logger.getLogger(AFX2ComponentWorker.class.getName()).fine(
					">> " + message);
		}
	}

}
