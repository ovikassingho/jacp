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

import java.util.Map;
import java.util.concurrent.ExecutionException;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.MenuBar;
import org.jacp.api.action.IAction;
import org.jacp.api.component.IVComponent;
import org.jacp.api.componentLayout.Layout;

/**
 * Background Worker to execute components; handle method to init component
 * @author Andy Moncsek
 */
public class FX2ComponentInitWorker extends AFX2ComponentWorker<IVComponent<Node, EventHandler, ActionEvent, Object>> {

    private final Map<String, Node> targetComponents;
    private final IVComponent<Node, EventHandler, ActionEvent, Object> component;
    private final Map<Layout, Node> bars;
    private final IAction<ActionEvent, Object> action;
    private final MenuBar menu;

    public FX2ComponentInitWorker(final Map<String, Node> targetComponents, final IVComponent<Node, EventHandler, ActionEvent, Object> component, final Map<Layout, Node> bars, final IAction<ActionEvent, Object> action, final MenuBar menu) {
        this.targetComponents = targetComponents;
        this.component = component;
        this.action = action;
        this.bars = bars;
        this.menu = menu;
    }

    @Override
    protected IVComponent<Node, EventHandler, ActionEvent, Object> call() throws Exception {
        synchronized (component) {
			component.setBlocked(true);
			log("3.4.4.2.1: subcomponent handle init START: "
					+ component.getName());
			final Node editorComponent = component.handle(action);
			component.setRoot(editorComponent);
			editorComponent.setVisible(true);
			log("3.4.4.2.2: subcomponent handle init get valid container: "
					+ component.getName());
			final Node validContainer = getValidContainerById(
					targetComponents, component.getExecutionTarget());
			log("3.4.4.2.3: subcomponent handle init add component by type: "
					+ component.getName());
			
			addComonent(validContainer, component, bars, menu);

			log("3.4.4.2.4: subcomponent handle init END: "
					+ component.getName());
			component.setBlocked(false);
			return component;
		}
    }
    
    /**
	 * handles "component add" in EDT must be called outside EDT
	 * @param validContainer
	 * @param component
	 * @param bars
	 * @param menu
	 * @throws InterruptedException
	 * @throws InvocationTargetException
	 */
	private void addComonent(
			final Node validContainer,
			final IVComponent<Node, EventHandler, ActionEvent, Object> component,
			final Map<Layout, Node> bars, final MenuBar menu)
			throws InterruptedException{

		 Platform.runLater(new Runnable() {

			@Override
			public void run() {
				// handle
                            //TODO check addComponentByType
				addComponentByType(validContainer, component, bars, menu);

			}
		});

	}
        
        @Override
	public final void done() {
		synchronized (component) {
			try {
				this.get();
			} catch (final InterruptedException e) {
				System.out.println("Exception in Component INIT Worker, Thread interrupted:");
				e.printStackTrace();
				// TODO add to error queue and restart thread if
				// messages in
				// queue
			} catch (final ExecutionException e) {
				System.out.println("Exception in Component INIT Worker, Thread Excecution Exception:");
				e.printStackTrace();
				// TODO add to error queue and restart thread if
				// messages in
				// queue
			} catch (final Exception e) {
				System.out.println("Exception in Component INIT Worker, Thread Exception:");
				e.printStackTrace();
				// TODO add to error queue and restart thread if
				// messages in
				// queue
			}
			component.setBlocked(false);
			component.setBlocked(false);
			// check if news messages received while handled in
			// initialization
			// worker; if so then start replace worker
			if (component.hasIncomingMessage()) {
                            
                            // TODO add replace worker
				//new ComponentReplaceWorker(targetComponents, component, bars,
				//		menu).execute();
			}
		}
	}
}
