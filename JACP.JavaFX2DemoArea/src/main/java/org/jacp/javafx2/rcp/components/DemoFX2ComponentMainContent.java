package org.jacp.javafx2.rcp.components;

import javafx.event.Event;
import javafx.scene.Node;
import javafx.scene.effect.DropShadow;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import net.miginfocom.layout.CC;

import org.jacp.api.action.IAction;
import org.jacp.javafx2.rcp.component.AFX2Component;
import org.jacp.javafx2.rcp.componentLayout.FX2ComponentLayout;
import org.tbee.javafx.scene.layout.MigPane;

public class DemoFX2ComponentMainContent extends AFX2Component {

	@Override
	public Node handleAction(IAction<Event, Object> action) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Node postHandleAction(Node node, IAction<Event, Object> action) {
		MigPane mainContent = new MigPane("fill", "35[]15", "15[]15");
		int x = 0;
		while (x < 20) {
			Rectangle r = new Rectangle(80, 80);
			r.setFill(Color.LIGHTGRAY);
			r.getStyleClass().add("main-container");
			r.setEffect(new DropShadow());

			if ((x + 1) % 5 == 0)
				mainContent.add(r, new CC().wrap());
			else {
				mainContent.add(r);
			}
			x++;
		}

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
