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

import org.jacp.api.componentLayout.IBaseLayout;

/**
 * Represents an extended component with menu entries and tool bar access. The
 * onStart/onTeardown methods gives you the reference to global bar and menu
 * entries. This methods will be executed once on component startup and
 * teardown; also when component is moving to an other perspective. Use both
 * method to initialize and to clean up component.
 * 
 * @author Andy Moncsek
 * @param <C>
 *            defines the base component where others extend from
 */
public interface IExtendedComponent<C> {

	/**
	 * This method is always executed when the component is activated. Use this
	 * entry point to add menu entries or bar entries or to create stuff only
	 * need to create once.
	 * 
	 * @param layout
	 */
	void onStart(final IBaseLayout<C> layout);

	/**
	 * This method is always executed when the component is deactivated. Use
	 * this method to clean up your component before it will be deactivated.
	 * 
	 * @param layout
	 */
	void onTearDown(final IBaseLayout<C> layout);

}
