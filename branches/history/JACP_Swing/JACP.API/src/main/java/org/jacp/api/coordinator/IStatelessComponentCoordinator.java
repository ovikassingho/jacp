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

import org.jacp.api.action.IAction;
import org.jacp.api.component.IBGComponent;

/**
 * Handles instances of a state less component; delegates message to a non
 * blocked component instance or if all components are blocked message is
 * delegated to queue in one of existing instances
 * 
 * @author Andy Moncsek
 * 
 * @param <L>
 * @param <A>
 * @param <M>
 */
public interface IStatelessComponentCoordinator<L, A, M> {
    /**
     * Handles incoming message to managed state less component.
     * 
     * @param message
     */
    void incomingMessage(final IAction<A, M> message);

    /**
     * Returns a new instance of managed state less component.
     * 
     * @param <T>
     * @param clazz
     * @return an cloned instance of a state less component.
     */
    <T extends IBGComponent<L, A, M>> IBGComponent<L, A, M> getCloneBean(
	    final Class<T> clazz);

}