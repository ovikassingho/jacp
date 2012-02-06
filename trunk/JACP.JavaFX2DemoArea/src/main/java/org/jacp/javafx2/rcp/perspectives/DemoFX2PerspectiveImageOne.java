package org.jacp.javafx2.rcp.perspectives;

import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.SplitPane;
import javafx.scene.control.ToolBar;
import javafx.scene.effect.DropShadow;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;

import org.jacp.api.action.IAction;
import org.jacp.api.action.IActionListener;
import org.jacp.api.util.ToolbarPosition;
import org.jacp.javafx2.rcp.componentLayout.FX2ComponentLayout;
import org.jacp.javafx2.rcp.componentLayout.FX2PerspectiveLayout;
import org.jacp.javafx2.rcp.components.CustomOptionPane;
import org.jacp.javafx2.rcp.components.optionPane.JACPDialogButton;
import org.jacp.javafx2.rcp.components.optionPane.JACPDialogUtil;
import org.jacp.javafx2.rcp.components.optionPane.JACPModalDialog;
import org.jacp.javafx2.rcp.components.optionPane.JACPOptionPane;
import org.jacp.javafx2.rcp.components.toolBar.JACPToolBar;
import org.jacp.javafx2.rcp.perspective.AFX2Perspective;

/**
 * Demo perspective class for jacp JavaFX2 implementation
 * 
 * @author Andy Moncsek
 * 
 */
public class DemoFX2PerspectiveImageOne extends AFX2Perspective {

	private Pane p = null;

	private StackPane mainStackPane = null;

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
		mainStackPane = new StackPane();

		BorderPane mainLayout = new BorderPane();

		// register left button menu
		GridPane leftMenu = new GridPane();

		perspectiveLayout.registerTargetLayoutComponent("PleftMenu", leftMenu);

		// Breadcrumb (top Component)
		GridPane breadCrumbBar = new GridPane();

		perspectiveLayout.registerTargetLayoutComponent("PBreadCrumb",
				breadCrumbBar);
		mainLayout.setTop(breadCrumbBar);

		// Main Content Area to the right

		// register main content
		GridPane mainContent = new GridPane();
		perspectiveLayout.registerTargetLayoutComponent("PmainContent",
				mainContent);

		SplitPane splitPane = new SplitPane();

		Pane main = new Pane();
		// mainContent.getStyleClass().add("dark");
		mainContent.setStyle("-fx-background-color:#00ff00;");
		mainContent.minWidthProperty().bind(main.widthProperty());
		mainContent.minHeightProperty().bind(main.heightProperty());
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

		final IActionListener<EventHandler<Event>, Event, Object> listenerBottomOne = getActionListener("id02", "oneButtonBottomOne");
		Button bc = new Button("Button Perspective 1");
		bc.setStyle("-fx-background-color: slateblue; -fx-text-fill: white;");
		bc.setOnMouseEntered((EventHandler<? super MouseEvent>) listenerBottomOne);

		// Register all Components
		GridPane.setVgrow(mainStackPane, Priority.ALWAYS);
		GridPane.setHgrow(mainStackPane, Priority.ALWAYS);
		mainStackPane.getChildren().add(mainLayout);

		p = new Pane();

		VBox menu = new VBox();
		menu.setPrefWidth(230);
		menu.setMaxWidth(230);
		menu.setPrefHeight(400);
		menu.setMaxHeight(400);
		p.getChildren().add(menu);

		ToolBar toolbar = new ToolBar();
		for (int i = 0; i < 5; i++) {
			Button test = new Button("test");
			toolbar.getItems().add(test);
		}

		Pane arrow = new Pane();
		arrow.setMinHeight(10);
		arrow.setId("top-arrow");

		Pane mainMenuContent = new Pane();
		mainMenuContent.setStyle("-fx-background-color:#CCCCCC;");
		Button check = new Button("check");

		menu.getChildren().add(arrow);
		mainMenuContent.getChildren().add(check);
		menu.getChildren().add(toolbar);
		menu.getChildren().add(mainMenuContent);

		mainStackPane.getChildren().add(p);

		p.setVisible(false);

		perspectiveLayout.registerRootComponent(mainStackPane);
		// register bottom Picture Bar

	}

	@Override
	public void onStartPerspective(final FX2ComponentLayout layout) {
		System.out.println("run on start perspective one bar:"
				+ layout.getMenu() + " bars:"
				+ layout.getRegisteredToolBar(ToolbarPosition.NORTH));
		final IActionListener<EventHandler<Event>, Event, Object> listenerBottomOne = getActionListener("id02", "oneButtonBottomOne");
		Button bc = new Button("Button Perspective 1");
		bc.setOnMouseClicked((EventHandler<? super MouseEvent>) listenerBottomOne);
		ToolBar south = layout.getRegisteredToolBar(ToolbarPosition.SOUTH);
		south.getItems().add(bc);

		JACPToolBar north = (JACPToolBar) layout
				.getRegisteredToolBar(ToolbarPosition.NORTH);

		Button custom = new Button("login");
		custom.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent e) {

				JACPModalDialog.getInstance().showModalMessage(
						new CustomOptionPane());
			}
		});

		Button glasspane = new Button("glassPane");
		glasspane.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent e) {

				Pane p = layout.getGlassPane();
				p.setEffect(new DropShadow());

				VBox menu = new VBox();
				menu.setPrefWidth(225);
				menu.setMaxWidth(225);
				menu.setPrefHeight(400);
				menu.setMaxHeight(400);
				p.getChildren().add(menu);

				ToolBar toolbar = new ToolBar();
				toolbar.getStyleClass().add("hover-menu-toolbar");
				for (int i = 0; i < 2; i++) {
					Button test = new Button("test");
					toolbar.getItems().add(test);
				}

				Pane arrow = new Pane();
				arrow.setMinHeight(10);
				arrow.setId("top-arrow");

				Pane mainMenuContent = new Pane();
				mainMenuContent.setStyle("-fx-background-color:#373837");
				Button check = new Button("check");

				TilePane flowPane = new TilePane();
				flowPane.setPrefColumns(3);

				for (int i = 0; i < 9; i++) {
					Pane r = new Pane();
					r.setPrefWidth(64);
					r.setPrefHeight(64);
					r.getStyleClass().add("mockIcon");
					TilePane.setMargin(r, new Insets(5));
					flowPane.getChildren().add(r);
				}

				menu.getChildren().add(arrow);
				mainMenuContent.getChildren().add(flowPane);
				menu.getChildren().add(toolbar);
				menu.getChildren().add(mainMenuContent);
				p.maxWidthProperty().bind(menu.widthProperty());
				p.maxHeightProperty().bind(menu.heightProperty());

				p.setVisible(!p.isVisible());

			}

		});

		north.addOnEnd(glasspane);

		Button bv2 = new Button("option pane");
		bv2.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent e) {
				JACPOptionPane dialog = JACPDialogUtil.createOptionPane(
						"JACP Option Pane", "This is a JACP OptionPane.");
				dialog.setDefaultButton(JACPDialogButton.NO);
				dialog.setDefaultCloseButtonOrientation(Pos.CENTER_RIGHT);
				dialog.setOnYesAction(new EventHandler<ActionEvent>() {
					@Override
					public void handle(ActionEvent arg0) {
						System.out.println("Click");
					}
				});

				dialog.setOnNoAction(new EventHandler<ActionEvent>() {
					@Override
					public void handle(ActionEvent arg0) {
						System.out.println("CLACK");

					}
				});
				dialog.showDialog();

			}
		});
		north.add(bv2);
		north.addOnEnd(custom);

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
