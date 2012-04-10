/************************************************************************
 * 
 * Copyright (C) 2010 - 2012
 *
 * [FX2Action.java]
 * AHCP Project (http://jacp.googlecode.com)
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
package org.jacp.javafx.rcp.action;

import java.util.HashMap;
import java.util.Map;

import javafx.event.Event;

import org.jacp.api.action.IAction;

/**
 * represents an action which is fired by an component, has a target and a
 * message targeting the component itself or an other component
 * 
 * @author Andy Moncsek
 */
public final class FXAction implements IAction<Event, Object> {

	private final Map<String, Object> messages = new HashMap<String, Object>();
	private Object message;
	private final String sourceId;
	private Event event;
	private String target;

	public FXAction(final String sourceId) {
		this.sourceId = sourceId;
	}

	public FXAction(final String sourceId, final Object message) {
		this.sourceId = sourceId;
		this.setMessage(message);
	}

	public FXAction(final String sourceId, final String targetId,
			final Object message) {
		this.sourceId = sourceId;
		this.target = targetId;
		this.setMessage(message);
	}

	@Override
	public void setMessage(final Object message) {
		this.message = message;
		this.target = this.target != null ? this.target : this.getSourceId();
		this.getMessageList().put(this.target, message);
	}

	@Override
	public void addMessage(final String targetId, final Object message) {
		this.target = targetId;
		this.message = message;
		this.getMessageList().put(this.target, message);
	}

	@Override
	public Object getLastMessage() {
		return this.message;
	}

	@Override
	public Map<String, Object> getMessageList() {
		return this.messages;
	}

	@Override
	public String getSourceId() {
		return this.sourceId;
	}

	@Override
	public void setActionEvent(final Event event) {
		this.event = event;
	}

	@Override
	public Event getActionEvent() {
		return this.event;
	}

	@Override
	public IAction<Event, Object> clone() {
		final IAction<Event, Object> clone = new FXAction(this.sourceId);
		clone.setActionEvent(this.event);
		return clone;
	}

	@Override
	public String getTargetId() {
		return this.target;
	}

}
