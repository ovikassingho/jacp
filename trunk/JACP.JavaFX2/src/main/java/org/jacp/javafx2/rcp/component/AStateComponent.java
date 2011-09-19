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

/**
 * represents a basic, stateful background component
 * 
 * @author Andy Moncsek
 * 
 */
public class AStateComponent implements
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
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void putIncomingMessage(IAction<ActionEvent, Object> action) {
		// TODO Auto-generated method stub

	}

	@Override
	public IAction<ActionEvent, Object> getNextIncomingMessage() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isBlocked() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void setBlocked(boolean blocked) {
		// TODO Auto-generated method stub

	}

	@Override
	public IActionListener<EventHandler<ActionEvent>, ActionEvent, Object> getActionListener() {
		// TODO Auto-generated method stub
		return null;
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
		// TODO Auto-generated method stub

	}

	@Override
	public <C> C handle(IAction<ActionEvent, Object> action) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getHandleTargetAndClear() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setHandleTarget(String componentTargetId) {
		// TODO Auto-generated method stub

	}

}
