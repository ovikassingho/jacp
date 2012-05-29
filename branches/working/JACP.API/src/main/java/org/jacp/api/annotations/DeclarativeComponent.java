/************************************************************************
 * 
 * Copyright (C) 2010 - 2012
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
 * This annotation defines the meta definition of a declarative JACP UI component.
 * 
 * @author Andy Moncsek
 * 
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface DeclarativeComponent {
	/**
	 * The components name.
	 * 
	 * @return
	 */
	String name();

	/**
	 * The component id.
	 * 
	 * @return
	 */
	String id();

	/**
	 * The active state at start time.
	 * 
	 * @return
	 */
	boolean active() default true;

	/**
	 * The execution target at start time.
	 * 
	 * @return
	 */
	String defaultExecutionTarget();
	
	/**
	 * Represents the location (URI) of the declarative UI.
	 * @return
	 */
	String viewLocation();
}
