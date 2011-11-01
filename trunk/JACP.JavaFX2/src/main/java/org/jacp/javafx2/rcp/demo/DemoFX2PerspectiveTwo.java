package org.jacp.javafx2.rcp.demo;

import java.util.Map;

import javafx.event.Event;
import javafx.scene.Node;
import javafx.scene.control.MenuBar;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import org.jacp.api.action.IAction;
import org.jacp.api.componentLayout.Layout;
import org.jacp.javafx2.rcp.componentLayout.FX2PerspectiveLayout;
import org.jacp.javafx2.rcp.perspective.AFX2Perspective;

public class DemoFX2PerspectiveTwo extends AFX2Perspective{

	@Override
	public void handleMenuEntries(MenuBar menuBar) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void handleBarEntries(Map<Layout, Node> bars) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void handlePerspective(IAction<Event, Object> action,
			FX2PerspectiveLayout perspectiveLayout) {
		System.out.println("message from perspective two: "+ action.getLastMessage());
		BorderPane layout = new BorderPane();
		layout.setTop(new Rectangle(1024, 50, Color.ALICEBLUE));
		layout.setBottom(new Rectangle(1024, 50, Color.ALICEBLUE));
		layout.setCenter(new Rectangle(100, 100, Color.BEIGE));
		layout.setLeft(new Rectangle(50, 300, Color.BURLYWOOD));
		layout.setRight(new Rectangle(50, 300, Color.BURLYWOOD));
		perspectiveLayout.setRootComponent(layout);
		
	}

}

