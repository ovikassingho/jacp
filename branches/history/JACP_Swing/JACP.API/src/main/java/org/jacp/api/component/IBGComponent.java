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

/**
 * This interface defines methods for background/ non ui components.
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
public interface IBGComponent<L, A, M> extends ISubComponent<L, A, M>,
		Cloneable {

	/**
	 * Returns component id which is targeted by bg component return value; the
	 * return value will be handled like an average message and will be
	 * delivered to targeted component
	 * 
	 * @return the target id
	 */
	String getHandleTargetAndClear();

	/**
	 * Set component target id which is targeted by background component return value;
	 * the return value will be handled like an average message and will be
	 * delivered to targeted component
	 * 
	 * @param componentTargetId
	 */
	void setHandleTarget(final String componentTargetId);

}
