package org.jacp.javafx2.rcp.components;

import java.io.File;

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
		if(action.getLastMessage() instanceof File){
			File folder = (File) action.getLastMessage();
			if(folder.isDirectory()) {
				for(String file: folder.list()){
					System.out.println(file);
				}
			}
		}
		System.out.println("message to BG stateless compoent one: >>"+ action.getLastMessage() + "<< in thread"
				+ Thread.currentThread() + " :: "+ this);
		return "pong";
	}

}
