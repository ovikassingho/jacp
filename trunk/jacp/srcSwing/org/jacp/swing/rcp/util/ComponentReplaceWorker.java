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

package org.jacp.swing.rcp.util;

import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutionException;

import javax.swing.JMenu;

import org.jacp.api.action.IAction;
import org.jacp.api.component.IVComponent;
import org.jacp.api.componentLayout.Layout;
import org.jacp.api.perspective.IPerspective;

/**
 * Background Worker to execute components handle method to replace or add the
 * component
 * 
 * @author Andy Moncsek
 * 
 */
public class ComponentReplaceWorker
	extends
	AbstractComponentWorker<IVComponent<Container, ActionListener, ActionEvent, Object>> {
    private final Map<String, Container> targetComponents;
    private final IVComponent<Container, ActionListener, ActionEvent, Object> component;
    private final IAction<ActionEvent, Object> action;
    private final JMenu menu;
    private final Map<Layout, Container> bars;
    private volatile BlockingQueue<Boolean> lock = new ArrayBlockingQueue<Boolean>(
	    1);

    public ComponentReplaceWorker(
	    final Map<String, Container> targetComponents,
	    final IVComponent<Container, ActionListener, ActionEvent, Object> component,
	    final IAction<ActionEvent, Object> action,
	    final Map<Layout, Container> bars, final JMenu menu) {
	this.targetComponents = targetComponents;
	this.component = component;
	this.action = action;
	this.bars = bars;
	this.menu = menu;
    }

    @Override
    protected IVComponent<Container, ActionListener, ActionEvent, Object> doInBackground()
	    throws Exception {
	return runHandleSubcomponent(component, action);
    }

    @Override
    protected IVComponent<Container, ActionListener, ActionEvent, Object> runHandleSubcomponent(
	    final IVComponent<Container, ActionListener, ActionEvent, Object> component,
	    final IAction<ActionEvent, Object> action) {
	synchronized (component) {
	    try {
		component.setBlocked(true);
		lock.add(true);
		while (component.hasIncomingMessage()) {
		    final IAction<ActionEvent, Object> myAction = component
			    .getNextIncomingMessage();
		    waitOnLock();
		    log(" //1.1.1.1.1// handle replace component BEGIN: "
			    + component.getName());

		    final Container previousContainer = component.getRoot();
		    final String currentTaget = component.getExecutionTarget();
		    // run code
		    log(" //1.1.1.1.2// handle component: "
			    + component.getName());
		    prepareAndHandleComponent(component, myAction);
		    log(" //1.1.1.1.3// publish component: "
			    + component.getName());
		    if (component.isActive()) {
			publishComponentValue(previousContainer, currentTaget);
		    } else {
			// unregister component
			//removeComponentValue(component,previousContainer);
		    }

		}
	    } finally {
		component.setBlocked(false);
	    }

	}
	return component;

    }

    private void removeComponentValue(final IVComponent<Container, ActionListener, ActionEvent, Object> component,final Container previousContainer) {
	synchronized (previousContainer) {
	    if (previousContainer == null) {
		lock.add(true);
	    } else {
		final Container parent  = previousContainer.getParent();
		if (parent != null) {
		    parent.remove(component.getRoot());
		}
		lock.add(true);
	    }
	    
	}
    }

    /**
     * run in thread
     * 
     * @param previousContainer
     * @param currentTaget
     */
    private void publishComponentValue(final Container previousContainer,
	    final String currentTaget) {
	synchronized (previousContainer) {
	    boolean publish = false;
	    Container parent = null;
	    if (previousContainer == null) {
		lock.add(true);
	    } else {
		parent = previousContainer.getParent();
		if (parent == null) {
		    publish = true;
		} else if (!currentTaget.equals(component.getExecutionTarget())
			|| !previousContainer.equals(component.getRoot())) {
		    publish = true;
		} else {
		    lock.add(true);
		}
	    }
	    if (publish) {
		publish(new ChunkDTO(parent, previousContainer,
			targetComponents, currentTaget, component, bars, menu));
	    }
	}
    }

    /**
     * run in thread
     */
    private void waitOnLock() {
	try {
	    lock.take();
	} catch (final InterruptedException e) {
	    e.printStackTrace();
	}
    }

    @Override
    /**
     * run in EDT
     */
    protected void process(final List<ChunkDTO> chunks) {
	// process method runs in EventDispatchThread
	for (final ChunkDTO dto : chunks) {
	    final Container parent = dto.getParent();
	    final IVComponent<Container, ActionListener, ActionEvent, Object> component = dto
		    .getComponent();
	    // TODO decide if menu and bars are always handled or only at start
	    // time
	    // component.handleBarEntries(dto.getBars());
	    // component.handleMenuEntries(dto.getMenu());
	    final Container previousContainer = dto.getPreviousContainer();
	    final String currentTaget = dto.getCurrentTaget();
	    // remove old view
	    log(" //1.1.1.1.3// handle old component remove: "
		    + component.getName());
	    if (parent != null && previousContainer != null) {
		handleOldComponentRemove(parent, previousContainer);
	    }

	    final Container root = component.getRoot();
	    if (root != null) {
		// add new view
		log(" //1.1.1.1.4// handle new component insert: "
			+ component.getName());
		root.setVisible(true);
		root.setEnabled(true);
		handleNewComponentValue(component, targetComponents, parent,
			currentTaget);
	    }

	}
	lock.add(true);
    }

    @Override
    protected void done() {
	try {
	    final IVComponent<Container, ActionListener, ActionEvent, Object> component = this
		    .get();
	    component.setBlocked(false);
	} catch (final InterruptedException e) {
	    e.printStackTrace();
	    // TODO add to error queue and restart thread if messages in queue
	} catch (final ExecutionException e) {
	    e.printStackTrace();
	    // TODO add to error queue and restart thread if messages in queue
	}
	component.setBlocked(false);
    }

}
