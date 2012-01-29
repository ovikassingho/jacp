package org.jacp.javafx2.rcp.components;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

import org.jacp.javafx2.rcp.components.optionPane.JACPModalDialog;

public class CustomOptionPane extends VBox {

	public CustomOptionPane() {
		createAddContactDialog();
	}
	
	// T R  L 

	private void createAddContactDialog() {
		setId("ProxyDialog");
		setMaxSize(300, VBox.USE_PREF_SIZE);
		final Label title = new Label("Login");
		title.setId("jacp-custom-title");
		VBox.setMargin(title, new Insets(2,2,10,2 ));

		final HBox hboxInput = new HBox();

		final Label nameLabel = new Label("username:");
		HBox.setMargin(nameLabel, new Insets(2));

		final TextField nameInput = new TextField();
		HBox.setMargin(nameInput, new Insets(0, 0, 0, 5));
		HBox.setHgrow(nameInput, Priority.ALWAYS);

		hboxInput.getChildren().addAll(nameLabel, nameInput);

		final Label passwdLabel = new Label("password:");
		HBox.setMargin(passwdLabel, new Insets(2));

		final PasswordField passwordField = new PasswordField();
		HBox.setMargin(passwordField, new Insets(0, 0, 0, 5));
		HBox.setHgrow(passwordField, Priority.ALWAYS);

		final HBox hboxPasswd = new HBox();
		hboxPasswd.getChildren().addAll(passwdLabel, passwordField);
		getChildren().addAll(title, hboxInput, hboxPasswd);

		final HBox hboxButtons = new HBox();
		hboxButtons.setAlignment(Pos.CENTER_RIGHT);

		final Button ok = new Button("OK");
		HBox.setMargin(ok, new Insets(4, 5, 4, 2));

		final Button cancel = new Button("Cancel");
		HBox.setMargin(cancel, new Insets(4, 2, 4, 5));

		cancel.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent arg0) {
				JACPModalDialog.getInstance().hideModalMessage();
			}
		});

		ok.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent arg0) {
				String catName = nameInput.getText();

			}
		});
		hboxButtons.getChildren().addAll(ok, cancel);
		getChildren().add(hboxButtons);
		JACPModalDialog.getInstance().showModalMessage(this);
	}

}
