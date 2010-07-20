package org.jacp.swing.demo1.editors;

import java.awt.event.ActionEvent;

import org.jacp.api.action.IAction;
import org.jacp.swing.rcp.component.AStateComponent;

public class DemoBackgroundMessagePerformanceEditor2 extends AStateComponent {
	private int counter = 0;
	private long startTime;
	@Override
	public Object handleAction(final IAction<ActionEvent, Object> action) {


		System.out.println("BGComponent2: "+action.getMessage());
		if (action.getMessage().equals("stop")) {
			final long stopTime = System.currentTimeMillis();

			System.out.println("stop Time: " + (stopTime - startTime));
			this.setHandleComponentTarget("id09");
			return (stopTime - startTime);
		} else if (action.getMessage().equals("start")) {
			startTime = System.currentTimeMillis();
			System.out.println("count: " + counter);
			counter++;

		} else if(action.getMessage().equals("init")) {
			
		}
		else {
			System.out.println("count: " + counter);
			counter++;
		}
		
		return null;
	}


}
