package org.jacp.swing.demo1.editors;

import java.awt.event.ActionEvent;

import org.jacp.api.action.IAction;
import org.jacp.swing.rcp.component.AStateComponent;

public class BGPing extends AStateComponent{
   private boolean start=false;

    @Override
    public Object handleAction(IAction<ActionEvent, Object> action) {
	if(action.getMessage().equals("pong") && start) {
	    try {
		Thread.sleep(100);
	    } catch (InterruptedException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	    }
	    System.out.println(action.getMessage()+" : "+this);
	    return "ping";
	    
	} else if(action.getMessage().equals("start")) {
	    start=true;
	    System.out.println(action.getMessage());
	    this.setHandleTarget("id14");
	    return "ping";
	}else if(action.getMessage().equals("stop")) {
	    start=false;
	    return null;
	}
	return null;
    }

}
