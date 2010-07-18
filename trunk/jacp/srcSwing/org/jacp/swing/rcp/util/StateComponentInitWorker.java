package org.jacp.swing.rcp.util;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import org.jacp.api.action.IAction;
import org.jacp.api.component.IBGComponent;
import org.jacp.swing.rcp.action.SwingAction;

public class StateComponentInitWorker
		extends
		AbstractComponentWorker<IBGComponent<ActionListener, ActionEvent, Object>> {

	private final IBGComponent<ActionListener, ActionEvent, Object> component;
	private final IAction<ActionEvent, Object> action;

	public StateComponentInitWorker(
			final IBGComponent<ActionListener, ActionEvent, Object> component,
			final IAction<ActionEvent, Object> action) {
		this.component = component;
		this.action = action;
	}

	@Override
	protected IBGComponent<ActionListener, ActionEvent, Object> runHandleSubcomponent(
			final IBGComponent<ActionListener, ActionEvent, Object> component,
			final IAction<ActionEvent, Object> action) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected IBGComponent<ActionListener, ActionEvent, Object> doInBackground()
			throws Exception {
		final IBGComponent<ActionListener, ActionEvent, Object> comp = component;
		synchronized (comp) {
			final Object value = comp.handle(action);
			final String targetId = comp.getHandleComponentTarget();
			if (value != null && targetId != null) {
				IAction<ActionEvent, Object> action2 = new SwingAction(comp
						.getId(), targetId, value);
				// TODO delegate directly to observer !!
				comp.getActionListener().setAction(action2);
				comp.getActionListener().getListener().actionPerformed(
						action.getActionEvent());
			}
		}
		return comp;
	}

}
