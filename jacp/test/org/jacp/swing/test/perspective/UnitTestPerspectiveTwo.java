package org.jacp.swing.test.perspective;

import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JMenu;
import javax.swing.JMenuItem;

import org.jacp.api.action.IActionListener;
import org.jacp.api.componentLayout.Layout;
import org.jacp.swing.rcp.action.SwingAction;
import org.jacp.swing.rcp.componentLayout.SwingPerspectiveLayout;
import org.jacp.swing.rcp.perspective.ASwingPerspective;

public class UnitTestPerspectiveTwo extends ASwingPerspective {

	@Override
	public void handleMenuEntries(JMenu menuBar) {
		final JMenuItem perspectiveTwoMenuItem = new JMenuItem("PerspectiveTwo");
		final IActionListener<ActionListener, ActionEvent, Object> listener = getActionListener();
		listener.getAction().setMessage("twoMenu");
		perspectiveTwoMenuItem.addActionListener(listener.getListener());
		menuBar.add(perspectiveTwoMenuItem);

	}

	@Override
	public void handleBarEntries(Map<Layout, Container> bars) {
		////////////////TOOL BARS ////////////////////
		final Container toolBar = bars.get(Layout.NORTH);
		final JButton perspectiveTwoButtoneOne = new JButton();
		perspectiveTwoButtoneOne.setText("PerspectiveTwoToolBarButton");
		perspectiveTwoButtoneOne.setName("PerspectiveTwoToolBarButton");
		final IActionListener<ActionListener, ActionEvent, Object> listenerTwo = getActionListener();
		listenerTwo.getAction().setMessage("twoButtonOne");

		perspectiveTwoButtoneOne.addActionListener(listenerTwo.getListener());
		toolBar.add(perspectiveTwoButtoneOne);
		////////////////TOOL BARS END ////////////////////
		
		////////////////BOTTOM BARS ////////////////////
		final Container bottomBar = bars.get(Layout.SOUTH);
		final JButton perspectiveTwoButtonBottomOne = new JButton();
		perspectiveTwoButtonBottomOne.setText("PerspectiveTwoBottomBarButton");
		perspectiveTwoButtonBottomOne.setName("PerspectiveTwoToolBottomButton");

		final IActionListener<ActionListener, ActionEvent, Object> listenerBottomTwo = getActionListener();
		listenerBottomTwo.getAction().setMessage("twoButtonBottomOne");
		
		perspectiveTwoButtonBottomOne.addActionListener(listenerBottomTwo.getListener());
		bottomBar.add(perspectiveTwoButtonBottomOne);
		////////////////BOTTOM BARS END////////////////////
	}

	@Override
	public void handlePerspective(SwingAction action,
			SwingPerspectiveLayout perspectiveLayout) {
		if (action.getLastMessage().equals("twoMenu")) {

		} else if (action.getLastMessage().equals("twoMenu")) {

		}

	}

}
