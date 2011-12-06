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

import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.Node;

import org.jacp.api.component.IVComponent;
import org.jacp.api.perspective.IPerspective;

/**
 * Handles ui return value of component and add to correct perspective target
 * 
 * @author Andy Moncsek
 * 
 */
public class FX2ComponentAddWorker
		extends
		AFX2ComponentWorker<IVComponent<Node, EventHandler<Event>, Event, Object>> {

	private final Map<String, Node> targetComponents;
	private final IVComponent<Node, EventHandler<Event>, Event, Object> component;
	private final IPerspective<EventHandler<Event>, Event, Object> parent;

	public FX2ComponentAddWorker(
			final Map<String, Node> targetComponents,final IPerspective<EventHandler<Event>, Event, Object> parent,
			final IVComponent<Node, EventHandler<Event>, Event, Object> component) {
		this.targetComponents = targetComponents;
		this.component = component;
		this.parent = parent;
	}
	
	public FX2ComponentAddWorker() {
		this(null,null,null);
	}

	@Override
	protected IVComponent<Node, EventHandler<Event>, Event, Object> call()
			throws Exception {
		return null;
	}

	@Override
	public final void done() {
		this.component.setExecutionTarget(FX2Util
				.getTargetComponentId(this.component.getExecutionTarget()));
		this.handleNewComponentValue(this.parent,this.component, this.targetComponents,
				null, "");
	}
	
	public void handleInApplicationThread(final Map<String, Node> targetComponents,final IPerspective<EventHandler<Event>, Event, Object> parent,
			final IVComponent<Node, EventHandler<Event>, Event, Object> component) {
		component.setExecutionTarget(FX2Util
				.getTargetComponentId(component.getExecutionTarget()));
		this.handleNewComponentValue(parent,component, targetComponents,
				null, "");
	}
}
