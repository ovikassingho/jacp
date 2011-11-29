package org.jacp.javafx2.rcp.workbench;

import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ToolBar;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import org.jacp.api.action.IAction;
import org.jacp.api.componentLayout.IWorkbenchLayout;
import org.jacp.api.componentLayout.Layout;
import org.jacp.api.util.ToolbarPosition;
import org.jacp.javafx2.rcp.componentLayout.FX2ComponentLayout;
import org.jacp.javafx2.rcp.workbench.AFX2Workbench;

/**
 * Test workbench for jacp javafx2
 * 
 * @author Andy Moncsek
 * 
 */
public class DemoFX2Workbench extends AFX2Workbench {
	private double mouseDragOffsetX = 0;
	private double mouseDragOffsetY = 0;
	private Stage stage;

	public DemoFX2Workbench() {
		super("UnitTestWorkbench");

	}

	@Override
	public void handleInitialLayout(final IAction<Event, Object> action,
			final IWorkbenchLayout<Node> layout, final Stage stage) {
		this.stage = stage;
		System.out.println("1: " + action.getLastMessage());
		layout.setWorkbenchXYSize(1024, 400);
		//layout.registerToolBar(Layout.NORTH);
		layout.registerToolBar(ToolbarPosition.NORTH);
	//	layout.registerToolBar(ToolbarPosition.EAST);
	//	layout.registerToolBar(ToolbarPosition.WEST);
		layout.registerToolBar(ToolbarPosition.SOUTH);
		//layout.registerToolBar(Layout.SOUTH);
		layout.setStyle(StageStyle.DECORATED);
		layout.setMenuEnabled(true);
		System.out.println("2: " + action.getLastMessage());
	}

	@Override
	public void postHandle(FX2ComponentLayout layout) {

		// ////////////// TOOLBAR ////////////////////////7
		final ToolBar topBar = layout.getRegisteredToolBar(ToolbarPosition.NORTH);
		// add window dragging
		topBar.setOnMousePressed(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				mouseDragOffsetX = event.getSceneX();
				mouseDragOffsetY = event.getSceneY();
			}
		});
		topBar.setOnMouseDragged(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {

				stage.setX(event.getScreenX() - mouseDragOffsetX);
				stage.setY(event.getScreenY() - mouseDragOffsetY);

			}
		});
		Button close = new Button("close");
		close.setOnMouseClicked(new EventHandler<Event>() {

			@Override
			public void handle(Event arg0) {
				System.exit(0);

			}
		});
		topBar.getItems().add(close);

		// ////////////TOOLBAR END ////////////////////

		// ////////////MENU //////////////////////

		MenuBar menu = layout.getMenu();
		final Menu menu1 = new Menu("File");
		final MenuItem item = new MenuItem("Exit");
		item.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent arg0) {
				System.exit(0);

			}
		});
		menu1.getItems().add(item);
		menu.getMenus().addAll(menu1);

		// /////////////MENU END //////////////////
	}

}
