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
package org.jacp.javafx2.rcp.util;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;

import org.jacp.api.component.IBGComponent;

/**
 * this class handles running stateful background components
 * 
 * @author Andy Moncsek
 * 
 */
public class StateComponentRunWorker
		extends
		AFX2ComponentWorker<IBGComponent<EventHandler<ActionEvent>, ActionEvent, Object>> {
	private final IBGComponent<EventHandler<ActionEvent>, ActionEvent, Object> component;
	
	public StateComponentRunWorker(final IBGComponent<EventHandler<ActionEvent>, ActionEvent, Object> component) {
		this.component = component;
	}
	@Override
	protected IBGComponent<EventHandler<ActionEvent>, ActionEvent, Object> call()
			throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

}
