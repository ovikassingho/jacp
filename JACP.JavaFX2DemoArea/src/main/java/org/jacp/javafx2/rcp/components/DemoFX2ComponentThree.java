package org.jacp.javafx2.rcp.components;

import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import org.jacp.api.action.IAction;
import org.jacp.api.action.IActionListener;
import org.jacp.javafx2.rcp.component.AFX2Component;
import org.jacp.javafx2.rcp.componentLayout.FX2ComponentLayout;

public class DemoFX2ComponentThree extends AFX2Component {
	VBox vbox = null;
	Button start = new Button("start");
	Button bc = new Button("message 2");
	boolean flag = false;
	int c = 0;

	@Override
	public Node handleAction(IAction<Event, Object> action) {
		System.out.println("message to component two "
				+ action.getLastMessage() + " in thread"
				+ Thread.currentThread());
		if (action.getLastMessage().equals("start")) {

			for (int i = 0; i < 10000; i++) {
				EventHandler<? super MouseEvent> listener = getListener(
						"id001", "ping");
				listener.handle(null);

			}

		} else if (action.getLastMessage().equals("stop")) {
			flag = false;
		}
		return null;
	}

	private HBox createBreadCrumb(IAction<Event, Object> action) {
		BreadcrumbBar crumb = new BreadcrumbBar();
		crumb.setPath("Home/test/test/test/test/test");
		return crumb;
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
			return createBreadCrumb(action);
		}
		if (action.getLastMessage().equals("me")) {
			bc.setStyle("-fx-background-color: red; -fx-text-fill: white;");
		} else {

			bc.setStyle("-fx-background-color: slateblue; -fx-text-fill: white;");
		}
		return vbox;
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
