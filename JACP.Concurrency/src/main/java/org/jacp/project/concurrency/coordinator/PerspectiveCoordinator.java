/*
 * Copyright (C) 2010,2011.
 * AHCP Project
 * All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at 
 *
 *     http://www.apache.org/licenses/LICENSE-2.0 
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an "AS IS"
 * BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either 
 * express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */
package org.jacp.project.concurrency.coordinator;



import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import org.jacp.api.action.IAction;
import org.jacp.api.component.IComponent;
import org.jacp.api.component.ISubComponent;
import org.jacp.api.coordinator.IPerspectiveCoordinator;
import org.jacp.api.perspective.IPerspective;
import org.jacp.api.workbench.IBase;
import org.jacp.project.concurrency.action.ActionListener;
import org.jacp.project.concurrency.action.Event;


/**
 * 
 * @author Andy Moncsek
 * 
 */
public class PerspectiveCoordinator extends ACoordinator implements
		IPerspectiveCoordinator<ActionListener, Event, Object> {
	private final List<IPerspective<ActionListener, Event, Object>> perspectives = new CopyOnWriteArrayList<IPerspective<ActionListener, Event, Object>>();
	private final IBase<ActionListener, Event, Object> base;

	public PerspectiveCoordinator(
			final IBase<ActionListener, Event, Object> base) {
		this.base = base;
	}

	@Override
	public void handleMessage(String id, IAction<Event, Object> action) {
		final IPerspective<ActionListener, Event, Object> perspective = getObserveableById(
				getTargetPerspectiveId(id), perspectives);
			if (perspective != null) {
			    final IAction<Event, Object> actionClone = getValidAction(
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
			IAction<Event, Object> action) {
		// Find local Target; if target is perspective handle target or
		// delegate
		// message to responsible component observer
		if (isLocalMessage(target)) {
		    handleMessage(target, action);
		} else {
		    callComponentDelegate(target, action);
		}


	}

	@Override
	public <P extends IComponent<ActionListener, Event, Object>> void handleActive(
			P component, IAction<Event, Object> action) {
//		workbench.handleAndReplaceComponent(action,
//				(IPerspective<ActionListener, JACPEvent, Object>) component);

	}

	@Override
	public <P extends IComponent<ActionListener, Event, Object>> void handleInActive(
			P component, IAction<Event, Object> action) {
		// TODO Auto-generated method stub

	}

	@Override
	public void delegateTargetChange(String target,
			ISubComponent<ActionListener, Event, Object> component) {
		// find responsible perspective
		final IPerspective<ActionListener, Event, Object> responsiblePerspective = getObserveableById(
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
	    final IPerspective<ActionListener, Event, Object> responsiblePerspective,
	    final ISubComponent<ActionListener, Event, Object> component) {
	if (!responsiblePerspective.isActive()) {
	    // 1. init perspective (do not register component before perspective
	    // is active, otherwise component will be handled once again)
	  //REMOVE  handleInActive(responsiblePerspective,
//	REMOVE	    new SwingAction(responsiblePerspective.getId(),
//		REMOVE	    responsiblePerspective.getId(), "init"));
	}
	addToActivePerspective(responsiblePerspective, component);
    }

	/**
	 * handle message target hit
	 * 
	 * @param target
	 * @param action
	 */
	private void handleComponentHit(final String target,
			final IAction<Event, Object> action,
			final IPerspective<ActionListener, Event, Object> perspective) {
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
			final IAction<Event, Object> action,
			final IPerspective<ActionListener, Event, Object> perspective) {
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
			IPerspective<ActionListener, Event, Object> perspective) {
		perspective.setObserver(this);
		perspectives.add(perspective);

	}

	@Override
	public void removePerspective(
			IPerspective<ActionListener, Event, Object> perspective) {
		perspective.setObserver(null);
		perspectives.remove(perspective);

	}
	
	/**
     * handle component delegate when no target found
     */
    private void handleTargetMiss() {
	throw new UnsupportedOperationException(
		"No responsible perspective found. Handling not implemented yet.");
    }
    
	/**
	 * add active component to perspective
	 * 
	 * @param responsiblePerspective
	 * @param component
	 */
	private void addToActivePerspective(
			final IPerspective<ActionListener, Event, Object> responsiblePerspective,
			final ISubComponent<ActionListener, Event, Object> component) {
		responsiblePerspective.addActiveComponent(component);
	}
	
	/**
	 * delegate to responsible componentObserver in correct perspective
	 * 
	 * @param target
	 * @param action
	 */
	private void callComponentDelegate(final String target,
			final IAction<Event, Object> action) {
		final IPerspective<ActionListener, Event, Object> perspective = getObserveableById(
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

}
