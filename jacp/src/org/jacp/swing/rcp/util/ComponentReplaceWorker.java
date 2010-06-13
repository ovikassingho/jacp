package org.jacp.swing.rcp.util;

import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Map;

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
	private final ISubComponent<Container, ActionListener, ActionEvent, Object> component;
	private final IAction<ActionEvent, Object> action;

	public ComponentReplaceWorker(
			final Map<String, Container> targetComponents,
			final ISubComponent<Container, ActionListener, ActionEvent, Object> component,
			final IAction<ActionEvent, Object> action) {
		this.targetComponents = targetComponents;
		this.component = component;
		this.action = action;
	}

	@Override
	protected ISubComponent<Container, ActionListener, ActionEvent, Object> doInBackground()
			throws Exception {
		return runHandleSubcomponent(component, action);
	}

	@Override
	protected ISubComponent<Container, ActionListener, ActionEvent, Object> runHandleSubcomponent(
			final ISubComponent<Container, ActionListener, ActionEvent, Object> component,
			final IAction<ActionEvent, Object> action) {
		synchronized (component) {
			int i =0;
			while (component.hasIncomingMessage()) {
				System.out.print("Iteration :: " + i++);
				final IAction<ActionEvent, Object> myAction = component.getNextIncomingMessage();
				log(" //1.1.1.1.1// handle replace component BEGIN: "
						+ component.getName());
				final Map<String, Container> targetComponents = this.targetComponents;
				final Container currentContainer = component.getRoot();
				final String currentTaget = component.getTarget();
				// run code
				log(" //1.1.1.1.2// handle component: " + component.getName());
				prepareAndHandleComponent(component, myAction);
				// remove old view
				log(" //1.1.1.1.3// handle old component remove: "
						+ component.getName());
				final Container parent = currentContainer.getParent();
				handleOldComponentRemove(parent, currentContainer);
				// add new view
				log(" //1.1.1.1.4// handle new component insert: "
						+ component.getName());
				handleNewComponentValue(component, targetComponents, parent,
						currentTaget);
			}
			component.setBlocked(false);
			return component;

		}

	}

	@Override
	public void done() {

	}

}
