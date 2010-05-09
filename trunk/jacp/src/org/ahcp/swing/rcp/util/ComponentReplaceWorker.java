package org.ahcp.swing.rcp.util;

import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Map;

import javax.swing.SwingUtilities;

import org.ahcp.api.action.IAction;
import org.ahcp.api.base.ISubComponent;

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
	private final IAction<Object, ActionEvent> action;

	public ComponentReplaceWorker(
			final Map<String, Container> targetComponents,
			final ISubComponent<Container, ActionListener, ActionEvent, Object> editor,
			final IAction<Object, ActionEvent> action) {
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
			final IAction<Object, ActionEvent> action) {
		synchronized (component) {
			final Container currentContainer = component.getRoot();
			final String currentTaget = component.getTarget();
			final Container parent = currentContainer.getParent();
			System.out.print("doInBackground() PART1 parent: " + parent);
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

			prepareComponent(component, action);

			handleNewComponentValue(component, parent, currentTaget);
			// /////
			return component;

		}

	}

	private void handleNewComponentValue(
			final ISubComponent<Container, ActionListener, ActionEvent, Object> component,
			final Container parent, final String currentTaget) {
		final String newTaget = component.getTarget();
		if (currentTaget.equals(newTaget)) {
			addComponentByType(parent, component);
		} else {
			final Container validContainer = getValidContainerById(
					targetComponents, component.getTarget());
			addComponentByType(validContainer, component);
			invalidateHost(parent);

		}
	}

	private Container prepareComponent(
			final ISubComponent<Container, ActionListener, ActionEvent, Object> component,
			final IAction<Object, ActionEvent> action) {
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
