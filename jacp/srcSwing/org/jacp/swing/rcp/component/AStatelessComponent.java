package org.jacp.swing.rcp.component;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import org.jacp.api.action.IAction;
import org.jacp.api.action.IActionListener;
import org.jacp.api.component.IBGComponent;
import org.jacp.api.observers.IObserver;
import org.jacp.api.perspective.IPerspective;
import org.jacp.swing.rcp.action.SwingAction;
import org.jacp.swing.rcp.action.SwingActionListener;

/**
 * Represents a state less background component
 * @author Andy Moncsek
 *
 */
public abstract class AStatelessComponent implements
	IBGComponent<ActionListener, ActionEvent, Object>{

    private String id;
    private String target = "";
    private String name;
    private volatile String handleComponentTarget;
    private volatile boolean active=true;
    private volatile boolean blocked=false;
    private IObserver<ActionListener, ActionEvent, Object> componentObserver;
    private IPerspective<ActionListener, ActionEvent, Object> parentPerspective;
    private BlockingQueue<IAction<ActionEvent, Object>> incomingActions = new ArrayBlockingQueue<IAction<ActionEvent, Object>>(
	    20);
    
     

    @Override
    public String getExecutionTarget() {
	return this.target;
    }

    @Override
    public void setExecutionTarget(String target) {
	this.target = target;
    }

    @Override
    public void setParentPerspective(
	    IPerspective<ActionListener, ActionEvent, Object> perspective) {
	this.parentPerspective = perspective;
    }

    @Override
    public IPerspective<ActionListener, ActionEvent, Object> getParentPerspective() {
	return this.parentPerspective;
    }

    @Override
    public boolean hasIncomingMessage() {
	return !incomingActions.isEmpty();
    }

    @Override
    public void putIncomingMessage(IAction<ActionEvent, Object> action) {
	try {
		incomingActions.put(action);
	} catch (final InterruptedException e) {
		e.printStackTrace();
	}

    }

    @Override
    public IAction<ActionEvent, Object> getNextIncomingMessage() {
	if (hasIncomingMessage()) {
		try {
			return incomingActions.take();
		} catch (final InterruptedException e) {
			e.printStackTrace();
		}
	}
	return null;
    }

    @Override
    public boolean isBlocked() {
	return this.blocked;
    }

    @Override
    public void setBlocked(boolean blocked) {
	this.blocked = blocked;
    }

    @Override
    public IActionListener<ActionListener, ActionEvent, Object> getActionListener() {
	return new SwingActionListener(new SwingAction(id), componentObserver);
    }

    @Override
    public String getId() {
	if (id == null) {
		throw new UnsupportedOperationException("No id set");
	}
	return id;
    }

    @Override
    public void setId(String id) {
	this.id = id;
    }

    @Override
    public boolean isActive() {
	return this.active;
    }

    @Override
    public void setActive(boolean active) {
	this.active = active;
    }

    @Override
    public String getName() {
	if (name == null) {
		throw new UnsupportedOperationException("No name set");
	}
	return name;
    }

    @Override
    public void setName(String name) {
	this.name = name;
    }

    @Override
    public void setObserver(
	    IObserver<ActionListener, ActionEvent, Object> observer) {
	componentObserver = observer;
    }

    @Override
    public String getHandleTarget() {
	return this.handleComponentTarget;
    }

    @Override
    public void setHandleTarget(String componentTargetId) {
	this.handleComponentTarget = componentTargetId;
    }
    
    public IBGComponent<ActionListener, ActionEvent, Object> getNewInstance() {
	return (IBGComponent<ActionListener, ActionEvent, Object>) this.clone();
    }
    

    @Override
    protected Object clone() {
	try {
	    AStatelessComponent comp = (AStatelessComponent) super.clone();
	    comp.setId(this.id);
	    comp.setActive(this.active);
	    comp.setName(this.name);
	    comp.setExecutionTarget(this.target);
	    comp.setHandleTarget(this.handleComponentTarget);
	    comp.setObserver(this.componentObserver);
	    comp.setParentPerspective(this.parentPerspective);
	    comp.setIncomingMessages(new ArrayBlockingQueue<IAction<ActionEvent, Object>>(
		    20));
	    
	    return comp;
	} catch (CloneNotSupportedException e) {
	    e.printStackTrace();
	}
	
	return null;
    }
    
    private void setIncomingMessages(final BlockingQueue<IAction<ActionEvent, Object>> incomingActions) {
	this.incomingActions = incomingActions;
    }

    @Override
    public <C> C handle(final IAction<ActionEvent, Object> action) {
	return (C) handleAction(action);
    }

    public abstract Object handleAction(IAction<ActionEvent, Object> action);

}
