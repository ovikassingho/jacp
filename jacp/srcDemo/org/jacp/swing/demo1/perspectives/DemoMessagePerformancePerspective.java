package org.jacp.swing.demo1.perspectives;

import java.awt.Container;
import java.util.Map;

import javax.swing.JMenu;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;

import org.jacp.api.componentLayout.Layout;
import org.jacp.swing.rcp.action.SwingAction;
import org.jacp.swing.rcp.componentLayout.SwingPerspectiveLayout;
import org.jacp.swing.rcp.perspective.ASwingPerspective;

public class DemoMessagePerformancePerspective extends ASwingPerspective {

	@Override
	public void handleMenuEntries(final JMenu menuBar) {
		// TODO Auto-generated method stub

	}



	@Override
	public void handlePerspective(final SwingAction action,
			final SwingPerspectiveLayout perspectiveLayout) {
		final JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
		final JScrollPane scrollPaneView = new JScrollPane();
		splitPane.add(scrollPaneView, JSplitPane.BOTTOM);

		final JScrollPane scrollPaneEditor = new JScrollPane();
		splitPane.add(scrollPaneEditor, JSplitPane.TOP);

		perspectiveLayout.registerTargetLayoutComponent("editor1",
				scrollPaneView);
		perspectiveLayout.registerTargetLayoutComponent("editor0",
				scrollPaneEditor);

		perspectiveLayout.setRootComponent(splitPane);

	}






	@Override
	public void handleBarEntries(Map<Layout, Container> bars) {
	    // TODO Auto-generated method stub
	    
	}

}
