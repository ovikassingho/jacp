package org.jacp.swing.rcp.util;

import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Map;

import org.jacp.api.action.IAction;
import org.jacp.api.base.ISubComponent;

/**
 * Background Worker to execute components; handle method to init component
 * 
 * @author Andy Moncsek
 * 
 */
public class ComponentInitWorker extends AbstractComponentWorker {
	private final Map<String, Container> targetComponents;
	private final ISubComponent<Container, ActionListener, ActionEvent, Object> editor;
	private final IAction<ActionEvent,Object> action;

	public ComponentInitWorker(
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
			final Container editorComponent = component.handle(action);
			component.setRoot(editorComponent);
			editorComponent.setVisible(true);
			editorComponent.setEnabled(true);
			final Container validContainer = getValidContainerById(
					targetComponents, component.getTarget());

			addComponentByType(validContainer, component);

		}
		return component;
	}

	@Override
	public void done() {

	}

}