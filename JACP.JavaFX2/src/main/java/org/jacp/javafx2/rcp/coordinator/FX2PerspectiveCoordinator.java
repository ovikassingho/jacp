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
package org.jacp.javafx2.rcp.coordinator;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import org.jacp.api.action.IAction;
import org.jacp.api.component.IComponent;
import org.jacp.api.component.ISubComponent;
import org.jacp.api.coordinator.IPerspectiveCoordinator;
import org.jacp.api.perspective.IPerspective;

/**
 * Observe perspective actions and delegates message to correct component
 * @author Andy Moncsek
 */
public class FX2PerspectiveCoordinator extends AFX2Coordinator  implements
	IPerspectiveCoordinator<EventHandler<ActionEvent>, ActionEvent, Object> {

    public void handleMessage(String id, IAction<ActionEvent, Object> action) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void delegateMessage(String target, IAction<ActionEvent, Object> action) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public <P extends IComponent<EventHandler<ActionEvent>, ActionEvent, Object>> void handleActive(P component, IAction<ActionEvent, Object> action) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public <P extends IComponent<EventHandler<ActionEvent>, ActionEvent, Object>> void handleInActive(P component, IAction<ActionEvent, Object> action) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void delegateTargetChange(String target, ISubComponent<EventHandler<ActionEvent>, ActionEvent, Object> component) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void addPerspective(IPerspective<EventHandler<ActionEvent>, ActionEvent, Object> perspective) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void removePerspective(IPerspective<EventHandler<ActionEvent>, ActionEvent, Object> perspective) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
}
