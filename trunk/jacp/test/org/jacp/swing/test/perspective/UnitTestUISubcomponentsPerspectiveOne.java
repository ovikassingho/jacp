package org.jacp.swing.test.perspective;

import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JSplitPane;

import org.jacp.api.action.IActionListener;
import org.jacp.api.componentLayout.Layout;
import org.jacp.swing.rcp.action.SwingAction;
import org.jacp.swing.rcp.componentLayout.SwingPerspectiveLayout;
import org.jacp.swing.rcp.perspective.ASwingPerspective;

public class UnitTestUISubcomponentsPerspectiveOne extends ASwingPerspective {

	private JSplitPane splitPane;
	private JPanel panelOne;
	private JPanel panelTwo;

	@Override
	public void handleMenuEntries(JMenu menuBar) {
		final JMenuItem perspectiveOneMenuItem = new JMenuItem("PerspectiveOne");
		perspectiveOneMenuItem.setName("PerspectiveOne");
		final IActionListener<ActionListener, ActionEvent, Object> listener = getActionListener();
		listener.getAction().setMessage("oneMenu");
		perspectiveOneMenuItem.addActionListener(listener.getListener());
		menuBar.add(perspectiveOneMenuItem);
	}

	@Override
	public void handleBarEntries(Map<Layout, Container> bars) {

		// ////////////// TOOL BARS ////////////////////
		final Container toolBar = bars.get(Layout.NORTH);
		final JButton perspectiveOneButtoneOne = new JButton();
		perspectiveOneButtoneOne.setText("PerspectiveOneToolBarButton");
		perspectiveOneButtoneOne.setName("PerspectiveOneToolBarButton");

		final IActionListener<ActionListener, ActionEvent, Object> listenerOne = getActionListener();
		listenerOne.getAction().setMessage("oneButtonOne");

		perspectiveOneButtoneOne.addActionListener(listenerOne.getListener());
		toolBar.add(perspectiveOneButtoneOne);
		// //////////////TOOL BARS END ////////////////////

		// //////////////BOTTOM BARS ////////////////////
		final Container bottomBar = bars.get(Layout.SOUTH);
		final JButton perspectiveOneButtonBottomOne = new JButton();
		perspectiveOneButtonBottomOne.setText("PerspectiveOneBottomBarButton");
		perspectiveOneButtonBottomOne.setName("PerspectiveOneBottomBarButton");

		final IActionListener<ActionListener, ActionEvent, Object> listenerBottomOne = getActionListener();
		listenerBottomOne.getAction().setMessage("oneButtonBottomOne");

		perspectiveOneButtonBottomOne.addActionListener(listenerBottomOne
				.getListener());
		bottomBar.add(perspectiveOneButtonBottomOne);
		// //////////////BOTTOM BARS END////////////////////
	}

	@Override
	public void handlePerspective(SwingAction action,
			SwingPerspectiveLayout perspectiveLayout) {
		System.out.println(action.getLastMessage());
		if (this.splitPane == null) {
			splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
			splitPane.setName("splitPanePerspectiveOne");
		}
		if (this.panelOne == null) {
			panelOne = new JPanel();
			panelOne.setName("panelOnePerspectiveOne");
		}
		if (this.panelTwo == null) {
			panelTwo = new JPanel();
			panelTwo.setName("panelTwoPerspectiveOne");
		}

		if (action.getLastMessage().equals("init")) {

			perspectiveLayout.registerTargetLayoutComponent(
					"perspectiveOneLeft", panelOne);
			perspectiveLayout.registerTargetLayoutComponent(
					"perspectiveOneRight", panelTwo);
			splitPane.setDividerLocation(512);
			splitPane.add(panelOne);
			splitPane.add(panelTwo);
			perspectiveLayout.setRootComponent(splitPane);
		} else if (action.getLastMessage().equals("oneButtonOne")) {
			splitPane.setOrientation(JSplitPane.VERTICAL_SPLIT);
		} else if (action.getLastMessage().equals("oneButtonBottomOne")) {
			splitPane.setOrientation(JSplitPane.HORIZONTAL_SPLIT);
		} 
	}

}
