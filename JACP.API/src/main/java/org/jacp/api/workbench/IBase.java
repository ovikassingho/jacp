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
package org.jacp.api.workbench;

import java.util.List;

import org.jacp.api.launcher.Launcher;
import org.jacp.api.perspective.IPerspective;

/**
 * 
 * This Interface defines the basic root construct of an AHCP application, it has no dependencies to any UI 
 * @param <L>
 *            defines the action listener type
 * @param <A>
 *            defines the basic action type
 * @param <M>
 *            defines the basic message type
 * @author Andy Moncsek
 *
 */
public interface IBase<L, A, M> {
	
	/**
	 * set perspectives to workbench
	 * 
	 * @param perspectives
	 */
	public abstract void setPerspectives(
			final List<IPerspective<L, A, M>> perspectives);

	/**
	 * get perspectives in workbench
	 * 
	 * @return
	 */
	public abstract List<IPerspective<L, A, M>> getPerspectives();
	
	/**
	 * Initialization sequence returns basic container to handle perspectives
	 * 
	 * @param launcher
	 *            for di container
	 * @return
	 */
	public abstract void init(final Launcher<?> launcher);


}
