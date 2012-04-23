package com.trivadis.techevent.components;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.scene.Node;
import org.jacp.api.action.IAction;
import org.jacp.api.annotations.Component;
import org.jacp.javafx.rcp.component.AFXComponent;
import org.jacp.javafx.rcp.util.FXUtil.MessageUtil;

import com.trivadis.techevent.entity.Contact;
import com.trivadis.techevent.entity.ContactDTO;
import com.trivadis.techevent.util.Util;
import com.trivadis.techevent.views.ContactTableView;

/**
 * Simple UI component, creates a ScrollPane with a Button and a label. The
 * Button will trigger a message to the StatelessCallback.
 * 
 * @author Andy Moncsek
 * 
 */
@Component(defaultExecutionTarget = "PMain", id = "id002", name = "componentRight", active = true)
public class ComponentRight extends AFXComponent {
	private final Map<String, ContactTableView> all = Collections
			.synchronizedMap(new HashMap<String, ContactTableView>());
	private ContactTableView current;

	@Override
	/**
	 * run handleAction in worker Thread
	 */
	public Node handleAction(final IAction<Event, Object> action) {
		return null;
	}

	@Override
	/**
	 * run postHandle in FX application Thread, use this method to update UI code
	 */
	public Node postHandleAction(final Node node,
			final IAction<Event, Object> action) {

		if (action.getLastMessage() instanceof Contact) {
			// contact selected
			final Contact contact = (Contact) action.getLastMessage();
			if (contact.isEmpty()) {
				// redirect stateful component
				contact.setAmount(Util.MAX);
				contact.setEmpty(false);
				getActionListener("id01.id003", contact).performAction(null);
			}
			this.current = this.getView(contact);

		} else if (action.getLastMessage() instanceof ContactDTO) {
			// contact data received
			final ContactDTO dto = (ContactDTO) action.getLastMessage();
			final ContactTableView view = this.all.get(dto.getParentName());
			// add first 1000 entries directly to table
			if (view.getContactTableView().getItems().size() < Util.PARTITION_SIZE) {
				view.getContactTableView().getItems().addAll(dto.getContacts());
			} else {
				// all other entries are added to list for paging
				this.updateContactList(view, dto.getContacts());
			}
			view.updatePositionLabel();

		} else if (action.getLastMessage().equals(MessageUtil.INIT)) {
			return this.getView(null).getTableViewLayout();
		}

		return this.current.getTableViewLayout();
	}

	private void updateContactList(final ContactTableView view,
			final ObservableList<Contact> list) {
		// add chunk of contact list to contact
		view.getContact().getContacts().addAll(list);
		view.updateMaxValue();
	}

	private ContactTableView getView(final Contact contact) {
		ContactTableView view = null;
		if (contact == null) {
			view = this.createView(null);
		} else if (!this.all.containsKey(contact.getFirstName())) {
			view = this.createView(contact);
			this.all.put(contact.getFirstName(), view);
		} else if (contact != null) {
			view = this.all.get(contact.getFirstName());
		}
		return view;
	}

	private ContactTableView createView(final Contact contact) {
		return  new ContactTableView(contact);
	}

}
