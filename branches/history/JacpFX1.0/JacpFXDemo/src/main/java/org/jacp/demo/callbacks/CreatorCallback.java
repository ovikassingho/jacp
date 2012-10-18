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

import java.util.Random;

import javafx.event.Event;

import org.jacp.api.action.IAction;
import org.jacp.demo.entity.Contact;
import org.jacp.demo.entity.ContactDTO;
import org.jacp.javafx2.rcp.component.AStatelessCallbackComponent;
/**
 * The CreatorCallback creates contact data with random numbers 
 * @author Andy Moncsek
 *
 */
public class CreatorCallback extends AStatelessCallbackComponent {
	private final Random rnd = new Random();

	private final String[] firstNames = { "Walter", "Hans", "Peter", "Maria",
			"Heinrich", "Ray", "Richard", "Ulysses", "Robert", "Daniel",
			"Robin", "Johana" };
	private final String[] lastNames = { "Jung", "Matt", "D. Steinberg",
			"Roth", "Dittrich", "Peterson", "P. Phillips", "Holz", "Boll",
			"E. Morris", "J. Smith", "Hopp" };
	private final String[] address = { "Concord Street", "Trails End Road 3",
			"Hillside Drive", "Columbia Boulevard", "Teresien Str. 22",
			"Leisure Lane 4", "Simpson Street 1", "Primrose Lane 3",
			"Lewis Street", "Schlierenbch 10", "Marcus Street 55",
			"Zuerichsee 3" };
	private final String[] zip = { "0234", "2343", "3345", "2346", "2342",
			"22334", "4432", "2344", "4432", "2342", "234223", "2342" };
	private final String[] phones = { "34535453453", "345345345", "45645654",
			"47645634", "23452342", "23423423", "234234234", "234234211",
			"446564", "475457433", "2343413", "4645456456" };
	private final String[] country = { "DE", "US", "RU", "BE", "CH", "GB",
			"FR", "DE", "US", "RU", "BE", "FR" };

	@Override
	public Object handleAction(final IAction<Event, Object> action) {
		if (action.getLastMessage() instanceof ContactDTO) {
			// return all values to defined target
			this.setHandleTarget("id01.id002");
			return this.createEntries((ContactDTO) action.getLastMessage());
		}
		return null;
	}

	/**
	 * create a contact entry
	 * 
	 * @param dto
	 * @return
	 */
	private ContactDTO createEntries(final ContactDTO dto) {
		final int amount = dto.getAmount();
		for (int i = 0; i < amount; i++) {
			final Contact contact = new Contact();
			contact.setFirstName(this.firstNames[this.random()]);
			contact.setLastName(this.lastNames[this.random()]);
			contact.setAddress(this.address[this.random()]);
			contact.setZip(this.zip[this.random()]);
			contact.setPhoneNumber(this.phones[this.random()]);
			contact.setCountry(this.country[this.random()]);
			dto.getContacts().add(contact);
		}
		this.waitAmount(100);
		return dto;
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

	// Common but flawed!
	private int random() {
		return Math.abs(this.rnd.nextInt()) % this.firstNames.length;
	}

}
