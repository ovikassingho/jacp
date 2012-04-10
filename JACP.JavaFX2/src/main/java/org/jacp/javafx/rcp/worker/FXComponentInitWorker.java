/************************************************************************
 * 
 * Copyright (C) 2010 - 2012
 *
 * [FX2ComponentInitWorker.java]
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

import java.lang.reflect.InvocationTargetException;
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
import org.jacp.api.component.IComponentView;
import org.jacp.javafx.rcp.component.AComponent;
import org.jacp.javafx.rcp.component.ASubComponent;
import org.jacp.javafx.rcp.componentLayout.FXComponentLayout;
import org.jacp.javafx.rcp.util.FXUtil;

/**
 * Background Worker to execute components; handle method to init component.
 * 
 * @author Andy Moncsek
 */
public class FXComponentInitWorker
		extends
		AFXComponentWorker<IComponentView<Node, EventHandler<Event>, Event, Object>> {

	private final Map<String, Node> targetComponents;
	private final IComponentView<Node, EventHandler<Event>, Event, Object> component;
	private final IAction<Event, Object> action;
	private final FXComponentLayout layout;
	private volatile BlockingQueue<Boolean> appThreadlock = new ArrayBlockingQueue<Boolean>(
			1);

	public FXComponentInitWorker(
			final Map<String, Node> targetComponents,
			final IComponentView<Node, EventHandler<Event>, Event, Object> component,
			final FXComponentLayout layout, final IAction<Event, Object> action) {
		this.targetComponents = targetComponents;
		this.component = component;
		this.action = action;
		this.layout = layout;
	}

	@Override
	protected IComponentView<Node, EventHandler<Event>, Event, Object> call()
			throws Exception {
		synchronized (this.component) {
			FXUtil.setPrivateMemberValue(ASubComponent.class, this.component,
					"blocked", new AtomicBoolean(true));
			this.log("3.4.4.2.1: subcomponent handle init START: "
					+ this.component.getName());
			this.prepareAndHandleComponent(this.component, this.action);
			this.log("3.4.4.2.2: subcomponent handle init get valid container: "
					+ this.component.getName());
			// expect always local target id
			this.component.setExecutionTarget(FXUtil
					.getTargetComponentId(this.component.getExecutionTarget()));
			final Node validContainer = this.getValidContainerById(
					this.targetComponents, this.component.getExecutionTarget());
			this.log("3.4.4.2.3: subcomponent handle init add component by type: "
					+ this.component.getName());

			this.addComonent(validContainer, this.component, this.action,
					this.layout);

			this.waitOnAppThreadLockRelease();

			this.log("3.4.4.2.4: subcomponent handle init END: "
					+ this.component.getName());
			FXUtil.setPrivateMemberValue(ASubComponent.class, this.component,
					"blocked", new AtomicBoolean(false));
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
			final IComponentView<Node, EventHandler<Event>, Event, Object> component,
			final IAction<Event, Object> action, final FXComponentLayout layout)
			throws InterruptedException {

		Platform.runLater(new Runnable() {

			@Override
			public void run() {
				FXComponentInitWorker.this.executePostHandle(component, action);
				FXComponentInitWorker.this.addComponentByType(validContainer,
						component);
				FXComponentInitWorker.this.appThreadlock.add(true);
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
			FXUtil.setPrivateMemberValue(AComponent.class, this.component,
					"started", true);
			FXUtil.setPrivateMemberValue(ASubComponent.class, this.component,
					"blocked", new AtomicBoolean(false));
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
