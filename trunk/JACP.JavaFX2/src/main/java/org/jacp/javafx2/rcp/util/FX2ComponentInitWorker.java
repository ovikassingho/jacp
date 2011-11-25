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

import java.lang.reflect.InvocationTargetException;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutionException;

import javafx.application.Platform;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.Node;

import org.jacp.api.action.IAction;
import org.jacp.api.component.IVComponent;
import org.jacp.javafx2.rcp.componentLayout.FX2ComponentLayout;

/**
 * Background Worker to execute components; handle method to init component
 * 
 * @author Andy Moncsek
 */
public class FX2ComponentInitWorker
		extends
		AFX2ComponentWorker<IVComponent<Node, EventHandler<Event>, Event, Object>> {

	private final Map<String, Node> targetComponents;
	private final IVComponent<Node, EventHandler<Event>, Event, Object> component;
	private final IAction<Event, Object> action;
	private final FX2ComponentLayout layout;
	private volatile BlockingQueue<Boolean> appThreadlock = new ArrayBlockingQueue<Boolean>(
			1);

	public FX2ComponentInitWorker(
			final Map<String, Node> targetComponents,
			final IVComponent<Node, EventHandler<Event>, Event, Object> component,
			final FX2ComponentLayout layout, final IAction<Event, Object> action) {
		this.targetComponents = targetComponents;
		this.component = component;
		this.action = action;
		this.layout = layout;
	}

	@Override
	protected IVComponent<Node, EventHandler<Event>, Event, Object> call()
			throws Exception {
		synchronized (this.component) {
			this.component.setBlocked(true);
			this.log("3.4.4.2.1: subcomponent handle init START: "
					+ this.component.getName());
			final Node editorComponent = this.component.handle(this.action);
			this.component.setRoot(editorComponent);
			this.log("3.4.4.2.2: subcomponent handle init get valid container: "
					+ this.component.getName());
			// expect always local target id
			component.setExecutionTarget(this.getTargetComponentId(component.getExecutionTarget()));
			final Node validContainer = this.getValidContainerById(
					this.targetComponents, component.getExecutionTarget());
			this.log("3.4.4.2.3: subcomponent handle init add component by type: "
					+ this.component.getName());

			this.addComonent(validContainer, this.component, this.action,
					this.layout);

			this.waitOnAppThreadLockRelease();

			this.log("3.4.4.2.4: subcomponent handle init END: "
					+ this.component.getName());
			this.component.setBlocked(false);
			return this.component;
		}
	}

	/**
	 * handles "component add" in EDT must be called outside EDT
	 * 
	 * @param validContainer
	 * @param component
	 * @param bars
	 * @param menu
	 * @throws InterruptedException
	 * @throws InvocationTargetException
	 */
	private void addComonent(
			final Node validContainer,
			final IVComponent<Node, EventHandler<Event>, Event, Object> component,
			final IAction<Event, Object> action, final FX2ComponentLayout layout)
			throws InterruptedException {

		Platform.runLater(new Runnable() {

			@Override
			public void run() {
				FX2ComponentInitWorker.this
						.executePostHandle(component, action);
				FX2ComponentInitWorker.this.addComponentByType(validContainer,
						component);
				FX2ComponentInitWorker.this.appThreadlock.add(true);
			}
		});

	}

	@Override
	public final void done() {
		synchronized (this.component) {
			try {
				this.get();
			} catch (final InterruptedException e) {
				this.log("Exception in Component INIT Worker, Thread interrupted: "
						+ e.getMessage());
				// TODO add to error queue and restart thread if
				// messages in
				// queue
			} catch (final ExecutionException e) {
				this.log("Exception in Component INIT Worker, Thread Excecution Exception: "
						+ e.getMessage());
				// TODO add to error queue and restart thread if
				// messages in
				// queue
			} catch (final Exception e) {
				this.log("Exception in Component INIT Worker, Thread Exception: "
						+ e.getMessage());
				// TODO add to error queue and restart thread if
				// messages in
				// queue
			}
			this.component.setBlocked(false);
		}
	}

	private void waitOnAppThreadLockRelease() {

		try {
			this.appThreadlock.take();
		} catch (final InterruptedException e) {
			e.printStackTrace();
		}
	}
}
