package org.jacp.javafx2.rcp.demo;

import java.util.Map;

import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.MenuBar;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import org.jacp.api.action.IAction;
import org.jacp.api.componentLayout.Layout;
import org.jacp.javafx2.rcp.componentLayout.FX2PerspectiveLayout;
import org.jacp.javafx2.rcp.perspective.AFX2Perspective;


/**
 * Demo perspective class for jacp JavaFX2 implementation
 * @author Andy Moncsek
 *
 */
public class DemoFX2PerspectiveOne extends AFX2Perspective{

	@Override
	public void handleMenuEntries(MenuBar menuBar) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void handleBarEntries(Map<Layout, Node> bars) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void handlePerspective(IAction<ActionEvent, Object> action,
			FX2PerspectiveLayout perspectiveLayout) {
		System.out.println("message from perspective one: "+ action.getLastMessage());
		BorderPane layout = new BorderPane();
		layout.setTop(new Rectangle(1024, 50, Color.DARKCYAN));
		layout.setBottom(new Rectangle(1024, 50, Color.DARKCYAN));
		layout.setCenter(new Rectangle(100, 100, Color.MEDIUMAQUAMARINE));
		layout.setLeft(new Rectangle(50, 300, Color.DARKTURQUOISE));
		layout.setRight(new Rectangle(50, 300, Color.DARKTURQUOISE));
		perspectiveLayout.setRootComponent(layout);
		
	}

}
