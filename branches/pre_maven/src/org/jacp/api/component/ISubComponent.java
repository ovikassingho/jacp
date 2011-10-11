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

import org.jacp.api.action.IAction;
import org.jacp.api.perspective.IPerspective;

/**
 * defines a subcomponent handled by a root component
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
public interface ISubComponent<L, A, M> extends IComponent<L, A, M>,IHandleable<A, M> {
    /**
     * returns the target; defines the target perspective
     * 
     * @param target
     */
    public abstract String getExecutionTarget();

    /**
     * set the target; defines the target container in perspective
     * 
     * @param target
     */
    public abstract void setExecutionTarget(final String target);

    /**
     * set responsible perspective
     * 
     * @param perspective
     */
    public abstract void setParentPerspective(
	    final IPerspective<L, A, M> perspective);

    /**
     * returns responsible perspective
     * 
     * @return
     */
    public abstract IPerspective<L, A, M> getParentPerspective();

    /**
     * returns true if component has message in pipe
     * 
     * @return
     */
    public abstract boolean hasIncomingMessage();

    /**
     * add new message to component
     * 
     * @param action
     */
    public abstract void putIncomingMessage(final IAction<A, M> action);

    /**
     * returns next message in pipe
     * 
     * @return
     */
    public IAction<A, M> getNextIncomingMessage();

    /**
     * component is blocked when executed in thread
     * 
     * @return
     */
    public boolean isBlocked();

    /**
     * block component when run in thread
     * 
     * @param blocked
     */
    public void setBlocked(final boolean blocked);
}
