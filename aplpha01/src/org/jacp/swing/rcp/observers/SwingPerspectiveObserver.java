/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jacp.swing.rcp.observers;

import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import org.jacp.api.action.IAction;
import org.jacp.api.base.IComponent;
import org.jacp.api.base.IPerspective;
import org.jacp.api.base.ISubComponent;
import org.jacp.api.base.IWorkbench;
import org.jacp.api.observers.IPerspectiveObserver;
import org.jacp.swing.rcp.action.SwingAction;

/**
 * Observe perspective actions and delegates message to correct component
 * 
 * @author Andy Moncsek
 */
public class SwingPerspectiveObserver extends ASwingObserver implements
		IPerspectiveObserver<Container, ActionListener, ActionEvent, Object> {

	private final List<IPerspective<Container, ActionListener, ActionEvent, Object>> perspectives = new CopyOnWriteArrayList<IPerspective<Container, ActionListener, ActionEvent, Object>>();
	private IWorkbench<?, Container, ActionListener, ActionEvent, Object> workbench;

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
			final IAction<ActionEvent, Object> action) {
		final IPerspective<Container, ActionListener, ActionEvent, Object> perspective = getObserveableById(
				getTargetPerspectiveId(target), perspectives);
		if (perspective != null) {
			final IAction<ActionEvent, Object> actionClone = getValidAction(
					action, target, action.getMessageList().get(target));
			handleComponentHit(target, actionClone, perspective);
		} else {
			// TODO implement missing perspective handling!!
			throw new UnsupportedOperationException(
					"No responsible perspective found. Handling not implemented yet.");
		}
	}

	/**
	 * handle message target hit
	 * 
	 * @param target
	 * @param action
	 */
	private void handleComponentHit(
			final String target,
			final IAction<ActionEvent, Object> action,
			final IPerspective<Container, ActionListener, ActionEvent, Object> perspective) {
		if (perspective.isActive()) {
			handleMessageToActivePerspective(target, action, perspective);
		} else {
			// perspective was not active and will be initialized
			log(" //1.1.1.1// perspective HIT handle IN-ACTIVE: "
					+ action.getTargetId());
			handleWorkspaceModeSpecific();
			handleInActive(perspective, action);
		}
	}

	/**
	 * handle message to active perspective; check if target is perspective or
	 * component
	 * 
	 * @param target
	 * @param action
	 * @param perspective
	 */
	private void handleMessageToActivePerspective(
			final String target,
			final IAction<ActionEvent, Object> action,
			final IPerspective<Container, ActionListener, ActionEvent, Object> perspective) {
		// if perspective already active handle perspective and replace
		// with newly created layout component in workbench
		log(" //1.1.1.1// perspective HIT handle ACTIVE: "
				+ action.getTargetId());
		if (isLocalMessage(target)) {
			// message is addressing perspective
			handleWorkspaceModeSpecific();
			handleActive(perspective, action);
		} else {
			// delegate to addressed component
			perspective.delegateComponentMassege(target, action);
		}
	}

	/**
	 * handle workspace mode specific actions. When working in single pain mode
	 * all components are deactivated and only one (the active) will be
	 * activated; when working in window mode all components must be visible
	 */
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
			final IWorkbench<?, Container, ActionListener, ActionEvent, Object> workbench) {
		this.workbench = workbench;
	}

	@Override
	public synchronized void delegateMessage(final String target,
			final IAction<ActionEvent, Object> action) {
		// Find local Target; if target is perspective handle target or delegate
		// message to responsible component observer
		if (isLocalMessage(target)) {
			handleMessage(target, action);
		} else {
			callComponentDelegate(target, action);
		}

	}

	@Override
	public synchronized void delegateTargetChange(
			final String target,
			final ISubComponent<Container, ActionListener, ActionEvent, Object> component) {
		// find responsible perspective
		final IPerspective<Container, ActionListener, ActionEvent, Object> responsiblePerspective = getObserveableById(
				getTargetPerspectiveId(target), perspectives);
		// find correct target in perspective
		if (responsiblePerspective != null) {
			handleTargetHit(responsiblePerspective, component);

		} else {
			handleTargetMiss();
		}

	}

	/**
	 * handle component delegate when target was found
	 * 
	 * @param responsiblePerspective
	 * @param component
	 */
	private void handleTargetHit(
			final IPerspective<Container, ActionListener, ActionEvent, Object> responsiblePerspective,
			final ISubComponent<Container, ActionListener, ActionEvent, Object> component) {
		if (!responsiblePerspective.isActive()) {
			// 1. init perspective (do not register component before perspective
			// is active, otherwise component will be handled once again)
			handleInActive(responsiblePerspective, new SwingAction(
					responsiblePerspective.getId(), responsiblePerspective
							.getId(), "init"));
		}
		addToActivePerspective(responsiblePerspective, component);
	}

	private void addToActivePerspective(
			final IPerspective<Container, ActionListener, ActionEvent, Object> responsiblePerspective,
			final ISubComponent<Container, ActionListener, ActionEvent, Object> component) {
		// register new component at perspective
		responsiblePerspective.registerComponent(component);
		// add component root to correct target
		responsiblePerspective
				.addComponentUIValue(responsiblePerspective
						.getIPerspectiveLayout().getTargetLayoutComponents(),
						component);
	}

	/**
	 *handle component delegate when no target found
	 */
	private void handleTargetMiss() {
		throw new UnsupportedOperationException(
				"No responsible perspective found. Handling not implemented yet.");
	}

	/**
	 * delegate to responsible componentObserver in correct perspective
	 * 
	 * @param target
	 * @param action
	 */
	private void callComponentDelegate(final String target,
			final IAction<ActionEvent, Object> action) {
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
			final M component, final IAction<ActionEvent, Object> action) {
		workbench
				.replacePerspective(
						(IPerspective<Container, ActionListener, ActionEvent, Object>) component,
						action);

	}

	@Override
	public <M extends IComponent<Container, ActionListener, ActionEvent, Object>> void handleInActive(
			final M component, final IAction<ActionEvent, Object> action) {
		component.setActive(true);
		workbench
				.initPerspective(
						(IPerspective<Container, ActionListener, ActionEvent, Object>) component,
						action);

	}

}
