package org.jacp.javafx2.rcp.components;

import java.util.Map;

import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ToolBar;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;

import org.jacp.api.action.IAction;
import org.jacp.api.action.IActionListener;
import org.jacp.api.componentLayout.Layout;
import org.jacp.javafx2.rcp.component.AFX2Component;

public class DemoFX2ComponentTwo extends AFX2Component {

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
		System.out
				.println("message to component two" + action.getLastMessage());
		return createDemoButtonBar(action);
	}

	public Node createDemoButtonBar(IAction<Event, Object> action) {
		VBox vbox = new VBox();
		vbox.setPadding(new Insets(0, 0, 0, 0));
		vbox.setSpacing(10);
		vbox.autosize();
		ToolBar toolbar = new ToolBar();
		toolbar.setPrefSize(1024, 50);

		
		
		
		Button bc = new Button("message 2");
		if(action.getLastMessage().equals("me")) {
			bc.setStyle("-fx-background-color: red; -fx-text-fill: white;");
		} else {
			bc.setStyle("-fx-background-color: slateblue; -fx-text-fill: white;");
		}
		
		bc.setOnMouseClicked(getListener("id001", "DemoFX2ComponentTwo"));
		toolbar.getItems().add(bc);
		Button button2 = new Button("me");
		button2.setOnMouseClicked(getListener(null, "me"));
		toolbar.getItems().add(button2);
		vbox.getChildren().add(toolbar);
		return vbox;
	}
	
	private EventHandler<? super MouseEvent> getListener(final String id, final String message) {
		final IActionListener<EventHandler<Event>, Event, Object> listener = getActionListener();
		if(id!=null) {
			listener.getAction()
			.addMessage(id, message);
		}else {
			listener.getAction().setMessage(message);
		}

		
		return (EventHandler<? super MouseEvent>) listener;
	}

}
