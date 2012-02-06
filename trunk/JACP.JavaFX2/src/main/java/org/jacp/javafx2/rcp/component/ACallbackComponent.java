package org.jacp.javafx2.rcp.component;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;

import javafx.event.Event;
import javafx.event.EventHandler;

import org.jacp.api.action.IAction;
import org.jacp.api.action.IActionListener;
import org.jacp.api.component.ICallbackComponent;
import org.jacp.javafx2.rcp.action.FX2Action;
import org.jacp.javafx2.rcp.action.FX2ActionListener;

/**
 * represents a basic, stateful background component
 * 
 * @author Andy Moncsek
 * 
 */
public abstract class ACallbackComponent implements
		ICallbackComponent<EventHandler<Event>, Event, Object> {

	private String id;
	private String target = "";
	private String name;
	private String parentId;
	private volatile String handleComponentTarget;
	private volatile boolean active;
	private boolean isActivated = false;
	private volatile AtomicBoolean blocked = new AtomicBoolean(false);
	private BlockingQueue<IAction<Event, Object>> globalMessageQueue;
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

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final IActionListener<EventHandler<Event>, Event, Object> getActionListener(
			Object message) {
		final FX2Action action = new FX2Action(this.id);
		action.setMessage(message);
		return new FX2ActionListener(action, this.globalMessageQueue);
	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	public final IActionListener<EventHandler<Event>, Event, Object> getActionListener(
			String targetId, Object message) {
		final FX2Action action = new FX2Action(this.id);
		action.addMessage(targetId, message);
		return new FX2ActionListener(action, this.globalMessageQueue);
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
	public String getParentId() {
		return parentId;
	}

	@Override
	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final void setMessageQueue(BlockingQueue<IAction<Event, Object>> messageQueue){
		this.globalMessageQueue = messageQueue;
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
