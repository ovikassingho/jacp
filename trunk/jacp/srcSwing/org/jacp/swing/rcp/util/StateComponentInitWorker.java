package org.jacp.swing.rcp.util;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import org.jacp.api.action.IAction;
import org.jacp.api.base.ISubComponent;

public class StateComponentInitWorker
		extends
		AbstractComponentWorker<ISubComponent<ActionListener, ActionEvent, Object>> {

	private final ISubComponent<ActionListener, ActionEvent, Object> component;
	private final IAction<ActionEvent, Object> action;

	public StateComponentInitWorker(
			final ISubComponent<ActionListener, ActionEvent, Object> component,
			final IAction<ActionEvent, Object> action) {
		this.component = component;
		this.action = action;
	}

	@Override
	protected ISubComponent<ActionListener, ActionEvent, Object> runHandleSubcomponent(
			ISubComponent<ActionListener, ActionEvent, Object> component,
			IAction<ActionEvent, Object> action) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected ISubComponent<ActionListener, ActionEvent, Object> doInBackground()
			throws Exception {
		synchronized (component) {
			this.component.handle(this.action);
		}
		return this.component;
	}

}
