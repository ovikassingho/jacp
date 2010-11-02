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
package org.jacp.api.coordinator;

import org.jacp.api.component.ISubComponent;

/**
 * notifies components included in perspective
 * 
 * @param <C>
 *            defines the base component where others extend from
 * @param <L>
 *            defines the action listener type
 * @param <A>
 *            defines the basic action type
 * @param <M>
 *            defines the basic message type
 * 
 * @author Andy Moncsek
 */
public interface IComponentCoordinator<L, A, M> extends ICoordinator<L, A, M> {

	/**
	 * add component to observe
	 * 
	 * @param component
	 */
	public abstract void addComponent(ISubComponent<L, A, M> component);

	/**
	 * remove component; e.g. when component is deactivated
	 * 
	 * @param component
	 */
	public abstract void removeComponent(ISubComponent<L, A, M> component);

}
