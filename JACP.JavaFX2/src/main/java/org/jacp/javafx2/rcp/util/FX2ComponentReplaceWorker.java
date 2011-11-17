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
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutionException;

import javafx.application.Platform;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.MenuBar;

import org.jacp.api.action.IAction;
import org.jacp.api.component.IVComponent;
import org.jacp.api.componentLayout.Layout;

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
	private final Map<Layout, Node> bars;
	private final MenuBar menu;
	private volatile BlockingQueue<Boolean> appThreadlock = new ArrayBlockingQueue<Boolean>(
			1);

	public FX2ComponentReplaceWorker(
			final Map<String, Node> targetComponents,
			final IVComponent<Node, EventHandler<Event>, Event, Object> component,
			final Map<Layout, Node> bars, final MenuBar menu) {
		this.targetComponents = targetComponents;
		this.component = component;
		this.bars = bars;
		this.menu = menu;
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
							this.targetComponents, this.bars, this.menu,
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
			final Map<Layout, Node> bars, final MenuBar menu,
			final Node previousContainer, final String currentTaget) {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {

				if (component.isActive()) {
					FX2ComponentReplaceWorker.this.publishComponentValue(
							component, myAction, targetComponents, bars, menu,
							previousContainer, currentTaget);
				} else {
					// unregister component
					FX2ComponentReplaceWorker.this.removeComponentValue(
							component, previousContainer);
				}
				// release lock
				FX2ComponentReplaceWorker.this.appThreadlock.add(true);
			}
		});
	}

	private void removeComponentValue(
			final IVComponent<Node, EventHandler<Event>, Event, Object> component,
			final Node previousContainer) {
		// bar entries
		// final Map<Layout, Node> componentBarEnries =
		// component.getBarEntries();
		// when global bars and local bars are defined
		// TODO handle menu bar entries

		if (previousContainer != null) {
			final Node parent = previousContainer.getParent();
			if (parent != null) {
				FX2Util.getChildren(parent).remove(component.getRoot());
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
			final Map<Layout, Node> bars, final MenuBar menu,
			final Node previousContainer, final String currentTaget) {
		if (previousContainer != null) {

			this.executePostHandle(component, action);
			this.removeOldComponentValue(component, previousContainer);
			this.addNewComponentValue(component, previousContainer,
					currentTaget);

		}
	}

	/**
	 * remove old component value from root node
	 */
	private void removeOldComponentValue(
			final IVComponent<Node, EventHandler<Event>, Event, Object> component,
			final Node previousContainer) {
		final Node root = component.getRoot();
		final Node parent = previousContainer.getParent();
		// avoid remove/add when root component did not changed!
		if (root == null || root != previousContainer) {
			// remove old view
			this.log(" //1.1.1.1.3// handle old component remove: "
					+ component.getName());
			if (parent != null && previousContainer != null) {
				this.handleOldComponentRemove(parent, previousContainer);
			}
		}
	}

	/**
	 * add new component value to root node
	 */
	private void addNewComponentValue(
			final IVComponent<Node, EventHandler<Event>, Event, Object> component,
			final Node previousContainer, final String currentTaget) {
		final Node root = component.getRoot();
		final Node parent = previousContainer.getParent();
		if (root != null && root != previousContainer) {
			// add new view
			this.log(" //1.1.1.1.4// handle new component insert: "
					+ component.getName());
			root.setVisible(true);
			this.handleNewComponentValue(component, this.targetComponents,
					parent, currentTaget);

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
