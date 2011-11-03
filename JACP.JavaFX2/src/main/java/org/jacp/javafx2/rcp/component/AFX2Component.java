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
import javafx.event.Event;
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
public abstract class AFX2Component implements IVComponent<Node, EventHandler<Event>, Event, Object> {

    private String id;
    private String target;
    private String name;
    private Node root;
    private boolean active;
    private boolean isActived = false;
    private volatile AtomicBoolean blocked = new AtomicBoolean(false);
    private final BlockingQueue<IAction<Event, Object>> incomingActions = new ArrayBlockingQueue<IAction<Event, Object>>(1000);
    private final Map<Layout, Node> barEntries = new ConcurrentHashMap<Layout, Node>();
    private ICoordinator<EventHandler<Event>, Event, Object> componentObserver;
    private IPerspective<EventHandler<Event>, Event, Object> parentPerspective;

    public final void setRoot(final Node root) {
        this.root = root;
    }

    public final Node getRoot() {
        return this.root;
    }

    public final Map<Layout, Node> getBarEntries() {
        return this.barEntries;
    }


    public final String getExecutionTarget() {
        return this.target;
    }

    public final void setExecutionTarget(final String target) {
        this.target = target;
    }

    public final void setParentPerspective(final IPerspective<EventHandler<Event>, Event, Object> perspective) {
        this.parentPerspective = perspective;
    }

    public final IPerspective<EventHandler<Event>, Event, Object> getParentPerspective() {
        return this.parentPerspective;
    }

    public final boolean hasIncomingMessage() {
        return !incomingActions.isEmpty();
    }

    public final void putIncomingMessage(final IAction<Event, Object> action) {
        try {
            incomingActions.put(action);
        } catch (final InterruptedException e) {
            e.printStackTrace();
        }
    }

    public final IAction<Event, Object> getNextIncomingMessage() {
        if (hasIncomingMessage()) {
            try {
                return incomingActions.take();
            } catch (final InterruptedException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public final boolean isBlocked() {
        return blocked.get();
    }

    public final void setBlocked(final boolean blocked) {
        this.blocked.set(blocked);
    }

    public final IActionListener<EventHandler<Event>, Event, Object> getActionListener() {
        return new FX2ActionListener(new FX2Action(id), componentObserver);
    }

    public final String getId() {
        if (id == null) {
            throw new UnsupportedOperationException("No id set");
        }
        return this.id;
    }

    public final void setId(final String id) {
        this.id = id;
    }

    public final boolean isActive() {
        return this.active;
    }

    public final void setActive(final boolean active) {
        this.active = active;
    }

    public final void setActivated(final boolean isActive) {
        this.isActived = isActive;
    }

    public final boolean isActivated() {
        return this.isActived;
    }

    public final String getName() {
        if (name == null) {
            throw new UnsupportedOperationException("No name set");
        }
        return name;
    }

    public final void setName(final String name) {
        this.name = name;
    }

    public final void setObserver(final ICoordinator<EventHandler<Event>, Event, Object> observer) {
        this.componentObserver = observer;
    }
    
    @SuppressWarnings("unchecked")
    public final <C> C handle(final IAction<Event, Object> action) {
        return (C) handleAction(action);
    }
    /**
     * 
     * @param action
     * @return 
     */
    public abstract Node handleAction(IAction<Event, Object> action);
}
