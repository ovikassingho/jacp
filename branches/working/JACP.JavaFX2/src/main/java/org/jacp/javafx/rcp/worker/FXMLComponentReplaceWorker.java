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
package org.jacp.javafx.rcp.worker;

import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicBoolean;

import javafx.application.Platform;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.Node;

import org.jacp.api.action.IAction;
import org.jacp.api.annotations.OnTearDown;
import org.jacp.api.component.IDeclarativComponentView;
import org.jacp.api.component.ISubComponent;
import org.jacp.javafx.rcp.component.AFXComponent;
import org.jacp.javafx.rcp.component.ASubComponent;
import org.jacp.javafx.rcp.componentLayout.FXComponentLayout;
import org.jacp.javafx.rcp.util.FXUtil;

/**
 * Background Worker to execute fxml components handle method in separate
 * thread; While the handle method is executed in an own thread the postHandle
 * method is executed in application main thread.
 * 
 * @author Andy Moncsek
 * 
 */
public class FXMLComponentReplaceWorker extends
		AFXComponentWorker<IDeclarativComponentView<Node, EventHandler<Event>, Event, Object>> {

	private final Map<String, Node> targetComponents;
	private final IDeclarativComponentView<Node, EventHandler<Event>, Event, Object> component;
	private final FXComponentLayout layout;
	private final BlockingQueue<ISubComponent<EventHandler<Event>, Event, Object>> componentDelegateQueue;
	private volatile BlockingQueue<Boolean> appThreadlock = new ArrayBlockingQueue<Boolean>(1);

	public FXMLComponentReplaceWorker(final Map<String, Node> targetComponents,
			final BlockingQueue<ISubComponent<EventHandler<Event>, Event, Object>> componentDelegateQueue,
			final IDeclarativComponentView<Node, EventHandler<Event>, Event, Object> component,
			final FXComponentLayout layout) {
		this.targetComponents = targetComponents;
		this.component = component;
		this.layout = layout;
		this.componentDelegateQueue = componentDelegateQueue;
	}

	@Override
	protected IDeclarativComponentView<Node, EventHandler<Event>, Event, Object> call() throws Exception {
		synchronized (this.component) {
			try {
				FXUtil.setPrivateMemberValue(ASubComponent.class, this.component, FXUtil.ACOMPONENT_BLOCKED,
						new AtomicBoolean(true));
				while (this.component.hasIncomingMessage()) {
					final IAction<Event, Object> myAction = this.component.getNextIncomingMessage();
					this.log(" //1.1.1.1.1// handle replace component BEGIN: " + this.component.getName());

					final String currentTaget = this.component.getExecutionTarget();
					// run code
					this.log(" //1.1.1.1.2// handle component: " + this.component.getName());
					final Node handleReturnValue = this.prepareAndRunHandleMethod(this.component, myAction);
					this.log(" //1.1.1.1.3// publish component: " + this.component.getName());

					this.publish(this.component, myAction, this.targetComponents, this.layout, handleReturnValue,
							currentTaget);

					this.waitOnAppThreadLockRelease();

				}
			} catch (final IllegalStateException e) {
				if (e.getMessage().contains("Not on FX application thread")) {
					throw new UnsupportedOperationException(
							"Do not reuse Node components in handleAction method, use postHandleAction instead to verify that you change nodes in JavaFX main Thread");
				}
			} finally {
				FXUtil.setPrivateMemberValue(ASubComponent.class, this.component, FXUtil.ACOMPONENT_BLOCKED,
						new AtomicBoolean(false));
			}

		}
		return this.component;
	}

	/**
	 * publish handle result in application main thread
	 */
	private void publish(final IDeclarativComponentView<Node, EventHandler<Event>, Event, Object> component,
			final IAction<Event, Object> myAction, final Map<String, Node> targetComponents,
			final FXComponentLayout layout, final Node handleReturnValue, final String currentTaget) {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				if (component.isActive()) {
					FXMLComponentReplaceWorker.this.publishComponentValue(component, myAction, targetComponents,
							layout, handleReturnValue, currentTaget);
				} else {
					// unregister component
					FXMLComponentReplaceWorker.this.removeComponentValue(component, component.getRoot(), layout);
					// run teardown
					if (component instanceof AFXComponent) {
						FXUtil.invokeHandleMethodsByAnnotation(OnTearDown.class, component, layout);
					}
				}
				// release lock
				FXMLComponentReplaceWorker.this.appThreadlock.add(true);
			}
		});
	}

	private void removeComponentValue(final IDeclarativComponentView<Node, EventHandler<Event>, Event, Object> component,
			final Node root, final FXComponentLayout layout) {
		if (root != null) {
			final Node parent = root.getParent();
			if (parent != null) {
				this.handleOldComponentRemove(parent, root);
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
			final IDeclarativComponentView<Node, EventHandler<Event>, Event, Object> component,
			final IAction<Event, Object> action, final Map<String, Node> targetComponents,
			final FXComponentLayout layout, final Node handleReturnValue, final String currentTaget) {
		this.executeDeclarativComponentViewPostHandle(handleReturnValue, component, action);
		this.removeOldComponentValue(component, currentTaget);
		this.checkAndHandleTargetChange(component, currentTaget, layout);
	}

	/**
	 * remove old component value from root node
	 */
	private void removeOldComponentValue(final IDeclarativComponentView<Node, EventHandler<Event>, Event, Object> component, final String currentTaget) {
		if (!currentTaget.equals(component.getExecutionTarget())) {
			// remove old view
			this.removeComponentValue(component, component.getRoot(), this.layout);
		}
	}

	/**
	 * add new component value to root node
	 */
	private void checkAndHandleTargetChange(final IDeclarativComponentView<Node, EventHandler<Event>, Event, Object> component,
			final String currentTaget, final FXComponentLayout layout) {
		if (!currentTaget.equals(component.getExecutionTarget())) {
			final String validId = this.getValidTargetId(currentTaget, component.getExecutionTarget());
			final Node validContainer = this.getValidContainerById(this.targetComponents, validId);
			if (validContainer != null) {
				this.handleLocalTargetChange(component, this.targetComponents, validContainer);
			} else {
				this.handlePerspectiveChange(this.componentDelegateQueue, component, layout);
			}
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
			final IDeclarativComponentView<Node, EventHandler<Event>, Event, Object> component = this.get();
			FXUtil.setPrivateMemberValue(ASubComponent.class, component, FXUtil.ACOMPONENT_BLOCKED, new AtomicBoolean(
					false));
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
			FXUtil.setPrivateMemberValue(ASubComponent.class, this.component, FXUtil.ACOMPONENT_BLOCKED,
					new AtomicBoolean(false));
		}

	}

}
