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
package org.jacp.javafx2.rcp.util;

import java.util.concurrent.ExecutionException;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;

import org.jacp.api.action.IAction;
import org.jacp.api.action.IActionListener;
import org.jacp.api.component.IBGComponent;
import org.jacp.javafx2.rcp.action.FX2Action;

/**
 * this class handles running stateless background components
 * 
 * @author Andy Moncsek
 * 
 */
public class StateLessComponentRunWorker extends
		AFX2ComponentWorker<IBGComponent<EventHandler<ActionEvent>, ActionEvent, Object>> {
	private final IBGComponent<EventHandler<ActionEvent>, ActionEvent, Object> component;
	public StateLessComponentRunWorker(final IBGComponent<EventHandler<ActionEvent>, ActionEvent, Object> component) {
		this.component = component;
	}
	@Override
	protected IBGComponent<EventHandler<ActionEvent>, ActionEvent, Object> call()
			throws Exception {
		final IBGComponent<EventHandler<ActionEvent>, ActionEvent, Object> comp = component;
		synchronized (comp) {
			comp.setBlocked(true);
			while (comp.hasIncomingMessage()) {
				final IAction<ActionEvent, Object> myAction = comp
						.getNextIncomingMessage();;
				final Object value = comp.handle(myAction);
				final String targetId = comp.getHandleTargetAndClear();
				delegateReturnValue(comp, targetId, value);
			}
			comp.setBlocked(false);
		}
		return comp;
	}
	
	/**
	 * delegate components handle return value to specified target
	 * 
	 * @param comp
	 * @param targetId
	 * @param value
	 */
	private void delegateReturnValue(
			final IBGComponent<EventHandler<ActionEvent>, ActionEvent, Object> comp,
			final String targetId, final Object value) {
		if (value != null && targetId != null) {
			final IActionListener<EventHandler<ActionEvent>, ActionEvent, Object> listener = comp
					.getActionListener();
			listener.setAction(new FX2Action(comp.getId(), targetId, value));
			listener.notifyComponents(listener.getAction());
		}
	}

	@Override
	protected void done() {
		try {
			this.get();
		} catch (final InterruptedException e) {
			e.printStackTrace();
			// TODO add to error queue and restart thread if messages in queue
		} catch (final ExecutionException e) {
			e.printStackTrace();
			// TODO add to error queue and restart thread if messages in queue
		} finally {
			// release lock
			component.setBlocked(false);
		}

	}
}
