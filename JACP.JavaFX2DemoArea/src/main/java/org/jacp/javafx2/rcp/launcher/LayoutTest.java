package org.jacp.javafx2.rcp.launcher;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ToolBar;
import javafx.stage.Stage;
import net.miginfocom.layout.CC;

import org.tbee.javafx.scene.layout.MigPane;

public class LayoutTest extends Application {

	@Override
	public void start(Stage stage) throws Exception {
		MigPane mig = new MigPane("fill");

		ToolBar topbar = new ToolBar();
		ToolBar leftbar = new ToolBar();
		ToolBar rightbar = new ToolBar();
		ToolBar bottombar = new ToolBar();
		MenuBar menuBar = new MenuBar();

		final Menu menu1 = new Menu("File");
		final MenuItem menu12 = new MenuItem("Open");
		final Menu menu2 = new Menu("Options");
		final Menu menu3 = new Menu("Help");

		final Button bTop = new Button("press");
		final Button bBottom = new Button("press");
		final Button sBottom = new Button("press");
		final Button rBotton = new Button("press");
		sBottom.setRotate(-90);
		rBotton.setRotate(90);

		menuBar.setPrefWidth(800);
		leftbar.getItems().add(sBottom);
		leftbar.setPrefHeight(600);

		rightbar.getItems().add(rBotton);
		rightbar.setPrefHeight(600);
		menu1.getItems().add(menu12);

		menuBar.getMenus().addAll(menu1, menu2, menu3);
		topbar.getItems().add(bTop);
		bottombar.getItems().add(bBottom);
		mig.add(rightbar, new CC().dockEast());
		mig.add(leftbar, new CC().dockWest());
		mig.add(topbar, new CC().dockNorth());
		mig.add(bottombar, new CC().dockSouth());

		// pane.setTop(topbar);
		stage.setScene(new Scene(mig, 800, 600));
		stage.show();
	}

	public static void main(String[] args) {
		launch(args);
	}
}
