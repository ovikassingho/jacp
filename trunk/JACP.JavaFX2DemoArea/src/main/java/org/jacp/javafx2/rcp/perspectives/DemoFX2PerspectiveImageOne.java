package org.jacp.javafx2.rcp.perspectives;

import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.SplitPane;
import javafx.scene.control.ToolBar;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;

import org.jacp.api.action.IAction;
import org.jacp.api.action.IActionListener;
import org.jacp.api.util.ToolbarPosition;
import org.jacp.javafx2.rcp.componentLayout.FX2ComponentLayout;
import org.jacp.javafx2.rcp.componentLayout.FX2PerspectiveLayout;
import org.jacp.javafx2.rcp.perspective.AFX2Perspective;

/**
 * Demo perspective class for jacp JavaFX2 implementation
 * 
 * @author Andy Moncsek
 * 
 */
public class DemoFX2PerspectiveImageOne extends AFX2Perspective {

	@Override
	public void handlePerspective(IAction<Event, Object> action,
			FX2PerspectiveLayout perspectiveLayout) {
		/*
		 * 
		 * ----------------------------------------
		 * |              BreadCrumb              |
		 * ----------------------------------------
		 * |         |                            |
		 * | Left-   |        MainContent         |
		 * | Buttons |                            |
		 * |         |                            |
		 * ----------------------------------------
		 * |              BottomBar               |
		 * ----------------------------------------
		 * 
		 * 
		 */

		BorderPane mainLayout = new BorderPane();

		// register left button menu
		GridPane leftMenu = new GridPane();

		perspectiveLayout.registerTargetLayoutComponent("PleftMenu", leftMenu);

		// register main content

		GridPane mainContent = new GridPane();
		perspectiveLayout.registerTargetLayoutComponent("PmainContent",
				mainContent);

		// Breadcrumb (top Component)
		GridPane breadCrumbBar = new GridPane();

		perspectiveLayout.registerTargetLayoutComponent("PBreadCrumb",
				breadCrumbBar);
		mainLayout.setTop(breadCrumbBar);

		// Main Content Area to the right

		SplitPane splitPane = new SplitPane();

		Pane main = new Pane();
		// GridPane.setHgrow(mainContent, Priority.ALWAYS);
		// GridPane.setVgrow(mainContent, Priority.ALWAYS);
		main.getChildren().add(mainContent);

		splitPane.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
		splitPane.setDividerPosition(0, 0.25f);
		splitPane.getItems().addAll(leftMenu, main);
		splitPane.setId("page-splitpane");

		mainLayout.setCenter(splitPane);

		// Bottom Picture Bar (bottom)

		GridPane bottomBar = new GridPane();

		perspectiveLayout
				.registerTargetLayoutComponent("PbottomBar", bottomBar);
		mainLayout.setBottom(bottomBar);
		bottomBar.setStyle("-fx-border-color: black;");

		final IActionListener<EventHandler<Event>, Event, Object> listenerBottomOne = getActionListener();
		listenerBottomOne.getAction().addMessage("id02", "oneButtonBottomOne");
		Button bc = new Button("Button Perspective 1");
		bc.setStyle("-fx-background-color: slateblue; -fx-text-fill: white;");
		bc.setOnMouseEntered((EventHandler<? super MouseEvent>) listenerBottomOne);

		// Register all Components
		GridPane.setVgrow(mainLayout, Priority.ALWAYS);
		GridPane.setHgrow(mainLayout, Priority.ALWAYS);
		perspectiveLayout.registerRootComponent(mainLayout);
		// register bottom Picture Bar

	}

	@Override
	public void onStartPerspective(final FX2ComponentLayout layout) {
		System.out.println("run on start perspective one bar:"
				+ layout.getMenu() + " bars:"
				+ layout.getRegisteredToolBar(ToolbarPosition.NORTH));
		final IActionListener<EventHandler<Event>, Event, Object> listenerBottomOne = getActionListener();
		listenerBottomOne.getAction().addMessage("id02", "oneButtonBottomOne");
		Button bc = new Button("Button Perspective 1");
		bc.setOnMouseClicked((EventHandler<? super MouseEvent>) listenerBottomOne);
		ToolBar south = layout.getRegisteredToolBar(ToolbarPosition.SOUTH);
		south.getItems().add(bc);

		MenuBar menu = layout.getMenu();
		final Menu menu2 = new Menu("Options");
		final Menu menu3 = new Menu("Help");

		menu.getMenus().addAll(menu2, menu3);

	}

	@Override
	public void onTearDownPerspective(final FX2ComponentLayout layout) {
		// TODO Auto-generated method stub

	}

}
