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
package org.jacp.javafx2.rcp.coordinator;

import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.logging.Logger;

import javafx.application.Platform;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.Node;

import org.jacp.api.action.IAction;
import org.jacp.api.component.IComponent;
import org.jacp.api.component.IDelegateDTO;
import org.jacp.api.component.ISubComponent;
import org.jacp.api.coordinator.IComponentDelegator;
import org.jacp.api.perspective.IPerspective;
import org.jacp.api.workbench.IWorkbench;
import org.jacp.javafx2.rcp.action.FX2Action;
import org.jacp.javafx2.rcp.workbench.AFX2Workbench;

/**
 * The perspective coordinator provides messages to perspective, this messages
 * can also have a component as target but must pass the correct perspective.
 * 
 * @author Andy Moncsek
 * 
 */
public class FX2PerspectiveDelegator extends Thread implements
		IComponentDelegator<EventHandler<Event>, Event, Object> {
	private final Logger logger = Logger.getLogger(this.getClass().getName());
	private final IWorkbench<Node, EventHandler<Event>, Event, Object> workbench;
	private final List<IPerspective<EventHandler<Event>, Event, Object>> perspectives = new CopyOnWriteArrayList<IPerspective<EventHandler<Event>, Event, Object>>();
	private volatile BlockingQueue<IDelegateDTO<EventHandler<Event>, Event, Object>> dtos = new ArrayBlockingQueue<IDelegateDTO<EventHandler<Event>, Event, Object>>(
			100);

	public FX2PerspectiveDelegator(
			final IWorkbench<Node, EventHandler<Event>, Event, Object> workbench) {
		this.workbench = workbench;
	}

	@Override
	public final void run() {
		while (!Thread.interrupted()) {
			IDelegateDTO<EventHandler<Event>, Event, Object> dto = null;
			try {
				dto = this.dtos.take();
				final String targetId = dto.getTarget();
				final ISubComponent<EventHandler<Event>, Event, Object> component = dto
						.getComponent();
				final IAction<Event, Object> action = dto.getAction();
				if (component != null) {
					delegateTargetChange(targetId, component);
				} else {
					delegateMessage(targetId, action);
				}
			} catch (final InterruptedException e) {
				e.printStackTrace();
			}

		}
	}

	public void delegateTargetChange(final String target,
			final ISubComponent<EventHandler<Event>, Event, Object> component) {
		// find responsible perspective
		final IPerspective<EventHandler<Event>, Event, Object> responsiblePerspective = this
				.getObserveableById(this.getTargetPerspectiveId(target),
						this.perspectives);
		// find correct target in perspective
		if (responsiblePerspective != null) {
			this.handleTargetHit(responsiblePerspective, component);

		} // End if
		else {
			this.handleTargetMiss();
		} // End else
	}

	/**
	 * handle component delegate when target was found
	 * 
	 * @param responsiblePerspective
	 * @param component
	 */
	private void handleTargetHit(
			final IPerspective<EventHandler<Event>, Event, Object> responsiblePerspective,
			final ISubComponent<EventHandler<Event>, Event, Object> component) {
		if (!responsiblePerspective.isActive()) {
			// 1. init perspective (do not register component before perspective
			// is active, otherwise component will be handled once again)
			this.handleInActive(responsiblePerspective,
					new FX2Action(responsiblePerspective.getId(),
							responsiblePerspective.getId(), "init"));
		} // End if
		responsiblePerspective.registerComponent(component);
		responsiblePerspective.initComponent(new FX2Action(component.getId(),
				component.getId(), "init"), component);
	}

	/**
	 * handle component delegate when no target found
	 */
	private void handleTargetMiss() {
		throw new UnsupportedOperationException(
				"No responsible perspective found. Handling not implemented yet.");
	}

	public void delegateMessage(final String target,
			final IAction<Event, Object> action) {
		// Find local Target; if target is perspective handle target or
		// delegate
		// message to responsible component observer
		if (this.isLocalMessage(target)) {
			this.handleMessage(target, action);
		} // End if
		else {
			this.callComponentDelegate(target, action);
		} // End else
	}

	/**
	 * delegate to responsible componentObserver in correct perspective
	 * 
	 * @param target
	 * @param action
	 */
	private void callComponentDelegate(final String target,
			final IAction<Event, Object> action) {
		final IPerspective<EventHandler<Event>, Event, Object> perspective = this
				.getObserveableById(this.getTargetPerspectiveId(target),
						this.perspectives);
		if (perspective != null) {
			if (!perspective.isActive()) {
				this.handleInActive(perspective, action);
			} // End inner if
			else {
				perspective.delegateComponentMassege(target, action);
			} // End else

		} // End if

	}

	/**
	 * when id has no separator it is a local message // TODO remove code
	 * duplication
	 * 
	 * @param messageId
	 * @return
	 */
	protected final boolean isLocalMessage(final String messageId) {
		return !messageId.contains(".");
	}

	/**
	 * returns target message with perspective and component name // TODO remove
	 * code duplication
	 * 
	 * @param messageId
	 * @return
	 */
	protected final String[] getTargetId(final String messageId) {
		return messageId.split("\\.");
	}

	/**
	 * returns the message target perspective id
	 * 
	 * @param messageId
	 * @return
	 */
	protected final String getTargetPerspectiveId(final String messageId) {
		final String[] targetId = this.getTargetId(messageId);
		if (!this.isLocalMessage(messageId)) {
			return targetId[0];
		}
		return messageId;
	}

	public void handleMessage(final String target,
			final IAction<Event, Object> action) {
		final IPerspective<EventHandler<Event>, Event, Object> perspective = this
				.getObserveableById(this.getTargetPerspectiveId(target),
						this.perspectives);
		if (perspective != null) {
			final IAction<Event, Object> actionClone = this.getValidAction(
					action, target, action.getMessageList().get(target));
			this.handleComponentHit(target, actionClone, perspective);
		} // End if
		else {
			// TODO implement missing perspective handling!!
			throw new UnsupportedOperationException(
					"No responsible perspective found. Handling not implemented yet. target: "
							+ target + " perspectives: " + perspectives);
		} // End else
	}

	protected final IAction<Event, Object> getValidAction(
			final IAction<Event, Object> action, final String target,
			final Object message) {
		final IAction<Event, Object> actionClone = action.clone();
		actionClone.addMessage(target, message);
		return actionClone;
	}

	/**
	 * handle message target hit
	 * 
	 * @param target
	 * @param action
	 */
	private void handleComponentHit(final String target,
			final IAction<Event, Object> action,
			final IPerspective<EventHandler<Event>, Event, Object> perspective) {
		if (perspective.isActive()) {
			this.handleMessageToActivePerspective(target, action, perspective);
		} // End if
		else {
			// perspective was not active and will be initialized
			this.log(" //1.1.1.1// perspective HIT handle IN-ACTIVE: "
					+ action.getTargetId());
			this.handleInActive(perspective, action);
		} // End else
	}

	public <P extends IComponent<EventHandler<Event>, Event, Object>> P getObserveableById(
			final String id, final List<P> components) {
		for (int i = 0; i < components.size(); i++) {
			final P p = components.get(i);
			if (p.getId().equals(id)) {
				return p;
			}
		}
		return null;
	}

	public <P extends IComponent<EventHandler<Event>, Event, Object>> void handleInActive(
			final P component, final IAction<Event, Object> action) {
		component.setActive(true);
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				((AFX2Workbench) FX2PerspectiveDelegator.this.workbench)
						.initComponent(
								action,
								(IPerspective<EventHandler<Event>, Event, Object>) component);
			}
		});
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
			final IPerspective<EventHandler<Event>, Event, Object> perspective) {
		// if perspective already active handle perspective and replace
		// with newly created layout component in workbench
		this.log(" //1.1.1.1// perspective HIT handle ACTIVE: "
				+ action.getTargetId());
		if (this.isLocalMessage(target)) {
			// message is addressing perspective
			this.handleActive(perspective, action);
		} // End if
		else {
			// delegate to addressed component
			perspective.delegateComponentMassege(target, action);
		} // End else
	}

	public <P extends IComponent<EventHandler<Event>, Event, Object>> void handleActive(
			final P component, final IAction<Event, Object> action) {
		Platform.runLater(new Runnable() {
			@Override
			public final void run() {
				((AFX2Workbench) FX2PerspectiveDelegator.this.workbench)
						.handleAndReplaceComponent(
								action,
								(IPerspective<EventHandler<Event>, Event, Object>) component);
			} // End run
		} // End runnable
		); // End runlater
	}

	protected void log(final String message) {
		this.logger.fine(message);
	}

	@Override
	public void delegate(IDelegateDTO<EventHandler<Event>, Event, Object> dto) {
		dtos.add(dto);
	}

	@Override
	public BlockingQueue<IDelegateDTO<EventHandler<Event>, Event, Object>> getDelegateQueue() {
		return this.dtos;
	}

	@Override
	public void addPerspective(
			IPerspective<EventHandler<Event>, Event, Object> perspective) {
		perspectives.add(perspective);

	}

	@Override
	public void removePerspective(
			IPerspective<EventHandler<Event>, Event, Object> perspective) {
		perspectives.remove(perspective);

	}
}
