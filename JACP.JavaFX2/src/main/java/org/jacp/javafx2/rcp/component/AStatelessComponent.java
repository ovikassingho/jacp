package org.jacp.javafx2.rcp.component;


import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;

import org.jacp.api.action.IAction;
import org.jacp.api.action.IActionListener;
import org.jacp.api.component.IStateLessBGComponent;
import org.jacp.api.coordinator.ICoordinator;
import org.jacp.api.coordinator.IStatelessComponentCoordinator;
import org.jacp.api.launcher.Launcher;
import org.jacp.api.perspective.IPerspective;

public class AStatelessComponent  implements
IStateLessBGComponent<EventHandler<ActionEvent>, ActionEvent, Object>{
    private String id;
    private String target = "";
    private String name;
    private volatile String handleComponentTarget;
    private volatile boolean active = true;
    private boolean isActived = false;
    private volatile AtomicBoolean blocked = new AtomicBoolean(false);
    private ICoordinator<EventHandler<ActionEvent>, ActionEvent, Object> componentObserver;
    private IPerspective<EventHandler<ActionEvent>, ActionEvent, Object> parentPerspective;
    private final BlockingQueue<IAction<ActionEvent, Object>> incomingActions = new ArrayBlockingQueue<IAction<ActionEvent, Object>>(
	    500);
    private IStatelessComponentCoordinator<EventHandler<ActionEvent>, ActionEvent, Object> coordinator;
    private Launcher<?> launcher;
	@Override
	public String getHandleTargetAndClear() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setHandleTarget(String componentTargetId) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getExecutionTarget() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setExecutionTarget(String target) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setParentPerspective(
			IPerspective<EventHandler<ActionEvent>, ActionEvent, Object> perspective) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public IPerspective<EventHandler<ActionEvent>, ActionEvent, Object> getParentPerspective() {
		// TODO Auto-generated method stub
		return null;
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
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setId(String id) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean isActive() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void setActive(boolean active) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setActivated(boolean isActive) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean isActivated() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setName(String name) {
		// TODO Auto-generated method stub
		
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
	public void setLauncher(Launcher<?> launcher) {
		// TODO Auto-generated method stub
		
	}

}
