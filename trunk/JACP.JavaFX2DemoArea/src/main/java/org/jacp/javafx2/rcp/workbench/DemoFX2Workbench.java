package org.jacp.javafx2.rcp.workbench;

import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ToolBar;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import org.jacp.api.action.IAction;
import org.jacp.api.componentLayout.IWorkbenchLayout;
import org.jacp.api.componentLayout.Layout;
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

	public DemoFX2Workbench() {
		super("UnitTestWorkbench");

	}

	@Override
	public void handleInitialLayout(final IAction<Event, Object> action,
			final IWorkbenchLayout<Node> layout, final Stage stage ) {
		System.out.println("1: "+action.getLastMessage());
		layout.setWorkbenchXYSize(1024, 400);
		final ToolBar topBar = new ToolBar();
		 // add window dragging
		topBar.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override public void handle(MouseEvent event) {
                mouseDragOffsetX = event.getSceneX();
                mouseDragOffsetY = event.getSceneY();
            }
        });
		topBar.setOnMouseDragged(new EventHandler<MouseEvent>() {
            @Override public void handle(MouseEvent event) {
               
            	stage.setX(event.getScreenX()-mouseDragOffsetX);
            	stage.setY(event.getScreenY()-mouseDragOffsetY);
                
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

		ToolBar bottomBar = new ToolBar();
		bottomBar.setPrefSize(1024, 20);
		
		layout.registerToolBar(Layout.TOP, topBar);
		layout.registerToolBar(Layout.BOTTOM, bottomBar);
	    layout.setStyle(StageStyle.UNDECORATED);
		System.out.println("2: "+action.getLastMessage());
	}
	


}
