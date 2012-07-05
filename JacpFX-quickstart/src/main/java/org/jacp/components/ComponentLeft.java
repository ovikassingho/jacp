/************************************************************************
 * 
 * Copyright (C) 2010 - 2012
 *
 * [ComponentLeft.java]
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
package org.jacp.components;

import java.util.logging.Logger;

import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

import org.jacp.api.action.IAction;
import org.jacp.api.annotations.Component;
import org.jacp.api.annotations.OnStart;
import org.jacp.api.annotations.OnTearDown;
import org.jacp.javafx.rcp.component.AFXComponent;
import org.jacp.javafx.rcp.componentLayout.FXComponentLayout;
import org.jacp.javafx.rcp.util.FXUtil.MessageUtil;

/**
 * A simple JacpFX UI component
 * 
 * @author Andy Moncsek
 * 
 */
@Component(defaultExecutionTarget = "Pleft", id = "id001", name = "componentLeft", active = true)
public class ComponentLeft extends AFXComponent {
	private AnchorPane pane;
	private Label leftLabel;
	private Logger log = Logger.getLogger(ComponentLeft.class.getName());

	@Override
	/**
	 * The handleAction method always runs outside the main application thread. You can create new nodes, execute long running tasks but you are not allowed to manipulate existing nodes here.
	 */
	public Node handleAction(IAction<Event, Object> action) {
		// runs in worker thread
		if (action.getLastMessage().equals(MessageUtil.INIT)) {
			return createUI();
		}
		return null;
	}

	@Override
	/**
	 * The postHandleAction method runs always in the main application thread.
	 */
	public Node postHandleAction(Node arg0, IAction<Event, Object> action) {
		// runs in FX application thread
		if (action.getLastMessage().equals(MessageUtil.INIT)) {
			this.pane = (AnchorPane) arg0;
		} else {
			leftLabel.setText(action.getLastMessage().toString());
		}
		return this.pane;
	}

	@OnStart
	/**
	 * The @OnStart annotation labels methods executed when the component switch from inactive to active state
	 * @param arg0
	 */
	public void onStartComponent(FXComponentLayout arg0) {
		log.info("run on start of ComponentLeft ");

	}

	@OnTearDown
	/**
	 * The @OnTearDown annotations labels methods executed when the component is set to inactive
	 * @param arg0
	 */
	public void onTearDownComponent(FXComponentLayout arg0) {
		log.info("run on tear down of ComponentLeft ");

	}

	/**
	 * create the UI on first call
	 * 
	 * @return
	 */
	private Node createUI() {


		AnchorPane anchor = new AnchorPane();
		anchor.getStyleClass().add("roundedAnchorPaneFX");
		final Label heading = new Label("JavaFX component");
		heading.setAlignment(Pos.CENTER);
		heading.getStyleClass().add("propLabel");
		anchor.getChildren().add(heading);
		
		
		AnchorPane.setTopAnchor(heading, 10.0);	

		final Button left = new Button("Left");
		left.setLayoutY(120);
		left.setOnMouseClicked(getMessage());
		left.setAlignment(Pos.CENTER);
		anchor.getChildren().add(left);
		AnchorPane.setTopAnchor(left, 80.0);	
		
		leftLabel = new Label("");			
		leftLabel.setAlignment(Pos.CENTER);
		anchor.getChildren().add(leftLabel);
		AnchorPane.setTopAnchor(leftLabel, 50.0);	
		
		GridPane.setMargin(anchor, new Insets(5, 2, 4, 5));
		GridPane.setHgrow(anchor, Priority.ALWAYS);
		GridPane.setVgrow(anchor, Priority.ALWAYS);

		return anchor;
	}

	private EventHandler<Event> getMessage() {
		return new EventHandler<Event>() {
			@Override
			public void handle(Event arg0) {
				getActionListener("id01.id003", "hello stateful component").performAction(arg0);
			}
		};
	}

}
