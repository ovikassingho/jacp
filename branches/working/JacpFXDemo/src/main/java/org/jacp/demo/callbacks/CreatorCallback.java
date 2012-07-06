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

import org.jacp.api.action.IAction;
import org.jacp.api.annotations.CallbackComponent;
import org.jacp.demo.entity.ContactDTO;
import org.jacp.javafx.rcp.component.AStatelessCallbackComponent;

/**
 * The CreatorCallback creates contact data with random numbers
 * 
 * @author Andy Moncsek
 * 
 */
@CallbackComponent(id = "id005", name = "creatorCallback", active = false)
public class CreatorCallback extends AStatelessCallbackComponent {

	@Override
	public Object handleAction(final IAction<Event, Object> action) {
		if (action.getLastMessage() instanceof ContactDTO) {
			// return all values to defined target
			this.setHandleTarget("id01.id002");
			waitAmount(100);
			return ContentGenerator.createEntries((ContactDTO) action.getLastMessage());
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
	

}