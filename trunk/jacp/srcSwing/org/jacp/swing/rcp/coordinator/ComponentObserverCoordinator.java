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

package org.jacp.swing.rcp.coordinator;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import org.jacp.api.action.IAction;
import org.jacp.api.component.ISubComponent;
import org.jacp.api.coordinator.IComponentCoordinator;
import org.jacp.api.perspective.IPerspective;

/**
 * This Handler coordinates observers and components in perspectives; each added
 * component get his own observer; handler instantiates an observer set the
 * parent perspective an the component list
 * 
 * @author Andy Moncsek
 * 
 */
public class ComponentObserverCoordinator {
    private List<ISubComponent<ActionListener, ActionEvent, Object>> components = new CopyOnWriteArrayList<ISubComponent<ActionListener, ActionEvent, Object>>();

    private final IPerspective<ActionListener, ActionEvent, Object> perspective;

    public ComponentObserverCoordinator(
	    final IPerspective<ActionListener, ActionEvent, Object> perspective,
	    final List<ISubComponent<ActionListener, ActionEvent, Object>> components) {
	this.perspective = perspective;
	this.components = components;
    }

    public ComponentObserverCoordinator(
	    final IPerspective<ActionListener, ActionEvent, Object> perspective) {
	this.perspective = perspective;
    }

    /**
     * 
     * @param component
     */
    public void addComponent(
	    final ISubComponent<ActionListener, ActionEvent, Object> component) {
	components.add(component);
	component.setObserver(getObserverInstance());
    }

    private IComponentCoordinator<ActionListener, ActionEvent, Object> getObserverInstance() {
	final IComponentCoordinator<ActionListener, ActionEvent, Object> componentObserver = new SwingComponentCoordinator(
		perspective, components);
	((SwingComponentCoordinator) componentObserver).start();
	return componentObserver;
    }

    public void removeComponent(
	    final ISubComponent<ActionListener, ActionEvent, Object> component) {
	component.setObserver(null);
	components.remove(component);

    }

    // TODO create correct implementation; don not create always a new observer;
    // add message to queue
    public void delegateMessage(final String target,
	    final IAction<ActionEvent, Object> action) {
	getObserverInstance().delegateMessage(target, action);
    }

}
