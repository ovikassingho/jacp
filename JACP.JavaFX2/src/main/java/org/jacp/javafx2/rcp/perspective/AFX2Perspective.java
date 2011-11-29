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

package org.jacp.javafx2.rcp.perspective;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.application.Platform;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.Node;

import org.jacp.api.action.IAction;
import org.jacp.api.action.IActionListener;
import org.jacp.api.component.ICallbackComponent;
import org.jacp.api.component.IExtendedComponent;
import org.jacp.api.component.ILayoutAbleComponent;
import org.jacp.api.component.ISubComponent;
import org.jacp.api.componentLayout.IBaseLayout;
import org.jacp.api.componentLayout.IPerspectiveLayout;
import org.jacp.api.coordinator.IComponentCoordinator;
import org.jacp.api.coordinator.ICoordinator;
import org.jacp.api.launcher.Launcher;
import org.jacp.api.perspective.IPerspective;
import org.jacp.javafx2.rcp.action.FX2Action;
import org.jacp.javafx2.rcp.action.FX2ActionListener;
import org.jacp.javafx2.rcp.component.AFX2Component;
import org.jacp.javafx2.rcp.component.ACallbackComponent;
import org.jacp.javafx2.rcp.component.AStatelessCallbackComponent;
import org.jacp.javafx2.rcp.componentLayout.FX2ComponentLayout;
import org.jacp.javafx2.rcp.componentLayout.FX2PerspectiveLayout;
import org.jacp.javafx2.rcp.coordinator.FX2ComponentCoordinator;
import org.jacp.javafx2.rcp.util.FX2ComponentAddWorker;
import org.jacp.javafx2.rcp.util.FX2ComponentInitWorker;
import org.jacp.javafx2.rcp.util.FX2ComponentReplaceWorker;
import org.jacp.javafx2.rcp.util.StateComponentRunWorker;

/**
 * represents a basic javafx2 perspective that handles subcomponents,
 * perspectives are not handled in thread so avoid long running tasks in
 * perspectives.
 * 
 * @author Andy Moncsek
 */
