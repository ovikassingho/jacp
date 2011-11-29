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
package org.jacp.api.component;

import org.jacp.api.launcher.Launcher;

/**
 * Represents a state less background/callback component. This component has a
 * typical handle method, the return value is typically a non UI value (but it
 * can be). Every message to this component should be handled in a separate
 * thread and component instance, managed by an executor (to avoid garbage). Do
 * not use private members as it is not guaranteed that that you contact the
 * same instance twice. This component type is good for scaling tasks like
 * performing operations on many folders or tables in database. The return value
 * will be send to message caller or to specified handleTargetId.
 * 
 * @author Andy Moncsek
 * 
 * @param <L>
 *            defines the action listener type
 * @param <A>
 *            defines the basic action type
 * @param <M>
 *            defines the basic message type
 */
public interface IStateLessCallabackComponent<L, A, M> extends
		ICallbackComponent<L, A, M> {
	/**
	 * The launcher is needed to create many instances of a component
	 * 
	 * @param launcher
	 */
	void setLauncher(final Launcher<?> launcher);
}