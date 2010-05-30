/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jacp.swing.rcp.action;

import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.jacp.api.action.IAction;
import org.jacp.api.action.IActionListener;
import org.jacp.api.observers.IObserver;

/**
 * 
 * @author amo
 */
public class SwingActionListener implements ActionListener,
		IActionListener<ActionListener, ActionEvent, Object> {

	private IAction<ActionEvent, Object> action;
	private final IObserver<Container, ActionListener, ActionEvent, Object> observer;
	private final Logger logger = Logger.getLogger(this.getClass().getName());

	public SwingActionListener(
			final IAction<ActionEvent, Object> action,
			final IObserver<Container, ActionListener, ActionEvent, Object> observer) {
		this.action = action;
		this.observer = observer;
	}

	@Override
	public void actionPerformed(final ActionEvent e) {
		action.setActionEvent(e);
		notifyComponents(action);
		log(" //1// message send from / to: "+action.getSourceId()+" / "+action.getTargetId());
	}

	@Override
	public void notifyComponents(final IAction<ActionEvent, Object> action) {
		observer.handle(action);
	}

	public void setAction(final IAction<ActionEvent, Object> action) {
		this.action = action;
	}

	@Override
	public IAction<ActionEvent, Object> getAction() {
		return action;
	}

	@Override
	public ActionListener getListener() {
		return this;
	}
	private void log(final String message) {
		if (logger.isLoggable(Level.FINE)) {
			logger.fine(">> " + message);
		}
	}
}
