/************************************************************************
 * 
 * Copyright (C) 2010 - 2012
 *
 * [AFX2ComponentWorker.java]
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
package org.jacp.javafx.rcp.worker;

import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.Node;

import org.jacp.api.action.IAction;
import org.jacp.api.action.IActionListener;
import org.jacp.api.annotations.OnTearDown;
import org.jacp.api.component.ICallbackComponent;
import org.jacp.api.component.IComponentView;
import org.jacp.api.component.IDeclarativComponentView;
import org.jacp.api.component.ISubComponent;
import org.jacp.api.component.UIComponent;
import org.jacp.javafx.rcp.action.FXAction;
import org.jacp.javafx.rcp.component.AFXComponent;
import org.jacp.javafx.rcp.component.AFXMLComponent;
import org.jacp.javafx.rcp.componentLayout.FXComponentLayout;
import org.jacp.javafx.rcp.util.FXUtil;

/**
 * handles component methods in own thread;
 * 
 * @author Andy Moncsek
 */
public abstract class AFXComponentWorker<T> extends Task<T> {
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
			final UIComponent<Node, EventHandler<Event>, Event, Object> component) {
		this.handleAdd(validContainer, component.getRoot(), component.getName());
		this.handleViewState(validContainer, true);

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
			this.handleViewState(uiComponent, true);
			final ObservableList<Node> children = FXUtil
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
	protected final void handleOldComponentRemove(final Node parent,
			final Node currentContainer) {
		this.handleViewState(currentContainer, false);
		final ObservableList<Node> children = FXUtil.getChildren(parent);
		children.remove(currentContainer);
	}

	/**
	 * set visibility and enable/disable
	 * 
	 * @param uiComponent
	 * @param state
	 */
	protected final void handleViewState(final Node uiComponent,
			final boolean state) {
		uiComponent.setVisible(state);
		uiComponent.setDisable(!state);
	}

	/**
	 * delegate components handle return value to specified target
	 * 
	 * @param comp
	 * @param targetId
	 * @param value
	 */
	protected void delegateReturnValue(
			final ICallbackComponent<EventHandler<Event>, Event, Object> comp,
			final String targetId, final Object value,
			final IAction<Event, Object> myAction) {
		if (value != null && targetId != null
				&& !myAction.getLastMessage().equals("init")) {
			final IActionListener<EventHandler<Event>, Event, Object> listener = comp
					.getActionListener(null);
			listener.setAction(new FXAction(comp.getId(), targetId, value));
			listener.notifyComponents(listener.getAction());
		}
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
			final BlockingQueue<ISubComponent<EventHandler<Event>, Event, Object>> delegateQueue,
			final IComponentView<Node, EventHandler<Event>, Event, Object> component,
			final Map<String, Node> targetComponents, final Node parent,
			final String currentTaget) {
		if (parent == null) {
			final String validId = this.getValidTargetId(currentTaget,
					component.getExecutionTarget());
			this.handleTargetChange(delegateQueue, component, targetComponents,
					validId);
		} else if (currentTaget.equals(component.getExecutionTarget())) {
			this.addComponentByType(parent, component);
		} else {
			final String validId = this.getValidTargetId(currentTaget,
					component.getExecutionTarget());
			this.handleTargetChange(delegateQueue, component, targetComponents,
					validId);
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
		return currentTaget.length() < 2 ? FXUtil
				.getTargetComponentId(futureTarget) : futureTarget;
	}

	/**
	 * Handle component when target has changed
	 * 
	 * @param component
	 * @param targetComponents
	 */
	protected void handleTargetChange(
			final BlockingQueue<ISubComponent<EventHandler<Event>, Event, Object>> delegateQueue,
			final IComponentView<Node, EventHandler<Event>, Event, Object> component,
			final Map<String, Node> targetComponents, final String target) {
		final Node validContainer = this.getValidContainerById(
				targetComponents, target);
		if (validContainer != null) {
			this.handleLocalTargetChange(component, targetComponents,
					validContainer);
		} else {
			// handle target outside current perspective
			this.changeComponentTarget(delegateQueue, component);
		}
	}

	/**
	 * Handle target change inside perspective.
	 * 
	 * @param component
	 * @param targetComponents
	 * @param validContainer
	 */
	protected void handleLocalTargetChange(
			final UIComponent<Node, EventHandler<Event>, Event, Object> component,
			final Map<String, Node> targetComponents, final Node validContainer) {
		this.addComponentByType(validContainer, component);
	}

	/**
	 * Handle target change to an other perspective. If target component not
	 * found in current perspective, move to an other perspective and run
	 * teardown.
	 * 
	 * @param component
	 * @param targetComponents
	 * @param target
	 * @param layout
	 */
	protected void handlePerspectiveChange(
			final BlockingQueue<ISubComponent<EventHandler<Event>, Event, Object>> delegateQueue,
			final UIComponent<Node, EventHandler<Event>, Event, Object> component,
			final FXComponentLayout layout) {
		if (component instanceof AFXComponent || component instanceof AFXMLComponent) {
			FXUtil.invokeHandleMethodsByAnnotation(OnTearDown.class, component,
					layout);
		}
		// handle target outside current perspective
		this.changeComponentTarget(delegateQueue, component);
	}

	/**
	 * Move component to new target in perspective.
	 * 
	 * @param component
	 */
	protected final void changeComponentTarget(
			final BlockingQueue<ISubComponent<EventHandler<Event>, Event, Object>> delegateQueue,
			final ISubComponent<EventHandler<Event>, Event, Object> component) {
		final String targetId = component.getExecutionTarget();
		final String parentIdOld = component.getParentId();
		final String parentId = FXUtil.getTargetParentId(targetId);
		if (!parentIdOld.equals(parentId)) {
			// delegate to perspective observer
			delegateQueue.add(component);

		}
	}

	/**
	 * Runs the handle method of a componentView.
	 * 
	 * @param component
	 * @param action
	 * @return
	 */
	protected final Node prepareAndRunHandleMethod(
			final UIComponent <Node, EventHandler<Event>, Event, Object> component,
			final IAction<Event, Object> action) {
		return component.handle(action);
		
	}	


	/**
	 * Executes post handle method in application main thread. The result value of handle method (from worker thread) is Input for the postHandle Method. The return value or the handleReturnValue are the root node of this component.  
	 * 
	 * @param component
	 * @param action
	 */
	protected void executeComponentViewPostHandle(final Node handleReturnValue,
			final IComponentView<Node, EventHandler<Event>, Event, Object> component,
			final IAction<Event, Object> action) {
		Node tmp = component.postHandle(handleReturnValue, action);
		if(tmp==null) {
			tmp = handleReturnValue;
		}
		if (tmp != null) {
			tmp.setVisible(true);
			FXUtil.setPrivateMemberValue(AFXComponent.class, component,
					FXUtil.AFXCOMPONENT_ROOT, tmp);
		} 
	}
	
	/**
	 * Executes post handle method in application main thread. The result value of handle method (from worker thread) is Input for the postHandle Method. The return value or the handleReturnValue are the root node of this component.  
	 * 
	 * @param component
	 * @param action
	 */
	protected void executeDeclarativComponentViewPostHandle(final Node handleReturnValue,
			final IDeclarativComponentView<Node, EventHandler<Event>, Event, Object> component,
			final IAction<Event, Object> action) {
		component.postHandle(component.getRoot(),handleReturnValue, action);
		
	}


	protected void log(final String message) {
		if (Logger.getLogger(AFXComponentWorker.class.getName()).isLoggable(
				Level.FINE)) {
			Logger.getLogger(AFXComponentWorker.class.getName()).fine(
					">> " + message);
		}
	}

}
