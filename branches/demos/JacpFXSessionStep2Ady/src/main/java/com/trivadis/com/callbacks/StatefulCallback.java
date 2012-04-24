package com.trivadis.com.callbacks;



import javafx.event.Event;

import org.jacp.api.action.IAction;
import org.jacp.api.annotations.CallbackComponent;
import org.jacp.javafx.rcp.component.AStatefulCallbackComponent;

import com.trivadis.techevent.entity.Contact;
import com.trivadis.techevent.entity.ContactDTO;
import com.trivadis.techevent.util.Util;

/**
 * A StatefulCallback component is a simple background component whose result
 * will be always send back to the caller component. Unlike a Steteless
 * Component you can use it like a "normal" bean.
 * 
 * @author Andy Moncsek
 * 
 */
@CallbackComponent(id = "id003", name = "statefulCallback", active = true)
public class StatefulCallback extends AStatefulCallbackComponent {
	@Override
	public Object handleAction(final IAction<Event, Object> action) {
		if (action.getLastMessage() instanceof Contact) {			
			final Contact contact = (Contact) action.getLastMessage();
			if (contact.getContacts().isEmpty()) {
				int amount = contact.getAmount();
				while (amount > Util.PARTITION_SIZE) {
					this.waitAmount(100);
					this.createAmountAndSend(contact.getFirstName(),
							Util.PARTITION_SIZE);
					amount = amount - Util.PARTITION_SIZE;
				}
				this.createAmountAndSend(contact.getFirstName(), amount);
			}
		}
		return null;
	}

	/**
	 * for demo purposes
	 */
	private void waitAmount(final int amount) {
		try {
			Thread.sleep(amount);
		} catch (final InterruptedException e) {
			e.printStackTrace();
		}
	}

	/**
	 * create a contact dto with parent name and amount of contacts to create,
	 * send dto to creator callback
	 * 
	 * @param name
	 * @param amount
	 */
	private void createAmountAndSend(final String name, final int amount) {
		getActionListener("id01.id004", new ContactDTO(name, amount)).performAction(null);
	
	}

}
