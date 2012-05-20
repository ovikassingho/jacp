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

import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.Initializable;
import javafx.scene.Node;

import org.jacp.api.action.IAction;
import org.jacp.api.component.IDeclarativComponentView;

/**
 * A FXML component defines it's view trough a *.fxml file. the handle and
 * postHandle method do not have return values because the component always
 * returns the parent node of the FXML file
 * 
 * @author Andy Moncsek
 * 
 */
public abstract class AFXMLComponent extends ASubComponent implements
		IDeclarativComponentView<Node, EventHandler<Event>, Event, Object>,
		Initializable {

	private Node root;

	private String document;

	private URL documentURL;

	private ResourceBundle resourceBundle;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final String getDocument() {
		return document;
	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	public final void setDocument(String document){
		super.checkPolicy(this.document, "Do Not Set document manually");
		this.document = document;
	}

	

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
	public final URL getDocumentURL() {
		return documentURL;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ResourceBundle getResourceBundle() {
		return resourceBundle;
	}

	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("unchecked")
	@Override
	public final <C> C handle(final IAction<Event, Object> action) {
		return (C) this.handleAction(action);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final void postHandle(final Node xmlParent, final Node handleNode,
			final IAction<Event, Object> action) {
		postHandleAction(xmlParent, handleNode, action);
	}

	/**
	 * Handles action on component call, this method is always executed in an
	 * worker thread. NEVER manipulate UI components in this method, use
	 * postHandle instead.
	 * 
	 * @see org.jacp.api.component.IHandleable#handle(IAction) {@inheritDoc}
	 * @param action ; the triggering action
	 * @return a node
	 */
	public abstract Node handleAction(final IAction<Event, Object> action);

	/**
	 * @see org.jacp.api.component.IDeclarativComponentView#postHandle(Node,
	 *      Node, IAction) {@inheritDoc}
	 * @param node
	 * @param action
	 */
	public abstract void postHandleAction(final Node xmlParent,
			final Node handleNode, final IAction<Event, Object> action);

	@Override
	public final void initialize(URL url, ResourceBundle resourceBundle) {
		this.documentURL = url;
		this.resourceBundle = resourceBundle;
	}

}
