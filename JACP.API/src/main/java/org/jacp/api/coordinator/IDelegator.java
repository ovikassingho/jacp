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

import org.jacp.api.action.IAction;
import org.jacp.api.component.ISubComponent;

/**
 * Basic delegate interface
 * 
 * @author Andy Moncsek
 * 
 * @param <L>
 * @param <A>
 * @param <M>
 */
public interface IDelegator<L, A, M> {
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
	 * Delegate the component target change to an other perspective.
	 * 
	 * @param target
	 * @param component
	 */
	void delegateTargetChange(final String target,
			final ISubComponent<L, A, M> component);
}
