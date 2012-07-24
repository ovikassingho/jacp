package org.jacp.demo.components;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;

import org.jacp.api.action.IAction;
import org.jacp.api.annotations.DeclarativeComponent;
import org.jacp.api.annotations.OnStart;
import org.jacp.api.annotations.OnTearDown;
import org.jacp.demo.common.GenderType;
import org.jacp.demo.entity.Contact;
import org.jacp.javafx.rcp.component.AFXComponent;
import org.jacp.javafx.rcp.componentLayout.FXComponentLayout;

@DeclarativeComponent(defaultExecutionTarget = "PdetailComponent", id = "id007", name = "XMlDetailView", active = true, viewLocation = "/fxml/UserDetail.fxml", resourceBundleLocation = "bundles.languageBundle", localeID="en_US")
public class XMLDetailView extends AFXComponent {

	@FXML
	private Label lblFirstname;
	@FXML
	private Label lblLastname;
	@FXML
	private Label lblStreet;
	@FXML
	private Label lblZip;
	@FXML
	private Label lblCountry;

	@FXML
	private Pane imagePanel;


	@FXML
	private void handleSubmit(ActionEvent event) {
		System.out.println("UPDATE");
	}

	@Override
	public Node handleAction(IAction<Event, Object> action) {
		System.out.println("HANDLE");
		return null;
	}

	private void fillView(Contact contact) {
		imagePanel.getStyleClass().clear();
		String styleClass = GenderType.FEMALE.getLabel().equals(
				contact.getGender()) ? "female" : "male";
		imagePanel.getStyleClass().add(styleClass);
		lblFirstname.setText(contact.getFirstName());
		lblLastname.setText(contact.getLastName());
		lblZip.setText(contact.getZip());
		lblStreet.setText(contact.getAddress());
		lblCountry.setText(contact.getCountry());
	}

	@Override
	public Node postHandleAction(final Node node,
			final IAction<Event, Object> action) {
		System.out.println("POST HANDLE");
		if (action.getLastMessage() instanceof Contact) {
			// contact selected
			final Contact contact = (Contact) action.getLastMessage();
			if (contact != null) {
				fillView(contact);
			}
		}

		return null;

	}

	@OnStart
	public void start(FXComponentLayout layout, URL url,
			ResourceBundle resourceBundle) {
		System.out.println("START");
	}

	@OnTearDown
	public void stop(FXComponentLayout layout) {
		System.out.println("STOP");
	}

}
