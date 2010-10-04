package org.jacp.swing.demo1.editors;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import org.jacp.api.action.IAction;
import org.jacp.api.component.IBGComponent;
import org.jacp.swing.rcp.component.AStateComponent;

public class DemoBackgroundMessagePerformanceEditor extends AStateComponent {

	@Override
	public Object handleAction(final IAction<ActionEvent, Object> action) {

		System.out.println("Hallo Welt BG Component!!");
		System.out.println("BGComponent: "+action.getMessage());
		int i = 0;
		while (i < 10) {
			System.out.println("wait");
			try {
				Thread.sleep(100);
			} catch (final InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			i++;
		}
		this.setHandleTarget("id03");
		return "Hallo, viele Gruesse";
	}

	


}
