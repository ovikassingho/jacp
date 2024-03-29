/************************************************************************
 * 
 * Copyright (C) 2010 - 2013
 *
 * [Component.java]
 * AHCP Project (http://jacp.googlecode.com/)
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
package org.jacp.api.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Defines the meta attributes for a callback component.
 * 
 * @author Andy Moncsek
 * 
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface CallbackComponent {
	/**
	 * The components name.
	 * 
	 * @return The component name.
	 */
	String name();

	/**
	 * The component id.
	 * 
	 * @return The component Id.
	 */
	String id();

	/**
	 * The active state at start time.
	 * 
	 * @return True
	 */
	boolean active() default true;
}
