/*
 * Copyright (C) 2010 - 2012.
 * AHCP Project (http://code.google.com/p/jacp)
 * All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at 
 *
 *     http://www.apache.org/licenses/LICENSE-2.0 
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an "AS IS"
 * BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either 
 * express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */
package org.jacp.demo.components;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.util.Callback;
import org.jacp.api.action.IAction;
import org.jacp.api.action.IActionListener;
import org.jacp.api.util.ToolbarPosition;
import org.jacp.demo.entity.Contact;
import org.jacp.demo.enums.BarChartAction;
import org.jacp.javafx2.rcp.component.AFX2Component;
import org.jacp.javafx2.rcp.componentLayout.FX2ComponentLayout;
import org.jacp.javafx2.rcp.components.optionPane.JACPModalDialog;
import org.jacp.javafx2.rcp.components.toolBar.JACPToolBar;
import org.jacp.javafx2.rcp.util.FX2Util.MessageUtil;
import org.springframework.util.StringUtils;

/**
 * The ContactTreeViewComponent displays the contact category on the left side of the
 * application; It creates a "add category" button to add new categories to view
 * 
 * @author Andy Moncsek
 * 			Patrick Symmangk
 */
public class ContactTreeViewComponent extends AFX2Component {
	private ScrollPane pane;
	private ObservableList<Contact> contactList;

	@Override
	/**
	 * handle the component in worker thread
	 */
	public Node handleAction(final IAction<Event, Object> action) {
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
		if (action.getLastMessage() instanceof Contact) {
			final Contact contact = (Contact) action.getLastMessage();
			this.addNewContact(contact);
		}
		return this.pane;
	}

	private void addNewContact(final Contact contact) {
		this.contactList.add(contact);
	}

	@Override
	/**
	 * handle menu an toolbar entries on component start up
	 */
	public void onStartComponent(final FX2ComponentLayout layout) {
		final JACPToolBar north = (JACPToolBar) layout
				.getRegisteredToolBar(ToolbarPosition.NORTH);
		final Button add = new Button("add category");
		add.getStyleClass().add("first");
		add.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(final ActionEvent event) {
				ContactTreeViewComponent.this.createAddContactDialog(event);
			}

		});
		north.add(add);
	}

	private void createAddContactDialog(final ActionEvent arg0) {
		final VBox box = new VBox();
		box.setId("ProxyDialog");
		box.setMaxSize(300, Region.USE_PREF_SIZE);
		// the title
		final Label title = new Label("Add new category");
		title.setId("jacp-custom-title");
		VBox.setMargin(title, new Insets(2, 2, 10, 2));

		final HBox hboxInput = new HBox();
		final Label nameLabel = new Label("category name:");
		HBox.setMargin(nameLabel, new Insets(2));
		final TextField nameInput = new TextField();
		HBox.setMargin(nameInput, new Insets(0, 0, 0, 5));
		HBox.setHgrow(nameInput, Priority.ALWAYS);
		hboxInput.getChildren().addAll(nameLabel, nameInput);

		final HBox hboxButtons = new HBox();
		hboxButtons.setAlignment(Pos.CENTER_RIGHT);
		final Button ok = new Button("OK");
		HBox.setMargin(ok, new Insets(6, 5, 4, 2));
		final Button cancel = new Button("Cancel");
		HBox.setMargin(cancel, new Insets(6, 2, 4, 5));
		cancel.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(final ActionEvent arg0) {
				JACPModalDialog.getInstance().hideModalMessage();
			}
		});
		ok.setDefaultButton(true);

		ok.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(final ActionEvent actionEvent) {
				final String catName = nameInput.getText();
				if (catName != null && StringUtils.hasText(catName)) {

					// contacts
					final Contact contact = new Contact();
					contact.setFirstName(catName);
					final IActionListener<EventHandler<Event>, Event, Object> listener = ContactTreeViewComponent.this
							.getActionListener(contact);
					listener.performAction(actionEvent);

					JACPModalDialog.getInstance().hideModalMessage();
				}
			}
		});
		hboxButtons.getChildren().addAll(ok, cancel);

		box.getChildren().addAll(title, hboxInput, hboxButtons);
		JACPModalDialog.getInstance().showModalMessage(box);
	}

	@Override
	public void onTearDownComponent(final FX2ComponentLayout layout) {

	}

	private ScrollPane createInitialLayout() {
		final GridPane gridPane = new GridPane();
		gridPane.getStyleClass().addAll("dark", "dark-border");
		this.pane = new ScrollPane();
		pane.getStyleClass().addAll("dark-scrollpane");
		this.pane.setFitToHeight(true);
		this.pane.setFitToWidth(true);
		GridPane.setHgrow(this.pane, Priority.ALWAYS);
		GridPane.setVgrow(this.pane, Priority.ALWAYS);
		this.pane.setContent(gridPane);

		gridPane.setPadding(new Insets(5));
		gridPane.setHgap(10);
		gridPane.setVgap(10);

		// the label
		final Label categoryLbl = new Label("Category");
		categoryLbl.getStyleClass().addAll("light-label","list-label");
		GridPane.setHalignment(categoryLbl, HPos.CENTER);
		gridPane.add(categoryLbl, 0, 0);

		this.contactList = this.getCategoryList();
		final ListView<Contact> categoryListView = this.createList();
		GridPane.setHgrow(categoryListView, Priority.ALWAYS);
		GridPane.setVgrow(categoryListView, Priority.ALWAYS);
		gridPane.add(categoryListView, 0, 1);
		GridPane.setMargin(categoryListView, new Insets(0, 10, 10, 10));
		return this.pane;
	}

	private ListView<Contact> createList() {
		final ListView<Contact> categoryListView = new ListView<Contact>(
				this.contactList);
		categoryListView
				.setCellFactory(new Callback<ListView<Contact>, ListCell<Contact>>() {

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
									configureProgressBar(contact);
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
											final IActionListener<EventHandler<Event>, Event, Object> listener = ContactTreeViewComponent.this
													.getActionListener(
															"id01.id002",
															contact);
											listener.performAction(event);
											final IActionListener<EventHandler<Event>, Event, Object> resetListener = ContactTreeViewComponent.this
													.getActionListener(
															"id01.id003",
															BarChartAction.RESET);
											resetListener.performAction(event);
										}
									});
								}

							}
						}; // ListCell
						return cell;
					}
				});

		return categoryListView;
	}

	private void configureProgressBar(final Contact contact) {
		if (contact.getProgress() == null) {
			final ProgressBar progressBar = new ProgressBar();
			progressBar.getStyleClass().add("jacp-progress-bar");
			contact.setProgress(progressBar);
		}
		if (contact.getContacts().isEmpty()) {
			contact.getProgress().setVisible(false);
		} else {
			contact.getProgress().setVisible(true);
		}
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
