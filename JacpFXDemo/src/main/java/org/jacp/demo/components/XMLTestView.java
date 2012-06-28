package org.jacp.demo.components;

import java.net.URL;
import java.util.Locale;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;

import org.jacp.api.action.IAction;
import org.jacp.api.annotations.DeclarativeComponent;
import org.jacp.api.annotations.OnStart;
import org.jacp.api.annotations.OnTearDown;
import org.jacp.javafx.rcp.component.AFXComponent;
import org.jacp.javafx.rcp.componentLayout.FXComponentLayout;
import org.jacp.javafx.rcp.util.FXUtil.MessageUtil;

@DeclarativeComponent(defaultExecutionTarget = "PmainContentTop", id = "id006", name = "XMLTestView", active = false, viewLocation = "/fxml/AdoptionForm.fxml", resourceBundleLocation = "bundles.languageBundle")
// , localeID="en_US")
public class XMLTestView extends AFXComponent {
	@FXML
	private GridPane grid;
	@FXML
	private TextField field1;
	@FXML
	private TextArea addTextField;

	private String[] targets = { "PmainContentBottom", "PmainContentTop" };

	private int count = 0;

	@SuppressWarnings("unused")
	@FXML
	private void update(ActionEvent event) {
		this.getActionListener("update").performAction(null);
	}

	@SuppressWarnings("unused")
	@FXML
	private void handleSubmit(ActionEvent event) {
		System.out.println("action : " + grid);
		grid.setGridLinesVisible(!grid.isGridLinesVisible());
		this.getActionListener("stop").performAction(null);

	}

	@SuppressWarnings("unused")
	@FXML
	private void change(ActionEvent event) {
		System.out.println("default locale: " + Locale.getDefault());
		System.out.println("action : " + grid);
		grid.setGridLinesVisible(!grid.isGridLinesVisible());
		this.getActionListener("change").performAction(null);

	}

	@Override
	public Node handleAction(IAction<Event, Object> action) {
		System.out.println("handle : " + grid + " " + action.getLastMessage()
				+ " thread" + Thread.currentThread());
		if (action.getLastMessage().equals("stop"))
			this.setActive(false);
		if (action.getLastMessage().equals("change")) {
			this.setExecutionTarget(targets[count]);
			count = count == 0 ? 1 : 0;
		}
		return null;
	}

	@Override
	public Node postHandleAction(final Node node,
			final IAction<Event, Object> action) {
		// TODO Auto-generated method stub
		System.out.println("postHandle : " + grid + " "
				+ action.getLastMessage() + " thread" + Thread.currentThread());
		if (!action.getLastMessage().equals(MessageUtil.INIT)
				&& !action.getLastMessage().equals("stop")) {
			field1.setText(action.getLastMessage().toString());

			addTextField.setText(action.getLastMessage().toString());
		}
		return null;

	}

	@OnStart
	public void start(FXComponentLayout layout, URL url,
			ResourceBundle resourceBundle) {
		System.out.println("STRAT" + grid + "  " + layout + " url: "
				+ url.getPath() + " resourceBundle" + resourceBundle);
	}

	@OnTearDown
	public void stop(FXComponentLayout layout) {
		System.out.println("STOP");
	}

	// @OnStart
	// public void start(FXComponentLayout layout) {
	// System.out.println("STRAT" + grid+"  "+ layout);
	// }

	// @OnStart
	// public void start(URL url, ResourceBundle resourceBundle) {
	// System.out.println("STRAT" + grid+"  "+ " url: "+ url.getPath());
	// }

	// @OnStart
	// public void start(URL url) {
	// System.out.println("STRAT" + grid+"  ");
	// }
}
