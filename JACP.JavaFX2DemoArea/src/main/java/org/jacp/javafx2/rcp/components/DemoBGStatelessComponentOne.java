package org.jacp.javafx2.rcp.components;

import javafx.event.Event;

import org.jacp.api.action.IAction;
import org.jacp.javafx2.rcp.component.AStatelessCallbackComponent;
/**
 * represents a statelss callback component
 * @author Andy Moncsek
 *
 */
public class DemoBGStatelessComponentOne extends AStatelessCallbackComponent{

	@Override
	public Object handleAction(IAction<Event, Object> action) {
		System.out.println("message to BG stateless compoent one: >>"+ action.getLastMessage() + "<< in thread"
				+ Thread.currentThread() + " :: "+ this);
		return "pong";
	}

}
