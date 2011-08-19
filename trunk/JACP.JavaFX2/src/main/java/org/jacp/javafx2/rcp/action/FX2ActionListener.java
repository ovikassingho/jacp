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
package org.jacp.javafx2.rcp.action;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import org.jacp.api.action.IAction;
import org.jacp.api.action.IActionListener;
import org.jacp.api.coordinator.ICoordinator;

/**
 * This class represents the JACP FX2 Event listener... this class can be assigned to components, it reacts on actions and notifies other components in JACP 
 * @author Andy Moncsek
 */
public class FX2ActionListener implements EventHandler<ActionEvent>, IActionListener<EventHandler<ActionEvent>, ActionEvent, Object> {
    private IAction<ActionEvent, Object> action;
    private ICoordinator<EventHandler<ActionEvent>, ActionEvent, Object> coordinator;
    
    public FX2ActionListener(final IAction<ActionEvent, Object> action, final ICoordinator<EventHandler<ActionEvent>, ActionEvent, Object> coordinator) {
        this.action = action;
        this.coordinator = coordinator;
    }
    
    
    public void notifyComponents(IAction<ActionEvent, Object> action) {
       coordinator.handle(action);
    }

    public void setAction(IAction<ActionEvent, Object> action) {
       this.action = action;
    }

    public IAction<ActionEvent, Object> getAction() {
       return this.action;
    }

    public EventHandler<ActionEvent> getListener() {
       return this;
    }

    public void handle(ActionEvent t) {
       action.setActionEvent(t);
       notifyComponents(action);
    }
    
}
