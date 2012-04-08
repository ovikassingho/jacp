/************************************************************************
 * 
 * Copyright (C) 2010 - 2012
 *
 * [AFXSubComponent.java]
 * AHCP Project (http://jacp.googlecode.com/)
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
package org.jacp.javafx.rcp.component;

import java.util.concurrent.BlockingQueue;

import javafx.event.Event;
import javafx.event.EventHandler;

import org.jacp.api.action.IAction;
import org.jacp.api.action.IActionListener;
import org.jacp.api.component.IComponent;
import org.jacp.javafx.rcp.action.FXAction;
import org.jacp.javafx.rcp.action.FXActionListener;
/**
 * The most abstract component, used to define components as well as perspectives
 * @author Andy Moncsek
 *
 */

public class AComponent implements IComponent<EventHandler<Event>, Event, Object>{
	protected volatile String id;
	private volatile String name;
	private volatile boolean active;
	private volatile boolean started = false;
	protected volatile BlockingQueue<IAction<Event, Object>> globalMessageQueue;
	@Override
	public final IActionListener<EventHandler<Event>, Event, Object> getActionListener(
			Object message) {
		return new FXActionListener(new FXAction(this.id, message),
				this.globalMessageQueue);
	}

	@Override
	public final IActionListener<EventHandler<Event>, Event, Object> getActionListener(
			String targetId, Object message) {
		return new FXActionListener(new FXAction(this.id, targetId, message),
				this.globalMessageQueue);
	}
	
	private final void checkPolicy(String member, String name) {
		if(member !=null || started) {
			throw new UnsupportedOperationException(name);
		}
	}


	/**
	 * {@inheritDoc}
	 */
	@Override
	public final String getId() {
		if (this.id == null) {
			throw new UnsupportedOperationException("No id set");
		} 
		return this.id;

	}

	@Override
	public final void setId(String id) {
		checkPolicy(this.id,"Do Not Set ID manually");
		this.id = id;
	}

	@Override
	public final boolean isActive() {
		return this.active;
	}

	@Override
	public final void setActive(boolean active) {
		this.active = active;

	}
	

	@Override
	public final boolean isStarted() {
		return this.started;
	}

	@Override
	public final String getName() {
		if (this.name == null) {
			throw new UnsupportedOperationException("No name set");
		}
		return this.name;
	}

	@Override
	public final void setName(String name) {		
		checkPolicy(this.name, "Do Not Set NAME manually");
		this.name = name;
	}

}
