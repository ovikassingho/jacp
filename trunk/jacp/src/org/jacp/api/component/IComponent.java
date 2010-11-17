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
import org.jacp.api.action.IActionListener;
import org.jacp.api.coordinator.ICoordinator;

/**
 * represents a basic component
 * 
 * @author Andy Moncsek
 * @param <C>
 *            defines the return type of component handling; when component is
 *            an UI component C is the basic type where other elements extends
 *            from
 * @param <L>
 *            defines the action listener type
 * @param <A>
 *            defines the basic action type
 * @param <M>
 *            defines the basic message type
 */
public interface IComponent<L, A, M> {

    /**
     * handles component when called
     * 
     * @param action
     * @return view component
     */
    public abstract <C> C handle(final IAction<A, M> action);

    /**
     * returns action listener (for local, target and global use)
     * 
     * @return
     */
    public abstract IActionListener<L, A, M> getActionListener();

    /**
     * returns id of component
     * 
     * @return
     */
    public abstract String getId();

    /**
     * set unique id of component
     * 
     * @param id
     */
    public abstract void setId(final String id);

    /**
     * get active status of perspective
     * 
     * @return
     */
    public abstract boolean isActive();

    /**
     * set active state of perspective
     * 
     * @param active
     */
    public abstract void setActive(boolean active);

    /**
     * returns the name of a component
     * 
     * @return
     */
    public abstract String getName();

    /**
     * defines the name of a component
     * 
     * @param name
     */
    public abstract void setName(String name);

    /**
     * observer to handle changes in components
     * 
     * @return
     */
    public abstract void setObserver(final ICoordinator<L, A, M> observer);

}
