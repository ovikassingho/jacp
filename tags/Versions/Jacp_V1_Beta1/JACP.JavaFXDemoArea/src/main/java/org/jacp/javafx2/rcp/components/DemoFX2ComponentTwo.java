package org.jacp.javafx2.rcp.components;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Menu;
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
 * Demo ui component two
 * @author Andy Moncsek
 *
 */
public class DemoFX2ComponentTwo extends AFX2Component {
	VBox vbox = null;
	Button start = null;
	Button bc = null;
	Button move = null;
	Button move2 = null;
	Button b1 = null;
	MenuItem item = null;
	String exec = "id02";
	Button button2 = null;
	HBox toolbar = null;
	int c = 0;

	@Override
	public Node handleAction(IAction<Event, Object> action) {
		c = c + 1;
		System.out.println("message to component two >>"
				+ action.getLastMessage() + "<< in thread"
				+ Thread.currentThread() + " counter: " + c);

		if (action.getLastMessage().equals("start")) {

			for (int i = 0; i < 10000; i++) {
				/*EventHandler<? super MouseEvent> listener = getListener(
						"id01.id001", "ping");
				listener.handle(null);*/
				EventHandler<? super MouseEvent> listener2 = getListener(
						"id01.id004", "ping");
				listener2.handle(null);

			}

		} else if (action.getLastMessage().equals("move")) {
			String target = this.getExecutionTarget();
			if (target.equals("P0")) {
				this.setExecutionTarget("P1");
			} else {
				this.setExecutionTarget("P0");
			}
		} else if (action.getLastMessage().equals("close")) {
			// shutdown
			this.setActive(false);
		} else if (action.getLastMessage().equals("beam")) {
			// move to different perspective
			if (exec.equals("id02")) {

				this.setExecutionTarget("id02.P1");
				EventHandler<? super MouseEvent> listener = getListener(
						"id01.id001", "id02");
				listener.handle(null);
			} else {

				this.setExecutionTarget("id01.P1");
				EventHandler<? super MouseEvent> listener = getListener(
						"id01.id001", "id01");
				listener.handle(null);
			}

		}
		return null;
	}

	public Node createDemoButtonBar() {

		vbox = new VBox();
		vbox.setPadding(new Insets(0, 0, 0, 0));
		vbox.setSpacing(10);

		vbox.getChildren().clear();
		vbox.getChildren().add(createBar());
		return vbox;
	}

	private HBox createBar() {

		toolbar.setPrefSize(1024, 50);

		bc.setOnMouseClicked(getListener("id01.id001", "DemoFX2ComponentTwo"));

		button2.setOnMouseClicked(getListener(null, "me"));

		start.setOnMouseClicked(getListener(null, "start"));

		toolbar.getChildren().add(bc);
		toolbar.getChildren().add(start);
		toolbar.getChildren().add(button2);

		toolbar.getChildren().add(move);
		toolbar.getChildren().add(move2);

		move.setOnMouseClicked(new EventHandler<Event>() {

			@Override
			public void handle(Event arg0) {
				if (true) {
					move();
				}

			}
		});
		move2.setOnMouseClicked(new EventHandler<Event>() {

			@Override
			public void handle(Event arg0) {
				if (true) {
					EventHandler<? super MouseEvent> listener2 = getListener(
							null, "beam");
					listener2.handle(null);
				}

			}
		});
		return toolbar;
	}

	public final void move() {
		EventHandler<? super MouseEvent> listener = getListener("id001", "move");
		listener.handle(null);
		EventHandler<? super MouseEvent> listener2 = getListener(null, "move");
		listener2.handle(null);
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
		if (action.getLastMessage().equals("me")) {
			bc.setStyle("-fx-background-color: red; -fx-text-fill: white;");
		} else {

			bc.setStyle("-fx-background-color: slateblue; -fx-text-fill: white;");
		}

		if (action.getLastMessage().equals("beam")) {
			// move to different perspective
			if (exec.equals("id02")) {
				exec = "id01";
				move2.setText("move to: " + exec);

			} else {
				exec = "id02";
				move2.setText("move to: " + exec);
			}
		}
		return vbox;
	}

	@Override
	public void onStartComponent(final FX2ComponentLayout layout) {

		start = new Button("start");
		bc = new Button("message 2");
		move = new Button("move");
		move2 = new Button("move to: " + exec);
		b1 = new Button("close component 2");
		item = new MenuItem("close component 2");
		String label = "me:" + c;
		button2 = new Button(label);

		toolbar = new HBox();

		createDemoButtonBar();

		ToolBar north = layout.getRegisteredToolBar(ToolbarPosition.NORTH);

		b1.setOnMouseClicked(new EventHandler<Event>() {

			@Override
			public void handle(Event arg0) {
				EventHandler<? super MouseEvent> listener2 = getListener(null,
						"close");
				listener2.handle(null);

			}
		});
		north.getItems().add(b1);
		MenuBar menu = layout.getMenu();
		ObservableList<javafx.scene.control.Menu> menuList = menu.getMenus();
		for (Menu m : menuList) {
			if (m.getText().equals("File")) {
				item.setOnAction(new EventHandler<ActionEvent>() {
					@Override
					public void handle(ActionEvent arg0) {
						EventHandler<? super MouseEvent> listener2 = getListener(
								null, "close");
						listener2.handle(null);

					}
				});
				m.getItems().add(item);
			}
		}

	}

	@Override
	public void onTearDownComponent(final FX2ComponentLayout layout) {

		ToolBar north = layout.getRegisteredToolBar(ToolbarPosition.NORTH);
		north.getItems().remove(b1);
		MenuBar menu = layout.getMenu();
		ObservableList<javafx.scene.control.Menu> menuList = menu.getMenus();
		for (Menu m : menuList) {
			if (m.getText().equals("File")) {
				m.getItems().remove(item);
			}
		}

		start = null;
		bc = null;
		move = null;
		move2 = null;
		b1 = null;
		item = null;
		vbox = null;
		button2 = null;
		toolbar = null;
	}

}
