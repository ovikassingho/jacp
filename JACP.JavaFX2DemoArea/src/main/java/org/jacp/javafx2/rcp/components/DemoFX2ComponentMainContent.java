package org.jacp.javafx2.rcp.components;

import javafx.event.Event;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.scene.Node;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.TilePane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import org.jacp.api.action.IAction;
import org.jacp.javafx2.rcp.component.AFX2Component;
import org.jacp.javafx2.rcp.componentLayout.FX2ComponentLayout;

public class DemoFX2ComponentMainContent extends AFX2Component {

	@Override
	public Node handleAction(IAction<Event, Object> action) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Node postHandleAction(Node node, IAction<Event, Object> action) {
		// Pane group = new Pane();
		// FlowPane mainContent = new FlowPane(Orientation.VERTICAL);
		TilePane mainContent = new TilePane();
		mainContent.setPrefRows(3);
		// Pane mainContent = new Pane();
		mainContent.setPadding(new Insets(30));

		int x = 0;
		while (x < 20) {
			Rectangle r = new Rectangle(80, 80);
			r.setFill(Color.LIGHTGRAY);
			r.getStyleClass().add("main-container");
			r.setEffect(new DropShadow());
			TilePane.setMargin(r, new Insets(20));

			mainContent.getChildren().add(r);
			x++;
		}

		// mainContent.setStyle("-fx-background-color: blue;");
		// GridPane.setVgrow(group, Priority.ALWAYS);
		// GridPane.setHgrow(group, Priority.ALWAYS);
		// group.getChildren().add(mainContent);
		return mainContent;
	}

	@Override
	public void onStartComponent(FX2ComponentLayout layout) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onTearDownComponent(FX2ComponentLayout layout) {
		// TODO Auto-generated method stub

	}

}
