/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jacp.javafx2.rcp.util;

import java.beans.EventHandler;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import org.jacp.api.action.IAction;
import org.jacp.api.component.ISubComponent;
import org.jacp.api.component.IVComponent;
import org.jacp.api.componentLayout.Layout;

/**
 *
 * @author Andy Moncsek
 */
public class AFX2ComponentWorker<T> extends Task<T> {

    private final Logger logger = Logger.getLogger(this.getClass().getName());
    private final Map<Layout, Node> empty = new HashMap<Layout, Node>();


    @Override
    protected T call() throws Exception {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    /**
	 * find valid target component in perspective
	 * 
	 * @param targetComponents
	 * @param id
	 * @return
	 */
	protected final Node getValidContainerById(
			final Map<String, Node> targetComponents, final String id) {
		return targetComponents.get(id);
	}

	/**
	 * invalidate swing host after changes
	 * 
	 * @param host
	 */
	protected final void invalidateHost(final Node host) {
		//TODO check if repaint or revalidate is nessesary
	}

	/**
	 * find valid target and add type specific new component. Handles Container,
	 * ScrollPanes, Menus and Bar Entries from user
	 * 
	 * @param layout
	 * @param editor
	 */
	protected final void addComponentByType(
			final Node validContainer,
			final IVComponent<Node, EventHandler, ActionEvent, Object> editor,
			final Map<Layout, Node> bars, final MenuBar menu) {

		handleAdd(validContainer, editor.getRoot(), editor.getName());
		if (menu != null) {
			// menu handling in EDT!!
			editor.handleMenuEntries(menu);
		}
		if (!bars.isEmpty()) {
			// bar handling in EDT!!
			handleUserBarEntries(editor, bars);

		}
	
		validContainer.setVisible(true);

	}


	
	/**
	 * create dummy root component and handle users bar invocation method
	 * 
	 * @param editor
	 * @param bars
	 */
	private final void handleUserBarEntries(
			final IVComponent<Node, EventHandler, ActionEvent, Object> editor,
			final Map<Layout, Node> bars) {
		final Set<Layout> keys = bars.keySet();
		final Map<Layout, Node> myBars = editor.getBarEntries();
		myBars.clear();
		//for (final Layout layout : keys) {
		//	myBars.put(layout, new CustomJComponent());

		//}

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
/**	TODO migrate custom component for menu entries
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
*/
	/**
	 * add users bar entries to bar
	 * 
	 * @param editor
	 * @param bars
	 */
        // TODO migrate menu bar handling
	private final void addBarEntries(
			final IVComponent<Node, EventHandler, ActionEvent, Object> editor,
			final Map<Layout, Node> bars) {
		final Map<Layout, Node> currentBars = editor.getBarEntries();
		final Iterator<Entry<Layout, Node>> it = currentBars.entrySet()
				.iterator();
		while (it.hasNext()) {
			final Entry<Layout, Node> entry = it.next();
			final Layout key = entry.getKey();
			if (bars.containsKey(key)) {
				final Node systemBar = bars.get(key);
				final Node wrapper = entry.getValue();
				//final Node[] componentList = wrapper.getComponents();
				//addToSystemBar(systemBar, componentList);
			}

		}
	}

	/**
	 * add users entries to bar in EDT
	 * 
	 * @param systemBar
	 * @param componentList
	 */
        // TODO migrate system bar handling
	private final void addToSystemBar(final Node systemBar,
			final Node[] componentList) {
		for (int i = 0; i < componentList.length; i++) {
		//	systemBar.add(componentList[i]);
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
	private final void handleAdd(final Node validContainer,
			final Node uiComponent, final String name) {
		uiComponent.setVisible(true);
		if (validContainer != null) {
			//validContainer.add(name, uiComponent);
			
		}

	}

	/**
	 * removes old ui component of subcomponent form parent ui component
	 * 
	 * @param parent
	 * @param currentContainer
	 */
	protected final void handleOldComponentRemove(final Node parent,
			final Node currentContainer) {
		//parent.remove(currentContainer);
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
			final IVComponent<Node, EventHandler, ActionEvent, Object> component,
			final Map<String, Node> targetComponents,
			final Node parent, final String currentTaget) {
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
			final IVComponent<Node, EventHandler, ActionEvent, Object> component,
			final Map<String, Node> targetComponents, final String target) {
		final Node validContainer = getValidContainerById(
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
			final ISubComponent<EventHandler, ActionEvent, Object> component) {
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
	protected final Node prepareAndHandleComponent(
			final IVComponent<Node,EventHandler, ActionEvent, Object> component,
			final IAction<ActionEvent, Object> action) {
		final Node editorComponent = component.handle(action);
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
