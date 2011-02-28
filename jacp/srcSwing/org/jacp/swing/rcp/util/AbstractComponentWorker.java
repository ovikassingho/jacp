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

import java.awt.Component;
import java.awt.Container;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JComponent;
import javax.swing.JMenu;
import javax.swing.JScrollPane;
import javax.swing.RepaintManager;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.jacp.api.action.IAction;
import org.jacp.api.component.ISubComponent;
import org.jacp.api.component.IVComponent;
import org.jacp.api.componentLayout.Layout;

/**
 * handles component methods in own thread; all methodes from interface are used
 * to add, remove and manipulate swing components, be aware of calling this
 * methods outside the EDT; The Swing worker provides a done() and a process()
 * method which are guarantee to be handled on EDT!
 * http://bugs.sun.com/view_bug.do?bug_id=6880336 TODO!!!! create interface for
 * methods non specific for the swing worker!!!!
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
	protected final Container getValidContainerById(
			final Map<String, Container> targetComponents, final String id) {
		return targetComponents.get(id);
	}

	/**
	 * invalidate swing host after changes
	 * 
	 * @param host
	 */
	protected final void invalidateHost(final Container host) {
		if (host instanceof JComponent) {
			((JComponent) host).revalidate();
		} else {
			host.invalidate();
		}
		host.repaint();
	}

	/**
	 * find valid target and add type specific new component. Handles Container,
	 * ScrollPanes, Menus and Bar Entries from user
	 * 
	 * @param layout
	 * @param editor
	 */
	protected final void addComponentByType(
			final Container validContainer,
			final IVComponent<Container, ActionListener, ActionEvent, Object> editor,
			final Map<Layout, Container> bars, final JMenu menu) {

		if (validContainer instanceof JScrollPane) {
			((JScrollPane) validContainer).getViewport().add(editor.getName(),
					editor.getRoot());
		} else {
			handleAdd(validContainer, editor.getRoot(), editor.getName());

		}
		if (menu != null) {
			// menu handling in EDT!!
			editor.handleMenuEntries(menu);
		}
		if (!bars.isEmpty()) {
			// bar handling in EDT!!
			handleUserBarEntries(editor, bars);

		}
		validContainer.setEnabled(true);
		validContainer.setVisible(true);

	}

	/**
	 * create a view listener to notify changes in parent node (the valid
	 * container); the listener will be notified by the view when he was
	 * resized, moved, shown or hidden
	 * 
	 * @param validContainer
	 * @param view
	 */
	protected final void addViewListener(final Container validContainer,
			final Container view) {
		view.addComponentListener(this.createViewListener(validContainer));
	}

	/**
	 * Creates a listener for the view. Listener repaint component on resize, move, hide, show
	 * 
	 * @return a <code>ViewListener</code>
	 */
	protected ViewListener createViewListener(final Container container) {
		return new ViewListener(container);
	}

	/**
	 * A listener for the view.
	 */
	protected final class ViewListener extends ComponentAdapter implements
			Serializable {
		/**
		 * 
		 */
		private static final long serialVersionUID = 8432488825263877693L;
		private final Container container;

		public ViewListener(final Container container) {
			this.container = container;
		}

		@Override
		public void componentResized(final ComponentEvent e) {
			handleChange();
		}

		@Override
		public void componentMoved(final ComponentEvent e) {
			handleChange();
		}

		@Override
		public void componentShown(final ComponentEvent e) {
			handleChange();
		}

		@Override
		public void componentHidden(final ComponentEvent e) {
			handleChange();
		}
		/**
		 * handles change event in component
		 */
		private void handleChange() {
			final ChangeListener[] listener = this.container
					.getListeners(ChangeListener.class);
			if (listener != null && listener.length > 0) {
				for (int i = 0; i < listener.length; i++) {
					final ChangeEvent changeEvent = new ChangeEvent(container);
					listener[i].stateChanged(changeEvent);
				}
			}

			// revalidate
			if (container instanceof JComponent) {
				container.invalidate();
				RepaintManager.currentManager(container).addInvalidComponent(
						((JComponent) container));
			}
		}
	}

	/**
	 * create dummy root component and handle users bar invocation method
	 * 
	 * @param editor
	 * @param bars
	 */
	private final void handleUserBarEntries(
			final IVComponent<Container, ActionListener, ActionEvent, Object> editor,
			final Map<Layout, Container> bars) {
		final Set<Layout> keys = bars.keySet();
		final Map<Layout, Container> myBars = editor.getBarEntries();
		myBars.clear();
		for (final Layout layout : keys) {
			myBars.put(layout, new CustomJComponent());

		}

		editor.handleBarEntries(editor.getBarEntries());
		addBarEntries(editor, bars);
	}

	/**
	 * provides custom methods to access valid menu entries; this class acts
	 * like a proxy for easy access of menu components added by user (developer)
	 * in "handle menu"
	 * 
	 * @author Andy Moncsek
	 * 
	 */
	private final class CustomJComponent extends JComponent {
		private static final long serialVersionUID = 2148589637515686904L;
		private final List<Component> components = new ArrayList<Component>();

		@Override
		public final Component add(final Component comp) {
			components.add(comp);
			return super.add(comp);
		}

		@Override
		public final Component[] getComponents() {
			final Component[] tmp = new Component[components.size()];
			for (int p = 0; p < components.size(); p++) {
				tmp[p] = components.get(p);
			}
			return tmp;
		}
	}

	/**
	 * add users bar entries to bar
	 * 
	 * @param editor
	 * @param bars
	 */
	private final void addBarEntries(
			final IVComponent<Container, ActionListener, ActionEvent, Object> editor,
			final Map<Layout, Container> bars) {
		final Map<Layout, Container> currentBars = editor.getBarEntries();
		final Iterator<Entry<Layout, Container>> it = currentBars.entrySet()
				.iterator();
		while (it.hasNext()) {
			final Entry<Layout, Container> entry = it.next();
			final Layout key = entry.getKey();
			if (bars.containsKey(key)) {
				final Container systemBar = bars.get(key);
				final Container wrapper = entry.getValue();
				final Component[] componentList = wrapper.getComponents();
				addToSystemBar(systemBar, componentList);
			}

		}
	}

	/**
	 * add users entries to bar in EDT
	 * 
	 * @param systemBar
	 * @param componentList
	 */
	private final void addToSystemBar(final Container systemBar,
			final Component[] componentList) {
		for (int i = 0; i < componentList.length; i++) {
			systemBar.add(componentList[i]);
		}

		invalidateHost(systemBar);
	}

	/**
	 * enables component an add to container
	 * 
	 * @param validContainer
	 * @param uiComponent
	 * @param name
	 */
	private final void handleAdd(final Container validContainer,
			final Container uiComponent, final String name) {
		uiComponent.setEnabled(true);
		uiComponent.setVisible(true);
		if (validContainer != null) {
			validContainer.add(name, uiComponent);
			final Rectangle bounds = uiComponent.getBounds();
			validContainer.repaint(bounds.x,bounds.y,bounds.width,bounds.height);
			if (validContainer instanceof JComponent) {
				((JComponent) validContainer).revalidate();
			}
		}

	}

	/**
	 * removes old ui component of subcomponent form parent ui component
	 * 
	 * @param parent
	 * @param currentContainer
	 */
	protected final void handleOldComponentRemove(final Container parent,
			final Container currentContainer) {
		parent.remove(currentContainer);
	}

	/**
	 * set new ui component to parent ui component, be careful! call this method
	 * only in EDT... never run from separate thread
	 * 
	 * @param component
	 * @param parent
	 * @param currentTaget
	 */
	protected final void handleNewComponentValue(
			final IVComponent<Container, ActionListener, ActionEvent, Object> component,
			final Map<String, Container> targetComponents,
			final Container parent, final String currentTaget) {
		if (parent == null) {
			final String validId = getValidTargetId(currentTaget,
					component.getExecutionTarget());
			handleTargetChange(component, targetComponents, validId);
		} else if (currentTaget.equals(component.getExecutionTarget())) {
			addComponentByType(parent, component, empty, null);
		} else {
			final String validId = getValidTargetId(currentTaget,
					component.getExecutionTarget());
			handleTargetChange(component, targetComponents, validId);
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
	private final String getValidTargetId(final String currentTaget,
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
	private final void handleTargetChange(
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
	protected final void changeComponentTarget(
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
	protected final Container prepareAndHandleComponent(
			final IVComponent<Container, ActionListener, ActionEvent, Object> component,
			final IAction<ActionEvent, Object> action) {
		final Container editorComponent = component.handle(action);
		component.setRoot(editorComponent);
		return editorComponent;
	}

	/**
	 * returns the message target component id
	 * 
	 * @param messageId
	 * @return
	 */
	protected final String getTargetComponentId(final String messageId) {
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
	protected final boolean isLocalMessage(final String messageId) {
		return !messageId.contains(".");
	}

	/**
	 * returns target message with perspective and component name as array
	 * 
	 * @param messageId
	 * @return
	 */
	protected final String[] getTargetId(final String messageId) {
		return messageId.split("\\.");
	}

	protected void log(final String message) {
		if (logger.isLoggable(Level.FINE)) {
			logger.fine(">> " + message);
		}
	}

}
