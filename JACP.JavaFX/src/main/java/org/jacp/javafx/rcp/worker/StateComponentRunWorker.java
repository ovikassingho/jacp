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
package org.jacp.javafx.rcp.worker;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutionException;

import javafx.event.Event;
import javafx.event.EventHandler;

import org.jacp.api.action.IAction;
import org.jacp.api.annotations.OnStart;
import org.jacp.api.annotations.OnTearDown;
import org.jacp.api.component.ICallbackComponent;
import org.jacp.api.component.ISubComponent;
import org.jacp.javafx.rcp.util.Checkable;
import org.jacp.javafx.rcp.util.FXUtil;

/**
 * This class handles running stateful background components
 * 
 * @author Andy Moncsek
 * 
 */
public class StateComponentRunWorker
		extends
		AFXComponentWorker<ICallbackComponent<EventHandler<Event>, Event, Object>> {
	private final ICallbackComponent<EventHandler<Event>, Event, Object> component;
	private final BlockingQueue<ISubComponent<EventHandler<Event>, Event, Object>> delegateQueue;

	public StateComponentRunWorker(
			final BlockingQueue<ISubComponent<EventHandler<Event>, Event, Object>> delegateQueue,
			final ICallbackComponent<EventHandler<Event>, Event, Object> component) {
		super(component.getName());
		this.component = component;
		this.delegateQueue = delegateQueue;
	}

	@Override
	protected ICallbackComponent<EventHandler<Event>, Event, Object> call()
			throws Exception {
		synchronized (this.component) {
			this.component.lock();
			try {
				runOnStart(this.component);
				while (this.component.hasIncomingMessage()) {
					final IAction<Event, Object> myAction = this.component
							.getNextIncomingMessage();
					this.component.setHandleTarget(myAction.getSourceId());
					final String targetCurrent = this.component
							.getExecutionTarget();
					final Object value = this.component.handle(myAction);
					final String targetId = this.component
							.getHandleTargetAndClear();
					this.delegateReturnValue(this.component, targetId, value,
							myAction);
					this.checkAndHandleTargetChange(this.component,
							targetCurrent);
				}
				postHandling(component);
			} finally {
				this.component.release();
			}
		}
		return this.component;
	}
	
	/**
	 * checks if component started, if so run OnStart annotations
	 * @param component
	 */
	private void runOnStart(final ICallbackComponent<EventHandler<Event>, Event, Object> component) {
		if (!component.isStarted())
			FXUtil.invokeHandleMethodsByAnnotation(OnStart.class,
					component);
	}
	/**
	 * checks if component was deactivated, if so run OnTeardown annotations. If not set component activeted.
	 * @param component
	 */
	private void postHandling(final ICallbackComponent<EventHandler<Event>, Event, Object> component) {
		if (!component.isStarted())
			FXUtil.setPrivateMemberValue(Checkable.class,
					component, FXUtil.ACOMPONENT_STARTED, true);
		// turn off component
		if (!component.isActive()) {
			FXUtil.setPrivateMemberValue(Checkable.class,
					component, FXUtil.ACOMPONENT_STARTED, false);
			// run teardown
			FXUtil.invokeHandleMethodsByAnnotation(OnTearDown.class,
					component);
		}
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
			if (!component.isActive())
				throw new UnsupportedOperationException(
						"Component may be moved or set to inactive but not both");
			this.changeComponentTarget(this.delegateQueue, comp);
		}
	}

	@Override
	protected final void done() {
		synchronized (this.component) {
			try {
				this.get();
			} catch (final InterruptedException e) {
				// FIXME: Handle Exceptions the right way
				e.printStackTrace();
			} catch (final ExecutionException e) {
				// FIXME: Handle Exceptions the right way
				e.printStackTrace();
			} 
		}
	}

}
