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
import org.jacp.api.observers.IComponentObserver;

/**
 * observe component actions and delegates to correct component
 * 
 * @author Andy Moncsek
 * 
 */
public class SwingComponentObserver extends ASwingObserver implements
		IComponentObserver<Container, ActionListener, ActionEvent, Object> {

	private final List<ISubComponent<Container, ActionListener, ActionEvent, Object>> components = new CopyOnWriteArrayList<ISubComponent<Container, ActionListener, ActionEvent, Object>>();

	private IPerspective<Container, ActionListener, ActionEvent, Object> perspective;

	public SwingComponentObserver(
			final IPerspective<Container, ActionListener, ActionEvent, Object> perspective) {
		this.perspective = perspective;
	}

	@Override
	public void addComponent(
			final ISubComponent<Container, ActionListener, ActionEvent, Object> component) {
		component.setObserver(this);
		components.add(component);

	}

	@Override
	public void removeComponent(
			final ISubComponent<Container, ActionListener, ActionEvent, Object> component) {
		component.setObserver(null);
		components.remove(component);

	}

	@Override
	public void setParentPerspective(
			final IPerspective<Container, ActionListener, ActionEvent, Object> perspective) {
		this.perspective = perspective;

	}

	/**
	 * handle message to specific component
	 * 
	 * @param message
	 * @param action
	 */
	@Override
	public void handleMessage(final String targetId,
			final IAction<ActionEvent, Object> action) {
		final ISubComponent<Container, ActionListener, ActionEvent, Object> component = getObserveableById(
				getTargetComponentId(targetId), components);
		if (component != null) {
			handleComponentHit(targetId, action, component);
		} else {
			// delegate message to parent perspective
			handleComponentMiss(targetId, action);
		}

	}

	/**
	 * handle method if no valid component found; delegate to responsible
	 * perspective
	 * 
	 * @param targetId
	 * @param action
	 */
	private void handleComponentMiss(final String targetId,
			final IAction<ActionEvent, Object> action) {
		final boolean local = isLocalMessage(targetId);
		if (!local) {
			final String targetPerspectiveId = getTargetPerspectiveId(targetId);
			if (perspective.getId().equals(targetPerspectiveId)) {
				// TODO target is in same perspective but component was not
				// found previously
			} else {
				// delegate to parent perspective, then find responsible
				// perspective
				perspective.delegateMassege(targetId, action);
			}
		} else {
			// TODO handle invalid id
		}
	}

	/**
	 * handle method if component was found in local context
	 * 
	 * @param targetId
	 * @param action
	 * @param component
	 */
	private void handleComponentHit(
			final String targetId,
			final IAction<ActionEvent, Object> action,
			final ISubComponent<Container, ActionListener, ActionEvent, Object> component) {
		final IAction<ActionEvent, Object> actionClone = getValidAction(action,
				targetId, action.getMessageList().get(targetId));
		if (component.isActive()) {
			handleActive(component, actionClone);
		} else {
			handleInActive(component, actionClone);
		}
	}

	@Override
	public synchronized void delegateMessage(final String target,
			final IAction<ActionEvent, Object> action) {
		handleMessage(target, action);

	}

	@Override
	public synchronized <P extends IComponent<Container, ActionListener, ActionEvent, Object>> void handleActive(
			final P component, final IAction<ActionEvent, Object> action) {
		perspective
				.replaceSubcomponent(
						perspective.getIPerspectiveLayout(),
						(ISubComponent<Container, ActionListener, ActionEvent, Object>) component,
						action);

	}

	@Override
	public synchronized <P extends IComponent<Container, ActionListener, ActionEvent, Object>> void handleInActive(
			final P component, final IAction<ActionEvent, Object> action) {
		component.setActive(true);
		perspective
				.initSubcomonent(
						action,
						perspective.getIPerspectiveLayout(),
						(ISubComponent<Container, ActionListener, ActionEvent, Object>) component);

	}

	@Override
	public void delegateTargetChange(
			final String target,
			final ISubComponent<Container, ActionListener, ActionEvent, Object> component) {
		// TODO Auto-generated method stub

	}

}
