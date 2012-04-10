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
package org.jacp.javafx.rcp.component;

import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.Node;

import org.jacp.api.action.IAction;
import org.jacp.api.component.IComponentView;

/**
 * represents a basic FX2 component to extend from, uses this abstract class to
 * create UI components
 * 
 * @author Andy Moncsek
 */
public abstract class AFXComponent extends ASubComponent implements
		IComponentView<Node, EventHandler<Event>, Event, Object> {

	private Node root;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final Node getRoot() {
		return this.root;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	@SuppressWarnings("unchecked")
	public final <C> C handle(final IAction<Event, Object> action) {
		return (C) this.handleAction(action);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final Node postHandle(final Node node,
			final IAction<Event, Object> action) {
		return this.postHandleAction(node, action);
	}

	/**
	 * @see org.jacp.api.component.IHandleable#handle(IAction) {@inheritDoc}
	 * @param action
	 * @return a node
	 */
	public abstract Node handleAction(final IAction<Event, Object> action);

	/**
	 * @see org.jacp.api.component.IVComponent#postHandle(Object, IAction)
	 *      {@inheritDoc}
	 * @param node
	 * @param action
	 * @return a node
	 */
	public abstract Node postHandleAction(final Node node,
			final IAction<Event, Object> action);

}
