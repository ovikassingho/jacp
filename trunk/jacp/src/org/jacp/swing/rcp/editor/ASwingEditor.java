package org.jacp.swing.rcp.editor;

import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import org.jacp.api.action.IActionListener;
import org.jacp.api.base.IEditor;
import org.jacp.api.base.IPerspective;
import org.jacp.api.observers.IObserver;
import org.jacp.swing.rcp.action.SwingAction;
import org.jacp.swing.rcp.action.SwingActionListener;

public abstract class ASwingEditor implements
		IEditor<Container, ActionListener, ActionEvent, Object> {

	private IPerspective<Container, ActionListener, ActionEvent, Object> parentPerspective;
	private String id;
	private String target;
	private String name;
	private boolean active = false;
	private IObserver<Container, ActionListener, ActionEvent, Object> componentObserver;
	private Container root;
	private volatile boolean blocked;

	@Override
	public IActionListener<ActionListener, ActionEvent, Object> getActionListener() {
		return new SwingActionListener(new SwingAction(id), componentObserver);
	}

	@Override
	public IPerspective<Container, ActionListener, ActionEvent, Object> getParentPerspective() {
		return parentPerspective;
	}

	@Override
	public void setParentPerspective(
			final IPerspective<Container, ActionListener, ActionEvent, Object> perspective) {
		parentPerspective = perspective;

	}

	@Override
	public void setObserver(
			final IObserver<Container, ActionListener, ActionEvent, Object> componentObserver) {
		this.componentObserver = componentObserver;
	}

	@Override
	public String getId() {
		if (id == null) {
			throw new UnsupportedOperationException("No id set");
		}
		return id;
	}

	@Override
	public void setId(final String id) {
		this.id = id;
	}

	@Override
	public String getTarget() {
		return target;
	}

	@Override
	public void setTarget(final String target) {
		this.target = target;
	}

	@Override
	public Container getRoot() {
		return root;
	}

	@Override
	public void setRoot(final Container root) {
		this.root = root;
	}

	@Override
	public String getName() {
		if (name == null) {
			throw new UnsupportedOperationException("No name set");
		}
		return name;
	}

	@Override
	public void setName(final String name) {
		this.name = name;
	}

	@Override
	public boolean isActive() {
		return active;
	}

	@Override
	public void setActive(final boolean active) {
		this.active = active;
	}

	public boolean isBlocked() {
		return blocked;
	}

	public void setBlocked(final boolean blocked) {
		this.blocked = blocked;
	}

}
