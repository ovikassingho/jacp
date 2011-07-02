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
package org.jacp.project.concurrency.action;


import java.util.HashMap;
import java.util.Map;

import org.jacp.api.action.IAction;
/**
 * The JACP Action class.  represents an action which is fired by an component, has a target and a
 * message targeting the component itself or an other component
 * @author Andy Moncsek
 *
 */
public class Action implements IAction<JACPEvent, Object>{
	
    private final Map<String, Object> messages = new HashMap<String, Object>();
    private Object message;
    private final String sourceId;
    private String target;
    private JACPEvent event;
	
	public Action(final String sourceId) {
		this.sourceId = sourceId;
	}

	@Override
	public void setMessage(final Object message) {
		this.message = message;
		target = target != null ? target : getSourceId();
		getMessageList().put(target, message);
	}

	@Override
	public void addMessage(final String targetId, final Object message) {
		target = targetId;
		this.message = message;
		getMessageList().put(targetId, message);
		
	}

	@Override
	public Object getLastMessage() {
		return message;
	}

	@Override
	public Map<String, Object> getMessageList() {
		return messages;
	}

	@Override
	public String getSourceId() {
		return sourceId;
	}

	@Override
	public void setActionEvent(final JACPEvent event) {
		this.event = event;		
	}

	@Override
	public JACPEvent getActionEvent() {
		return this.event;
	}

	@Override
	public String getTargetId() {
		return this.target;
	}
	
	@Override
	public IAction<JACPEvent, Object> clone()  {
		final IAction<JACPEvent, Object> clone = new Action(sourceId);
		clone.setActionEvent(event);
		return clone;
	}

}
