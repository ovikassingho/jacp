package org.jacp.javafx2.rcp.perspectives;

import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.ToolBar;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;

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
public class DemoFX2PerspectiveOne extends AFX2Perspective {

	@Override
	public void handlePerspective(IAction<Event, Object> action,
			FX2PerspectiveLayout perspectiveLayout) {
		BorderPane layout = new BorderPane();
		layout.setStyle("-fx-background-color: green;");

		final IActionListener<EventHandler<Event>, Event, Object> listenerBottomOne = getActionListener();
		listenerBottomOne.getAction().addMessage("id02", "oneButtonBottomOne");
		Button bc = new Button("Button Perspective 1");
		bc.setStyle("-fx-background-color: slateblue; -fx-text-fill: white;");
		bc.setOnMouseClicked((EventHandler<? super MouseEvent>) listenerBottomOne);
		Pane top = new Pane();
		top.setStyle("-fx-background-color: yellow;");
		layout.setTop(top);
		Pane bottom = new Pane();
		bottom.setStyle("-fx-background-color: blue;");
		Pane left = new Pane();
		Pane right = new Pane();
		left.setMinSize(50, 240);
		right.setMinSize(50, 240);

		// bottom.getChildren().add(new Rectangle(1024, 50, Color.BLACK));
		layout.setBottom(bottom);
		layout.setCenter(bc);
		layout.setLeft(left);
		layout.setRight(right);
		perspectiveLayout.setRootComponent(layout);
		perspectiveLayout.registerTargetLayoutComponent("P1", bottom);
		perspectiveLayout.registerTargetLayoutComponent("P2", left);
		perspectiveLayout.registerTargetLayoutComponent("P3", right);
		perspectiveLayout.registerTargetLayoutComponent("P0", top);

	}

	@Override
	public void onStartPerspective(final FX2ComponentLayout layout) {
		System.out.println("run on start perspective one bar:"
				+ layout.getMenu() + " bars:"
				+ layout.getRegisteredToolBar(ToolbarPosition.NORTH));
		ToolBar north = layout.getRegisteredToolBar(ToolbarPosition.SOUTH);

		final IActionListener<EventHandler<Event>, Event, Object> listenerBottomOne = getActionListener();
		listenerBottomOne.getAction().addMessage("id99",
				"oneButtonBottomImageDemo");
		Button bc = new Button("Image Demo");
		bc.setOnMouseClicked((EventHandler<? super MouseEvent>) listenerBottomOne);

		Button b1 = new Button("p1");
		north.getItems().add(b1);
		north.getItems().add(bc);

	}

	@Override
	public void onTearDownPerspective(final FX2ComponentLayout layout) {
		// TODO Auto-generated method stub

	}

}
