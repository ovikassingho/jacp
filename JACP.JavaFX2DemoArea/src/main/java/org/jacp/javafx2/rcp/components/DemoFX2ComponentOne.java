package org.jacp.javafx2.rcp.components;

import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import org.jacp.api.action.IAction;
import org.jacp.api.action.IActionListener;
import org.jacp.javafx2.rcp.component.AFX2Component;
import org.jacp.javafx2.rcp.componentLayout.FX2ComponentLayout;

public class DemoFX2ComponentOne extends AFX2Component {

	VBox vbox;
	Button counter = new Button("counter");
	Button bc = new Button("message 1");
	int c = 0;


	@Override
	public Node handleAction(IAction<Event, Object> action) {
		System.out.println("message to component one "
				+ action.getLastMessage() + " in thread"
				+ Thread.currentThread() + " counter: " + c);
		if (action.getLastMessage().equals("ping")) {
			c++;

		}
		return null;
	}

	public Node createDemoButtonBar(IAction<Event, Object> action) {
		if (vbox == null) {
			vbox = new VBox();
			vbox.setPadding(new Insets(0, 0, 0, 0));
			vbox.setSpacing(10);
		}

		vbox.getChildren().clear();
		vbox.getChildren().add(createBar(action));
		return vbox;
	}

	private HBox createBar(IAction<Event, Object> action) {
		HBox toolbar = new HBox();
		toolbar.setPrefSize(1024, 50);

		bc.setOnMouseClicked(getListener("id002",
				"message from DemoFX2ComponentOne"));

		Button button2 = new Button("me");
		button2.setOnMouseClicked(getListener(null, "me"));

		counter.setOnMouseClicked(getListener("id002", "start"));
		toolbar.getChildren().add(bc);
		toolbar.getChildren().add(button2);
		toolbar.getChildren().add(counter);

		return toolbar;
	}

	private EventHandler<? super MouseEvent> getListener(final String id,
			final String message) {
		final IActionListener<EventHandler<Event>, Event, Object> listener = getActionListener();
		if (id != null) {
			listener.getAction().addMessage(id, message);
		} else {
			listener.getAction().setMessage(message);
		}

		return (EventHandler<? super MouseEvent>) listener;
	}

	@Override
	public Node postHandleAction(Node node, IAction<Event, Object> action) {
		if (vbox == null) {
			return createDemoButtonBar(action);
		}
		if (action.getLastMessage().equals("ping")) {
			counter.setText(c + "");

		} else {

			if (action.getLastMessage().equals("me")) {
				bc.setStyle("-fx-background-color: red; -fx-text-fill: white;");
			} else {
				bc.setStyle("-fx-background-color: slateblue; -fx-text-fill: white;");
			}
		}
		vbox.setVisible(true);
		return vbox;
	}

	@Override
	public void onStartComponent(final FX2ComponentLayout layout) {

		
	}

	@Override
	public void onTearDownComponent(final FX2ComponentLayout layout) {
		
	}

	
	


}
