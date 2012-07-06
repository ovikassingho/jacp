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
import org.jacp.api.component.IComponentView;
import org.jacp.api.component.IDeclarative;
import org.jacp.api.util.UIType;

/**
 * Represents a basic FX2 component to extend from, uses this abstract class to
 * create UI components.
 * 
 * @author Andy Moncsek
 */
public abstract class AFXComponent extends ASubComponent implements
		IComponentView<Node, EventHandler<Event>, Event, Object>, IDeclarative,
		Initializable  {

	private volatile Node root;

	private String viewLocation;

	private URL documentURL;

	private ResourceBundle resourceBundle;
	
	private UIType type = UIType.PROGRAMMATIC;
	
	private String localeID;
	
	private String resourceBundleLocation;

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
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public final String getViewLocation() {
		if(type.equals(UIType.PROGRAMMATIC))throw new UnsupportedOperationException("Only supported when @DeclarativeComponent annotation is used");
		return viewLocation;
	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	public final void setViewLocation(String document){
		super.checkPolicy(this.viewLocation, "Do Not Set document manually");
		this.viewLocation = document;
		this.type = UIType.DECLARATIVE;
	}

	@Override
	public final void initialize(URL url, ResourceBundle resourceBundle) {
		this.documentURL = url;
		this.resourceBundle = resourceBundle;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final URL getDocumentURL() {
		if(type.equals(UIType.PROGRAMMATIC))throw new UnsupportedOperationException("Only supported when @DeclarativeComponent annotation is used");
		return documentURL;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final ResourceBundle getResourceBundle() {
		return resourceBundle;
	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	public final UIType getType() {
		return type;
	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getLocaleID() {
		return localeID;
	}

	public void setLocaleID(String localeID) {
		super.checkPolicy(this.localeID, "Do Not Set document manually");
		this.localeID = localeID;
	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getResourceBundleLocation() {
		return resourceBundleLocation;
	}

	public void setResourceBundleLocation(String resourceBundleLocation) {
		super.checkPolicy(this.resourceBundleLocation, "Do Not Set document manually");
		this.resourceBundleLocation = resourceBundleLocation;
	}

}
