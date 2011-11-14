package org.jacp.javafx2.rcp.components;

import java.util.Map;

import javafx.event.Event;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ToolBar;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import org.jacp.api.action.IAction;
import org.jacp.api.componentLayout.Layout;
import org.jacp.javafx2.rcp.component.AFX2Component;

public class DemoFX2ComponentOne extends AFX2Component {

	@Override
	public void handleMenuEntries(Node menuBar) {
		// TODO Auto-generated method stub

	}

	@Override
	public void handleBarEntries(Map<Layout, Node> bars) {
		// TODO Auto-generated method stub

	}

	@Override
	public Node handleAction(IAction<Event, Object> action) {
		System.out.println("message to component one");
		return  createDemoButtonBar();
	}

	public Node createDemoButtonBar() {
		VBox vbox = new VBox();
		vbox.setPadding(new Insets(0, 0, 0, 0));
		vbox.setSpacing(10);
		vbox.autosize();
		ToolBar toolbar = new ToolBar();
		toolbar.setPrefSize(1024, 50);

		Button bc = new Button("Options");
		bc.setStyle("-fx-background-color: slateblue; -fx-text-fill: white;");
		toolbar.getItems().add(bc);
		toolbar.getItems().add(new Button("Help"));
		vbox.getChildren().add(toolbar);
		return vbox;
	}

}
