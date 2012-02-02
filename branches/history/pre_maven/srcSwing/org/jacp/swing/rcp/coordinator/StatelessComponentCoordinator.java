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

package org.jacp.swing.rcp.coordinator;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;

import org.jacp.api.action.IAction;
import org.jacp.api.component.IBGComponent;
import org.jacp.api.coordinator.IStatelessComponentCoordinator;
import org.jacp.impl.Launcher;
import org.jacp.swing.rcp.component.AStatelessComponent;
import org.jacp.swing.rcp.util.StateLessComponentRunWorker;

/**
 * controls instantiation of state less component clones; each put to a new
 * instance until max count is reached, if no instance is unblocked message is
 * put to queue of first instance thread
 * 
 * @author Andy Moncsek
 * 
 */
public class StatelessComponentCoordinator
                implements
                IStatelessComponentCoordinator<ActionListener, ActionEvent, Object> {

        public static int MAX_INCTANCE_COUNT;

        private final AtomicInteger threadCount = new AtomicInteger(0);

        private final List<IBGComponent<ActionListener, ActionEvent, Object>> componentInstances = new CopyOnWriteArrayList<IBGComponent<ActionListener, ActionEvent, Object>>();

        private IBGComponent<ActionListener, ActionEvent, Object> baseComponent;
        private final Launcher<?> launcher;

        public StatelessComponentCoordinator(
                        final IBGComponent<ActionListener, ActionEvent, Object> baseComponent,
                        final Launcher<?> launcher) {
                this.launcher = launcher;
                setBaseComponent(baseComponent);
        }
        
        static {
        	final Runtime runtime = Runtime.getRuntime();
    		final int nrOfProcessors = runtime.availableProcessors();
    		MAX_INCTANCE_COUNT = nrOfProcessors+(nrOfProcessors/2);
        }

        /*
         * (non-Javadoc)
         * 
         * @see org.jacp.swing.rcp.coordinator.IStatelessComponentCoordinator#
         * incomingMessage (org.jacp.api.action.IAction)
         */
        @Override
        public final void incomingMessage(
                        final IAction<ActionEvent, Object> message) {
                synchronized (baseComponent) {
                        // get active instance
                        final IBGComponent<ActionListener, ActionEvent, Object> comp = getActiveComponent();
                        if (comp != null) {
                                if (componentInstances.size() < MAX_INCTANCE_COUNT) {
                                        // create new instance
                                        componentInstances
                                                        .add(getCloneBean(((AStatelessComponent) baseComponent)
                                                                        .getClass()));
                                }
                                // run component in thread
                                instanceRun(comp, message);
                        } else {
                                // check if new instances can be created
                                if (componentInstances.size() < MAX_INCTANCE_COUNT) {
                                        createInstanceAndRun(message);
                                } else {
                                        seekAndPutMessage(message);
                                }
                        }

                }
        }

        /**
         * block component, put message to component's queue and run in thread
         * 
         * @param comp
         * @param message
         */
        private final void instanceRun(
                        final IBGComponent<ActionListener, ActionEvent, Object> comp,
                        final IAction<ActionEvent, Object> message) {
                comp.setBlocked(true);
                comp.putIncomingMessage(message);
                final StateLessComponentRunWorker worker = new StateLessComponentRunWorker(
                                comp);
                worker.execute();
        }

        /**
         * if max thread count is not reached and all available component
         * instances are blocked create a new one, block it an run in thread
         * 
         * @param message
         */
        private void createInstanceAndRun(
                        final IAction<ActionEvent, Object> message) {
                final IBGComponent<ActionListener, ActionEvent, Object> comp = getCloneBean(((AStatelessComponent) baseComponent)
                                .getClass());
                componentInstances.add(comp);
                instanceRun(comp, message);
        }

        /**
         * seek to first running component in instance list and add message to
         * queue of selected component
         * 
         * @param message
         */
        private final void seekAndPutMessage(
                        final IAction<ActionEvent, Object> message) {
                // if max count reached, seek through components and add
                // message to queue of oldest component
                final Integer seek = Integer.valueOf(threadCount
                                .incrementAndGet()) % componentInstances.size();
                final IBGComponent<ActionListener, ActionEvent, Object> comp = componentInstances
                                .get(seek);
                // put message to queue
                comp.putIncomingMessage(message);
        }

        @Override
        public final <T extends IBGComponent<ActionListener, ActionEvent, Object>> IBGComponent<ActionListener, ActionEvent, Object> getCloneBean(
                        final Class<T> clazz) {
                return ((AStatelessComponent) baseComponent).init(launcher
                                .getBean(clazz));
        }

        private final IBGComponent<ActionListener, ActionEvent, Object> getActiveComponent() {
                for (int i = 0; i < componentInstances.size(); i++) {
                        final IBGComponent<ActionListener, ActionEvent, Object> comp = componentInstances
                                        .get(i);
                        if (!comp.isBlocked()) {
                                return comp;
                        }
                }

                return null;
        }

        public void flushInstances() {

        }

        public void handleWorkerCall() {

        }

        public void handleWorkerCallToPostbox() {

        }

        public final IBGComponent<ActionListener, ActionEvent, Object> getBaseComponent() {
                return baseComponent;
        }

        public final void setBaseComponent(
                        final IBGComponent<ActionListener, ActionEvent, Object> baseComponent) {
                this.baseComponent = baseComponent;
                componentInstances
                                .add(getCloneBean(((AStatelessComponent) baseComponent)
                                                .getClass()));
        }

    
        protected final List<IBGComponent<ActionListener, ActionEvent, Object>> getComponentInstances() {
                return componentInstances;
        }
}
