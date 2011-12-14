package org.jacp.javafx2.rcp.test1;

import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ToolBar;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import org.jacp.api.action.IAction;
import org.jacp.api.action.IActionListener;
import org.jacp.api.componentLayout.IWorkbenchLayout;
import org.jacp.api.util.ToolbarPosition;
import org.jacp.javafx2.rcp.componentLayout.FX2ComponentLayout;
import org.jacp.javafx2.rcp.workbench.AFX2Workbench;

public class TestOneFX2Workbench extends AFX2Workbench{

	@Override
	public void handleInitialLayout(IAction<Event, Object> action,
			IWorkbenchLayout<Node> layout, Stage stage) {
		layout.setWorkbenchXYSize(1024, 1000);
		layout.registerToolBar(ToolbarPosition.NORTH);
		layout.registerToolBar(ToolbarPosition.SOUTH);
		layout.setStyle(StageStyle.DECORATED);
		layout.setMenuEnabled(true);
		System.out.println("TestOneFX2Workbench action: " + action.getLastMessage());
	}

	@Override
	public void postHandle(FX2ComponentLayout layout) {
		ToolBar north = layout.getRegisteredToolBar(ToolbarPosition.NORTH);
		MenuBar menu = layout.getMenu();
		MenuBar toolBarMenu = new MenuBar();
		final Menu menuFile = new Menu("File");
		final Menu menuDemos = new Menu("Demos");
		final Menu menuToolbarDemos = new Menu("Demos");
		menuFile.getItems().add(menuDemos);
		menuFile.getItems().add(getMenuItemExit());
		menu.getMenus().addAll(menuFile);

		menuDemos.getItems().add(getMenuItemDemo1());
		menuToolbarDemos.getItems().add(getMenuItemDemo1());
		

		toolBarMenu.getMenus().addAll(menuToolbarDemos);
		north.getItems().add(toolBarMenu);
		
	}
	
	private MenuItem getMenuItemExit() {
		MenuItem item = new MenuItem("Exit");
		item.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				System.exit(0);

			}
		});
		
		return item;
	}
	
	private MenuItem getMenuItemDemo1() {
		MenuItem item = new MenuItem("Demo 1");
		item.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				EventHandler<? super MouseEvent> listener2 = getListener(
						"id01", "initWB");
				listener2.handle(null);

			}
		});
		
		return item;
	}
	

	private EventHandler<? super MouseEvent> getListener(final String id,
			final String message) {
		final IActionListener<EventHandler<Event>, Event, Object> listener = getActionListener();
		if (id != null) {
			listener.getAction().addMessage(id, message);
		} else {
			listener.getAction().setMessage(message);
		}
		return (EventHandler<? super MouseEvent>) listener;
	}
}
