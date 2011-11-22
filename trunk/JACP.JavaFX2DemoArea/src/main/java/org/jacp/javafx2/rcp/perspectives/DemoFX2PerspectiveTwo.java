package org.jacp.javafx2.rcp.perspectives;

import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.ToolBar;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import org.jacp.api.action.IAction;
import org.jacp.api.action.IActionListener;
import org.jacp.api.componentLayout.Layout;
import org.jacp.javafx2.rcp.componentLayout.FX2ComponentLayout;
import org.jacp.javafx2.rcp.componentLayout.FX2PerspectiveLayout;
import org.jacp.javafx2.rcp.perspective.AFX2Perspective;

public class DemoFX2PerspectiveTwo extends AFX2Perspective{

	

	@Override
	public void handlePerspective(IAction<Event, Object> action,
			FX2PerspectiveLayout perspectiveLayout) {
		BorderPane layout = new BorderPane();
		final  IActionListener<EventHandler<Event>, Event, Object> listenerBottomOne = getActionListener();
		listenerBottomOne.getAction().addMessage("id01","oneButtonBottomTwo");
		Button bc=  new Button("Button Perspective 2");
        bc.setStyle("-fx-background-color: slateblue; -fx-text-fill: white;");
        bc.setOnMouseClicked((EventHandler<? super MouseEvent>) listenerBottomOne);
        bc.setOnMouseExited((EventHandler<? super MouseEvent>) listenerBottomOne);
		layout.setTop(new Rectangle(1024, 50, Color.ALICEBLUE));
		layout.setBottom(new Rectangle(1024, 50, Color.ALICEBLUE));
		layout.setCenter(bc);
		layout.setLeft(new Rectangle(50, 250, Color.BURLYWOOD));
		layout.setRight(new Rectangle(50, 250, Color.BURLYWOOD));
		perspectiveLayout.setRootComponent(layout);
		
	}
	
	@Override
	public void onStartPerspective(final FX2ComponentLayout layout) {
		System.out.println("run on start perspective two bar:"+layout.getMenu() +" bars:"+layout.getToolBar(Layout.NORTH));
		ToolBar north = layout.getToolBar(Layout.SOUTH);
		Button b1= new Button("p2");
		north.getItems().add(b1);
	}

	@Override
	public void onTearDownPerspective(final FX2ComponentLayout layout) {
		// TODO Auto-generated method stub
		
	}

}

