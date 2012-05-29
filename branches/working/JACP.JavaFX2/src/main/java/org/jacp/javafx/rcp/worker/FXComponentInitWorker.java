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

import java.io.IOException;
import java.net.URL;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicBoolean;

import javafx.application.Platform;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.util.Callback;

import org.jacp.api.action.IAction;
import org.jacp.api.annotations.OnStart;
import org.jacp.api.component.IComponentView;
import org.jacp.api.component.IDeclarativComponentView;
import org.jacp.api.component.UIComponent;
import org.jacp.javafx.rcp.component.AFXMLComponent;
import org.jacp.javafx.rcp.component.ASubComponent;
import org.jacp.javafx.rcp.componentLayout.FXComponentLayout;
import org.jacp.javafx.rcp.util.Checkable;
import org.jacp.javafx.rcp.util.FXUtil;

/**
 * Background Worker to execute components; handle method to init component.
 * 
 * @author Andy Moncsek
 */
public class FXComponentInitWorker extends AFXComponentWorker<UIComponent<Node, EventHandler<Event>, Event, Object>> {

	private final Map<String, Node> targetComponents;
	private final UIComponent<Node, EventHandler<Event>, Event, Object> component;
	private final FXComponentLayout layout;
	private final IAction<Event, Object> action;
	private volatile BlockingQueue<Boolean> appThreadlock = new ArrayBlockingQueue<Boolean>(1);

	/**
	 * The workers constructor.
	 * 
	 * @param targetComponents
	 *            ; a map with all targets provided by perspective
	 * @param component
	 *            ; the UI component to init
	 * @param action
	 *            ; the init action
	 */
	public FXComponentInitWorker(final Map<String, Node> targetComponents,
			final UIComponent<Node, EventHandler<Event>, Event, Object> component, final IAction<Event, Object> action,
			final FXComponentLayout layout) {
		this.targetComponents = targetComponents;
		this.component = component;
		this.action = action;
		this.layout = layout;
	}

	/**
	 * run all methods that need to be invoked before worker thread start to run
	 */
	public void runPreInitMethods() {
		synchronized (this.component) {
			if (this.component instanceof IDeclarativComponentView) {
				IDeclarativComponentView <Node, EventHandler<Event>, Event, Object> dcComponent = (IDeclarativComponentView<Node, EventHandler<Event>, Event, Object>) this.component;
				FXUtil.setPrivateMemberValue(
						AFXMLComponent.class,
						component,
						FXUtil.ADECLARATIVECOMPONENT_ROOT,
						loadFXMLandSetController(dcComponent));
				this.runComponentOnStartupSequence((UIComponent<Node, EventHandler<Event>, Event, Object>) component, this.layout,dcComponent.getDocumentURL(),dcComponent.getResourceBundle());
				return;
			}
			this.runComponentOnStartupSequence((UIComponent<Node, EventHandler<Event>, Event, Object>) component, this.layout);
		}
	}

	@Override
	protected UIComponent<Node, EventHandler<Event>, Event, Object> call() throws Exception {
		synchronized (this.component) {
			FXUtil.setPrivateMemberValue(ASubComponent.class, this.component, FXUtil.ACOMPONENT_BLOCKED,
					new AtomicBoolean(true));
			this.log("3.4.4.2.1: subcomponent handle init START: " + this.component.getName());

			final Node handleReturnValue = this.prepareAndRunHandleMethod(this.component, this.action);

			this.log("3.4.4.2.2: subcomponent handle init get valid container: " + this.component.getName());
			// expect always local target id
			this.component.setExecutionTarget(FXUtil.getTargetComponentId(this.component.getExecutionTarget()));
			final Node validContainer = this.getValidContainerById(this.targetComponents,
					this.component.getExecutionTarget());
			this.log("3.4.4.2.3: subcomponent handle init add component by type: " + this.component.getName());
			if (this.component instanceof IComponentView) {
				this.addComonent(validContainer, handleReturnValue,
						(IComponentView<Node, EventHandler<Event>, Event, Object>) this.component, this.action);
			} else {
				this.addComonent(validContainer, handleReturnValue,
						(IDeclarativComponentView<Node, EventHandler<Event>, Event, Object>) this.component,
						this.action);
			}

			this.waitOnAppThreadLockRelease();

			this.log("3.4.4.2.4: subcomponent handle init END: " + this.component.getName());
			FXUtil.setPrivateMemberValue(ASubComponent.class, this.component, FXUtil.ACOMPONENT_BLOCKED,
					new AtomicBoolean(false));
			return this.component;
		}
	}

