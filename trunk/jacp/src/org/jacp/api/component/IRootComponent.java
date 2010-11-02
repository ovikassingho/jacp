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
package org.jacp.api.component;

/**
 * all root components have containing sub components (workspace ->
 * perspectives; perspective - editors) and listeners; all sub components have
 * to be registered
 * 
 * @author Andy Moncsek
 * 
 * @param <T>
 *            component to register
 * @param <H>
 *            handler where component have to be registered
 */
public interface IRootComponent<T, H> {

	/**
	 * register component at listener
	 * 
	 * @param component
	 */
	abstract public void registerComponent(final T component, final H handler);

	/**
	 * unregister component from current perspective
	 * 
	 * @param component
	 */
	abstract public void unregisterComponent(final T component, final H handler);

}
