package org.jacp.swing.demo1.editors;

import java.awt.event.ActionEvent;

import org.jacp.api.action.IAction;
import org.jacp.swing.rcp.component.AStateComponent;

public class BGPong extends AStateComponent{

    @Override
    public Object handleAction(IAction<ActionEvent, Object> action) {
	if(action.getLastMessage().equals("ping")) {
	    try {
		Thread.sleep(100);
	    } catch (InterruptedException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	    }
	    System.out.println(action.getLastMessage()+" : "+this);
	    return "pong";
	}
	return null;
    }

}