	/**
	 * Run at startup method in perspective.
	 * 
	 * @param component
	 */
	private void runComponentOnStartupSequence(UIComponent<Node, EventHandler<Event>, Event, Object> component, Object ...param) {
		FXUtil.invokeHandleMethodsByAnnotation(OnStart.class, component, param);
	}

	private Node loadFXMLandSetController(
			final IDeclarativComponentView<Node, EventHandler<Event>, Event, Object> fxmlComponent) {
		final URL url = getClass().getResource(fxmlComponent.getViewLocation());
		final FXMLLoader fxmlLoader = new FXMLLoader(url);
		fxmlLoader.setControllerFactory(new Callback<Class<?>, Object>() {
			@Override
			public Object call(Class<?> paramClass) {
				log("set FXML controller" + fxmlComponent.getName());
				return fxmlComponent;
			}
		});

		try {
			return (Node) fxmlLoader.load();
		} catch (IOException e) {
			throw new MissingResourceException(
					"fxml file not found --  place in resource folder and reference like this: uiDescriptionFile = \"/myUIFile.fxml\"",
					fxmlComponent.getViewLocation(), "");
		}
	}

	/**
	 * Handles "component add" in EDT must be called outside EDT.
	 * 
	 * @param validContainer
	 *            , a valid target where the component ui node is included
	 * @param component
	 *            , the ui component
	 * @throws InterruptedException
	 * @throws InvocationTargetException
	 */
	private void addComonent(final Node validContainer, final Node handleReturnValue,
			final IComponentView<Node, EventHandler<Event>, Event, Object> myComponent,
			final IAction<Event, Object> myAction) throws InterruptedException {

		Platform.runLater(new Runnable() {

			@Override
			public void run() {
				FXComponentInitWorker.this.executeComponentViewPostHandle(handleReturnValue, myComponent, myAction);
				FXComponentInitWorker.this.addComponentByType(validContainer, myComponent);
				FXComponentInitWorker.this.appThreadlock.add(true);
			}
		});

	}

	/**
	 * Handles "component add" in EDT must be called outside EDT.
	 * 
	 * @param validContainer
	 *            , a valid target where the component ui node is included
	 * @param component
	 *            , the ui component
	 * @throws InterruptedException
	 * @throws InvocationTargetException
	 */
	private void addComonent(final Node validContainer, final Node handleReturnValue,
			final IDeclarativComponentView<Node, EventHandler<Event>, Event, Object> myComponent,
			final IAction<Event, Object> myAction) throws InterruptedException {

		Platform.runLater(new Runnable() {

			@Override
			public void run() {
				FXComponentInitWorker.this.executeDeclarativComponentViewPostHandle(handleReturnValue, myComponent,
						myAction);
				FXComponentInitWorker.this.addComponentByType(validContainer, myComponent);
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
				this.log("Exception in Component INIT Worker, Thread interrupted: " + e.getMessage());
				// TODO add to error queue and restart thread if
				// messages in
				// queue
			} catch (final ExecutionException e) {
				this.log("Exception in Component INIT Worker, Thread Excecution Exception: " + e.getMessage());
				// TODO add to error queue and restart thread if
				// messages in
				// queue
			} catch (final Exception e) {
				this.log("Exception in Component INIT Worker, Thread Exception: " + e.getMessage());
				// TODO add to error queue and restart thread if
				// messages in
				// queue
			}
			FXUtil.setPrivateMemberValue(Checkable.class, this.component, FXUtil.ACOMPONENT_STARTED, true);
			FXUtil.setPrivateMemberValue(ASubComponent.class, this.component, FXUtil.ACOMPONENT_BLOCKED,
					new AtomicBoolean(false));
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
