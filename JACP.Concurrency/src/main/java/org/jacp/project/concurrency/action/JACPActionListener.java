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

import java.awt.event.ActionListener;
import java.util.EventListener;
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
public class JACPActionListener implements EventListener,
		IActionListener<EventListener, JACPEvent, Object> {
	private IAction<JACPEvent, Object> action;
	private final ICoordinator<ActionListener, JACPEvent, Object> observer;
	private final Logger logger = Logger.getLogger(this.getClass().getName());

	public JACPActionListener(
			final ICoordinator<ActionListener, JACPEvent, Object> observer) {
		this.observer = observer;
	}

	@Override
	public void notifyComponents(final IAction<JACPEvent, Object> action) {
		this.observer.handle(action);

	}

	@Override
	public void setAction(final IAction<JACPEvent, Object> action) {
		this.action = action;

	}

	@Override
	public IAction<JACPEvent, Object> getAction() {
		return this.action;
	}

	@Override
	public EventListener getListener() {
		return null;
	}

}
