/************************************************************************
 * 
 * Copyright (C) 2010 - 2012
 *
 * [FX2Util.java]
 * AHCP Project (http://jacp.googlecode.com)
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
 *
 *
 ************************************************************************/
package org.jacp.javafx.rcp.util;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutionException;

import javafx.event.Event;
import javafx.event.EventHandler;

import org.jacp.api.action.IAction;
import org.jacp.api.component.ICallbackComponent;
import org.jacp.api.component.ISubComponent;

/**
 * this class handles running stateful background components
 * 
 * @author Andy Moncsek
 * 
 */
public class StateComponentRunWorker
		extends
		AFX2ComponentWorker<ICallbackComponent<EventHandler<Event>, Event, Object>> {
	private final ICallbackComponent<EventHandler<Event>, Event, Object> component;
	private final BlockingQueue<ISubComponent<EventHandler<Event>, Event, Object>> delegateQueue;

	public StateComponentRunWorker(
			final BlockingQueue<ISubComponent<EventHandler<Event>, Event, Object>> delegateQueue,
			final ICallbackComponent<EventHandler<Event>, Event, Object> component) {
		this.component = component;
		this.delegateQueue = delegateQueue;
	}

	@Override
	protected ICallbackComponent<EventHandler<Event>, Event, Object> call()
			throws Exception {
		final ICallbackComponent<EventHandler<Event>, Event, Object> comp = this.component;
		synchronized (comp) {
			comp.setBlocked(true);
			while (comp.hasIncomingMessage()) {
				final IAction<Event, Object> myAction = comp
						.getNextIncomingMessage();
				comp.setHandleTarget(myAction.getSourceId());
				final String targetCurrent = comp.getExecutionTarget();
				final Object value = comp.handle(myAction);
				final String targetId = comp.getHandleTargetAndClear();
				this.delegateReturnValue(comp, targetId, value, myAction);
				this.checkAndHandleTargetChange(comp, targetCurrent);
			}
			comp.setBlocked(false);
		}
		return comp;
	}

	/**
	 * check if target has changed
	 * 
	 * @param comp
	 * @param currentTaget
	 */
	private void checkAndHandleTargetChange(
			final ICallbackComponent<EventHandler<Event>, Event, Object> comp,
			final String currentTaget) {
		final String targetNew = comp.getExecutionTarget();
		if (!targetNew.equals(currentTaget)) {
			this.changeComponentTarget(this.delegateQueue, comp);
		}
	}

	@Override
	protected final void done() {
		try {
			this.get();
		} catch (final InterruptedException e) {
			// FIXME: Handle Exceptions the right way
			// e.printStackTrace();
		} catch (final ExecutionException e) {
			// FIXME: Handle Exceptions the right way
			// e.printStackTrace();
		} finally {
			// release lock
			this.component.setBlocked(false);
		}
	}
}
