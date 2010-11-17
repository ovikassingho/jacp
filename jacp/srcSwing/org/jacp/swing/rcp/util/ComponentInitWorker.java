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
import java.util.Map;

import javax.swing.JMenu;

import org.jacp.api.action.IAction;
import org.jacp.api.component.IVComponent;
import org.jacp.api.componentLayout.Layout;

/**
 * Background Worker to execute components; handle method to init component
 * 
 * @author Andy Moncsek
 * 
 */
public class ComponentInitWorker
	extends
	AbstractComponentWorker<IVComponent<Container, ActionListener, ActionEvent, Object>> {
    private final Map<String, Container> targetComponents;
    private final IVComponent<Container, ActionListener, ActionEvent, Object> component;
    private final Map<Layout, Container> bars;
    private final IAction<ActionEvent, Object> action;
    private final JMenu menu;

    public ComponentInitWorker(
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
	synchronized (component) {
	    component.setBlocked(true);
	    log("3.4.4.2.1: subcomponent handle init START: "
		    + component.getName());
	    final Container editorComponent = component.handle(action);
	    component.setRoot(editorComponent);
	    editorComponent.setVisible(true);
	    editorComponent.setEnabled(true);
	    log("3.4.4.2.2: subcomponent handle init get valid container: "
		    + component.getName());
	    final Container validContainer = getValidContainerById(
		    targetComponents, component.getExecutionTarget());
	    log("3.4.4.2.3: subcomponent handle init add component by type: "
		    + component.getName());
	    addComponentByType(validContainer, component, bars, menu);
	    log("3.4.4.2.4: subcomponent handle init END: "
		    + component.getName());
	    component.setBlocked(false);
	    return component;
	}
    }

    @Override
    protected IVComponent<Container, ActionListener, ActionEvent, Object> runHandleSubcomponent(
	    final IVComponent<Container, ActionListener, ActionEvent, Object> component,
	    final IAction<ActionEvent, Object> action) {

	return component;
    }

    @Override
    public void done() {
	synchronized (component) {
	    component.setBlocked(false);
	    // check if news messages received while handled in initialization
	    // worker; if so then start replace worker
	    if (component.hasIncomingMessage()) {
		new ComponentReplaceWorker(targetComponents, component, action,
			bars, menu).execute();
	    }
	}
    }

}
