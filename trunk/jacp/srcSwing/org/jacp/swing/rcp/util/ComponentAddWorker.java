package org.jacp.swing.rcp.util;

import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Map;

import org.jacp.api.action.IAction;
import org.jacp.api.base.IVComponent;

/**
 * Handles ui return value of component and add to correct perspective target
 * 
 * @author Andy Moncsek
 * 
 */
public class ComponentAddWorker extends AbstractComponentWorker<IVComponent<Container, ActionListener, ActionEvent, Object>> {

	private final Map<String, Container> targetComponents;
	private final IVComponent<Container, ActionListener, ActionEvent, Object> component;

	public ComponentAddWorker(
			final Map<String, Container> targetComponents,
			final IVComponent<Container, ActionListener, ActionEvent, Object> component) {
		this.targetComponents = targetComponents;
		this.component = component;
	}

	@Override
	protected IVComponent<Container, ActionListener, ActionEvent, Object> runHandleSubcomponent(
			final IVComponent<Container, ActionListener, ActionEvent, Object> component,
			final IAction<ActionEvent, Object> action) {
		return null;
	}

	@Override
	protected IVComponent<Container, ActionListener, ActionEvent, Object> doInBackground()
			throws Exception {
		return null;
	}

	@Override
	public void done() {
		component.setTarget(getTargetComponentId(component.getTarget()));
		handleNewComponentValue(component, targetComponents, null, "");
	}

}
