/************************************************************************
 * 
 * Copyright (C) 2010 - 2012
 *
 * [StateLessComponentRunWorker.java]
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
package org.jacp.javafx.rcp.worker;

import java.util.concurrent.ExecutionException;

import javafx.event.Event;
import javafx.event.EventHandler;

import org.jacp.api.action.IAction;
import org.jacp.api.component.ICallbackComponent;

/**
 * Component worker to run instances of a stateless component in a worker
 * thread.
 * 
 * @author Andy Moncsek
 * 
 */
public class StateLessComponentRunWorker
		extends
		AFXComponentWorker<ICallbackComponent<EventHandler<Event>, Event, Object>> {
	private final ICallbackComponent<EventHandler<Event>, Event, Object> component;

	public StateLessComponentRunWorker(
			final ICallbackComponent<EventHandler<Event>, Event, Object> component) {
		super(component.getName()+component);
		this.component = component;
	}

	@Override
	protected ICallbackComponent<EventHandler<Event>, Event, Object> call()
			throws Exception {
		final ICallbackComponent<EventHandler<Event>, Event, Object> comp = this.component;
		synchronized (comp) {
			comp.lock();
			try {
				while (comp.hasIncomingMessage()) {
					final IAction<Event, Object> myAction = comp
							.getNextIncomingMessage();
					comp.setHandleTarget(myAction.getSourceId());
					final Object value = comp.handle(myAction);
					final String targetId = comp.getHandleTargetAndClear();
					this.delegateReturnValue(comp, targetId, value, myAction);
				}
			} finally{
				comp.release();
			}
		}
		return comp;
	}

	@Override
	protected void done() {
		try {
			this.get();
		} catch (final InterruptedException e) {
			e.printStackTrace();
			// TODO add to error queue and restart thread if messages in
			// queue
		} catch (final ExecutionException e) {
			e.printStackTrace();
			// TODO add to error queue and restart thread if messages in
			// queue
		} 

	}
}