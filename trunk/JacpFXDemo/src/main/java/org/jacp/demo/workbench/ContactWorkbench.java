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
package org.jacp.demo.workbench;

import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.scene.text.TextBuilder;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import org.jacp.api.action.IAction;
import org.jacp.api.componentLayout.IWorkbenchLayout;
import org.jacp.api.util.ToolbarPosition;
import org.jacp.javafx2.rcp.componentLayout.FX2ComponentLayout;
import org.jacp.javafx2.rcp.components.optionPane.JACPModalDialog;
import org.jacp.javafx2.rcp.workbench.AFX2Workbench;

/**
 * Workbench for contact demo with JacpFX (JavaFX2 and Spring). The workbench is
 * the root node of your JacpFX application.
 * 
 * @author Andy Moncsek
 * 
 */
public class ContactWorkbench extends AFX2Workbench {


	private final String projectURL = "http://code.google.com/p/jacp/wiki/Documentation";
	private final String message = "JacpFX is a Framework to create Rich Clients in MVC style with JavaFX 2, Spring and an Actor like component approach. It provides a simple API to create a workspace, perspectives, components and to compose your Client application easily. More Info see: ";

	@Override
	public void handleInitialLayout(final IAction<Event, Object> action,
			final IWorkbenchLayout<Node> layout, final Stage stage) {
		layout.setWorkbenchXYSize(1024, 768);
		layout.registerToolBar(ToolbarPosition.NORTH);
		layout.setStyle(StageStyle.DECORATED);
		layout.setMenuEnabled(true);
	}

	@Override
	public void postHandle(final FX2ComponentLayout layout) {
		final MenuBar menu = layout.getMenu();
		final Menu menuFile = new Menu("File");
		final MenuItem itemExit = new MenuItem("Exit");
		itemExit.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(final ActionEvent arg0) {
				System.exit(0);

			}
		});

		final MenuItem info = new MenuItem("Info");
		info.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(final ActionEvent arg0) {
				final VBox box = new VBox();
				box.setId("HelpDialog");
				box.setMaxSize(500, Region.USE_PREF_SIZE);
				// the title
				final Label title = new Label("JacpFX project demo");
				Hyperlink link = new Hyperlink();
				link.setText(projectURL);
				link.setOnAction(new EventHandler<ActionEvent>() {
					@Override
					public void handle(ActionEvent event) {
						WebView wv = new WebView();
						wv.getEngine().load(projectURL);
						Scene scene = new Scene(wv, 1024, 768);
						Stage stage = new Stage();
						stage.setTitle("JacpFX documentation");
						stage.setScene(scene);
						stage.show();
					}
				});
				title.setId("jacp-custom-title");
				VBox.setMargin(title, new Insets(2, 2, 10, 2));
				final Button ok = new Button("OK");
				HBox.setMargin(ok, new Insets(6, 5, 4, 2));
				final HBox hboxButtons = new HBox();
				hboxButtons.setAlignment(Pos.CENTER_RIGHT);
				ok.setOnAction(new EventHandler<ActionEvent>() {

					@Override
					public void handle(final ActionEvent actionEvent) {
						JACPModalDialog.getInstance().hideModalMessage();
					}
				});

				Text textRef = TextBuilder.create().layoutY(100)
						.textOrigin(VPos.TOP)
						.textAlignment(TextAlignment.JUSTIFY)
						.wrappingWidth(400).text(message).fill(Color.WHITE)
						.build();
				hboxButtons.getChildren().addAll(ok);

				box.getChildren().addAll(title, textRef, link, hboxButtons);
				JACPModalDialog.getInstance().showModalMessage(box);

			}
		});

		menuFile.getItems().addAll(itemExit, info);
		menu.getMenus().addAll(menuFile);

	}

}
