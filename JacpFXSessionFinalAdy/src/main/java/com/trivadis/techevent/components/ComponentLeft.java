package com.trivadis.techevent.components;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.util.Callback;

import org.jacp.api.action.IAction;
import org.jacp.api.annotations.Component;
import org.jacp.api.annotations.OnStart;
import org.jacp.javafx.rcp.component.AFXComponent;
import org.jacp.javafx.rcp.componentLayout.FXComponentLayout;
import org.jacp.javafx.rcp.util.FXUtil.MessageUtil;

import com.trivadis.techevent.entity.Contact;
import com.trivadis.techevent.views.ContactTreeView;

/**
 * a simple UI component, creates a ScrollPane with a Button and a label. The Button will trigger a message to the StatefulCallback component.
 * 
 * @author Andy Moncsek
 * 
 */
@Component(defaultExecutionTarget = "PLeft", id = "id001", name = "componentLeft", active = true)
public class ComponentLeft extends AFXComponent {
	private ContactTreeView pane;

	@Override
	/**
	 * handle the component in worker thread
	 */
	public Node handleAction(final IAction<Event, Object> action) {
		// on initial message create the layout outside the FXApplication thread
		if (action.getLastMessage().equals(MessageUtil.INIT)) {
			return this.createInitialLayout();
		}
		return null;
	}

	@Override
	/**
	 * handle the component in FX application thread
	 */
	public Node postHandleAction(final Node node,
			final IAction<Event, Object> action) {
		return this.pane;
	}

	

	/**
	 * handle menu an toolbar entries on component start up
	 */
	@OnStart
	public void onStartComponent(final FXComponentLayout layout) {
		
	}

	private ContactTreeView createInitialLayout() {
		this.pane = new ContactTreeView(this.getCategoryList());
		addCellFactory(pane);
		return this.pane;
	}
	
	private void addCellFactory(final ContactTreeView pane) {
		pane.getCategoryListView().setCellFactory(new Callback<ListView<Contact>, ListCell<Contact>>() {

			@Override
			public ListCell<Contact> call(final ListView<Contact> arg0) {
				final Label label = new Label();
				label.getStyleClass().add("dark-text");
				final HBox box = new HBox();
				final Pane spacer = new Pane();
				final ListCell<Contact> cell = new ListCell<Contact>() {
					@Override
					public void updateItem(final Contact contact,
							final boolean emty) {
						super.updateItem(contact, emty);
						if (contact != null) {
							label.setText(contact.getFirstName());
							pane.configureProgressBar(contact);
							HBox.setMargin(contact.getProgress(),
									new Insets(3, 0, 0, 0));
							HBox.setHgrow(spacer, Priority.ALWAYS);
							box.getChildren().addAll(label, spacer,
									contact.getProgress());

							this.setGraphic(box);
							this.setOnMouseClicked(new EventHandler<Event>() {

								@Override
								public void handle(final Event event) {
									// send contact to TableView
									// component to show containing
									// contacts
									getActionListener(
													"id01.id002",
													contact).performAction(event);

								}
							});
						}

					}
				}; // ListCell
				return cell;
			}
		});
	}

	/**
	 * create dummy category list
	 * 
	 * @return
	 */
	private ObservableList<Contact> getCategoryList() {
		final ObservableList<Contact> categories = FXCollections
				.<Contact> observableArrayList();
		final Contact privateContact = new Contact();
		privateContact.setFirstName("private");
		final Contact publicContact = new Contact();
		publicContact.setFirstName("public");
		final Contact officeContact = new Contact();
		officeContact.setFirstName("office");
		categories.add(privateContact);
		categories.add(publicContact);
		categories.add(officeContact);
		return categories;
	}
	
	


}
