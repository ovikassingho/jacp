package org.jacp.javafx2.rcp.components;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javafx.event.Event;
import javafx.scene.Node;
import javafx.scene.control.Accordion;
import javafx.scene.control.Button;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

import org.jacp.api.action.IAction;
import org.jacp.javafx2.rcp.component.AFX2Component;
import org.jacp.javafx2.rcp.componentLayout.FX2ComponentLayout;

public class DemoFX2ComponentLeftMenu extends AFX2Component {

	@Override
	public Node handleAction(IAction<Event, Object> action) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Node postHandleAction(Node node, IAction<Event, Object> action) {
		final Accordion accordion = new Accordion();

		List<TitledPane> tps = new ArrayList<TitledPane>();

		Random r = new Random();
		for (int i = 0; i < 10; i++) {
			int stop = r.nextInt(12);

			stop = (stop == 0) ? 5 : stop;
			VBox box = new VBox();
			box.setFillWidth(true);
			box.getStyleClass().add("dark");
			for (int j = 0; j < stop; j++) {
				Button b = new Button("Test " + j);
				b.setMaxWidth(Double.MAX_VALUE);
				box.getChildren().add(b);

			}
			TitledPane tp = new TitledPane("Test", box);
			tps.add(tp);

		}
		accordion.getPanes().addAll(tps);
		accordion.setExpandedPane(tps.get(0));

		GridPane.setHgrow(accordion, Priority.ALWAYS);
		GridPane.setVgrow(accordion, Priority.ALWAYS);

		return accordion;

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
