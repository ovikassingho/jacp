/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jacp.swing.rcp.action;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.jacp.api.action.IAction;
import org.jacp.api.action.IActionListener;
import org.jacp.api.coordinator.ICoordinator;

/**
 * 
 * @author amo
 */
public class SwingActionListener implements ActionListener,
	IActionListener<ActionListener, ActionEvent, Object> {

    private IAction<ActionEvent, Object> action;
    private final ICoordinator<ActionListener, ActionEvent, Object> observer;
    private final Logger logger = Logger.getLogger(this.getClass().getName());

    public SwingActionListener(final IAction<ActionEvent, Object> action,
	    final ICoordinator<ActionListener, ActionEvent, Object> observer) {
	this.action = action;
	this.observer = observer;
    }

    @Override
    public void actionPerformed(final ActionEvent e) {
	action.setActionEvent(e);
	log(" //1// message send from / to: " + action.getSourceId() + " / "
		+ action.getTargetId());
	notifyComponents(action);

    }

    @Override
    public void notifyComponents(final IAction<ActionEvent, Object> action) {
	observer.handle(action);
    }

    @Override
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
