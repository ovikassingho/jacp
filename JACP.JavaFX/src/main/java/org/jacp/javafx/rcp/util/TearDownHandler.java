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

import java.util.List;

import javafx.event.Event;
import javafx.event.EventHandler;

import org.jacp.api.annotations.OnTearDown;
import org.jacp.api.component.IPerspective;
import org.jacp.api.component.ISubComponent;
import org.jacp.api.workbench.IBase;

/**
 * Handles TearDown annotations on all components when application is closed.
 * 
 * @author Andy Moncsek
 * 
 */
public class TearDownHandler {
	private static IBase<EventHandler<Event>, Event, Object> rootWorkbench;

	/**
	 * Register the parent workbench, from here all perspectives and component
	 * should be reachable.
	 * 
	 * @param rootWorkbench
	 */
	public static void registerBase(
			IBase<EventHandler<Event>, Event, Object> rootWorkbench) {
		TearDownHandler.rootWorkbench = rootWorkbench;
	}
	/**
	 * perform global teardown on all components. This method will cause all @TearDown annotated methods to be executed.
	 */
	public static void handleGlobalTearDown() {
		if(rootWorkbench==null)throw new UnsupportedOperationException(
				"can't teardown workbench");
		final List<IPerspective<EventHandler<Event>, Event, Object>> perspectives = rootWorkbench.getPerspectives();
		for(final IPerspective<EventHandler<Event>, Event, Object> perspective : perspectives) {
			// TODO ... teardown perspective itself
			final List<ISubComponent<EventHandler<Event>, Event, Object>> subcomponents = perspective.getSubcomponents();
			for(final ISubComponent<EventHandler<Event>, Event, Object> component : subcomponents) {
				// run teardown
				FXUtil.invokeHandleMethodsByAnnotation(OnTearDown.class,
						component);
			}
		}
	}
}
