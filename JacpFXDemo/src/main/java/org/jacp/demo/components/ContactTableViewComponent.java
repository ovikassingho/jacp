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

import java.util.HashMap;
import java.util.Map;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.layout.GridPane;
import javafx.util.Callback;
import org.jacp.api.action.IAction;
import org.jacp.api.action.IActionListener;
import org.jacp.api.annotations.Component;
import org.jacp.demo.entity.Contact;
import org.jacp.demo.entity.ContactDTO;
import org.jacp.demo.main.Util;
import org.jacp.javafx.rcp.component.AFXComponent;
import org.jacp.javafx.rcp.componentLayout.FXComponentLayout;
import org.jacp.javafx.rcp.components.optionPane.JACPDialogButton;
import org.jacp.javafx.rcp.components.optionPane.JACPDialogUtil;
import org.jacp.javafx.rcp.components.optionPane.JACPOptionPane;
import org.jacp.javafx.rcp.util.FXUtil.MessageUtil;

/**
 * The ContactTableViewComponent create the table view for an category
 * 
 * @author Andy Moncsek
 * 
 */
@Component(defaultExecutionTarget = "PmainContentTop", id = "id002", name = "contactDemoTableView", active = true)
public class ContactTableViewComponent extends AFXComponent {

	private final Map<String, ContactView> all = new HashMap<String, ContactView>();
	private ContactView current;

	@Override
	/**
	 * run handleAction in worker Thread
	 */
	public Node handleAction(final IAction<Event, Object> action) {
		if (action.getLastMessage() instanceof ContactDTO) {
			this.updateContactList((ContactDTO) action.getLastMessage());

		}
		return null;
	}

	@Override
	/**
	 * run postHandle in FX application Thread, use this method to update UI code
	 */
	public Node postHandleAction(final Node node,
			final IAction<Event, Object> action) {
		GridPane pane = null;
		if (action.getLastMessage() instanceof Contact) {
			final Contact contact = (Contact) action.getLastMessage();
			if (contact.isEmpty()) {
				this.showDialogIfEmpty(contact);
			}
			this.current = this.getView(contact);
			pane = this.current.getTableViewLayout();
			return pane;

		} else if (action.getLastMessage() instanceof ContactDTO) {
			final ContactDTO dto = (ContactDTO) action.getLastMessage();
			final ContactView view = this.all.get(dto.getParentName());
			// first 1000 entries
			if (view.getContactTableView().getItems().size() < Util.PARTITION_SIZE) {
				view.getContactTableView().getItems().addAll(dto.getContacts());
			}
			view.updatePositionLabel();

		} else if (action.getLastMessage().equals(MessageUtil.INIT)) {
			return this.getView(null).getTableViewLayout();
		}

		return this.current.getTableViewLayout();
	}

	private Callback<TableView<Contact>, TableRow<Contact>> createRowCallback() {
		return new Callback<TableView<Contact>, TableRow<Contact>>() {

			@Override
			public TableRow<Contact> call(final TableView<Contact> arg0) {
				final TableRow<Contact> row = new TableRow<Contact>() {
					@Override
					public void updateItem(final Contact contact,
							final boolean emty) {
						super.updateItem(contact, emty);
						if (contact != null) {
							this.setOnMouseClicked(new EventHandler<Event>() {
								@Override
								public void handle(final Event arg0) {
									// send contact to TableView
									// component to show containing
									// contacts
									final IActionListener<EventHandler<Event>, Event, Object> listener = ContactTableViewComponent.this
											.getActionListener("id01.id006",
													contact);
									listener.performAction(arg0);
								}
							});
						}
					}
				};
				return row;
			}

		};
	}

	private void updateContactList(final ContactDTO dto) {
		// add chunk of contact list to contact
		final ContactView view = this.all.get(dto.getParentName());
		view.getContact().getContacts().addAll(dto.getContacts());
		view.updateMaxValue();
	}

	private ContactView getView(final Contact contact) {
		ContactView view = null;
		if (contact == null) {
			view = this.createView(null);
		} else if (!this.all.containsKey(contact.getFirstName())) {
			view= this.createView(contact);
			this.all.put(contact.getFirstName(), view);
		} else if (contact != null) {
			view = this.all.get(contact.getFirstName());
		}
		return view;
	}

	private ContactView createView(final Contact contact) {
		final ContactView view = new ContactView();
		view.createInitialTableViewLayout(contact);
		view.getContactTableView().setRowFactory(this.createRowCallback());
		return view;
	}

	private void showDialogIfEmpty(final Contact contact) {
		// show popup to ask how many contacts to create
		final JACPOptionPane dialog = JACPDialogUtil.createOptionPane(
				"Contact Demo Pane",
				"Currently are no contact in this category available. Do you want to create "
						+ Util.MAX + " contacts?");
		dialog.setDefaultButton(JACPDialogButton.NO);
		dialog.setDefaultCloseButtonVisible(true);

		dialog.setOnYesAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(final ActionEvent arg0) {
				contact.setAmount(Util.MAX);
				contact.setEmpty(false);
				// redirect contact to coordinator callback to create
				// contacts
				final IActionListener<EventHandler<Event>, Event, Object> listener = ContactTableViewComponent.this
						.getActionListener("id01.id004", contact);
				listener.performAction(arg0);
			}
		});

		dialog.setOnNoAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(final ActionEvent arg0) {

			}
		});
		dialog.showDialog();

	}

	@Override
	public void onStartComponent(final FXComponentLayout layout) {

	}

	@Override
	public void onTearDownComponent(final FXComponentLayout layout) {

	}

}
