/************************************************************************
 * 
 * Copyright (C) 2010 - 2012
 *
 * [JACPMenuBar.java]
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
package org.jacp.javafx2.rcp.components.menuBar;

import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.ToolBar;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.stage.Stage;

/**
 * The Class JACPMenuBar.
 * 
 * @author psy
 *
 */
public class JACPMenuBar extends HBox {

	/** The left bar. */
	private ToolBar leftBar;

	/** The right bar. */
	private ToolBar rightBar;

	/** The main bar. */
	private MenuBar mainBar;

	/**
	 * Instantiates a new jACP menu bar.
	 */
	public JACPMenuBar() {
		initMenuBar();
	}

	/**
	 * Inits the menu bar.
	 */
	private void initMenuBar() {
		leftBar = new ToolBar();
		leftBar.setMinWidth(0);
		rightBar = new ToolBar();
		rightBar.setMinWidth(0);
		mainBar = new MenuBar();

		setAlignment(Pos.CENTER_LEFT);
		HBox.setHgrow(mainBar, Priority.ALWAYS);
		HBox.setHgrow(leftBar, Priority.NEVER);
		HBox.setHgrow(rightBar, Priority.NEVER);

		getStyleClass().addAll(mainBar.getStyleClass());
		clearBackground(leftBar, mainBar, rightBar);

		// rightBar.getItems().addAll(b, b1, b2);
		bind(rightBar);
		this.getChildren().addAll(leftBar, mainBar, rightBar);
	}

	private void clearBackground(Node... node) {
		if (node != null) {
			for (final Node n : node) {
				n.setStyle("-fx-background-color: transparent;");
			}
		}
	}

	private void bind(ToolBar bar) {
		bar.maxHeightProperty().bind(mainBar.heightProperty());
		bar.prefHeightProperty().bind(mainBar.heightProperty());
	}

	/**
	 * Gets the menus.
	 *
	 * @return the menus
	 */
	public ObservableList<Menu> getMenus() {
		return mainBar.getMenus();
	}

	/**
	 * Adds the node.
	 *
	 * @param node the node
	 * @param orientation the orientation
	 */
	public void addNode(JACPMenuBarButtonOrientatioin orientation, Node... node) {
		if (JACPMenuBarButtonOrientatioin.LEFT.equals(orientation)) {
			leftBar.getItems().addAll(node);
		} else {
			rightBar.getItems().addAll(node);
		}
	}
	
	
	/** The mouse drag offset x. */
	private double mouseDragOffsetX = 0;

	/** The mouse drag offset y. */
	private double mouseDragOffsetY = 0;

	private void setMenuDragEnabled(JACPMenuBar bar, final Stage stage) {

		bar.setOnMousePressed(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				mouseDragOffsetX = event.getSceneX();
				mouseDragOffsetY = event.getSceneY();
			}
		});

		bar.setOnMouseDragged(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				// if (!windowButtons.isMaximized()) {
				stage.setX(event.getScreenX() - mouseDragOffsetX);
				stage.setY(event.getScreenY() - mouseDragOffsetY);
				// }
			}
		});
	}
	
	
	
}
