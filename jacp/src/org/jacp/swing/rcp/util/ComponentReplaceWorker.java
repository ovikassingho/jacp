package org.jacp.swing.rcp.util;

import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Map;

import javax.swing.SwingUtilities;

import org.jacp.api.action.IAction;
import org.jacp.api.base.ISubComponent;

/**
 * Background Worker to execute components handle method to replace or add the
 * component
 * 
 * @author Andy Moncsek
 * 
 */
public class ComponentReplaceWorker extends AbstractComponentWorker {
	private final Map<String, Container> targetComponents;
	private final ISubComponent<Container, ActionListener, ActionEvent, Object> editor;
	private final IAction<ActionEvent,Object> action;

	public ComponentReplaceWorker(
			final Map<String, Container> targetComponents,
			final ISubComponent<Container, ActionListener, ActionEvent, Object> editor,
			final IAction<ActionEvent,Object> action) {
		this.targetComponents = targetComponents;
		this.editor = editor;
		this.action = action;
	}

	@Override
	protected ISubComponent<Container, ActionListener, ActionEvent, Object> doInBackground()
			throws Exception {
		return runHandleSubcomponent(editor, action);
	}

	@Override
	protected ISubComponent<Container, ActionListener, ActionEvent, Object> runHandleSubcomponent(
			final ISubComponent<Container, ActionListener, ActionEvent, Object> component,
			final IAction<ActionEvent,Object> action) {
		synchronized (component) {
			final Container currentContainer = component.getRoot();
			final String currentTaget = component.getTarget();
			final Container parent = currentContainer.getParent();
			// run code
			prepareAndHandleComponent(component, action);
			//remove old view
			handleOldComponentRemove(parent, currentContainer);
			// add new view
			handleNewComponentValue(component, parent, currentTaget);
	
			return component;

		}

	}

	/**
	 * removes old ui component of subcomponent form parent ui component
	 * 
	 * @param parent
	 * @param currentContainer
	 */
	private void handleOldComponentRemove(final Container parent,
			final Container currentContainer) {
		final Thread worker = new Thread() {
			@Override
			public void run() {
				SwingUtilities.invokeLater(new Runnable() {
					@Override
					public void run() {
						// run in EventDispatchThread
						parent.remove(currentContainer);
					}
				}); // SWING UTILS END
			} // run end
		}; // thred END
		worker.start();
	}

	/**
	 * set new ui component to parent ui component
	 * 
	 * @param component
	 * @param parent
	 * @param currentTaget
	 */
	private void handleNewComponentValue(
			final ISubComponent<Container, ActionListener, ActionEvent, Object> component,
			final Container parent, final String currentTaget) {
		if (currentTaget.equals(component.getTarget())) {
			addComponentByType(parent, component);
		} else {
			final Container validContainer = getValidContainerById(
					targetComponents, component.getTarget());
			addComponentByType(validContainer, component);

		}
	}

	/**
	 * runs subcomponents handle method
	 * 
	 * @param component
	 * @param action
	 * @return
	 */
	private Container prepareAndHandleComponent(
			final ISubComponent<Container, ActionListener, ActionEvent, Object> component,
			final IAction<ActionEvent,Object> action) {
		final Container editorComponent = component.handle(action);
		component.setRoot(editorComponent);
		editorComponent.setVisible(true);
		editorComponent.setEnabled(true);
		return editorComponent;
	}

	@Override
	public void done() {

	}

}
