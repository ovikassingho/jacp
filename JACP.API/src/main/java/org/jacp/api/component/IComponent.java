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

import org.jacp.api.action.IActionListener;
import org.jacp.api.coordinator.ICoordinator;

/**
 * This Interface represents a very basic component that can exists in JACP
 * environment.
 * 
 * @author Andy Moncsek

 * @param <L>
 *            defines the action listener type
 * @param <A>
 *            defines the basic action type
 * @param <M>
 *            defines the basic message type
 */
public interface IComponent<L, A, M> {

	/**
	 * Returns an action listener (for local, target and global use).
	 * 
	 * @return the action listener instance
	 */
	IActionListener<L, A, M> getActionListener();

	/**
	 * Returns the id of the component.
	 * 
	 * @return the component id
	 */
	String getId();

	/**
	 * Set unique id of component.
	 * 
	 * @param id
	 */
	void setId(final String id);

	/**
	 * Get the default active status of component.
	 * 
	 * @return the active state of component
	 */
	boolean isActive();

	/**
	 * Set default active state of component.
	 * 
	 * @param active
	 */
	void setActive(boolean active);

	/**
	 * Set if component was activated, can occur if message was send before
	 * "init" message arrived
	 * 
	 * @param isActive
	 */
	void setStarted(boolean isActive);

	/**
	 * Get if component was activated, can occur if message was send before
	 * "init" message arrived
	 * 
	 * @return the active status
	 */
	boolean isStarted();

	/**
	 * Returns the name of a component.
	 * 
	 * @return the component name
	 */
	String getName();

	/**
	 * Defines the name of a component.
	 * 
	 * @param name
	 */
	void setName(String name);

	/**
	 * Set Observer to handle changes in components.
	 * 
	 * @param observer
	 */
	void setObserver(final ICoordinator<L, A, M> observer);
	
	/**
	 * returns the currently associated obsever instance.
	 * @return ICoordinator
	 */
	ICoordinator<L, A, M> getObserver();

}
