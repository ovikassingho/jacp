/************************************************************************
 *
 * Copyright (C) 2010 - 2012
 *
 * [FX2ComponentReplaceWorker.java]
 * AHCP Project (http://jacp.googlecode.com)
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
 *
 *
 ************************************************************************/
package org.jacp.javafx.rcp.worker;

import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutionException;
import java.util.logging.Logger;

import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.CacheHint;
import javafx.scene.Node;

import org.jacp.api.action.IAction;
import org.jacp.api.annotations.PreDestroy;
import org.jacp.api.component.ISubComponent;
import org.jacp.javafx.rcp.component.AFXComponent;
import org.jacp.javafx.rcp.componentLayout.FXComponentLayout;
import org.jacp.javafx.rcp.util.FXUtil;

/**
 * Background Worker to execute components handle method in separate thread and
 * to replace or add the component result node; While the handle method is
 * executed in an own thread the postHandle method is executed in application
 * main thread.
 *
 * @author Andy Moncsek
 */
public class FXComponentReplaceWorker extends AFXComponentWorker<AFXComponent> {

    private final Map<String, Node> targetComponents;
    private final AFXComponent component;
    private final FXComponentLayout layout;
    private final BlockingQueue<ISubComponent<EventHandler<Event>, Event, Object>> componentDelegateQueue;
    private static final Logger logger = Logger.getLogger("FXComponentReplaceWorker");

    public FXComponentReplaceWorker(
            final Map<String, Node> targetComponents,
            final BlockingQueue<ISubComponent<EventHandler<Event>, Event, Object>> componentDelegateQueue,
            final AFXComponent component, final FXComponentLayout layout) {
        super(component.getName());
        this.targetComponents = targetComponents;
        this.component = component;
        this.layout = layout;
        this.componentDelegateQueue = componentDelegateQueue;
        setCacheHints(true, CacheHint.SPEED);
    }

    private void setCacheHints(boolean cache, CacheHint hint) {
        Node currentRoot = this.component.getRoot();
        if (currentRoot != null && currentRoot.getParent() != null) {
            currentRoot.getParent().setCache(cache);
            currentRoot.getParent().setCacheHint(CacheHint.SPEED);
        }
    }

    @Override
    protected AFXComponent call() throws Exception {
        // TODO handle locks to components write methods
        try {
            this.component.lock();
            while (this.component.hasIncomingMessage()) {
                final IAction<Event, Object> myAction = this.component
                        .getNextIncomingMessage();
                this.log(" //1.1.1.1.1// handle replace component BEGIN: "
                        + this.component.getName());

                final Node previousContainer = this.component.getRoot();
                final String currentTaget = this.component.getExecutionTarget();
                // run code
                this.log(" //1.1.1.1.2// handle component: "
                        + this.component.getName());
                final Node handleReturnValue = this.prepareAndRunHandleMethod(
                        this.component, myAction);
                this.log(" //1.1.1.1.3// publish component: "
                        + this.component.getName());

                this.publish(this.component, myAction, this.targetComponents,
                        this.layout, handleReturnValue, previousContainer,
                        currentTaget);

            }
        } catch (final IllegalStateException e) {
            if (e.getMessage().contains("Not on FX application thread")) {
                throw new UnsupportedOperationException(
                        "Do not reuse Node components in handleAction method, use postHandleAction instead to verify that you change nodes in JavaFX main Thread:",
                        e);
            }
        } finally {
            this.component.release();
        }
        return this.component;
    }

    /**
     * publish handle result in application main thread
     *
     * @throws InterruptedException
     */
    private void publish(final AFXComponent component,
                         final IAction<Event, Object> myAction,
                         final Map<String, Node> targetComponents,
                         final FXComponentLayout layout, final Node handleReturnValue,
                         final Node previousContainer, final String currentTaget)
            throws InterruptedException {
        this.invokeOnFXThreadAndWait(new Runnable() {
            @Override
            public void run() {
                // check if component was set to inactive, if so remove
                if (component.isActive()) {
                    FXComponentReplaceWorker.this.publishComponentValue(
                            component, myAction, targetComponents, layout,
                            handleReturnValue, previousContainer, currentTaget);
                } else {
                    // unregister component
                    FXComponentReplaceWorker.this.removeComponentValue(
                            component, previousContainer, layout);
                    // run teardown
                    FXUtil.invokeHandleMethodsByAnnotation(PreDestroy.class,
                            component, layout);
                }
            }
        });
    }


