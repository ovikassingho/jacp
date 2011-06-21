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
package org.jacp.api.coordinator;


import org.jacp.api.perspective.IPerspective;

/**
 * notifies perspectives and components included in workbench C defines the base
 * component where others extend A defines the action listener type E defines
 * the basic action type T defines the basic message type
 * 
 * @author Andy Moncsek
 */
public interface IPerspectiveCoordinator<L, A, M> extends
	ICoordinator<L, A, M> {

    /**
     * add perspective to observe
     * 
     * @param perspective
     */
    public abstract void addPerspective(final IPerspective<L, A, M> perspective);

    /**
     * remove perspective; e.g. when perspective is deactivated
     * 
     * @param perspective
     */
    public abstract void removePerspective(
	    final IPerspective<L, A, M> perspective);


}
