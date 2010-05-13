/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jacp.swing.rcp.observers;

import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import org.jacp.api.action.IAction;
import org.jacp.api.base.IComponent;
import org.jacp.api.base.IPerspective;
import org.jacp.api.base.IWorkbench;
import org.jacp.api.observers.IPerspectiveObserver;

/**
 * Observe perspective actions and delegates message to correct component
 * 
 * @author Andy Moncsek
 */
public class SwingPerspectiveObserver extends ASwingObserver implements
		IPerspectiveObserver<Container, ActionListener, ActionEvent, Object> {

	private final List<IPerspective<Container, ActionListener, ActionEvent, Object>> perspectives = new ArrayList<IPerspective<Container, ActionListener, ActionEvent, Object>>();
	private IWorkbench<?, Container,  ActionListener, ActionEvent, Object> workbench;

	public SwingPerspectiveObserver(
			final IWorkbench<?, Container, ActionListener, ActionEvent, Object> workbench) {
		this.workbench = workbench;
	}

	@Override
	public void addPerspective(
			final IPerspective<Container, ActionListener, ActionEvent, Object> perspective) {
		perspective.setObserver(this);
		perspectives.add(perspective);
	}

	@Override
	public void removePerspective(
			final IPerspective<Container, ActionListener, ActionEvent, Object> perspective) {
		perspective.setObserver(null);
		perspectives.remove(perspective);
	}

	/**
	 * handles message to perspective be perspective or subcomponent !! watch
	 * out
	 * 
	 * @param message
	 * @param action
	 */
	@Override
	public void handleMessage(final String target,
			final IAction<ActionEvent,Object> action) {
		final IPerspective<Container, ActionListener, ActionEvent, Object> perspective = getObserveableById(
				target, perspectives);
		if (perspective != null) {
			handleWorkspaceModeSpecific();
			final IAction<ActionEvent,Object> actionClone = getValidAction(
					action, target, action.getMessageList().get(target));
			if (perspective.isActive()) {
				// if perspective already active handle perspective and replace
				// with newly created layout component in workbench
				handleActive(perspective, actionClone);

			} else {
				// perspective was not active and will be initialized
				handleInActive(perspective, actionClone);

			}
		}
	}

	private void handleWorkspaceModeSpecific() {
		switch (workbench.getWorkbenchLayout().getWorkspaceMode()) {
		case WINDOWED_PAIN:
			workbench.enableComponents();
			break;
		default:
			workbench.disableComponents();
			;
		}
	}

	@Override
	public IWorkbench<?, Container, ActionListener, ActionEvent, Object> getPerentWorkbench() {
		return workbench;
	}

	@Override
	public void setParentWorkbench(
			final IWorkbench<?,Container, ActionListener, ActionEvent, Object> workbench) {
		this.workbench = workbench;
	}

	@Override
	public synchronized void delegateMessage(final String target,
			final IAction<ActionEvent,Object> action) {
		// Find local Target; if target is perspective handle target or delegate
		// message to responsible component observer
		if (isLocalMessage(target)) {
			handleMessage(target, action);
		} else {
			callComponentDelegate(target, action);
		}

	}

	/**
	 * delegate to responsible componentObserver in correct perspective
	 * 
	 * @param target
	 * @param action
	 */
	private void callComponentDelegate(final String target,
			final IAction<ActionEvent,Object> action) {
		final IPerspective<Container, ActionListener, ActionEvent, Object> perspective = getObserveableById(
				getTargetPerspectiveId(target), perspectives);
		// TODO REMOVE null handling... use DUMMY instead (maybe like
		// Collections.EMPTY...)
		if (perspective != null) {
			if (!perspective.isActive()) {
				handleInActive(perspective, action);
			} else {
				perspective.delegateComponentMassege(target, action);
			}

		}

	}

	@Override
	public <M extends IComponent<Container, ActionListener, ActionEvent, Object>> void handleActive(
			final M component, final IAction<ActionEvent,Object> action) {
		workbench
				.replacePerspective(
						(IPerspective<Container, ActionListener, ActionEvent, Object>) component,
						action);

	}

	@Override
	public <M extends IComponent<Container, ActionListener, ActionEvent, Object>> void handleInActive(
			final M component, final IAction<ActionEvent,Object> action) {
		component.setActive(true);
		workbench
				.initPerspective(
						(IPerspective<Container, ActionListener, ActionEvent, Object>) component,
						action);

	}

}
