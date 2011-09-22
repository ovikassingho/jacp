package org.jacp.javafx2.rcp.component;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;

import org.jacp.api.action.IAction;
import org.jacp.api.action.IActionListener;
import org.jacp.api.component.IBGComponent;
import org.jacp.api.coordinator.ICoordinator;
import org.jacp.api.perspective.IPerspective;
import org.jacp.javafx2.rcp.action.FX2Action;
import org.jacp.javafx2.rcp.action.FX2ActionListener;

/**
 * represents a basic, stateful background component
 * 
 * @author Andy Moncsek
 * 
 */
public abstract class AStateComponent implements
		IBGComponent<EventHandler<ActionEvent>, ActionEvent, Object> {

	private String id;
	private String target = "";
	private String name;
	private volatile String handleComponentTarget;
	private volatile boolean active;
	private boolean isActivated = false;
	private volatile AtomicBoolean blocked = new AtomicBoolean(false);
	private ICoordinator<EventHandler<ActionEvent>, ActionEvent, Object> componentObserver;
	private IPerspective<EventHandler<ActionEvent>, ActionEvent, Object> parentPerspective;
	private final BlockingQueue<IAction<ActionEvent, Object>> incomingActions = new ArrayBlockingQueue<IAction<ActionEvent, Object>>(
			500);

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
			IPerspective<EventHandler<ActionEvent>, ActionEvent, Object> perspective) {
		this.parentPerspective = perspective;

	}

	@Override
	public IPerspective<EventHandler<ActionEvent>, ActionEvent, Object> getParentPerspective() {
		return this.parentPerspective;
	}

	@Override
	public boolean hasIncomingMessage() {
		return !this.incomingActions.isEmpty();
	}

	@Override
	public void putIncomingMessage(IAction<ActionEvent, Object> action) {
		try {
			this.incomingActions.put(action);
		} catch (final InterruptedException e) {
			e.printStackTrace();
		}
	}

	@Override
	public IAction<ActionEvent, Object> getNextIncomingMessage() {
		if (hasIncomingMessage()) {
			try {
				return this.incomingActions.take();
			} catch (final InterruptedException e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	@Override
	public boolean isBlocked() {
		return this.blocked.get();
	}

	@Override
	public void setBlocked(boolean blocked) {
		this.blocked.set(blocked);
	}

	@Override
	public IActionListener<EventHandler<ActionEvent>, ActionEvent, Object> getActionListener() {
		return new FX2ActionListener(new FX2Action(this.id),
				this.componentObserver);
	}

	@Override
	public String getId() {
		if (this.id == null) {
			throw new UnsupportedOperationException("No id set");
		}
		return this.id;
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
	public void setActivated(boolean isActivated) {
		this.isActivated = isActivated;
	}

	@Override
	public boolean isActivated() {
		return this.isActivated;
	}

	@Override
	public String getName() {
		if (this.name == null) {
			throw new UnsupportedOperationException("No name set");
		}
		return this.name;
	}

	@Override
	public void setName(String name) {
		this.name = name;
	}

	@Override
	public void setObserver(
			ICoordinator<EventHandler<ActionEvent>, ActionEvent, Object> observer) {
		this.componentObserver = observer;

	}

	@SuppressWarnings("unchecked")
	@Override
	public <C> C handle(final IAction<ActionEvent, Object> action) {
		return (C) handleAction(action);
	}

	public abstract Object handleAction(IAction<ActionEvent, Object> action);

	@Override
	public String getHandleTargetAndClear() {
		final String tempTarget = String.valueOf(this.handleComponentTarget);
		this.handleComponentTarget = null;
		return tempTarget;
	}

	@Override
	public void setHandleTarget(String componentTargetId) {
		this.handleComponentTarget = componentTargetId;
	}

}
