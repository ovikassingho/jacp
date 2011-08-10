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
package org.jacp.javafx2.rcp.component;

import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import org.jacp.api.action.IAction;
import org.jacp.api.action.IActionListener;
import org.jacp.api.component.IVComponent;
import org.jacp.api.componentLayout.Layout;
import org.jacp.api.coordinator.ICoordinator;
import org.jacp.api.perspective.IPerspective;
import org.jacp.javafx2.rcp.action.FX2Action;
import org.jacp.javafx2.rcp.action.FX2ActionListener;

/**
 * represents a basic FX2 component to extend from, uses this abstract class to create UI components
 * @author Andy Moncsek
 */
public abstract class AFX2Component implements IVComponent<Node, EventHandler, ActionEvent, Object> {

    private String id;
    private String target;
    private String name;
    private Node root;
    private boolean active;
    private boolean isActived = false;
    private volatile AtomicBoolean blocked = new AtomicBoolean(false);
    private final BlockingQueue<IAction<ActionEvent, Object>> incomingActions = new ArrayBlockingQueue<IAction<ActionEvent, Object>>(1000);
    private final Map<Layout, Node> barEntries = new ConcurrentHashMap<Layout, Node>();
    private ICoordinator<EventHandler, ActionEvent, Object> componentObserver;
    private IPerspective<EventHandler, ActionEvent, Object> parentPerspective;

    public void setRoot(Node root) {
        this.root = root;
    }

    public Node getRoot() {
        return this.root;
    }

    public Map<Layout, Node> getBarEntries() {
        return this.barEntries;
    }


    public String getExecutionTarget() {
        return this.target;
    }

    public void setExecutionTarget(String target) {
        this.target = target;
    }

    public void setParentPerspective(IPerspective<EventHandler, ActionEvent, Object> perspective) {
        this.parentPerspective = perspective;
    }

    public IPerspective<EventHandler, ActionEvent, Object> getParentPerspective() {
        return this.parentPerspective;
    }

    public boolean hasIncomingMessage() {
        return !incomingActions.isEmpty();
    }

    public void putIncomingMessage(IAction<ActionEvent, Object> action) {
        try {
            incomingActions.put(action);
        } catch (final InterruptedException e) {
            e.printStackTrace();
        }
    }

    public IAction<ActionEvent, Object> getNextIncomingMessage() {
        if (hasIncomingMessage()) {
            try {
                return incomingActions.take();
            } catch (final InterruptedException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public boolean isBlocked() {
        return blocked.get();
    }

    public void setBlocked(boolean blocked) {
        this.blocked.set(blocked);
    }

    public IActionListener<EventHandler, ActionEvent, Object> getActionListener() {
        return new FX2ActionListener(new FX2Action(id), componentObserver);
    }

    public String getId() {
        if (id == null) {
            throw new UnsupportedOperationException("No id set");
        }
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean isActive() {
        return this.active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public void setActivated(boolean isActive) {
        this.isActived = isActive;
    }

    public boolean isActivated() {
        return this.isActived;
    }

    public String getName() {
        if (name == null) {
            throw new UnsupportedOperationException("No name set");
        }
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setObserver(ICoordinator<EventHandler, ActionEvent, Object> observer) {
        this.componentObserver = observer;
    }
    
    @SuppressWarnings("unchecked")
    public <C> C handle(IAction<ActionEvent, Object> action) {
        return (C) handleAction(action);
    }
    /**
     * 
     * @param action
     * @return 
     */
    public abstract Node handleAction(IAction<ActionEvent, Object> action);
}
