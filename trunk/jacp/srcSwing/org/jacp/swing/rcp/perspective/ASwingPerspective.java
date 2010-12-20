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

package org.jacp.swing.rcp.perspective;

import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JMenu;

import org.jacp.api.action.IAction;
import org.jacp.api.action.IActionListener;
import org.jacp.api.component.IBGComponent;
import org.jacp.api.component.IExtendedComponent;
import org.jacp.api.component.ILayoutAbleComponent;
import org.jacp.api.component.ISubComponent;
import org.jacp.api.componentLayout.IPerspectiveLayout;
import org.jacp.api.componentLayout.Layout;
import org.jacp.api.coordinator.IComponentCoordinator;
import org.jacp.api.coordinator.ICoordinator;
import org.jacp.api.perspective.IPerspective;
import org.jacp.impl.Launcher;
import org.jacp.swing.rcp.action.SwingAction;
import org.jacp.swing.rcp.action.SwingActionListener;
import org.jacp.swing.rcp.component.AStateComponent;
import org.jacp.swing.rcp.component.AStatelessComponent;
import org.jacp.swing.rcp.component.ASwingComponent;
import org.jacp.swing.rcp.componentLayout.SwingPerspectiveLayout;
import org.jacp.swing.rcp.coordinator.SwingComponentCoordinator;
import org.jacp.swing.rcp.coordinator.SwingPerspectiveCoordinator;
import org.jacp.swing.rcp.util.ComponentAddWorker;
import org.jacp.swing.rcp.util.ComponentInitWorker;
import org.jacp.swing.rcp.util.ComponentReplaceWorker;
import org.jacp.swing.rcp.util.StateComponentRunWorker;

/**
 * represents a basic swing perspective that handles subcomponents
 * 
 * @author Andy Moncsek
 */
