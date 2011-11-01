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

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.MenuBar;

import org.jacp.api.action.IAction;
import org.jacp.api.action.IActionListener;

import org.jacp.api.componentLayout.Layout;
import org.jacp.api.component.IBGComponent;
import org.jacp.api.component.IExtendedComponent;
import org.jacp.api.component.ILayoutAbleComponent;
import org.jacp.api.component.ISubComponent;
import org.jacp.api.componentLayout.IPerspectiveLayout;
import org.jacp.api.coordinator.IComponentCoordinator;
import org.jacp.api.coordinator.ICoordinator;
import org.jacp.api.launcher.Launcher;
import org.jacp.api.perspective.IPerspective;
import org.jacp.javafx2.rcp.action.FX2Action;
import org.jacp.javafx2.rcp.action.FX2ActionListener;
import org.jacp.javafx2.rcp.component.AFX2Component;
import org.jacp.javafx2.rcp.component.AStateComponent;
import org.jacp.javafx2.rcp.component.AStatelessComponent;
import org.jacp.javafx2.rcp.componentLayout.FX2PerspectiveLayout;
import org.jacp.javafx2.rcp.coordinator.FX2ComponentCoordinator;
import org.jacp.javafx2.rcp.coordinator.FX2PerspectiveCoordinator;
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
		IPerspective<EventHandler<ActionEvent>, ActionEvent, Object>,
		IExtendedComponent<Node>, ILayoutAbleComponent<Node> {
	private String id;
	private String name;
	private boolean active;
	private boolean isActivated = false;
	public static int MAX_INCTANCE_COUNT;
	private final Logger logger = Logger.getLogger(this.getClass().getName());
	private Launcher<?> launcher;
	private final List<ISubComponent<EventHandler<ActionEvent>, ActionEvent, Object>> subcomponents = new CopyOnWriteArrayList<ISubComponent<EventHandler<ActionEvent>, ActionEvent, Object>>();
	private final IComponentCoordinator<EventHandler<ActionEvent>, ActionEvent, Object> componentHandler = new FX2ComponentCoordinator(
			this);
	private ICoordinator<EventHandler<ActionEvent>, ActionEvent, Object> perspectiveObserver;
	 
    private final IPerspectiveLayout<Node, Node> perspectiveLayout = new FX2PerspectiveLayout();
	private ExecutorService executor = Executors
			.newFixedThreadPool(MAX_INCTANCE_COUNT);

	static {
		final Runtime runtime = Runtime.getRuntime();
		final int nrOfProcessors = runtime.availableProcessors();
		MAX_INCTANCE_COUNT = nrOfProcessors + (nrOfProcessors / 2);
	}

	@Override
	public final void init(Launcher<?> launcher) {
		this.launcher = launcher;
		((FX2ComponentCoordinator) componentHandler).start();
	
	}

	@Override
	public final <C> C handle(IAction<ActionEvent, Object> action) {
		  throw new UnsupportedOperationException("Not supported yet.");
	}
	
	public abstract void handleMenuEntries(MenuBar menuBar) ;

	@Override
	public void handleMenuEntries(Node menuBar) {
		if(menuBar instanceof MenuBar){
			this.handleMenuEntries((MenuBar)menuBar);
		}
	}

	@Override
	public abstract void handleBarEntries(Map<Layout, Node> bars);
	
	public abstract void handlePerspective(IAction<ActionEvent, Object> action, final FX2PerspectiveLayout perspectiveLayout);

	@Override
	public void handlePerspective(IAction<ActionEvent, Object> action) {
		handlePerspective(action, (FX2PerspectiveLayout) perspectiveLayout);
	
	}

	@Override
	public final void registerComponent(
			ISubComponent<EventHandler<ActionEvent>, ActionEvent, Object> component) {
		log("register component: " + component.getId());
		componentHandler.addComponent(component);
		subcomponents.add(component);
		component.setParentPerspective(this);
	
	}

	@Override
	public final void unregisterComponent(
			ISubComponent<EventHandler<ActionEvent>, ActionEvent, Object> component) {
		log("unregister component: " + component.getId());
		componentHandler.removeComponent(component);
		subcomponents.remove(component);
		component.setParentPerspective(null);
	}

	@Override
	public final void initComponents(IAction<ActionEvent, Object> action) {
		final String targetId = getTargetComponentId(action.getTargetId());
		log("3.4.4.1: subcomponent targetId: " + targetId);
		final List<ISubComponent<EventHandler<ActionEvent>, ActionEvent, Object>> components = getSubcomponents();
		for (int i = 0; i < components.size(); i++) {
			final ISubComponent<EventHandler<ActionEvent>, ActionEvent, Object> component = components
					.get(i);
			if (component.getId().equals(targetId)) {
				log("3.4.4.2: subcomponent init with custom action");
				initComponent(action, component);
			} // else END
			else if (component.isActive() && !component.isActivated()) {
				log("3.4.4.2: subcomponent init with default action");
				initComponent(
						new FX2Action(component.getId(), component.getId(),
								"init"), component);
			} // if END
	
		} // for END
	}

	@Override
	public final void initComponent(
			IAction<ActionEvent, Object> action,
			ISubComponent<EventHandler<ActionEvent>, ActionEvent, Object> component) {
		if (component instanceof AFX2Component) {
			log("COMPONENT EXECUTE INIT:::" + component.getName());
			component.setActivated(true);			
			final FX2ComponentInitWorker tmp =  new FX2ComponentInitWorker(
					 perspectiveLayout.getTargetLayoutComponents(),
						((AFX2Component) component),
						((FX2PerspectiveCoordinator) perspectiveObserver).getBars(),action, (MenuBar) ((FX2PerspectiveCoordinator) perspectiveObserver)
						.getMenu());
			executor.execute(tmp);
		}// if END
		else if (component instanceof AStateComponent) {
			log("BACKGROUND COMPONENT EXECUTE INIT:::" + component.getName());
			putMessageToQueue(component, action);
			runStateComponent(action, ((AStateComponent) component));
		}// else if END
		else if (component instanceof AStatelessComponent) {
			log("SATELESS BACKGROUND COMPONENT EXECUTE INIT:::"
					+ component.getName());
			((AStatelessComponent) component).setLauncher(launcher);
			((AStatelessComponent) component).addMessage(action);
		}// else if END
	
	}

	@Override
	public final void handleAndReplaceComponent(
			IAction<ActionEvent, Object> action,
			ISubComponent<EventHandler<ActionEvent>, ActionEvent, Object> component) {
		if (component.isBlocked()) {
			putMessageToQueue(component, action);
			log("ADD TO QUEUE:::" + component.getName());
		} else {
			executeComponentReplaceThread(perspectiveLayout, component, action);
	
		}
		log("DONE EXECUTE REPLACE:::" + component.getName());
	}

	@Override
	public final void addActiveComponent(
			ISubComponent<EventHandler<ActionEvent>, ActionEvent, Object> component) {
	    // register new component at perspective
	    registerComponent(component);
	    if (component instanceof AFX2Component) {
	            // add component ui root to correct target
	            addComponentUIValue(getIPerspectiveLayout()
	                            .getTargetLayoutComponents(), component);
	    }
	
	}

	@Override
	public synchronized void delegateTargetChange(
			String target,
			ISubComponent<EventHandler<ActionEvent>, ActionEvent, Object> component) {
	    final String parentId = getTargetParentId(target);
	    if (!id.equals(parentId)) {
	            // unregister component in current perspective
	            unregisterComponent(component);
	            // delegate to perspective observer
	            perspectiveObserver.delegateTargetChange(target,
	                            component);
	
	    }
	
	}

	@Override
	public final void delegateComponentMassege(String target,
			IAction<ActionEvent, Object> action) {
		  componentHandler.delegateMessage(target, action);
	}

	@Override
	public final void delegateMassege(String target,
			IAction<ActionEvent, Object> action) {
		 perspectiveObserver.delegateMessage(target, action);
	}

	@Override
	public IActionListener<EventHandler<ActionEvent>, ActionEvent, Object> getActionListener() {
		return new FX2ActionListener(new FX2Action(id), perspectiveObserver);
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
	public List<ISubComponent<EventHandler<ActionEvent>, ActionEvent, Object>> getSubcomponents() {
		return this.subcomponents;
	}

	@Override
	public boolean isActive() {
		return this.active;
	}

	@Override
	public boolean isActivated() {
		return this.isActivated;
	}

	@Override
	public final void setName(String name) {
		this.name = name;
	}

	@Override
	public final void setObserver(
			ICoordinator<EventHandler<ActionEvent>, ActionEvent, Object> observer) {
		this.perspectiveObserver = observer;
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
	public final void setActivated(boolean isActive) {
		this.isActivated = isActive;
	}

	@Override
	public final void setSubcomponents(
			List<ISubComponent<EventHandler<ActionEvent>, ActionEvent, Object>> subComponents) {
		  registerSubcomponents(subComponents);
	
	}

	/**
	 * set component blocked and add message to queue
	 * 
	 * @param component
	 * @param action
	 */
	private final void putMessageToQueue(
			final ISubComponent<EventHandler<ActionEvent>, ActionEvent, Object> component,
			final IAction<ActionEvent, Object> action) {
		component.putIncomingMessage(action);
	}

	/**
	 * run background components thread
	 * 
	 * @param action
	 * @param component
	 */
	private final void runStateComponent(
                    final IAction<ActionEvent, Object> action,
                    final IBGComponent<EventHandler<ActionEvent>, ActionEvent, Object> component) {
    	executor.execute(new StateComponentRunWorker(component));
    }

	/**
	 * run component in background thread
	 * 
	 * @param layout
	 * @param component
	 */
	private final void runFXComponent(
			final IPerspectiveLayout<? extends Node, Node> layout,
			final ISubComponent<EventHandler<ActionEvent>, ActionEvent, Object> component) {
		executor.execute(new FX2ComponentReplaceWorker(layout
				.getTargetLayoutComponents(), ((AFX2Component) component),
				((FX2PerspectiveCoordinator) perspectiveObserver).getBars(),
				(MenuBar) ((FX2PerspectiveCoordinator) perspectiveObserver)
						.getMenu()));
	}

	/**
     * start component replace thread, be aware that all actions are in components message box!
     * 
     * @param layout
     * @param component
     */
    private final void executeComponentReplaceThread(
                    final IPerspectiveLayout<? extends Node, Node> layout,
                    final ISubComponent<EventHandler<ActionEvent>, ActionEvent, Object> component,
                    final IAction<ActionEvent, Object> action) {
            if (component instanceof AFX2Component) {
                    log("CREATE NEW THREAD:::"
                                    + component.getName());
                    putMessageToQueue(component, action);
                    runFXComponent(layout, component);

            } else if (component instanceof AStateComponent) {
                    log("CREATE NEW THREAD:::"
                                    + component.getName());
                    putMessageToQueue(component, action);
                    runStateComponent(action, ((AStateComponent) component));
            } else if(component instanceof AStatelessComponent) {
                    log("RUN STATELESS COMPONENTS:::"
                                    + component.getName());
                    ((AStatelessComponent) component)
                                    .addMessage(action);
            }

    }

	/**
     * handles ui return value and it to perspective TODO check correctness
     * of implementation
     * 
     * @param targetComponents
     * @param component
     */
    private void addComponentUIValue(
                    final Map<String, Node> targetComponents,
                    final ISubComponent<EventHandler<ActionEvent>, ActionEvent, Object> component) {
            	executor.execute(new FX2ComponentAddWorker(targetComponents,
                                    ((AFX2Component) component)));
    }

	private void log(final String message) {
		if (logger.isLoggable(Level.FINE)) {
			logger.fine(">> " + message);
		}
	}

	 /**
     * register components at componentHandler
     * 
     * @param <M>
     * @param components
     */
    private <M extends ISubComponent<EventHandler<ActionEvent>, ActionEvent, Object>> void registerSubcomponents(
                    final List<M> components) {
            for (int i=0; i<components.size(); i++) {
                    final M component = components.get(i);
                    registerComponent(component);
            }
    }

    /**
     * returns the message target id
     * 
     * @param messageId
     * @return
     */
    private String getTargetComponentId(final String messageId) {
            final String[] targetId = getTargetId(messageId);
            if (isFullValidId(targetId)) {
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
            final String[] parentId = getTargetId(messageId);
            if (isFullValidId(parentId)) {
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
     * a target id is valid, when it does contain a perspective and a
     * component id (perspectiveId.componentId)
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
