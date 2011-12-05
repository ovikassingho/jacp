package org.jacp.javafx2.rcp.components;

import javafx.event.Event;

import org.jacp.api.action.IAction;
import org.jacp.javafx2.rcp.component.ACallbackComponent;
/**
 * 
 * @author Andy Moncsek
 *
 */
public class DemoBGStatefulComponentOne extends ACallbackComponent{
	int c = 0;
	@Override
	public Object handleAction(IAction<Event, Object> action) {
		c = c+1;
		System.out.println("message to BG compoent one: >>"+ action.getLastMessage() + "<< in thread"
				+ Thread.currentThread() + " counter: " + c+" :: "+ this);
		return "pong";
	}

}
