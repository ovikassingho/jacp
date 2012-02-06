package org.jacp.javafx2.rcp.components;


import java.util.Iterator;

import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ToolBar;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import org.jacp.api.action.IAction;
import org.jacp.api.action.IActionListener;
import org.jacp.api.util.ToolbarPosition;
import org.jacp.javafx2.rcp.component.AFX2Component;
import org.jacp.javafx2.rcp.componentLayout.FX2ComponentLayout;
/**
 * Demo ui component one
 * @author Andy Moncsek
 *
 */
public class DemoFX2ComponentOne extends AFX2Component {

	VBox vbox;
	Button counter = new Button("counter");
	Button bc = new Button("message 1");
	Button move = new Button("move");
	int c = 0;
	String twoTarget="id01";


	@Override
	public Node handleAction(IAction<Event, Object> action) {
		System.out.println("message to component one "
				+ action.getLastMessage() + " in thread"
				+ Thread.currentThread() + " counter: " + c);
		if (action.getLastMessage().equals("ping")) {
			c++;

		}else if(action.getLastMessage().equals("move")) {
			System.out.println("should move");
			String target = this.getExecutionTarget();
			if(target.equals("P0")) {
				this.setExecutionTarget("P1");
			} else {
				this.setExecutionTarget("P0");
			}
		} else if(action.getLastMessage().toString().contains("id0"))  {
			twoTarget=action.getLastMessage().toString();
		}else if(action.getLastMessage().toString().equals("messageTo"))  {
			EventHandler<? super MouseEvent> listener = getListener(
					twoTarget+".id002", "message from DemoFX2ComponentOne");
			listener.handle(null);
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

		bc.setOnMouseClicked(getListener(null,
				"messageTo"));

		Button button2 = new Button("me");
		button2.setOnMouseClicked(getListener(null, "me"));

		counter.setOnMouseClicked(getListener("id002", "start"));
		toolbar.getChildren().add(bc);
		toolbar.getChildren().add(button2);
		toolbar.getChildren().add(counter);
		toolbar.getChildren().add(move);

		move.setOnMouseClicked(new EventHandler<Event>() {

			@Override
			public void handle(Event arg0) {
				if (true) {
					move();
				}

			}
		});
		return toolbar;
	}
	public final void move() {
		EventHandler<? super MouseEvent> listener = getListener("id002", "move");
		listener.handle(null);
		EventHandler<? super MouseEvent> listener2 = getListener(null, "move");
		listener2.handle(null);
	}
	private EventHandler<? super MouseEvent> getListener(final String id,
			final String message) {
		IActionListener<EventHandler<Event>, Event, Object> listener = null;
		if (id != null) {
			listener = getActionListener(id, message);
		} else {
			listener = getActionListener(message);
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
			System.out.println("NOT PING");
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
		
		ToolBar north = layout.getRegisteredToolBar(ToolbarPosition.SOUTH);
		Button b1= new Button("c1");
		north.getItems().add(b1);
		MenuBar menu = layout.getMenu();
		ObservableList<javafx.scene.control.Menu> menuList = menu.getMenus();
		Iterator<javafx.scene.control.Menu> it = menuList.iterator();
		while(it.hasNext()) {
			javafx.scene.control.Menu m = it.next();
			if(m.getText().equals("File")){
				final MenuItem item = new MenuItem("Hello C1");
				m.getItems().add(item);
			}
		}
		
	}

	@Override
	public void onTearDownComponent(final FX2ComponentLayout layout) {
		
	}

	
	


}
