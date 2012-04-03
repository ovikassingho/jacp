/************************************************************************
 * 
 * Copyright (C) 2010 - 2012
 *
 * [FX2ComponentReplaceWorker.java]
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
package org.jacp.javafx.rcp.util;

import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutionException;

import javafx.application.Platform;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.Node;

import org.jacp.api.action.IAction;
import org.jacp.api.component.ISubComponent;
import org.jacp.api.component.IVComponent;
import org.jacp.javafx.rcp.component.AFXComponent;
import org.jacp.javafx.rcp.componentLayout.FXComponentLayout;

/**
 * Background Worker to execute components handle method in separate thread and
 * to replace or add the component result node; While the handle method is
 * executed in an own thread the postHandle method is executed in application
 * main thread.
 * 
 * @author Andy Moncsek
 * 
 */
public class FX2ComponentReplaceWorker
		extends
		AFX2ComponentWorker<IVComponent<Node, EventHandler<Event>, Event, Object>> {

	private final Map<String, Node> targetComponents;
	private final IVComponent<Node, EventHandler<Event>, Event, Object> component;
	private final FXComponentLayout layout;
	private final BlockingQueue<ISubComponent<EventHandler<Event>, Event, Object>> componentDelegateQueue;
	private volatile BlockingQueue<Boolean> appThreadlock = new ArrayBlockingQueue<Boolean>(
			1);

	public FX2ComponentReplaceWorker(
			final Map<String, Node> targetComponents,final BlockingQueue<ISubComponent<EventHandler<Event>, Event, Object>> componentDelegateQueue,
			final IVComponent<Node, EventHandler<Event>, Event, Object> component,
			final FXComponentLayout layout) {
		this.targetComponents = targetComponents;
		this.component = component;
		this.layout = layout;
		this.componentDelegateQueue = componentDelegateQueue;
	}

	@Override
	protected IVComponent<Node, EventHandler<Event>, Event, Object> call()
			throws Exception {
		synchronized (this.component) {
			try {
				this.component.setBlocked(true);
				while (this.component.hasIncomingMessage()) {
					final IAction<Event, Object> myAction = this.component
							.getNextIncomingMessage();
					this.log(" //1.1.1.1.1// handle replace component BEGIN: "
							+ this.component.getName());

					final Node previousContainer = this.component.getRoot();
					final String currentTaget = this.component
							.getExecutionTarget();
					// run code
					this.log(" //1.1.1.1.2// handle component: "
							+ this.component.getName());
					this.prepareAndHandleComponent(this.component, myAction);
					this.log(" //1.1.1.1.3// publish component: "
							+ this.component.getName());

					this.publish(this.component, myAction,
							this.targetComponents, this.layout,
							previousContainer, currentTaget);

					this.waitOnAppThreadLockRelease();

				}
			} catch (final IllegalStateException e) {
				if (e.getMessage().contains("Not on FX application thread")) {
					throw new UnsupportedOperationException(
							"Do not reuse Node components in handleAction method, use postHandleAction instead to verify that you change nodes in JavaFX main Thread");
				}
			} finally {
				this.component.setBlocked(false);
			}

		}
		return this.component;
	}

	/**
	 * publish handle result in application main thread
	 */
	private void publish(
			final IVComponent<Node, EventHandler<Event>, Event, Object> component,
			final IAction<Event, Object> myAction,
			final Map<String, Node> targetComponents,
			final FXComponentLayout layout, final Node previousContainer,
			final String currentTaget) {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				if (component.isActive()) {
					FX2ComponentReplaceWorker.this.publishComponentValue(
							component, myAction, targetComponents, layout,
							previousContainer, currentTaget);
				} else {
					// unregister component
					FX2ComponentReplaceWorker.this.removeComponentValue(
							component, previousContainer, layout);
					// run teardown
					if (component instanceof AFXComponent) {
						((AFXComponent) component).onTearDownComponent(layout);
					}
				}
				// release lock
				FX2ComponentReplaceWorker.this.appThreadlock.add(true);
			}
		});
	}

	private void removeComponentValue(
			final IVComponent<Node, EventHandler<Event>, Event, Object> component,
			final Node previousContainer, final FXComponentLayout layout) {
		if (previousContainer != null) {
			final Node parent = previousContainer.getParent();
			if (parent != null) {
				handleOldComponentRemove(parent, previousContainer);
			}
		}

	}

	/**
	 * run in thread
	 * 
	 * @param previousContainer
	 * @param currentTaget
	 */
	private void publishComponentValue(
			final IVComponent<Node, EventHandler<Event>, Event, Object> component,
			final IAction<Event, Object> action,
			final Map<String, Node> targetComponents,
			final FXComponentLayout layout, final Node previousContainer,
			final String currentTaget) {
		if (previousContainer != null) {
			this.executePostHandle(component, action);
			this.removeOldComponentValue(component, previousContainer,
					currentTaget);
			this.addNewComponentValue(component, previousContainer,
					currentTaget, layout);

		}
	}

	/**
	 * remove old component value from root node
	 */
	private void removeOldComponentValue(
			final IVComponent<Node, EventHandler<Event>, Event, Object> component,
			final Node previousContainer, final String currentTaget) {
		final Node root = component.getRoot();
		// avoid remove/add when root component did not changed!
		if (!currentTaget.equals(component.getExecutionTarget())
				|| root == null || root != previousContainer) {
			// remove old view
			removeComponentValue(component, previousContainer, layout);
		}
	}

	/**
	 * add new component value to root node
	 */
	private void addNewComponentValue(
			final IVComponent<Node, EventHandler<Event>, Event, Object> component,
			final Node previousContainer, final String currentTaget,
			final FXComponentLayout layout) {
		final Node root = component.getRoot();
		final Node parentNode = previousContainer.getParent();
		if (!currentTaget.equals(component.getExecutionTarget())) {
			final String validId = this.getValidTargetId(currentTaget,
					component.getExecutionTarget());
			final Node validContainer = this.getValidContainerById(
					targetComponents, validId);
			if (validContainer != null) {
				handleLocalTargetChange(component, targetComponents,
						validContainer);
			} else {
				handlePerspectiveChange(this.componentDelegateQueue, component, layout);
			}
		} else if (root != null && root != previousContainer) {
			// add new view
			this.log(" //1.1.1.1.4// handle new component insert: "
					+ component.getName());
			handleViewState(root,true);
			this.handleNewComponentValue(this.componentDelegateQueue, component,
					this.targetComponents, parentNode, currentTaget);
		}

	}

	private void waitOnAppThreadLockRelease() {
		try {
			this.appThreadlock.take();
		} catch (final InterruptedException e) {
			e.printStackTrace();
		}
	}

	@Override
	protected final void done() {
		try {
			final IVComponent<Node, EventHandler<Event>, Event, Object> component = this
					.get();
			component.setBlocked(false);
		} catch (final InterruptedException e) {
			e.printStackTrace();
			// TODO add to error queue and restart thread if
			// messages in
			// queue
		} catch (final ExecutionException e) {
			e.printStackTrace();
			// TODO add to error queue and restart thread if
			// messages in
			// queue
		} catch (final Exception e) {
			e.printStackTrace();
			// TODO add to error queue and restart thread if
			// messages in
			// queue
		} finally {
			this.component.setBlocked(false);
		}

	}

}
