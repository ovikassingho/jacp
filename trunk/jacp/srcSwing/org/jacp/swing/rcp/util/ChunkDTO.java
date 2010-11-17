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
import javax.swing.JMenuBar;

import org.jacp.api.component.IVComponent;
import org.jacp.api.componentLayout.Layout;

/**
 * DTO container for processing component results in Event dispatch thread; for
 * use in componentWorker classes
 * 
 * @author Andy Moncsek
 * 
 */
public final class ChunkDTO {
    private final Container parent;
    private final Map<String, Container> targetComponents;
    private final String currentTaget;
    private final IVComponent<Container, ActionListener, ActionEvent, Object> component;
    private final Container previousContainer;
    private final Map<Layout, Container> bars;
    private final JMenu menu;

    public ChunkDTO(
	    final Container parent,
	    final Container previousContainer,
	    final Map<String, Container> targetComponents,
	    final String currentTaget,
	    final IVComponent<Container, ActionListener, ActionEvent, Object> component,final Map<Layout, Container> bars, final JMenu menu) {
	this.parent = parent;
	this.targetComponents = targetComponents;
	this.currentTaget = currentTaget;
	this.component = component;
	this.previousContainer = previousContainer;
	this.bars = bars;
	this.menu = menu;
    }

    public Container getParent() {
	return parent;
    }

    public Map<String, Container> getTargetComponents() {
	return targetComponents;
    }

    public String getCurrentTaget() {
	return currentTaget;
    }

    public IVComponent<Container, ActionListener, ActionEvent, Object> getComponent() {
	return component;
    }

    public Container getPreviousContainer() {
	return previousContainer;
    }

    public Map<Layout, Container> getBars() {
        return bars;
    }

    public JMenu getMenu() {
        return menu;
    }
}
