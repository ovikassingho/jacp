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

package org.jacp.swing.rcp.util;

import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JComponent;
import javax.swing.JMenu;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;

import org.jacp.api.action.IAction;
import org.jacp.api.component.ISubComponent;
import org.jacp.api.component.IVComponent;
import org.jacp.api.componentLayout.Layout;

/**
 * handles component methods in own thread; see
 * http://bugs.sun.com/view_bug.do?bug_id=6880336
 * 
 * @author Andy Moncsek
 * 
 */
public abstract class AbstractComponentWorker<T> extends
	org.jacp.swing.rcp.util.SwingWorker<T, ChunkDTO> {

    private final Logger logger = Logger.getLogger(this.getClass().getName());

    private final Map<Layout, Container> empty = new HashMap<Layout, Container>();

    /**
     * find valid target component in perspective
     * 
     * @param targetComponents
     * @param id
     * @return
     */
    protected Container getValidContainerById(
	    final Map<String, Container> targetComponents, final String id) {
	return targetComponents.get(id);
    }

    /**
     * invalidate swing host after changes
     * 
     * @param host
     */
    protected void invalidateHost(final Container host) {
	if (host instanceof JComponent) {
	    ((JComponent) host).revalidate();
	} else {
	    host.invalidate();
	}
	host.repaint();
    }

    /**
     * find valid target and add type specific new component
     * 
     * @param layout
     * @param editor
     */
    protected void addComponentByType(
	    final Container validContainer,
	    final IVComponent<Container, ActionListener, ActionEvent, Object> editor,
	    final Map<Layout, Container> bars, final JMenu menu) {

	SwingUtilities.invokeLater(new Runnable() {
	    @Override
	    public void run() {
		if (validContainer instanceof JScrollPane) {
		    ((JScrollPane) validContainer).getViewport().add(
			    editor.getName(), editor.getRoot());
		} else {
		    handleAdd(validContainer, editor.getRoot(),
			    editor.getName());

		}
		if (menu != null) {
		    editor.handleMenuEntries(menu);
		}
		if (!bars.isEmpty()) {
		    editor.handleBarEntries(bars);
		    final Collection<Container> container = bars.values();
		    for (final Container c : container) {
			invalidateHost(c);
		    }
		}
		validContainer.setEnabled(true);
		validContainer.setVisible(true);

	    }
	});

    }

    /**
     * enables component an add to container
     * 
     * @param validContainer
     * @param uiComponent
     * @param name
     */
    private void handleAdd(final Container validContainer,
	    final Container uiComponent, final String name) {
	uiComponent.setEnabled(true);
	uiComponent.setVisible(true);
	if (validContainer != null) {
	    validContainer.add(name, uiComponent);
	}

    }

    protected abstract T runHandleSubcomponent(final T component,
	    final IAction<ActionEvent, Object> action);

    /**
     * removes old ui component of subcomponent form parent ui component
     * 
     * @param parent
     * @param currentContainer
     */
    protected void handleOldComponentRemove(final Container parent,
	    final Container currentContainer) {
	parent.remove(currentContainer);
    }

    /**
     * set new ui component to parent ui component
     * 
     * @param component
     * @param parent
     * @param currentTaget
     */
    protected void handleNewComponentValue(
	    final IVComponent<Container, ActionListener, ActionEvent, Object> component,
	    final Map<String, Container> targetComponents,
	    final Container parent, final String currentTaget) {
	if (parent == null) {
	    final String validId = getValidTargetId(currentTaget,
		    component.getExecutionTarget());
	    handleTargetChange(component, targetComponents, validId);
	} else if (currentTaget.equals(component.getExecutionTarget())) {
	    addComponentByType(parent, component, empty, null);
	}
    }

    /**
     * currentTarget.length < 2 Happens when component changed target from one
     * perspective to an other
     * 
     * @param currentTaget
     * @param futureTarget
     * @return
     */
    private String getValidTargetId(final String currentTaget,
	    final String futureTarget) {
	return currentTaget.length() < 2 ? getTargetComponentId(futureTarget)
		: futureTarget;
    }

    /**
     * handle component when target has changed
     * 
     * @param component
     * @param targetComponents
     */
    private void handleTargetChange(
	    final IVComponent<Container, ActionListener, ActionEvent, Object> component,
	    final Map<String, Container> targetComponents, final String target) {
	final Container validContainer = getValidContainerById(
		targetComponents, target);
	if (validContainer != null) {
	    addComponentByType(validContainer, component, empty, null);
	} else {
	    // handle target outside current perspective
	    changeComponentTarget(component);
	}
    }

    /**
     * move component to new target
     * 
     * @param component
     */
    protected void changeComponentTarget(
	    final ISubComponent<ActionListener, ActionEvent, Object> component) {
	component.getParentPerspective().delegateTargetChange(
		component.getExecutionTarget(), component);
    }

    /**
     * runs subcomponents handle method
     * 
     * @param component
     * @param action
     * @return
     */
    protected Container prepareAndHandleComponent(
	    final IVComponent<Container, ActionListener, ActionEvent, Object> component,
	    final IAction<ActionEvent, Object> action) {
	final Container editorComponent;
	synchronized (component) {
	    // System.out.println("-------1" + Thread.currentThread());
	    editorComponent = component.handle(action);
	    // System.out.println("-------2");
	    component.setRoot(editorComponent);
	}
	return editorComponent;
    }

    /**
     * returns the message target component id
     * 
     * @param messageId
     * @return
     */
    protected String getTargetComponentId(final String messageId) {
	final String[] targetId = getTargetId(messageId);
	if (!isLocalMessage(messageId)) {
	    return targetId[1];
	}
	return messageId;
    }

    /**
     * when id has no separator it is a local message
     * 
     * @param messageId
     * @return
     */
    protected boolean isLocalMessage(final String messageId) {
	return !messageId.contains(".");
    }

    /**
     * returns target message with perspective and component name as array
     * 
     * @param messageId
     * @return
     */
    protected String[] getTargetId(final String messageId) {
	return messageId.split("\\.");
    }

    protected void log(final String message) {
	if (logger.isLoggable(Level.FINE)) {
	    logger.fine(">> " + message);
	}
    }

}
