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
import javafx.fxml.Initializable;
import javafx.scene.Node;
import org.jacp.api.component.*;
import org.jacp.api.context.Context;
import org.jacp.api.util.UIType;
import org.jacp.javafx.rcp.context.JACPContextImpl;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Represents a basic FX2 component to extend from, uses this abstract class to
 * create UI components.
 * 
 * @author Andy Moncsek
 */
public abstract class AFXComponent extends ASubComponent implements
		IUIComponent<Node, EventHandler<Event>, Event, Object>, IDeclarative,
		Initializable  {

	private volatile Node root;

	private String viewLocation;

	private URL documentURL;

	private ResourceBundle resourceBundle;
	
	private UIType type = UIType.PROGRAMMATIC;
	
	private String localeID="";
	
	private String resourceBundleLocation="";




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
    public final void setRoot(Node root) {
            this.root = root;
    }


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
    /**
     * {@inheritDoc}
     */
	@Override
	public final void initialize(URL url, ResourceBundle resourceBundle) {
		this.documentURL = url;
		this.resourceBundle = resourceBundle;
        setResourceToContext();
	}

    private void setResourceToContext() {
        Context context = this.getContext();
        JACPContextImpl jContext = JACPContextImpl.class.cast(context);
        jContext.setResourceBundle(this.resourceBundle );
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
    /**
     * {@inheritDoc}
     *//*
    @Override
	public void setLocaleID(String localeID) {
		super.checkPolicy(this.localeID, "Do Not Set document manually");
		this.localeID = localeID;
	}*/
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getResourceBundleLocation() {
		return resourceBundleLocation;
	}
    /**
     * {@inheritDoc}
     *//*
    @Override
	public final void setResourceBundleLocation(String resourceBundleLocation) {
		super.checkPolicy(this.resourceBundleLocation, "Do Not Set document manually");
		this.resourceBundleLocation = resourceBundleLocation;
	}*/



}
