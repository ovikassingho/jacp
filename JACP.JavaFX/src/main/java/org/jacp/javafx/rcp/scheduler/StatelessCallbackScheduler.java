/************************************************************************
 * 
 * Copyright (C) 2010 - 2012
 *
 * [StatelessCallbackScheduler.java]
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
package org.jacp.javafx.rcp.scheduler;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import javafx.event.Event;
import javafx.event.EventHandler;

import org.jacp.api.action.IAction;
import org.jacp.api.component.ICallbackComponent;
import org.jacp.api.component.IStatelessCallabackComponent;
import org.jacp.api.launcher.Launcher;
import org.jacp.api.scheduler.IStatelessComponentScheduler;
import org.jacp.javafx.rcp.component.AStatelessCallbackComponent;
import org.jacp.javafx.rcp.worker.StateLessComponentRunWorker;

public class StatelessCallbackScheduler implements
		IStatelessComponentScheduler<EventHandler<Event>, Event, Object> {

	private final Launcher<?> launcher;

	public StatelessCallbackScheduler(final Launcher<?> launcher) {
		this.launcher = launcher;
	}

	@Override
	public final void incomingMessage(
			final IAction<Event, Object> message,
			final IStatelessCallabackComponent<EventHandler<Event>, Event, Object> baseComponent) {
		synchronized (baseComponent) {
			// get active instance
			final ICallbackComponent<EventHandler<Event>, Event, Object> comp = this
					.getActiveComponent(baseComponent);
			final List<ICallbackComponent<EventHandler<Event>, Event, Object>> componentInstances = baseComponent
					.getInstances();
			if (comp != null) {
				if (componentInstances.size() < AStatelessCallbackComponent.MAX_INCTANCE_COUNT) {
					// create new instance
					componentInstances.add(this.getCloneBean(baseComponent,
							((AStatelessCallbackComponent) baseComponent)
									.getClass()));
				} // End inner if
					// run component in thread
				this.instanceRun(baseComponent, comp, message);
			} // End if
			else {
				// check if new instances can be created
				if (componentInstances.size() < AStatelessCallbackComponent.MAX_INCTANCE_COUNT) {
					this.createInstanceAndRun(baseComponent, message);
				} // End if
				else {
					this.seekAndPutMessage(baseComponent, message);
				} // End else
			} // End else

		} // End synchronized
	}

	/**
	 * block component, put message to component's queue and run in thread
	 * 
	 * @param comp
	 * @param message
	 */
	private void instanceRun(
			final IStatelessCallabackComponent<EventHandler<Event>, Event, Object> baseComponent,
			final ICallbackComponent<EventHandler<Event>, Event, Object> comp,
			final IAction<Event, Object> message) {
		comp.putIncomingMessage(message);
		baseComponent.getExecutorService().submit(new StateLessComponentRunWorker(
				comp,baseComponent));
	}

	/**
	 * if max thread count is not reached and all available component instances
	 * are blocked create a new one, block it an run in thread
	 * 
	 * @param message
	 */
	private void createInstanceAndRun(
			final IStatelessCallabackComponent<EventHandler<Event>, Event, Object> baseComponent,
			final IAction<Event, Object> message) {
		final ICallbackComponent<EventHandler<Event>, Event, Object> comp = this
				.getCloneBean(baseComponent,
						((AStatelessCallbackComponent) baseComponent)
								.getClass());
		baseComponent.getInstances().add(comp);
		this.instanceRun(baseComponent, comp, message);
	}

	@Override
	public final <T extends ICallbackComponent<EventHandler<Event>, Event, Object>> ICallbackComponent<EventHandler<Event>, Event, Object> getCloneBean(
			final IStatelessCallabackComponent<EventHandler<Event>, Event, Object> baseComponent,
			final Class<T> clazz) {
		return ((AStatelessCallbackComponent) baseComponent).init(this.launcher
				.getBean(clazz));
	}

	/**
	 * Returns a component instance that is currently not blocked.
	 * 
	 * @return
	 */
	private ICallbackComponent<EventHandler<Event>, Event, Object> getActiveComponent(
			final IStatelessCallabackComponent<EventHandler<Event>, Event, Object> baseComponent) {
		final List<ICallbackComponent<EventHandler<Event>, Event, Object>> componentInstances = baseComponent
				.getInstances();
		for (int i = 0; i < componentInstances.size(); i++) {
			final ICallbackComponent<EventHandler<Event>, Event, Object> comp = componentInstances
					.get(i);
			if (!comp.isBlocked()) {
				return comp;
			} // End if
		} // End for

		return null;
	}

	/**
	 * seek to first running component in instance list and add message to queue
	 * of selected component
	 * 
	 * @param message
	 */
	private void seekAndPutMessage(
			final IStatelessCallabackComponent<EventHandler<Event>, Event, Object> baseComponent,
			final IAction<Event, Object> message) {
		// if max count reached, seek through components and add
		// message to queue of oldest component
		final ICallbackComponent<EventHandler<Event>, Event, Object> comp = baseComponent
				.getInstances().get(this.getSeekValue(baseComponent));
		// put message to queue
		comp.putIncomingMessage(message);
	}

	private int getSeekValue(
			final IStatelessCallabackComponent<EventHandler<Event>, Event, Object> baseComponent) {
		final AtomicInteger threadCount = baseComponent.getThreadCounter();
		final int seek = threadCount.incrementAndGet()
				% baseComponent.getInstances().size();
		threadCount.set(seek);
		return seek;
	}

}