    private void removeComponentValue(final AFXComponent component,
                                      final Node previousContainer, final FXComponentLayout layout) {
        if (previousContainer != null) {
            final Node parent = previousContainer.getParent();
            if (parent != null) {
                this.handleOldComponentRemove(parent, previousContainer);
            }
        }

    }

    /**
     * run in thread
     *
     * @param previousContainer
     * @param currentTaget
     */
    private void publishComponentValue(final AFXComponent component,
                                       final IAction<Event, Object> action,
                                       final Map<String, Node> targetComponents,
                                       final FXComponentLayout layout, final Node handleReturnValue,
                                       final Node previousContainer, final String currentTaget) {
        this.executeComponentViewPostHandle(handleReturnValue, component,
                action);
        if (previousContainer != null) {
            // check again if component was set to inactive (in postHandle), if
            // so remove
            if (component.isActive()) {
                this.removeOldComponentValue(component, previousContainer,
                        currentTaget);
                this.checkAndHandleTargetChange(component, previousContainer,
                        currentTaget, layout);
            } else {
                // unregister component
                this.removeComponentValue(component, previousContainer, layout);
                // run teardown
                FXUtil.invokeHandleMethodsByAnnotation(PreDestroy.class,
                        component, layout);
            }

        }
    }

    /**
     * remove old component value from root node
     */
    private void removeOldComponentValue(final AFXComponent component,
                                         final Node previousContainer, final String currentTaget) {
        final Node root = component.getRoot();
        // avoid remove/add when root component did not changed!
        if (!currentTaget.equals(component.getExecutionTarget())
                || root == null || root != previousContainer) {
            // remove old view
            this.removeComponentValue(component, previousContainer, this.layout);
        }
    }

    /**
     * add new component value to root node
     */
    private void checkAndHandleTargetChange(final AFXComponent component,
                                            final Node previousContainer, final String currentTarget,
                                            final FXComponentLayout layout) {
        final Node root = component.getRoot();
        final Node parentNode = previousContainer.getParent();
        if (!currentTarget.equals(component.getExecutionTarget())) {
            executeTargetChange(component, currentTarget);
        } else if (root != null && root != previousContainer) {
            // add new view
            this.log(" //1.1.1.1.4// handle new component insert: "
                    + component.getName());
            this.handleViewState(root, true);
            this.handleNewComponentValue(this.componentDelegateQueue,
                    component, this.targetComponents, parentNode, currentTarget);
        }

    }

    /**
     * Performs target change of component or perspective
     *
     * @param component
     * @param currentTarget
     */
    private void executeTargetChange(final AFXComponent component,
                                     final String currentTarget) {
        final String validId = this.getValidTargetId(currentTarget,
                component.getExecutionTarget());
        final Node validContainer = this.getValidContainerById(
                this.targetComponents, validId);
        if (validContainer != null) {
            this.handleLocalTargetChange(component, this.targetComponents,
                    validContainer);
        } else {
            this.handlePerspectiveChange(this.componentDelegateQueue,
                    component, layout);
        }
    }

    @Override
    protected final void done() {
        try {
            this.get();
        } catch (final InterruptedException e) {
            logger.info("execution interrupted for component: " + this.component.getName());
        } catch (final ExecutionException e) {
            if (e.getCause() instanceof InterruptedException) {
                logger.info("execution interrupted for component: " + this.component.getName());
            } else {
                e.printStackTrace();
            }

            // TODO add to error queue and restart thread if
            // messages in
            // queue
        } catch (final UnsupportedOperationException e) {
            e.printStackTrace();
        } catch (final Exception e) {
            e.printStackTrace();
            // TODO add to error queue and restart thread if
            // messages in
            // queue
        } finally {
            setCacheHints(true, CacheHint.DEFAULT);
        }

    }

}
