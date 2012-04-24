package com.trivadis.com.callbacks;

import javafx.event.Event;

import org.jacp.api.action.IAction;
import org.jacp.api.annotations.CallbackComponent;
import org.jacp.javafx.rcp.component.AStatelessCallbackComponent;

import com.trivadis.techevent.entity.ContactDTO;
import com.trivadis.techevent.util.ContentGenerator;

/**
 * a simple stateless callback component. Messages to this type of component
 * will result in many instances, controlled by the JacpFX scheduler. Do not use
 * any private members as you can not be sure to get the same instance twice.
 * Stateless Components must have the spring scope="prototype", otherwise only
 * one instance will run. The Result will allays be send back to caller
 * component or create intermediate messages to other components.
 * 
 * @author Andy Moncsek
 * 
 */
@CallbackComponent(id = "id004", name = "statelessCallback", active = true)
public class StatelessCallback extends AStatelessCallbackComponent {
	@Override
	public Object handleAction(final IAction<Event, Object> action) {
		if (action.getLastMessage() instanceof ContactDTO) {
			// return all values to defined target
			this.setHandleTarget("id01.id002");
			return ContentGenerator.createEntries((ContactDTO) action.getLastMessage());
		}
		return null;
	}
	
}
