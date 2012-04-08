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
package org.jacp.demo.callbacks;

import javafx.event.Event;
import javafx.event.EventHandler;

import org.jacp.api.action.IAction;
import org.jacp.api.action.IActionListener;
import org.jacp.api.annotations.CallbackComponent;
import org.jacp.demo.entity.Contact;
import org.jacp.demo.entity.ContactDTO;
import org.jacp.demo.main.Util;
import org.jacp.javafx.rcp.component.AStatelessCallbackComponent;

/**
 * The coordinatorCallback splits the amount of contacts in to chunks and sends
 * it to the coordinatorCallback
 * 
 * @author Andy Moncsek
 * 
 */
@CallbackComponent(id = "id004", name = "coordinatorCallback", active = false)
public class CoordinatorCallback extends AStatelessCallbackComponent {

	@Override
	public Object handleAction(final IAction<Event, Object> action) {
		if (action.getLastMessage() instanceof Contact) {
			final Contact contact = (Contact) action.getLastMessage();
			if (contact.getContacts().isEmpty()) {
				if (contact.getAmount() > Util.PARTITION_SIZE) {
					int amount = contact.getAmount();
					while (amount > Util.PARTITION_SIZE) {
						this.waitAmount(100);
						this.createAmountAndSend(contact.getFirstName(),
								Util.PARTITION_SIZE);
						amount = amount - Util.PARTITION_SIZE;
					}
					this.createAmountAndSend(contact.getFirstName(), amount);
				} else {
					this.createAmountAndSend(contact.getFirstName(),
							contact.getAmount());
				}
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
		final ContactDTO dto = new ContactDTO(name, amount);
		final IActionListener<EventHandler<Event>, Event, Object> listener = this
				.getActionListener("id01.id005", dto);
		listener.performAction(null);
	}

}
