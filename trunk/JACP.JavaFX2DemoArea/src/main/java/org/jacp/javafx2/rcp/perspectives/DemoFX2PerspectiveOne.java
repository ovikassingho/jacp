package org.jacp.javafx2.rcp.perspectives;

import java.util.Map;

import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.MenuBar;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import org.jacp.api.action.IAction;
import org.jacp.api.action.IActionListener;
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
	public void handlePerspective(IAction<Event, Object> action,
			FX2PerspectiveLayout perspectiveLayout) {
		System.out.println("message from perspective one: "+ action.getLastMessage());
		BorderPane layout = new BorderPane();

		final  IActionListener<EventHandler<Event>, Event, Object> listenerBottomOne = getActionListener();
		listenerBottomOne.getAction().addMessage("id02","oneButtonBottomOne");
		Button bc=  new Button("Button Perspective 1");
        bc.setStyle("-fx-background-color: slateblue; -fx-text-fill: white;");
        bc.setOnMouseEntered((EventHandler<? super MouseEvent>) listenerBottomOne);
		 Group top = new Group();
		layout.setTop(top);
		 Group bottom = new Group();
		// bottom.getChildren().add(new Rectangle(1024, 50, Color.BLACK));
		layout.setBottom(bottom);
		layout.setCenter(bc);
		layout.setLeft(new Rectangle(50, 300, Color.DARKTURQUOISE));
		layout.setRight(new Rectangle(50, 300, Color.DARKTURQUOISE));
		perspectiveLayout.setRootComponent(layout);
		perspectiveLayout.registerTargetLayoutComponent("PBottomOne", bottom);
		perspectiveLayout.registerTargetLayoutComponent("PTopOne", top);
		
	}

}
