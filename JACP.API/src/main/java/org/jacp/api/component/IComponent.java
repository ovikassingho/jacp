/************************************************************************
 * 
 * Copyright (C) 2010 - 2012
 *
 * [IComponent.java]
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
package org.jacp.api.component;

import org.jacp.api.action.IActionListener;

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
	 * Returns an action listener (for local use). Message will be send to caller component.
	 * 
	 * @return the action listener instance
	 */
	IActionListener<L, A, M> getActionListener(M message);
	
	/**
	 * Returns an action listener (for  global use). targetId defines the id or your receiver component
	 * 
	 * @return the action listener instance
	 */
	IActionListener<L, A, M> getActionListener(String targetId, M message);

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
	@Deprecated
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
	@Deprecated
	void setName(String name);


	

}
