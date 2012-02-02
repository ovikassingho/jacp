package org.jacp.javafx2.rcp.components;

import javafx.event.Event;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.effect.Reflection;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import org.jacp.api.action.IAction;
import org.jacp.javafx2.rcp.component.AFX2Component;
import org.jacp.javafx2.rcp.componentLayout.FX2ComponentLayout;

public class DemoFX2ComponentBottomBar extends AFX2Component {

	@Override
	public Node handleAction(IAction<Event, Object> action) {
		// TODO Auto-generated method stub
		return null;
	}

	private Node getBottomBar(IAction<Event, Object> action) {
		Pane p = new Pane();
		HBox bottom = new HBox();
		bottom.setPrefHeight(80);
		int x = 0;
		while (x < 9) {
			Rectangle r = new Rectangle(60, 60);
			r.setFill(Color.LIGHTGREY);
			r.setStroke(Color.BLACK);
			HBox.setMargin(r, new Insets(10));
			Reflection ref = new Reflection();
			ref.setTopOffset(0.1);
			ref.setFraction(0.15);
			r.setEffect(ref);
			bottom.getChildren().add(r);
			x++;

		}
		p.getChildren().add(bottom);
		return p;

	}

	@Override
	public Node postHandleAction(Node node, IAction<Event, Object> action) {
		return getBottomBar(action);
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
