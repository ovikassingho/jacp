package org.jacp.project.concurrency.action;

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

import java.util.EventListener;
import java.util.EventObject;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.jacp.api.action.IAction;
import org.jacp.api.action.IActionListener;
import org.jacp.api.coordinator.ICoordinator;

/**
 * represents the Action Listener class
 * 
 * @author Andy Moncsek
 * 
 */
public class ActionListener implements EventListener,
		IActionListener<EventListener, Event, Object> {
	private IAction<Event, Object> action;
	private final ICoordinator<EventListener, Event, Object> observer;
	private final Logger logger = Logger.getLogger(this.getClass().getName());

	public ActionListener(final IAction<Event, Object> action,
			final ICoordinator<EventListener, Event, Object> observer) {
		this.observer = observer;
		this.action = action;
	}

	/**
	 * perform jacp action
	 * 
	 * @param event
	 */
	public void actionPerformed(final Event e) {
		action.setActionEvent(e);
		log(" //1// message send from / to: " + action.getSourceId() + " / "
				+ action.getTargetId());
		notifyComponents(action);

	}

	@Override
	public void notifyComponents(final IAction<Event, Object> action) {
		this.observer.handle(action);

	}

	@Override
	public void setAction(final IAction<Event, Object> action) {
		this.action = action;

	}

	@Override
	public IAction<Event, Object> getAction() {
		return this.action;
	}

	@SuppressWarnings("unchecked")
	@Override
	public ActionListener getListener() {
		return this;
	}

	private void log(final String message) {
		if (logger.isLoggable(Level.FINE)) {
			logger.fine(">> " + message);
		}

	}


	@Override
	public void performAction(EventObject arg0) {
		actionPerformed((Event) arg0);
		
	}

}