public abstract class AFX2Perspective implements
		IPerspective<EventHandler<Event>, Event, Object>,
		IExtendedComponent<Node>, ILayoutAbleComponent<Node> {
	private String id;
	private String name;
	private boolean active;
	private boolean isActivated = false;
	public static int MAX_INCTANCE_COUNT;
	private final Logger logger = Logger.getLogger(this.getClass().getName());
	private Launcher<?> launcher;
	private final List<ISubComponent<EventHandler<Event>, Event, Object>> subcomponents = new CopyOnWriteArrayList<ISubComponent<EventHandler<Event>, Event, Object>>();
	private final IComponentCoordinator<EventHandler<Event>, Event, Object> componentHandler = new FX2ComponentCoordinator(
			this);
	private final FX2ComponentAddWorker addWorker = new FX2ComponentAddWorker();
	private ICoordinator<EventHandler<Event>, Event, Object> perspectiveObserver;
	private FX2ComponentLayout layout;

	private final IPerspectiveLayout<Node, Node> perspectiveLayout = new FX2PerspectiveLayout();
	private final ExecutorService executor = Executors
			.newFixedThreadPool(AFX2Perspective.MAX_INCTANCE_COUNT);

	static {
		final Runtime runtime = Runtime.getRuntime();
		final int nrOfProcessors = runtime.availableProcessors();
		AFX2Perspective.MAX_INCTANCE_COUNT = nrOfProcessors
				+ (nrOfProcessors / 2);
	}

	@Override
	public final void init(Launcher<?> launcher) {
		this.launcher = launcher;
		((FX2ComponentCoordinator) this.componentHandler).start();

	}

	@Override
	public final <C> C handle(IAction<Event, Object> action) {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final void onStart(final IBaseLayout<Node> layout) {
		this.layout = (FX2ComponentLayout) layout;
		onStartPerspective(this.layout);

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onTearDown(final IBaseLayout<Node> layout) {
		onTearDownPerspective(this.layout);
		this.layout = null;
	}

	/**
	 * Handle menu, bars and other UI components on component start.
	 * 
	 * @param menuBar
	 * @param bars
	 */
	public abstract void onStartPerspective(final FX2ComponentLayout layout);

	/**
	 * Clean up menu, bars and other components on component teardown.
	 * 
	 * @param menuBar
	 * @param bars
	 */
	public abstract void onTearDownPerspective(final FX2ComponentLayout layout);

	public abstract void handlePerspective(IAction<Event, Object> action,
			final FX2PerspectiveLayout perspectiveLayout);

	@Override
	public void handlePerspective(IAction<Event, Object> action) {
		this.handlePerspective(action,
				(FX2PerspectiveLayout) this.perspectiveLayout);

	}

	@Override
	public final void registerComponent(
			ISubComponent<EventHandler<Event>, Event, Object> component) {
		this.log("register component: " + component.getId());
		this.componentHandler.addComponent(component);
		this.subcomponents.add(component);

	}

	@Override
	public final void unregisterComponent(
			ISubComponent<EventHandler<Event>, Event, Object> component) {
		this.log("unregister component: " + component.getId());
		this.componentHandler.removeComponent(component);
		this.subcomponents.remove(component);
	}

	@Override
	public final void initComponents(IAction<Event, Object> action) {
		final String targetId = this.getTargetComponentId(action.getTargetId());
		this.log("3.4.4.1: subcomponent targetId: " + targetId);
		final List<ISubComponent<EventHandler<Event>, Event, Object>> components = this
				.getSubcomponents();
		for (int i = 0; i < components.size(); i++) {
			final ISubComponent<EventHandler<Event>, Event, Object> component = components
					.get(i);
			if (component.getId().equals(targetId)) {
				this.log("3.4.4.2: subcomponent init with custom action");
				this.initComponent(action, component);
			} // else END
			else if (component.isActive() && !component.isStarted()) {
				this.log("3.4.4.2: subcomponent init with default action");
				this.initComponent(
						new FX2Action(component.getId(), component.getId(),
								"init"), component);
			} // if END

		} // for END
	}

	@Override
	public final void initComponent(final IAction<Event, Object> action,
			final ISubComponent<EventHandler<Event>, Event, Object> component) {
		if(Platform.isFxApplicationThread()) {
			handleInit(action, component);
		} else {
			Platform.runLater(new Runnable() {
				@Override
				public void run() {
					handleInit(action, component);
				}
			});
		}
		
	}
	/**
	 * Execute component initialization.
	 * @param action
	 * @param component
	 */
	private void handleInit(final IAction<Event, Object> action,
			final ISubComponent<EventHandler<Event>, Event, Object> component) {
		if (component instanceof AFX2Component) {
			this.log("COMPONENT EXECUTE INIT:::" + component.getName());
			component.setStarted(true);
			this.runComponentOnStartupSequence(((AFX2Component) component));
			final FX2ComponentInitWorker tmp = new FX2ComponentInitWorker(
					this.perspectiveLayout.getTargetLayoutComponents(),
					((AFX2Component) component), this.layout, action);
			this.executor.execute(tmp);
		}// if END
		else if (component instanceof ACallbackComponent) {
			this.log("BACKGROUND COMPONENT EXECUTE INIT:::"
					+ component.getName());
			this.putMessageToQueue(component, action);
			this.runStateComponent(action, ((ACallbackComponent) component));
		}// else if END
		else if (component instanceof AStatelessCallbackComponent) {
			this.log("SATELESS BACKGROUND COMPONENT EXECUTE INIT:::"
					+ component.getName());
			((AStatelessCallbackComponent) component).setLauncher(this.launcher);
			((AStatelessCallbackComponent) component).addMessage(action);
		}// else if END

	}

	/**
	 * run at startup method in perspective
	 * 
	 * @param component
	 */
	private void runComponentOnStartupSequence(final AFX2Component component) {
		component.onStartComponent(this.layout);
	}

	@Override
	public final void handleAndReplaceComponent(IAction<Event, Object> action,
			ISubComponent<EventHandler<Event>, Event, Object> component) {
		if (component.isBlocked()) {
			this.putMessageToQueue(component, action);
			this.log("ADD TO QUEUE:::" + component.getName());
		} else {
			this.executeComponentReplaceThread(this.perspectiveLayout,
					component, action, this.layout);

		}
		this.log("DONE EXECUTE REPLACE:::" + component.getName());
	}

	@Override
	public final void addActiveComponent(
			ISubComponent<EventHandler<Event>, Event, Object> component) {
		// register new component at perspective
		this.registerComponent(component);
		if (component instanceof AFX2Component) {
			runComponentOnStartupSequence((AFX2Component) component);
			// add component ui root to correct target
			this.addComponentUIValue(this.getIPerspectiveLayout()
					.getTargetLayoutComponents(), component);
		}

	}

	@Override
	public synchronized void delegateTargetChange(String target,
			ISubComponent<EventHandler<Event>, Event, Object> component) {
		final String parentId = this.getTargetParentId(target);
		if (!this.id.equals(parentId)) {
			// unregister component in current perspective
			this.unregisterComponent(component);
			// delegate to perspective observer
			this.perspectiveObserver.delegateTargetChange(target, component);

		}

	}

	@Override
	public final void delegateComponentMassege(String target,
			IAction<Event, Object> action) {
		this.componentHandler.delegateMessage(target, action);
	}

	@Override
	public final void delegateMassege(String target,
			IAction<Event, Object> action) {
		this.perspectiveObserver.delegateMessage(target, action);
	}

	@Override
	public IActionListener<EventHandler<Event>, Event, Object> getActionListener() {
		return new FX2ActionListener(new FX2Action(this.id),
				this.perspectiveObserver);
	}

	@Override
	public String getId() {
		return this.id;
	}

	@Override
	public String getName() {
		return this.name;
	}

	@Override
	public IPerspectiveLayout<? extends Node, Node> getIPerspectiveLayout() {
		return this.perspectiveLayout;
	}

	@Override
	public List<ISubComponent<EventHandler<Event>, Event, Object>> getSubcomponents() {
		return this.subcomponents;
	}

	@Override
	public boolean isActive() {
		return this.active;
	}

	@Override
	public boolean isStarted() {
		return this.isActivated;
	}

	@Override
	public final void setName(String name) {
		this.name = name;
	}

	@Override
	public final void setObserver(
			ICoordinator<EventHandler<Event>, Event, Object> observer) {
		this.perspectiveObserver = observer;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public final ICoordinator<EventHandler<Event>, Event, Object> getObserver(){
		return this.perspectiveObserver;
	}

	@Override
	public final void setId(String id) {
		this.id = id;
	}

	@Override
	public final void setActive(boolean active) {
		this.active = active;
	}

	@Override
	public final void setStarted(boolean isActive) {
		this.isActivated = isActive;
	}

	@Override
	public final void setSubcomponents(
			List<ISubComponent<EventHandler<Event>, Event, Object>> subComponents) {
		this.registerSubcomponents(subComponents);

	}

	/**
	 * set component blocked and add message to queue
	 * 
	 * @param component
	 * @param action
	 */
	private final void putMessageToQueue(
			final ISubComponent<EventHandler<Event>, Event, Object> component,
			final IAction<Event, Object> action) {
		component.putIncomingMessage(action);
	}

	/**
	 * run background components thread
	 * 
	 * @param action
	 * @param component
	 */
	private final void runStateComponent(final IAction<Event, Object> action,
			final ICallbackComponent<EventHandler<Event>, Event, Object> component) {
		this.executor.execute(new StateComponentRunWorker(component));
	}

	/**
	 * run component in background thread
	 * 
	 * @param layout
	 * @param component
	 */
	private final void runFXComponent(
			final IPerspectiveLayout<? extends Node, Node> perspectiveLayout,
			final ISubComponent<EventHandler<Event>, Event, Object> component,
			final FX2ComponentLayout layout) {
		this.executor.execute(new FX2ComponentReplaceWorker(perspectiveLayout
				.getTargetLayoutComponents(), ((AFX2Component) component),
				layout));
	}

	/**
	 * start component replace thread, be aware that all actions are in
	 * components message box!
	 * 
	 * @param layout
	 * @param component
	 */
	private final void executeComponentReplaceThread(
			final IPerspectiveLayout<? extends Node, Node> perspectiveLayout,
			final ISubComponent<EventHandler<Event>, Event, Object> component,
			final IAction<Event, Object> action, final FX2ComponentLayout layout) {
		if (component instanceof AFX2Component) {
			this.log("CREATE NEW THREAD:::" + component.getName());
			this.putMessageToQueue(component, action);
			this.runFXComponent(perspectiveLayout, component, layout);

		} else if (component instanceof ACallbackComponent) {
			this.log("CREATE NEW THREAD:::" + component.getName());
			this.putMessageToQueue(component, action);
			this.runStateComponent(action, ((ACallbackComponent) component));
		} else if (component instanceof AStatelessCallbackComponent) {
			this.log("RUN STATELESS COMPONENTS:::" + component.getName());
			((AStatelessCallbackComponent) component).addMessage(action);
		}

	}

	/**
	 * handles ui return value and it to perspective TODO check correctness of
	 * implementation
	 * 
	 * @param targetComponents
	 * @param component
	 */
	private void addComponentUIValue(final Map<String, Node> targetComponents,
			final ISubComponent<EventHandler<Event>, Event, Object> component) {
		addWorker.handleInApplicationThread(targetComponents,
				((AFX2Component) component));
	}

	private void log(final String message) {
		if (this.logger.isLoggable(Level.FINE)) {
			this.logger.fine(">> " + message);
		}
	}

	/**
	 * register components at componentHandler
	 * 
	 * @param <M>
	 * @param components
	 */
	private <M extends ISubComponent<EventHandler<Event>, Event, Object>> void registerSubcomponents(
			final List<M> components) {
		for (int i = 0; i < components.size(); i++) {
			final M component = components.get(i);
			this.registerComponent(component);
		}
	}

	/**
	 * returns the message target id
	 * 
	 * @param messageId
	 * @return
	 */
	private String getTargetComponentId(final String messageId) {
		final String[] targetId = this.getTargetId(messageId);
		if (this.isFullValidId(targetId)) {
			return targetId[1];
		}
		return messageId;
	}

	/**
	 * returns the message (parent) target id
	 * 
	 * @param messageId
	 * @return
	 */
	private String getTargetParentId(final String messageId) {
		final String[] parentId = this.getTargetId(messageId);
		if (this.isFullValidId(parentId)) {
			return parentId[0];
		}
		return messageId;
	}

	/**
	 * returns target message with perspective and component name
	 * 
	 * @param messageId
	 * @return
	 */
	private String[] getTargetId(final String messageId) {
		return messageId.split("\\.");
	}

	/**
	 * a target id is valid, when it does contain a perspective and a component
	 * id (perspectiveId.componentId)
	 * 
	 * @param targetId
	 * @return
	 */
	private boolean isFullValidId(final String[] targetId) {
		if (targetId != null && targetId.length == 2) {
			return true;
		}

		return false;
	}
}
