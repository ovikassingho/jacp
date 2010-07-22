package org.jacp.swing.rcp.util;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.concurrent.ExecutionException;

import org.jacp.api.action.IAction;
import org.jacp.api.action.IActionListener;
import org.jacp.api.component.IBGComponent;
import org.jacp.swing.rcp.action.SwingAction;

public class StateComponentRunWorker
		extends
		AbstractComponentWorker<IBGComponent<ActionListener, ActionEvent, Object>> {

	private final IBGComponent<ActionListener, ActionEvent, Object> component;

	public StateComponentRunWorker(
			final IBGComponent<ActionListener, ActionEvent, Object> component) {
		this.component = component;
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
			comp.setBlocked(true);
			while (comp.hasIncomingMessage()) {
				final IAction<ActionEvent, Object> myAction = component
						.getNextIncomingMessage();
				final Object value = comp.handle(myAction);
				final String targetId = comp.getHandleComponentTarget();
				if (value != null && targetId != null) {
					// delegate components handle return value to specified
					// target
					final IActionListener<ActionListener, ActionEvent, Object> listener = comp
							.getActionListener();
					final IAction<ActionEvent, Object> returnAction = new SwingAction(
							comp.getId(), targetId, value);
					listener.setAction(returnAction);
					listener.notifyComponents(returnAction);
				}
			}
			comp.setBlocked(false);
			return comp;
		}

	}

	@Override
	protected void done() {
		try {
			this.get();
		} catch (InterruptedException e) {
			e.printStackTrace();
			// TODO add to error queue and restart thread if messages in queue
		} catch (ExecutionException e) {
			e.printStackTrace();
			// TODO add to error queue and restart thread if messages in queue
		}
		component.setBlocked(false);
	}

}
