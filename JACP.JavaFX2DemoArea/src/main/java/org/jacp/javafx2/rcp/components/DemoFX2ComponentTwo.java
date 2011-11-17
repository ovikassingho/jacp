package org.jacp.javafx2.rcp.components;

import java.util.Map;

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
import org.jacp.api.componentLayout.Layout;
import org.jacp.javafx2.rcp.component.AFX2Component;

public class DemoFX2ComponentTwo extends AFX2Component {
	VBox vbox = null;
	Button start = new Button("start");
	Button bc = new Button("message 2");
	boolean flag =false;
	int c=0;
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
				.println("message to component two "+action.getLastMessage()+" in thread"+ Thread.currentThread());
		if(action.getLastMessage().equals("start")) {

			for(int i=0;i<10000;i++){
				EventHandler<? super MouseEvent> listener = getListener("id001", "ping");
				listener.handle(null);
				
			}

		}else if(action.getLastMessage().equals("stop")) {
			flag =false;
		}
		return  null;
	}

	public Node createDemoButtonBar(IAction<Event, Object> action) {
		if(vbox==null) {
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
		
		bc.setOnMouseClicked(getListener("id001", "DemoFX2ComponentTwo"));

		Button button2 = new Button("me");
		button2.setOnMouseClicked(getListener(null, "me"));

		start.setOnMouseClicked(getListener(null, "start"));

		toolbar.getChildren().add(bc);
		toolbar.getChildren().add(start);
		toolbar.getChildren().add(button2);

		return toolbar;
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

	@Override
	public Node postHandleAction(Node node, IAction<Event, Object> action) {
		if(vbox==null) {
			return createDemoButtonBar(action);
		} 
		if(action.getLastMessage().equals("me")) {
			bc.setStyle("-fx-background-color: red; -fx-text-fill: white;");
		} else  {

			bc.setStyle("-fx-background-color: slateblue; -fx-text-fill: white;");
		}
		return vbox;
	}

}
