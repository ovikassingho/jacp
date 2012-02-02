package org.jacp.swing.rcp.editor;

import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import org.jacp.api.action.IAction;
import org.jacp.api.action.IActionListener;
import org.jacp.api.base.IComponent;
import org.jacp.api.base.IPerspective;
import org.jacp.api.base.ISubComponent;
import org.jacp.api.observers.IObserver;
import org.jacp.swing.rcp.action.SwingAction;
import org.jacp.swing.rcp.action.SwingActionListener;

public class AStateComponent implements
ISubComponent<Object, ActionListener, ActionEvent, Object> {
	
	private String id;
	private String target;
	private String name;
	private Object root;
	private IObserver<Object, ActionListener, ActionEvent, Object> componentObserver;
	private volatile boolean active = false;

	@Override
	public IActionListener<ActionListener, ActionEvent, Object> getActionListener() {
		return null;
	}

	@Override
	public String getId() {
		if (id == null) {
			throw new UnsupportedOperationException("No id set");
		}
		return id;
	}

	@Override
	public String getName() {
		if (name == null) {
			throw new UnsupportedOperationException("No name set");
		}
		return name;
	}

	@Override
	public Object handle(IAction<ActionEvent, Object> action) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isActive() {
		return active;
	}

	@Override
	public void setActive(final boolean active) {
		this.active = active;
	}

	@Override
	public void setId(final String id) {
		this.id = id;
	}

	@Override
	public void setName(final String name) {
		this.name = name;
	}

	@Override
	public void setObserver(
			IObserver<Object, ActionListener, ActionEvent, Object> observer) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public IAction<ActionEvent, Object> getNextIncomingMessage() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IPerspective<Object, ActionListener, ActionEvent, Object> getParentPerspective() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object getRoot() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getTarget() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean hasIncomingMessage() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isBlocked() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void putIncomingMessage(IAction<ActionEvent, Object> action) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setBlocked(boolean blocked) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setParentPerspective(
			IPerspective<Object, ActionListener, ActionEvent, Object> perspective) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setRoot(Object root) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setTarget(String target) {
		// TODO Auto-generated method stub
		
	}

	

}
