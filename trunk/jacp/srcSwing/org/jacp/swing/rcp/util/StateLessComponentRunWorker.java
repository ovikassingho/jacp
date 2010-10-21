package org.jacp.swing.rcp.util;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.concurrent.ExecutionException;

import org.jacp.api.action.IAction;
import org.jacp.api.action.IActionListener;
import org.jacp.api.component.IBGComponent;
import org.jacp.swing.rcp.action.SwingAction;

public class StateLessComponentRunWorker
	extends
	AbstractComponentWorker<IBGComponent<ActionListener, ActionEvent, Object>> {

    private final IBGComponent<ActionListener, ActionEvent, Object> component;

    public StateLessComponentRunWorker(
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
		final IAction<ActionEvent, Object> myAction = comp
			.getNextIncomingMessage();
		synchronized (myAction) {
		    // final String targetCurrent = comp.getExecutionTarget();
		    final Object value = comp.handle(myAction);
		    final String targetId = comp.getHandleTarget();
		    delegateReturnValue(comp, targetId, value);
		    // checkAndHandleTargetChange(comp, targetCurrent);
		}
	    }
	    comp.setBlocked(false);
	}
	return comp;

    }

    private void checkAndHandleTargetChange(
	    final IBGComponent<ActionListener, ActionEvent, Object> comp,
	    final String currentTaget) {
	final String targetNew = comp.getExecutionTarget();
	if (!targetNew.equals(currentTaget)) {
	    changeComponentTarget(comp);
	}
    }

    /**
     * delegate components handle return value to specified target
     * 
     * @param comp
     * @param targetId
     * @param value
     */
    private void delegateReturnValue(
	    final IBGComponent<ActionListener, ActionEvent, Object> comp,
	    final String targetId, final Object value) {
	if (value != null && targetId != null) {
	    final IActionListener<ActionListener, ActionEvent, Object> listener = comp
		    .getActionListener();
	    listener.setAction(new SwingAction(comp.getId(), targetId, value));
	    listener.notifyComponents(listener.getAction());
	}
    }

    @Override
    protected void done() {
	try {
	    this.get();
	} catch (final InterruptedException e) {
	    e.printStackTrace();
	    // TODO add to error queue and restart thread if messages in queue
	} catch (final ExecutionException e) {
	    e.printStackTrace();
	    // TODO add to error queue and restart thread if messages in queue
	}
	component.setBlocked(false);
    }
}
