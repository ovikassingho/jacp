/*
 * Copyright (C) 2010,2011.
 * AHCP Project (http://code.google.com/p/jacp)
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
package org.jacp.demo.perspectives;

import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Orientation;
import javafx.scene.control.Button;
import javafx.scene.control.SplitPane;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;

import org.jacp.api.action.IAction;
import org.jacp.api.action.IActionListener;
import org.jacp.api.util.ToolbarPosition;
import org.jacp.javafx2.rcp.componentLayout.FX2ComponentLayout;
import org.jacp.javafx2.rcp.componentLayout.FX2PerspectiveLayout;
import org.jacp.javafx2.rcp.components.toolBar.JACPToolBar;
import org.jacp.javafx2.rcp.perspective.AFX2Perspective;
import org.jacp.javafx2.rcp.util.FX2Util.MessageUtil;

/**
 * Contact perspective; here you define the basic layout for your application
 * view and declare targets for your UI components.
 * 
 * @author Andy Moncsek
 * 
 */
public class ContactPerspective extends AFX2Perspective {

	private String topId = "PmainContentTop";
	private String bottomId = "PmainContentBottom";

	@Override
	/**
	 * create buttons in tool bars; menu entries  
	 */
	public void onStartPerspective(final FX2ComponentLayout layout) {
		// create button in toolbar; button should switch top and bottom id's
		final JACPToolBar north = (JACPToolBar) layout
				.getRegisteredToolBar(ToolbarPosition.NORTH);

		final Button custom = new Button("switch");
		custom.setTooltip(new Tooltip("Switch Components"));
		custom.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(final ActionEvent e) {
				final IActionListener<EventHandler<Event>, Event, Object> listener = ContactPerspective.this
						.getActionListener("switch");
				listener.getAction().setMessage("switch");
				listener.performAction(null);

			}
		});
		north.addOnEnd(custom);
	}

	@Override
	public void onTearDownPerspective(final FX2ComponentLayout layout) {

	}

	@Override
	public void handlePerspective(final IAction<Event, Object> action,
			final FX2PerspectiveLayout perspectiveLayout) {
		if (action.getLastMessage().equals(MessageUtil.INIT)) {
			this.createPerspectiveLayout(perspectiveLayout);
		} else if (action.getLastMessage().equals("switch")) {
			final String tmp = this.topId;
			this.topId = this.bottomId;
			this.bottomId = tmp;
			this.createPerspectiveLayout(perspectiveLayout);
		}
	}

	private void createPerspectiveLayout(
			final FX2PerspectiveLayout perspectiveLayout) {

		final BorderPane mainLayout = new BorderPane();
		// create left button menu
		final GridPane leftMenu = new GridPane();
		leftMenu.getStyleClass().add("dark-border");
		// GridPane.setHgrow(leftMenu, Priority.ALWAYS);
		// GridPane.setVgrow(leftMenu, Priority.ALWAYS);

		// create main content right
		final SplitPane splitPaneRight = new SplitPane();
		splitPaneRight.setOrientation(Orientation.VERTICAL);
		splitPaneRight.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
		splitPaneRight.setDividerPosition(0, 0.55f);
		splitPaneRight.setId("v-splitpane");

		// create main content Top
		final GridPane mainContentTop = new GridPane();
		// create main content Bottom
		final GridPane mainContentBottom = new GridPane();

		splitPaneRight.getItems().addAll(mainContentTop, mainContentBottom);

		// Main Content Area to the right
		final SplitPane splitPane = new SplitPane();
		splitPane.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
		splitPane.setDividerPosition(0, 0.25f);
		splitPane.getItems().addAll(leftMenu, splitPaneRight);
		splitPane.setId("h-splitpane");

		mainLayout.setCenter(splitPane);
		GridPane.setVgrow(mainLayout, Priority.ALWAYS);
		GridPane.setHgrow(mainLayout, Priority.ALWAYS);

		// Register root component
		perspectiveLayout.registerRootComponent(mainLayout);
		// register left menu
		perspectiveLayout.registerTargetLayoutComponent("PleftMenu", leftMenu);
		// register main content Top
		perspectiveLayout.registerTargetLayoutComponent(this.topId,
				mainContentTop);
		// register main content Bottom
		perspectiveLayout.registerTargetLayoutComponent(this.bottomId,
				mainContentBottom);
	}

}
