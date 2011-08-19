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
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutionException;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.MenuBar;
import org.jacp.api.component.IVComponent;
import org.jacp.api.componentLayout.Layout;

/**
 * Background Worker to execute components handle method to replace or add the
 * component
 * 
 * @author Andy Moncsek
 * 
 */
public class FX2ComponentReplaceWorker extends AFX2ComponentWorker<IVComponent<Node, EventHandler<ActionEvent>, ActionEvent, Object>> {

    private final Map<String, Node> targetComponents;
    private final IVComponent<Node, EventHandler<ActionEvent>, ActionEvent, Object> component;
    private final Map<Layout, Node> bars;
    private final MenuBar menu;
    private volatile BlockingQueue<Boolean> lock = new ArrayBlockingQueue<Boolean>(
            1);

    public FX2ComponentReplaceWorker(
            final Map<String, Node> targetComponents,
            final IVComponent<Node, EventHandler<ActionEvent>, ActionEvent, Object> component,
            final Map<Layout, Node> bars, final MenuBar menu) {
        this.targetComponents = targetComponents;
        this.component = component;
        this.bars = bars;
        this.menu = menu;
    }

    @Override
    protected IVComponent<Node, EventHandler<ActionEvent>, ActionEvent, Object> call() throws Exception {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    protected final void done() {
        try {
            final IVComponent<Node, EventHandler<ActionEvent>, ActionEvent, Object> component = this.get();
            component.setBlocked(false);
        } catch (final InterruptedException e) {
            System.out.println("Exception in Component REPLACE Worker, Thread interrupted:");
            e.printStackTrace();
            // TODO add to error queue and restart thread if
            // messages in
            // queue
        } catch (final ExecutionException e) {
            System.out.println("Exception in Component REPLACE Worker, Thread Excecution Exception:");
            e.printStackTrace();
            // TODO add to error queue and restart thread if
            // messages in
            // queue
        } catch (final Exception e) {
            System.out.println("Exception in Component REPLACE Worker, Thread Exception:");
            e.printStackTrace();
            // TODO add to error queue and restart thread if
            // messages in
            // queue
        } finally {
            component.setBlocked(false);
        }

    }

    /**
     * run in thread
     */
    private void waitOnLock() {
        try {
            lock.take();
        } catch (final InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * run in thread
     */
    private void releaseLock() {
        lock.add(true);
    }
}
