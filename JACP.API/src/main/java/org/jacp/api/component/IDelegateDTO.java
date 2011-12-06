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


import org.jacp.api.action.IAction;

/**
 * DTO interface to transfer components to desired target
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
public interface IDelegateDTO<L, A, M> {
	/**
	 * Get the component to transfer
	 * 
	 * @return the component
	 */
	ISubComponent<L, A, M> getComponent();

	/**
	 * get the target id to transfer to
	 * 
	 * @return targetId
	 */
	String getTarget();
	/**
	 * returns the action
	 * @return
	 */
	public IAction<A, M> getAction();
}