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

import java.util.Map;

import org.jacp.api.componentLayout.Layout;

/**
 * represents a extended component with menu entries and tool bar access
 * 
 * @author Andy Moncsek
 * @param <C>
 *            defines the base component where others extend from
 */
public interface IExtendedComponent<C> {

	/**
	 * get custom menu entries
	 * 
	 * @return
	 */
	public abstract void handleMenuEntries(final C meuneBar);

	/**
	 * add custom actions to toolbar
	 * 
	 * @param toolBar
	 */
	public abstract void handleBarEntries(final Map<Layout,C> bars);

}
