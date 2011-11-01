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


import java.util.Map;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.MenuBar;


import org.jacp.api.component.IVComponent;
import org.jacp.api.componentLayout.Layout;


/**
 * DTO container for processing component results in Event dispatch thread; for
 * use in componentWorker classes
 * 
 * @author Andy Moncsek
 * 
 */
public class ChunkDTO {
	 private final Node parent;
	    private final Map<String, Node> targetComponents;
	    private final String currentTaget;
	    private final IVComponent<Node, EventHandler<ActionEvent>, ActionEvent, Object> component;
	    private final Node previousContainer;
	    private final Map<Layout, Node> bars;
	    private final MenuBar menu;

	    public ChunkDTO(
		    final Node parent,
		    final Node previousContainer,
		    final Map<String, Node> targetComponents,
		    final String currentTaget,
		    final IVComponent<Node, EventHandler<ActionEvent>, ActionEvent, Object> component,
		    final Map<Layout, Node> bars, final MenuBar menu) {
		this.parent = parent;
		this.targetComponents = targetComponents;
		this.currentTaget = currentTaget;
		this.component = component;
		this.previousContainer = previousContainer;
		this.bars = bars;
		this.menu = menu;
	    }

	    public final Node getParent() {
		return parent;
	    }

	    public final Map<String, Node> getTargetComponents() {
		return targetComponents;
	    }

	    public final String getCurrentTaget() {
		return currentTaget;
	    }

	    public final IVComponent<Node, EventHandler<ActionEvent>, ActionEvent, Object> getComponent() {
		return component;
	    }

	    public final Node getPreviousContainer() {
		return previousContainer;
	    }

	    public final Map<Layout, Node> getBars() {
		return bars;
	    }

	    public final MenuBar getMenu() {
		return menu;
	    }
}
