/*
 * Copyright (C) 2010,2011.
 * AHCP Project (http://code.google.com/p/jacp/)
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
package org.jacp.api.coordinator;

import java.util.List;

import org.jacp.api.action.IAction;
import org.jacp.api.component.IComponent;
import org.jacp.api.component.ISubComponent;

/**
 * Defines a basic observer for component messages; handles the message and
 * delegate to responsible component.
 * 
 * @author Andy Moncsek
 * @param <L>
 *            defines the action listener type
 * @param <A>
 *            defines the basic action type
 * @param <M>
 *            defines the basic message type
 */
public interface ICoordinator<L, A, M> {
	/**
	 * Handles an action and delegates it to an addressed perspective.
	 * 
	 * @param action
	 */
	void handle(final IAction<A, M> action);

	/**
	 * Handles message to specific component addressed by the id.
	 * 
	 * @param id
	 * @param action
	 */
	void handleMessage(final String id, final IAction<A, M> action);

	/**
	 * Delegate message from a subcomponent to target perspective. If no target
	 * was found for current action in this perspective, delegate the message to
	 * upper level (the workbench) and try to find the component in an other
	 * perspective.
	 * 
	 * @param target
	 * @param action
	 */
	void delegateMessage(final String target, final IAction<A, M> action);

	/**
	 * Returns a specific, observed perspective or component by id.
	 * 
	 * @param id
	 * @return the corresponding component, perspective or null when nothing
	 *         found
	 */
	<P extends IComponent<L, A, M>> P getObserveableById(final String id,
			final List<P> perspectives);

	/**
	 * Handle a message to an active component.
	 * 
	 * @param component
	 * @param action
	 */
	<P extends IComponent<L, A, M>> void handleActive(final P component,
			final IAction<A, M> action);

	/**
	 * Handle a message to an inactive component.
	 * 
	 * @param component
	 * @param action
	 */
	<P extends IComponent<L, A, M>> void handleInActive(final P component,
			final IAction<A, M> action);

	/**
	 * Delegate the component target change to an other perspective.
	 * 
	 * @param target
	 * @param component
	 */
	void delegateTargetChange(final String target,
			final ISubComponent<L, A, M> component);
}
