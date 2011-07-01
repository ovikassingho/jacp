package org.jacp.project.concurrency.coordinator;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import org.jacp.api.action.IAction;
import org.jacp.api.component.IComponent;
import org.jacp.api.component.ISubComponent;
import org.jacp.api.coordinator.IPerspectiveCoordinator;
import org.jacp.api.perspective.IPerspective;
import org.jacp.api.workbench.IBase;


/**
 * 
 * @author Andy Moncsek
 * 
 */
public class PerspectiveCoordinator extends ACoordinator implements
		IPerspectiveCoordinator<ActionListener, ActionEvent, Object> {
	private final List<IPerspective<ActionListener, ActionEvent, Object>> perspectives = new CopyOnWriteArrayList<IPerspective<ActionListener, ActionEvent, Object>>();
	private final IBase<ActionListener, ActionEvent, Object> base;

	public PerspectiveCoordinator(
			final IBase<ActionListener, ActionEvent, Object> base) {
		this.base = base;
	}

	@Override
	public void handleMessage(String id, IAction<ActionEvent, Object> action) {
		final IPerspective<ActionListener, ActionEvent, Object> perspective = getObserveableById(
				getTargetPerspectiveId(id), perspectives);
			if (perspective != null) {
			    final IAction<ActionEvent, Object> actionClone = getValidAction(
				    action, id, action.getMessageList().get(id));
			    handleComponentHit(id, actionClone, perspective);
			} else {
			    // TODO implement missing perspective handling!!
			    throw new UnsupportedOperationException(
				    "No responsible perspective found. Handling not implemented yet.");
			}
	}

	@Override
	public void delegateMessage(String target,
			IAction<ActionEvent, Object> action) {
		// Find local Target; if target is perspective handle target or
		// delegate
		// message to responsible component observer
		if (isLocalMessage(target)) {
		    handleMessage(target, action);
		} else {
		 //   callComponentDelegate(target, action);
		}


	}

	@Override
	public <P extends IComponent<ActionListener, ActionEvent, Object>> void handleActive(
			P component, IAction<ActionEvent, Object> action) {
		// TODO Auto-generated method stub

	}

	@Override
	public <P extends IComponent<ActionListener, ActionEvent, Object>> void handleInActive(
			P component, IAction<ActionEvent, Object> action) {
		// TODO Auto-generated method stub

	}

	@Override
	public void delegateTargetChange(String target,
			ISubComponent<ActionListener, ActionEvent, Object> component) {
		// find responsible perspective
		final IPerspective<ActionListener, ActionEvent, Object> responsiblePerspective = getObserveableById(
			getTargetPerspectiveId(target), perspectives);
		// find correct target in perspective
		if (responsiblePerspective != null) {
		    handleTargetHit(responsiblePerspective, component);

		} else {
		 //REMOVE   handleTargetMiss();
		}

	}
	
	 /**
     * handle component delegate when target was found
     * 
     * @param responsiblePerspective
     * @param component
     */
    private void handleTargetHit(
	    final IPerspective<ActionListener, ActionEvent, Object> responsiblePerspective,
	    final ISubComponent<ActionListener, ActionEvent, Object> component) {
	if (!responsiblePerspective.isActive()) {
	    // 1. init perspective (do not register component before perspective
	    // is active, otherwise component will be handled once again)
	  //REMOVE  handleInActive(responsiblePerspective,
//	REMOVE	    new SwingAction(responsiblePerspective.getId(),
//		REMOVE	    responsiblePerspective.getId(), "init"));
	}
// REMOVE	addToActivePerspective(responsiblePerspective, component);
    }

	/**
	 * handle message target hit
	 * 
	 * @param target
	 * @param action
	 */
	private void handleComponentHit(final String target,
			final IAction<ActionEvent, Object> action,
			final IPerspective<ActionListener, ActionEvent, Object> perspective) {
		if (perspective.isActive()) {
			handleMessageToActivePerspective(target, action, perspective);
		} else {
			// perspective was not active and will be initialized
			log(" //1.1.1.1// perspective HIT handle IN-ACTIVE: "
					+ action.getTargetId());
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
	private void handleMessageToActivePerspective(final String target,
			final IAction<ActionEvent, Object> action,
			final IPerspective<ActionListener, ActionEvent, Object> perspective) {
		// if perspective already active handle perspective and replace
		// with newly created layout component in workbench
		log(" //1.1.1.1// perspective HIT handle ACTIVE: "
				+ action.getTargetId());
		if (isLocalMessage(target)) {
			// message is addressing perspective
			handleActive(perspective, action);
		} else {
			// delegate to addressed component
			perspective.delegateComponentMassege(target, action);
		}
	}

	@Override
	public void addPerspective(
			IPerspective<ActionListener, ActionEvent, Object> perspective) {
		perspective.setObserver(this);
		perspectives.add(perspective);

	}

	@Override
	public void removePerspective(
			IPerspective<ActionListener, ActionEvent, Object> perspective) {
		perspective.setObserver(null);
		perspectives.remove(perspective);

	}

}
