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

/**
 * Handles ui return value of component and add to correct perspective target
 * 
 * @author Andy Moncsek
 * 
 */
public class FX2ComponentAddWorker extends AFX2ComponentWorker<IVComponent<Node, EventHandler<Event>, Event, Object>> {

    private final Map<String, Node> targetComponents;
    private final IVComponent<Node, EventHandler<Event>, Event, Object> component;

    public FX2ComponentAddWorker(final Map<String, Node> targetComponents, final IVComponent<Node, EventHandler<Event>, Event, Object> component) {
        this.targetComponents = targetComponents;
        this.component = component;
    }

    @Override
    protected IVComponent<Node, EventHandler<Event>, Event, Object> call() throws Exception {
        return null;
    }

    @Override
    public final void done() {
        component.setExecutionTarget(getTargetComponentId(component.getExecutionTarget()));
        handleNewComponentValue(component, targetComponents, null, "");
    }
}
