/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jacp.swing.rcp.action;

import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import org.jacp.api.action.IAction;
import org.jacp.api.action.IActionListener;
import org.jacp.api.observers.IObserver;

/**
 * 
 * @author amo
 */
public class SwingActionListener implements ActionListener,
		IActionListener<ActionListener, ActionEvent, Object> {

	private IAction<Object, ActionEvent> action;
	private final IObserver<Container, ActionListener, ActionEvent, Object> observer;

	public SwingActionListener(
			final IAction<Object, ActionEvent> action,
			final IObserver<Container, ActionListener, ActionEvent, Object> observer) {
		this.action = action;
		this.observer = observer;
	}

	@Override
	public void actionPerformed(final ActionEvent e) {
		action.setActionEvent(e);
		notifyComponents(action);
	}

	@Override
	public void notifyComponents(final IAction<Object, ActionEvent> action) {
		observer.handle(action);
	}

	public void setAction(final IAction<Object, ActionEvent> action) {
		this.action = action;
	}

	@Override
	public IAction<Object, ActionEvent> getAction() {
		return action;
	}

	@Override
	public ActionListener getListener() {
		return this;
	}

}
