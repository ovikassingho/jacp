package org.jacp.project.JACP.UnitTests.perspecitve;

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

public class UnitTestUISubcomponentsPerspectiveTwo extends ASwingPerspective {

	private JSplitPane splitPane;
	private JPanel panelOne;
	private JPanel panelTwo;

	@Override
	public void handleMenuEntries(JMenu menuBar) {
		final JMenuItem perspectiveOneMenuItem = new JMenuItem("PerspectiveTwo");
		perspectiveOneMenuItem.setName("PerspectiveTwo");
		final IActionListener<ActionListener, ActionEvent, Object> listener = getActionListener();
		listener.getAction().setMessage("twoMenu");
		perspectiveOneMenuItem.addActionListener(listener.getListener());
		menuBar.add(perspectiveOneMenuItem);
	}

	@Override
	public void handleBarEntries(Map<Layout, Container> bars) {

		// ////////////// TOOL BARS ////////////////////
		final Container toolBar = bars.get(Layout.NORTH);
		final JButton perspectiveOneButtoneOne = new JButton();
		perspectiveOneButtoneOne.setText("PerspectiveTwoToolBarButton");
		perspectiveOneButtoneOne.setName("PerspectiveTwoToolBarButton");

		final IActionListener<ActionListener, ActionEvent, Object> listenerOne = getActionListener();
		listenerOne.getAction().setMessage("twoButtonOne");

		perspectiveOneButtoneOne.addActionListener(listenerOne.getListener());
		toolBar.add(perspectiveOneButtoneOne);
		// //////////////TOOL BARS END ////////////////////

		// //////////////BOTTOM BARS ////////////////////
		final Container bottomBar = bars.get(Layout.SOUTH);
		final JButton perspectiveOneButtonBottomOne = new JButton();
		perspectiveOneButtonBottomOne.setText("PerspectiveTwoBottomBarButton");
		perspectiveOneButtonBottomOne.setName("PerspectiveTwoBottomBarButton");

		final IActionListener<ActionListener, ActionEvent, Object> listenerBottomOne = getActionListener();
		listenerBottomOne.getAction().setMessage("twoButtonBottomOne");

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
			splitPane.setName("splitPanePerspectiveTwo");
		}
		if (this.panelOne == null) {
			panelOne = new JPanel();
			panelOne.setName("panelOnePerspectiveTwo");
		}
		if (this.panelTwo == null) {
			panelTwo = new JPanel();
			panelTwo.setName("panelTwoPerspectiveTwo");
		}

		if (action.getLastMessage().equals("init")) {

			perspectiveLayout.registerTargetLayoutComponent(
					"perspectiveTwoLeft", panelOne);
			perspectiveLayout.registerTargetLayoutComponent(
					"perspectiveTwoRight", panelTwo);
			splitPane.add(panelOne);
			splitPane.add(panelTwo);
			perspectiveLayout.setRootComponent(splitPane);
		} else if (action.getLastMessage().equals("oneButtonOne")) {
			splitPane.setOrientation(JSplitPane.VERTICAL_SPLIT);
			splitPane.setDividerLocation(256);
		} else {
			splitPane.setOrientation(JSplitPane.HORIZONTAL_SPLIT);
			splitPane.setDividerLocation(512);

		}

	}

}
