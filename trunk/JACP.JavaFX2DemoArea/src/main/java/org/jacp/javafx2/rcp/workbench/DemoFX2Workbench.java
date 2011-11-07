package org.jacp.javafx2.rcp.workbench;

import javafx.event.Event;
import javafx.scene.Node;
import javafx.scene.layout.Region;
import javafx.stage.StageStyle;

import org.jacp.api.action.IAction;
import org.jacp.api.componentLayout.IWorkbenchLayout;
import org.jacp.javafx2.rcp.workbench.AFX2Workbench;

/**
 * Test workbench for jacp javafx2
 * 
 * @author Andy Moncsek
 * 
 */
public class DemoFX2Workbench extends AFX2Workbench {

	public DemoFX2Workbench() {
		super("UnitTestWorkbench");

	}

	@Override
	public void handleInitialLayout(IAction<Event, Object> action,
			IWorkbenchLayout<Region, Node, StageStyle> layout) {
		System.out.println("1: "+action.getLastMessage());
		layout.setWorkbenchXYSize(1024, 400);
	//	layout.setStyle(StageStyle.UNDECORATED);
		System.out.println("2: "+action.getLastMessage());
	}
	


}
