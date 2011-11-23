package org.jacp.javafx2.rcp.component;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;

import javafx.event.Event;
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
		IBGComponent<EventHandler<Event>, Event, Object> {

	private String id;
	private String target = "";
	private String name;
	private volatile String handleComponentTarget;
	private volatile boolean active;
	private boolean isActivated = false;
	private volatile AtomicBoolean blocked = new AtomicBoolean(false);
	private ICoordinator<EventHandler<Event>, Event, Object> componentObserver;
	private IPerspective<EventHandler<Event>, Event, Object> parentPerspective;
	private final BlockingQueue<IAction<Event, Object>> incomingActions = new ArrayBlockingQueue<IAction<Event, Object>>(
			500);

	@Override
	/**
	 * {@inheritDoc}
	 */
	public String getExecutionTarget() {
		return this.target;
	}

	@Override
	/**
	 * {@inheritDoc}
	 */
	public void setExecutionTarget(String target) {
		this.target = target;
	}

	@Override
	/**
	 * {@inheritDoc}
	 */
	public void setParentPerspective(
			IPerspective<EventHandler<Event>, Event, Object> perspective) {
		this.parentPerspective = perspective;

	}

	@Override
	/**
	 * {@inheritDoc}
	 */
	public IPerspective<EventHandler<Event>, Event, Object> getParentPerspective() {
		return this.parentPerspective;
	}

	@Override
	/**
	 * {@inheritDoc}
	 */
	public boolean hasIncomingMessage() {
		return !this.incomingActions.isEmpty();
	}

	@Override
	/**
	 * {@inheritDoc}
	 */
	public void putIncomingMessage(IAction<Event, Object> action) {
		try {
			this.incomingActions.put(action);
		} catch (final InterruptedException e) {
			e.printStackTrace();
		}
	}

	@Override
	/**
	 * {@inheritDoc}
	 */
	public final IAction<Event, Object> getNextIncomingMessage() {
		if (this.hasIncomingMessage()) {
			try {
				return this.incomingActions.take();
			} catch (final InterruptedException e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	@Override
	/**
	 * {@inheritDoc}
	 */
	public final boolean isBlocked() {
		return this.blocked.get();
	}

	@Override
	/**
	 * {@inheritDoc}
	 */
	public final void setBlocked(boolean blocked) {
		this.blocked.set(blocked);
	}

	@Override
	/**
	 * {@inheritDoc}
	 */
	public final IActionListener<EventHandler<Event>, Event, Object> getActionListener() {
		return new FX2ActionListener(new FX2Action(this.id),
				this.componentObserver);
	}

	@Override
	/**
	 * {@inheritDoc}
	 */
	public String getId() {
		if (this.id == null) {
			throw new UnsupportedOperationException("No id set");
		}
		return this.id;
	}

	@Override
	/**
	 * {@inheritDoc}
	 */
	public void setId(String id) {
		this.id = id;

	}

	@Override
	/**
	 * {@inheritDoc}
	 */
	public boolean isActive() {
		return this.active;
	}

	@Override
	/**
	 * {@inheritDoc}
	 */
	public void setActive(boolean active) {
		this.active = active;
	}

	@Override
	/**
	 * {@inheritDoc}
	 */
	public final void setStarted(boolean isActivated) {
		this.isActivated = isActivated;
	}

	@Override
	/**
	 * {@inheritDoc}
	 */
	public final boolean isStarted() {
		return this.isActivated;
	}

	@Override
	/**
	 * {@inheritDoc}
	 */
	public String getName() {
		if (this.name == null) {
			throw new UnsupportedOperationException("No name set");
		}
		return this.name;
	}

	@Override
	/**
	 * {@inheritDoc}
	 */
	public void setName(String name) {
		this.name = name;
	}

	@Override
	/**
	 * {@inheritDoc}
	 */
	public final void setObserver(
			ICoordinator<EventHandler<Event>, Event, Object> observer) {
		this.componentObserver = observer;

	}

	@SuppressWarnings("unchecked")
	@Override
	/**
	 * {@inheritDoc}
	 */
	public <C> C handle(final IAction<Event, Object> action) {
		return (C) this.handleAction(action);
	}

	public abstract Object handleAction(IAction<Event, Object> action);

	@Override
	/**
	 * {@inheritDoc}
	 */
	public final String getHandleTargetAndClear() {
		final String tempTarget = String.valueOf(this.handleComponentTarget);
		this.handleComponentTarget = null;
		return tempTarget;
	}

	@Override
	/**
	 * {@inheritDoc}
	 */
	public final void setHandleTarget(String componentTargetId) {
		this.handleComponentTarget = componentTargetId;
	}

}
