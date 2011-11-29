package org.jacp.javafx2.rcp.components;

import javafx.event.Event;
import javafx.scene.Node;
import javafx.scene.effect.Reflection;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import org.jacp.api.action.IAction;
import org.jacp.javafx2.rcp.component.AFX2Component;
import org.jacp.javafx2.rcp.componentLayout.FX2ComponentLayout;
import org.tbee.javafx.scene.layout.MigPane;

public class DemoFX2ComponentBottomBar extends AFX2Component {

	@Override
	public Node handleAction(IAction<Event, Object> action) {
		// TODO Auto-generated method stub
		return null;
	}

	private Node getBottomBar(IAction<Event, Object> action) {
		MigPane bottom = new MigPane("fill", "30[]30", "15[]15");

		int x = 0;
		while (x < 9) {
			Rectangle r = new Rectangle(60, 60);
			r.setFill(Color.LIGHTGREY);
			r.setStroke(Color.BLACK);
			Reflection ref = new Reflection();
			ref.setTopOffset(0.1);
			ref.setFraction(0.15);
			r.setEffect(ref);
			bottom.add(r);

			x++;

		}
		return bottom;

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
