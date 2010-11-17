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
package org.jacp.swing.rcp.action;

import java.awt.event.ActionEvent;
import java.util.HashMap;
import java.util.Map;

import org.jacp.api.action.IAction;

/**
 * represents an action which is fired by an component, has a target and a
 * message targeting the component itself or an other component
 * 
 * @author Andy Moncsek
 */
public final class SwingAction implements IAction<ActionEvent, Object> {

    private final Map<String, Object> messages = new HashMap<String, Object>();
    private Object message;
    private final String sourceId;
    private ActionEvent event;
    private String target;

    public SwingAction(final String sourceId) {
	this.sourceId = sourceId;
    }

    public SwingAction(final String sourceId, final Object message) {
	this.sourceId = sourceId;
	setMessage(message);
    }

    public SwingAction(final String sourceId, final String target,
	    final Object message) {
	this.sourceId = sourceId;
	this.target = target;
	setMessage(message);

    }

    @Override
    public void setMessage(final Object message) {
	this.message = message;
	target = target != null ? target : getSourceId();
	getMessageList().put(target, message);
    }

    @Override
    public void addMessage(final String id, final Object message) {
	target = id;
	this.message = message;
	getMessageList().put(id, message);
    }

    @Override
    public String getSourceId() {
	return sourceId;
    }



    @Override
    public Map<String, Object> getMessageList() {
	return messages;
    }

    @Override
    public void setActionEvent(final ActionEvent event) {
	this.event = event;
    }

    @Override
    public ActionEvent getActionEvent() {
	return event;
    }

    @Override
    public String getTargetId() {
	return target;
    }

    @Override
    public IAction<ActionEvent, Object> clone() {
	final IAction<ActionEvent, Object> clone = new SwingAction(sourceId);
	clone.setActionEvent(event);
	return clone;
    }

    @Override
    public Object getLastMessage() {
	return this.message;
    }
}
