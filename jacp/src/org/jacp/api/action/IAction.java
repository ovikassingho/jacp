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
package org.jacp.api.action;

import java.util.Map;

/**
 * represents an action used by specific listener, targets a component and
 * contains a message, every target get a specific instance of an action (clone)
 * containing only his specific message and action event
 * 
 * @param <M>
 *            defines the type of message
 * @param <A>
 *            defines the type of ActionEvent
 * @author Andy Moncsek
 */
public interface IAction<A, M> extends Cloneable {

    /**
     * set message for target component: OWN
     * 
     * @param message
     */
    public abstract void setMessage(final M message);

    /**
     * set message for target component
     * 
     * @param targetId
     * @param message
     */
    public abstract void setMessage(final String targetId, final M message);

    /**
     * get action message
     * 
     * @return
     */
    public abstract M getMessage();

    /**
     * returns message list with target id's
     * 
     * @return
     */
    public abstract Map<String, M> getMessageList();

    /**
     * get caller id
     * 
     * @return
     */
    public abstract String getSourceId();

    /**
     * set implementation specific event
     * 
     * @param event
     */
    public abstract void setActionEvent(final A event);

    /**
     * get implementation specific action event
     * 
     * @return
     */
    public abstract A getActionEvent();

    /**
     * clone action and containing event
     * 
     * @return
     */
    public abstract IAction<A, M> clone();

    /**
     * returns action target id
     * 
     * @return
     */
    public abstract String getTargetId();

}
