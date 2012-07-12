/************************************************************************
 * 
 * Copyright (C) 2010 - 2012
 *
 * [ComponentTop.java]
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
package org.jacp.components;

import java.util.logging.Logger;

import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;

import org.jacp.api.action.IAction;
import org.jacp.api.annotations.Component;
import org.jacp.api.annotations.OnStart;
import org.jacp.api.annotations.OnTearDown;
import org.jacp.javafx.rcp.component.AFXComponent;
import org.jacp.javafx.rcp.componentLayout.FXComponentLayout;
import org.jacp.javafx.rcp.util.FXUtil.MessageUtil;

@Component(defaultExecutionTarget = "PTop", id = "id006", name = "componentTop", active = true, resourceBundleLocation = "bundles.languageBundle", localeID = "en_US")
public class ComponentTop extends AFXComponent {
	private AnchorPane pane;
	private TextField textField;
	private final Logger log = Logger.getLogger(ComponentTop.class.getName());

	@Override
	/**
	 * The handleAction method always runs outside the main application thread. You can create new nodes, execute long running tasks but you are not allowed to manipulate existing nodes here.
	 */
	public Node handleAction(final IAction<Event, Object> action) {
		// runs in worker thread
		if (action.getLastMessage().equals(MessageUtil.INIT)) {
			return this.createUI();
		}
		return null;
	}

	@Override
	/**
	 * The postHandleAction method runs always in the main application thread.
	 */
	public Node postHandleAction(final Node arg0,
			final IAction<Event, Object> action) {
		// runs in FX application thread
		if (action.getLastMessage().equals(MessageUtil.INIT)) {
			this.pane = (AnchorPane) arg0;
		} else {
			this.textField.setText(action.getLastMessage().toString());
		}
		return this.pane;
	}

	@OnStart
	/**
	 * The @OnStart annotation labels methods executed when the component switch from inactive to active state
	 * @param arg0
	 */
	public void onStartComponent(final FXComponentLayout arg0) {
		this.log.info("run on start of ComponentTop ");

	}

	@OnTearDown
	/**
	 * The @OnTearDown annotations labels methods executed when the component is set to inactive
	 * @param arg0
	 */
	public void onTearDownComponent(final FXComponentLayout arg0) {
		this.log.info("run on tear down of ComponentTop ");

	}

	/**
	 * create the UI on first call
	 * 
	 * @return
	 */
	private Node createUI() {
		final AnchorPane anchor = new AnchorPane();
		anchor.getStyleClass().add("roundedAnchorPaneFX");
		final Label heading = new Label(this.getResourceBundle().getString(
				"javafxCompTop"));
		heading.setAlignment(Pos.CENTER);
		heading.getStyleClass().add("propLabel");
		anchor.getChildren().add(heading);

		AnchorPane.setRightAnchor(heading, 50.0);
		AnchorPane.setTopAnchor(heading, 10.0);

		final Button top = new Button(this.getResourceBundle()
				.getString("send"));
		top.setLayoutY(120);
		top.setOnMouseClicked(this.getMessage());
		top.setAlignment(Pos.CENTER);
		anchor.getChildren().add(top);
		AnchorPane.setTopAnchor(top, 280.0);
		AnchorPane.setRightAnchor(top, 25.0);

		this.textField = new TextField("");
		this.textField.getStyleClass().add("synopsisField");
		this.textField.setAlignment(Pos.CENTER);
		anchor.getChildren().add(this.textField);
		AnchorPane.setTopAnchor(this.textField, 50.0);
		AnchorPane.setRightAnchor(this.textField, 25.0);

		GridPane.setHgrow(anchor, Priority.ALWAYS);
		GridPane.setVgrow(anchor, Priority.ALWAYS);

		return anchor;
	}

	private EventHandler<Event> getMessage() {
		return new EventHandler<Event>() {
			@Override
			public void handle(final Event arg0) {
				ComponentTop.this.getActionListener("id01.id003",
						"hello stateful component").performAction(arg0);
			}
		};
	}

}
