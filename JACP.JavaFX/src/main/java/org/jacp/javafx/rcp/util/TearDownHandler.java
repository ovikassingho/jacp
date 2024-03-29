/************************************************************************
 * 
 * Copyright (C) 2010 - 2012
 *
 * [TearDownHandler.java]
 * AHCP Project (http://jacp.googlecode.com/)
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

import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.RejectedExecutionException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.event.Event;
import javafx.event.EventHandler;

import org.jacp.api.annotations.OnTearDown;
import org.jacp.api.component.ICallbackComponent;
import org.jacp.api.component.IPerspective;
import org.jacp.api.component.IStatelessCallabackComponent;
import org.jacp.api.component.ISubComponent;
import org.jacp.api.workbench.IBase;
import org.jacp.javafx.rcp.worker.AFXComponentWorker;
import org.jacp.javafx.rcp.worker.TearDownWorker;

/**
 * Handles TearDown annotations on all components when application is closed.
 * 
 * @author Andy Moncsek
 * 
 */
public class TearDownHandler {
	private static IBase<EventHandler<Event>, Event, Object> rootWorkbench;
	private static final ExecutorService executor = Executors
			.newCachedThreadPool(new HandlerThreadFactory(
					"FXPerspectiveHandler:"));

	/**
	 * Register the parent workbench, from here all perspectives and component
	 * should be reachable.
	 * 
	 * @param rootWorkbench
	 */
	public final static void registerBase(
			IBase<EventHandler<Event>, Event, Object> rootWorkbench) {
		TearDownHandler.rootWorkbench = rootWorkbench;
	}

	/**
	 * perform global teardown on all components. This method will cause all @TearDown
	 * annotated methods to be executed.
	 */
	public final static void handleGlobalTearDown() {
		if (rootWorkbench == null)
			throw new UnsupportedOperationException("can't teardown workbench");
		final List<IPerspective<EventHandler<Event>, Event, Object>> perspectives = rootWorkbench
				.getPerspectives();
		for (final IPerspective<EventHandler<Event>, Event, Object> perspective : perspectives) {
			// TODO ... teardown perspective itself
			final List<ISubComponent<EventHandler<Event>, Event, Object>> subcomponents = perspective
					.getSubcomponents();
			final List<ICallbackComponent<EventHandler<Event>, Event, Object>> handleAsync = new ArrayList<ICallbackComponent<EventHandler<Event>, Event, Object>>();
			for (final ISubComponent<EventHandler<Event>, Event, Object> component : subcomponents) {
				if (component instanceof ICallbackComponent) {
					handleAsync
							.add((ICallbackComponent<EventHandler<Event>, Event, Object>) component);
				} else {
					// run teardown in app thread
					FXUtil.invokeHandleMethodsByAnnotation(OnTearDown.class,
							component);
				}

			}
			if (!handleAsync.isEmpty())
				handleAsyncTearDown(handleAsync);

		}
		executor.shutdown();
	}

	/**
	 * executes all methods in ICallbackComponent, annotated with @OnTeardown
	 * outside application thread.
	 * 
	 * @param components
	 */
	public final static void handleAsyncTearDown(
			ICallbackComponent<EventHandler<Event>, Event, Object>... components) {
		final List<ICallbackComponent<EventHandler<Event>, Event, Object>> handleAsync = new ArrayList<ICallbackComponent<EventHandler<Event>, Event, Object>>();
        Collections.addAll(handleAsync, components);
		handleAsyncTearDown(handleAsync);
	}

	/**
	 * executes all methods in ICallbackComponent, annotated with @OnTeardown
	 * outside application thread.
	 * 
	 * @param components
	 */
	public final static void handleAsyncTearDown(
			final List<ICallbackComponent<EventHandler<Event>, Event, Object>> components) {
		try {
			final Set<Future<Boolean>> set = new HashSet<Future<Boolean>>();
			for (final ICallbackComponent<EventHandler<Event>, Event, Object> component : components) {
				if(component instanceof IStatelessCallabackComponent){
					final IStatelessCallabackComponent<EventHandler<Event>, Event, Object>tmp = (IStatelessCallabackComponent<EventHandler<Event>, Event, Object>) component;
					final List<ICallbackComponent<EventHandler<Event>, Event, Object>> instances = tmp.getInstances();
					for(final ICallbackComponent<EventHandler<Event>, Event, Object> instance : instances) {
						set.add(executor.submit(new TearDownWorker(instance)));
					}
				}
				set.add(executor.submit(new TearDownWorker(component)));
			}
			// await termination
			for (final Future<Boolean> future : set) {
				try {
					future.get();
				} catch (InterruptedException e) {
					log("error while handle TearDown");
					e.printStackTrace();
				} catch (ExecutionException e) {
					log("error while handle TearDown");
					e.printStackTrace();
				}
			}
		} catch (RejectedExecutionException e) {
			log("component teardown was not executed");
		}

	}

	private static void log(final String message) {
		if (Logger.getLogger(AFXComponentWorker.class.getName()).isLoggable(
				Level.FINE)) {
			Logger.getLogger(AFXComponentWorker.class.getName()).fine(
					">> " + message);
		}
	}
}
