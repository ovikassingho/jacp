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

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import org.jacp.api.action.IAction;
import org.jacp.api.component.IComponent;
import org.jacp.api.component.ISubComponent;
import org.jacp.api.coordinator.IComponentCoordinator;
import org.jacp.api.perspective.IPerspective;

/**
 * observe component actions and delegates to correct component
 * @author Andy Moncsek
 */
public class FX2ComponentCoordinator extends AFX2Coordinator implements
        IComponentCoordinator<EventHandler<ActionEvent>, ActionEvent, Object> {

    private List<ISubComponent<EventHandler<ActionEvent>, ActionEvent, Object>> components = new CopyOnWriteArrayList<ISubComponent<EventHandler<ActionEvent>, ActionEvent, Object>>();
    private final IPerspective<EventHandler<ActionEvent>, ActionEvent, Object> perspective;

    public FX2ComponentCoordinator(final IPerspective<EventHandler<ActionEvent>, ActionEvent, Object> perspective) {
        setDaemon(true);
        this.perspective = perspective;
    }

    public FX2ComponentCoordinator(final IPerspective<EventHandler<ActionEvent>, ActionEvent, Object> perspective, final List<ISubComponent<EventHandler<ActionEvent>, ActionEvent, Object>> components) {
        setDaemon(true);
        this.perspective = perspective;
        this.components = components;
    }

    @Override
    public void addComponent(ISubComponent<EventHandler<ActionEvent>, ActionEvent, Object> component) {
        component.setObserver(this);
        components.add(component);
    }

    @Override
    public void removeComponent(ISubComponent<EventHandler<ActionEvent>, ActionEvent, Object> component) {
        component.setObserver(null);
        components.remove(component);

    }

    @Override
    public void handleMessage(String targetId, IAction<ActionEvent, Object> action) {
        synchronized (action) {
            final ISubComponent<EventHandler<ActionEvent>, ActionEvent, Object> component = getObserveableById(getTargetComponentId(targetId), components);
            log(" //1.1// component message to: " + action.getTargetId());
            if (component != null) {
                log(" //1.1.1// component HIT: " + action.getTargetId());
                handleComponentHit(targetId, action, component);
            } else {
                // delegate message to parent perspective
                log(" //1.1.1// component MISS: " + action.getTargetId());
                handleComponentMiss(targetId, action);
            }
        }
    }

    /**
     * handle method if component was found in local context
     * 
     * @param targetId
     * @param action
     * @param component
     */
    private void handleComponentHit(final String targetId,
            final IAction<ActionEvent, Object> action,
            final ISubComponent<EventHandler<ActionEvent>, ActionEvent, Object> component) {
        final IAction<ActionEvent, Object> actionClone = getValidAction(action,
                targetId, action.getMessageList().get(targetId));
        if (component.isActive()) {
            log(" //1.1.1.1// component HIT handle ACTIVE: "
                    + action.getTargetId());
            handleActive(component, actionClone);
        } else {
            log(" //1.1.1.1// component HIT handle IN-ACTIVE: "
                    + action.getTargetId());
            handleInActive(component, actionClone);
        }
    }

    /**
     * handle method if no valid component found; delegate to responsible
     * perspective
     * 
     * @param targetId
     * @param action
     */
    private void handleComponentMiss(final String targetId,
            final IAction<ActionEvent, Object> action) {
        final boolean local = isLocalMessage(targetId);
        if (!local) {
            final String targetPerspectiveId = getTargetPerspectiveId(targetId);
            if (perspective.getId().equals(targetPerspectiveId)) {
                // TODO target is in same perspective but component was not
                // found previously
                throw new UnsupportedOperationException(
                        "invalid component id handling not supported yet.");
            }
            // delegate to parent perspective, then find responsible
            // perspective
            perspective.delegateMassege(targetId, action);

        } else {
            // possible message to perspective
            perspective.delegateMassege(targetId, action);
        }
    }

    @Override
    public void delegateMessage(String target, IAction<ActionEvent, Object> action) {
        handleMessage(target, action);
    }

    @Override
    public final <P extends IComponent<EventHandler<ActionEvent>, ActionEvent, Object>> void handleActive(
            final P component, final IAction<ActionEvent, Object> action) {
        log(" //1.1.1.1.1// component " + action.getTargetId()
                + " delegate to perspective: " + perspective.getId());
        perspective.handleAndReplaceComponent(action,
                (ISubComponent<EventHandler<ActionEvent>, ActionEvent, Object>) component);

    }

    @Override
    public final <P extends IComponent<EventHandler<ActionEvent>, ActionEvent, Object>> void handleInActive(
            final P component, final IAction<ActionEvent, Object> action) {
        component.setActive(true);
        perspective.initComponent(action,
                (ISubComponent<EventHandler<ActionEvent>, ActionEvent, Object>) component);

    }

    @Override
    public void delegateTargetChange(String target, ISubComponent component) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}