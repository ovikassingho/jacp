package org.jacp.javafx2.rcp.test1;

import javafx.event.Event;
import javafx.scene.Node;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import org.jacp.api.action.IAction;
import org.jacp.api.componentLayout.IWorkbenchLayout;
import org.jacp.api.util.ToolbarPosition;
import org.jacp.javafx2.rcp.componentLayout.FX2ComponentLayout;
import org.jacp.javafx2.rcp.workbench.AFX2Workbench;

public class TestOneFX2Workbench extends AFX2Workbench{

	@Override
	public void handleInitialLayout(IAction<Event, Object> action,
			IWorkbenchLayout<Node> layout, Stage stage) {
		layout.setWorkbenchXYSize(1024, 960);
		layout.registerToolBar(ToolbarPosition.NORTH);
		layout.registerToolBar(ToolbarPosition.SOUTH);
		layout.setStyle(StageStyle.DECORATED);
		System.out.println("TestOneFX2Workbench action: " + action.getLastMessage());
	}

	@Override
	public void postHandle(FX2ComponentLayout layout) {
		// TODO Auto-generated method stub
		
	}

}