public abstract class ASwingPerspective implements
                IPerspective<ActionListener, ActionEvent, Object>,
                IExtendedComponent<Container>, ILayoutAbleComponent<Container> {

        private final List<ISubComponent<ActionListener, ActionEvent, Object>> subcomponents = new CopyOnWriteArrayList<ISubComponent<ActionListener, ActionEvent, Object>>();

        private ICoordinator<ActionListener, ActionEvent, Object> perspectiveObserver;
        private final IComponentCoordinator<ActionListener, ActionEvent, Object> componentHandler = new SwingComponentCoordinator(
                        this);
        private final IPerspectiveLayout<Container, Container> perspectiveLayout = new SwingPerspectiveLayout();
        private String id;
        private String name;
        private boolean active;
        private boolean isActived = false;
        private final Logger logger = Logger.getLogger(this.getClass()
                        .getName());
        private Launcher<?> launcher;

        @Override
        public void init(final Launcher<?> launcher) {
                this.launcher = launcher;
                ((SwingComponentCoordinator) componentHandler).start();
        }

        /**
         * add menu entries to perspective specific menu
         * 
         * @param menuBar
         */
        public abstract void handleMenuEntries(final JMenu menuBar);

        @Override
        public abstract void handleBarEntries(final Map<Layout, Container> bars);

        @Override
        public final <C> C handle(final IAction<ActionEvent, Object> action) {
                throw new UnsupportedOperationException("Not supported yet.");
        }

        /**
         * handle perspective call, create structure of perspective, set base
         * layout and register component containers
         * 
         * @param action
         * @param perspectiveLayout
         */
        public abstract void handlePerspective(final SwingAction action,
                        final SwingPerspectiveLayout perspectiveLayout);

        @Override
        public final void handlePerspective(
                        final IAction<ActionEvent, Object> action) {
                handlePerspective((SwingAction) action,
                                (SwingPerspectiveLayout) perspectiveLayout);

        }

        @Override
        public final void registerComponent(
                        final ISubComponent<ActionListener, ActionEvent, Object> component) {
                log("register component: " + component.getId());
                componentHandler.addComponent(component);
                subcomponents.add(component);
                component.setParentPerspective(this);

        }

        @Override
        public final void unregisterComponent(
                        final ISubComponent<ActionListener, ActionEvent, Object> component) {
                log("unregister component: " + component.getId());
                componentHandler.removeComponent(component);
                subcomponents.remove(component);
                component.setParentPerspective(null);
        }

        @Override
        public final void handleMenuEntries(final Container meuneBar) {
                if (meuneBar instanceof JMenu) {
                        this.handleMenuEntries((JMenu) meuneBar);
                }
        }

        @Override
        public final void initComponents(
                        final IAction<ActionEvent, Object> action) {
                final String targetId = getTargetComponentId(action
                                .getTargetId());
                log("3.4.4.1: subcomponent targetId: " + targetId);
                final List<ISubComponent<ActionListener, ActionEvent, Object>> components = getSubcomponents();
                for (int i=0; i<components.size(); i++) {
                        final ISubComponent<ActionListener, ActionEvent, Object> component = components.get(i);
                        if (component.getId().equals(targetId)) {
                                log("3.4.4.2: subcomponent init with custom action");
                                initComponent(action, component);
                        } else if (component.isActive()
                                        && !component.isActived()) {
                                log("3.4.4.2: subcomponent init with default action");
                                initComponent(new SwingAction(
                                                component.getId(),
                                                component.getId(), "init"),
                                                component);
                        } // if END

                } // for END
        }

        @Override
        public final void initComponent(
                        final IAction<ActionEvent, Object> action,
                        final ISubComponent<ActionListener, ActionEvent, Object> component) {
                if (component instanceof ASwingComponent) {
                        log("COMPONENT EXECUTE INIT:::" + component.getName());
                        component.setActived(true);
                        final ComponentInitWorker tmp = new ComponentInitWorker(
                                        perspectiveLayout
                                                        .getTargetLayoutComponents(),
                                        ((ASwingComponent) component),
                                        action,
                                        ((SwingPerspectiveCoordinator) perspectiveObserver)
                                                        .getBars(),
                                        (JMenu) ((SwingPerspectiveCoordinator) perspectiveObserver)
                                                        .getMenu());
                        tmp.execute();
                } else if (component instanceof AStateComponent) {
                        log("BACKGROUND COMPONENT EXECUTE INIT:::"
                                        + component.getName());
                        putMessageToQueue(component, action);
                        runStateComponent(action, ((AStateComponent) component));
                } else if (component instanceof AStatelessComponent) {
                        log("SATELESS BACKGROUND COMPONENT EXECUTE INIT:::"
                                        + component.getName());
                        ((AStatelessComponent) component).setLauncher(launcher);
                        ((AStatelessComponent) component).addMessage(action);
                }

        }

        /**
         * run background components thread
         * 
         * @param action
         * @param component
         */
        private void runStateComponent(
                        final IAction<ActionEvent, Object> action,
                        final IBGComponent<ActionListener, ActionEvent, Object> component) {
                new StateComponentRunWorker(component).execute();
        }

        @Override
        public final void handleAndReplaceComponent(
                        final IAction<ActionEvent, Object> action,
                        final ISubComponent<ActionListener, ActionEvent, Object> component) {

                if (component.isBlocked()) {
                        putMessageToQueue(component, action);
                        log("ADD TO QUEUE:::" + component.getName());
                } else {
                        if (component instanceof AStatelessComponent) {
                                log("RUN STATELESS COMPONENTS:::"
                                                + component.getName());
                                ((AStatelessComponent) component)
                                                .addMessage(action);
                        } else {
                                putMessageToQueue(component, action);
                                executeComponentReplaceThread(
                                                perspectiveLayout, component,
                                                action);
                                log("CREATE NEW THREAD:::"
                                                + component.getName());
                        }

                }
                log("DONE EXECUTE REPLACE:::" + component.getName());

        }

        /**
         * start component replace thread
         * 
         * @param layout
         * @param component
         * @param action
         */
        // TODO component instanceof AStatelessComponent !!
        private void executeComponentReplaceThread(
                        final IPerspectiveLayout<? extends Container, Container> layout,
                        final ISubComponent<ActionListener, ActionEvent, Object> component,
                        final IAction<ActionEvent, Object> action) {
                if (component instanceof ASwingComponent) {
                        new ComponentReplaceWorker(
                                        layout.getTargetLayoutComponents(),
                                        ((ASwingComponent) component),
                                        action,
                                        ((SwingPerspectiveCoordinator) perspectiveObserver)
                                                        .getBars(),
                                        (JMenu) ((SwingPerspectiveCoordinator) perspectiveObserver)
                                                        .getMenu()).execute();

                } else if (component instanceof AStateComponent) {
                        runStateComponent(action, ((AStateComponent) component));
                }

        }

        /**
         * set component blocked and add message to queue
         * 
         * @param component
         * @param action
         */
        private void putMessageToQueue(
                        final ISubComponent<ActionListener, ActionEvent, Object> component,
                        final IAction<ActionEvent, Object> action) {
                component.putIncomingMessage(action);
        }

        @Override
        public final void addActiveComponent(
                        final ISubComponent<ActionListener, ActionEvent, Object> component) {
                // register new component at perspective
                registerComponent(component);
                if (component instanceof ASwingComponent) {
                        // add component ui root to correct target
                        addComponentUIValue(getIPerspectiveLayout()
                                        .getTargetLayoutComponents(), component);
                }
        }

        /**
         * handles ui return value and it to perspective TODO check correctness
         * of implementation
         * 
         * @param targetComponents
         * @param component
         */
        private void addComponentUIValue(
                        final Map<String, Container> targetComponents,
                        final ISubComponent<ActionListener, ActionEvent, Object> component) {
                if (component instanceof ASwingComponent) {
                        new ComponentAddWorker(targetComponents,
                                        ((ASwingComponent) component))
                                        .execute();
                }
        }

        @Override
        public final void delegateTargetChange(
                        final String target,
                        final ISubComponent<ActionListener, ActionEvent, Object> component) {
                final String parentId = getTargetParentId(target);
                if (!id.equals(parentId)) {
                        // unregister component in current perspective
                        unregisterComponent(component);
                        // delegate to perspective observer
                        perspectiveObserver.delegateTargetChange(target,
                                        component);

                }
        }

        @Override
        public final void delegateMassege(final String target,
                        final IAction<ActionEvent, Object> action) {
                perspectiveObserver.delegateMessage(target, action);
        }

        @Override
        public final void delegateComponentMassege(final String target,
                        final IAction<ActionEvent, Object> action) {
                componentHandler.delegateMessage(target, action);
        }

        @Override
        public final String getName() {
                if (id == null) {
                        throw new UnsupportedOperationException("No name set");
                }
                return name;
        }

        @Override
        public final String getId() {
                if (id == null) {
                        throw new UnsupportedOperationException("No id set");
                }
                return id;
        }

        @Override
        public final void setName(final String name) {
                this.name = name;
        }

        @Override
        public final void setId(final String id) {
                this.id = id;
        }

        @Override
        public final void setActive(final boolean active) {
                this.active = active;
        }

        @Override
        public final boolean isActive() {
                return active;
        }

        @Override
        public final void setObserver(
                        final ICoordinator<ActionListener, ActionEvent, Object> perspectiveObserver) {
                this.perspectiveObserver = perspectiveObserver;
        }

        @Override
        public final IActionListener<ActionListener, ActionEvent, Object> getActionListener() {
                return new SwingActionListener(new SwingAction(id),
                                perspectiveObserver);
        }

        @Override
        public final IPerspectiveLayout<Container, Container> getIPerspectiveLayout() {
                return perspectiveLayout;
        }

        /**
         * register components at componentHandler
         * 
         * @param <M>
         * @param components
         */
        private <M extends ISubComponent<ActionListener, ActionEvent, Object>> void registerSubcomponents(
                        final List<M> components) {
                for (int i=0; i<components.size(); i++) {
                        final M component = components.get(i);
                        registerComponent(component);
                }
        }

        /**
         * returns the message target id
         * 
         * @param messageId
         * @return
         */
        private String getTargetComponentId(final String messageId) {
                final String[] targetId = getTargetId(messageId);
                if (isFullValidId(targetId)) {
                        return targetId[1];
                }
                return messageId;
        }

        /**
         * returns the message (parent) target id
         * 
         * @param messageId
         * @return
         */
        private String getTargetParentId(final String messageId) {
                final String[] parentId = getTargetId(messageId);
                if (isFullValidId(parentId)) {
                        return parentId[0];
                }
                return messageId;
        }

        /**
         * returns target message with perspective and component name
         * 
         * @param messageId
         * @return
         */
        private String[] getTargetId(final String messageId) {
                return messageId.split("\\.");
        }

        /**
         * a target id is valid, when it does contain a perspective and a
         * component id (perspectiveId.componentId)
         * 
         * @param targetId
         * @return
         */
        private boolean isFullValidId(final String[] targetId) {
                if (targetId != null && targetId.length == 2) {
                        return true;
                }

                return false;
        }

        @Override
        public final List<ISubComponent<ActionListener, ActionEvent, Object>> getSubcomponents() {
                return subcomponents;
        }

        @Override
        public final void setSubcomponents(
                        final List<ISubComponent<ActionListener, ActionEvent, Object>> subComponents) {
                registerSubcomponents(subComponents);
        }

        private void log(final String message) {
                if (logger.isLoggable(Level.FINE)) {
                        logger.fine(">> " + message);
                }
        }

        @Override
        public final boolean isActived() {
                return isActived;
        }

        @Override
        public final void setActived(final boolean isActived) {
                this.isActived = isActived;
        }
}
