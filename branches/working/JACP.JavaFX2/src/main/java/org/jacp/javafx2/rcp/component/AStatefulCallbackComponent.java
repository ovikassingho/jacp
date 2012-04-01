/************************************************************************
 * 
 * Copyright (C) 2010 - 2012
 *
 * [ACallbackComponent.java]
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
package org.jacp.javafx2.rcp.component;

import javafx.event.Event;
import javafx.event.EventHandler;

import org.jacp.api.action.IAction;
import org.jacp.api.component.IStatefulCallbackComponent;

/**
 * represents a basic, stateful background component
 * 
 * @author Andy Moncsek
 *  
 */
public abstract class AStatefulCallbackComponent extends AFXSubComponent implements
		IStatefulCallbackComponent<EventHandler<Event>, Event, Object> {

	private volatile String handleComponentTarget;

	@SuppressWarnings("unchecked")
	@Override
	/**
	 * {@inheritDoc}
	 */
	public <C> C handle(final IAction<Event, Object> action) {
		return (C) this.handleAction(action);
	}

	public abstract Object handleAction(IAction<Event, Object> action);

	@Override
	/**
	 * {@inheritDoc}
	 */
	public final String getHandleTargetAndClear() {
		final String tempTarget = String.valueOf(this.handleComponentTarget);
		this.handleComponentTarget = null;
		return tempTarget;
	}

	@Override
	/**
	 * {@inheritDoc}
	 */
	public final void setHandleTarget(final String componentTargetId) {
		this.handleComponentTarget = componentTargetId;
	}

}
