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

import java.util.concurrent.BlockingQueue;

import org.jacp.api.component.IDelegateDTO;
import org.jacp.api.perspective.IPerspective;

/**
 * A component delegate handles delegate actions.
 * @author Andy Moncsek
 *
 * @param <L>
 * @param <A>
 * @param <M>
 */
public interface IComponentDelegator<L, A, M> extends IDelegator<L, A, M>{
	
	/**
	 * handles delegate
	 * @param dto
	 */
	void delegate(IDelegateDTO<L, A, M> dto);
	
	/**
	 * returns the queue where delegate actions should be added
	 * @return
	 */
	BlockingQueue<IDelegateDTO<L, A, M>> getDelegateQueue();
	
    /**
     * Add the perspective to observe.
     *TODO merge with IPerspectiveCoordinator
     * @param perspective
     */
    void addPerspective(final IPerspective<L, A, M> perspective);

    /**
     * Remove the perspective; e.g. when perspective is deactivated
     * TODO merge with IPerspectiveCoordinator
     * @param perspective
     */
    void removePerspective(
	    final IPerspective<L, A, M> perspective);

}
