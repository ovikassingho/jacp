package org.jacp.swing.rcp.component;

import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;

import org.jacp.api.action.IAction;
import org.jacp.api.action.IActionListener;
import org.jacp.api.component.IVComponent;
import org.jacp.api.coordinator.ICoordinator;
import org.jacp.api.perspective.IPerspective;
import org.jacp.swing.rcp.action.SwingAction;
import org.jacp.swing.rcp.action.SwingActionListener;
import org.springframework.jmx.export.annotation.ManagedAttribute;
import org.springframework.jmx.export.annotation.ManagedResource;

/**
 * represents a basic swing component to extend from
 * 
 * @author Andy Moncsek
 * 
 */
@ManagedResource(objectName = "org.jacp:name=ASwingComponent", description = "a state ful swing component")
public abstract class ASwingComponent implements
		IVComponent<Container, ActionListener, ActionEvent, Object> {

	private String id;
	private String target;
	private String name;
	private Container root;
	private boolean active;
	private volatile AtomicBoolean blocked = new AtomicBoolean(false);
	private ICoordinator<ActionListener, ActionEvent, Object> componentObserver;
	private IPerspective<ActionListener, ActionEvent, Object> parentPerspective;
	private final BlockingQueue<IAction<ActionEvent, Object>> incomingActions = new ArrayBlockingQueue<IAction<ActionEvent, Object>>(
			20);

	@Override
	public IActionListener<ActionListener, ActionEvent, Object> getActionListener() {
		return new SwingActionListener(new SwingAction(id), componentObserver);
	}

	@Override
	@ManagedAttribute
	public IPerspective<ActionListener, ActionEvent, Object> getParentPerspective() {
		return parentPerspective;
	}

	@Override
	public void setParentPerspective(
			final IPerspective<ActionListener, ActionEvent, Object> perspective) {
		parentPerspective = perspective;

	}

	@Override
	public void setObserver(
			final ICoordinator<ActionListener, ActionEvent, Object> componentObserver) {
		this.componentObserver = componentObserver;
	}

	@Override
	@ManagedAttribute
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
	@ManagedAttribute
	public String getExecutionTarget() {
		return target;
	}

	@Override
	public void setExecutionTarget(final String target) {
		this.target = target;
	}

	@Override
	@ManagedAttribute
	public Container getRoot() {
		return root;
	}

	@Override
	public void setRoot(final Container root) {
		this.root = root;
	}

	@Override
	@ManagedAttribute
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
	@ManagedAttribute
	public boolean isActive() {
		return active;
	}

	@Override
	public void setActive(final boolean active) {
		this.active = active;
	}

	@Override
	@ManagedAttribute
	public boolean isBlocked() {
		return blocked.get();
	}

	@Override
	public void setBlocked(final boolean blocked) {
		this.blocked.set(blocked);
	}

	@Override
	@ManagedAttribute
	public boolean hasIncomingMessage() {
		return !incomingActions.isEmpty();
	}

	@Override
	public void putIncomingMessage(final IAction<ActionEvent, Object> action) {
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
	public <C> C handle(final IAction<ActionEvent, Object> action) {
		return (C) handleAction(action);
	}
	
	/**
	 * handle component
	 * @param action
	 * @return java.awt.Container
	 */
	public abstract Container handleAction(IAction<ActionEvent, Object> action);

}
