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
		final Container toolBar = bars.get(Layout.NORTH);
		final JButton perspectiveTwoButtoneOne = new JButton();
		perspectiveTwoButtoneOne.setText("PerspectiveTwo");
		perspectiveTwoButtoneOne.setName("PerspectiveTwo");
		final IActionListener<ActionListener, ActionEvent, Object> listenerOne = getActionListener();
		listenerOne.getAction().setMessage("twoButtonOne");

		perspectiveTwoButtoneOne.addActionListener(listenerOne.getListener());
		toolBar.add(perspectiveTwoButtoneOne);

	}

	@Override
	public void handlePerspective(SwingAction action,
			SwingPerspectiveLayout perspectiveLayout) {
		if (action.getLastMessage().equals("twoMenu")) {

		} else if (action.getLastMessage().equals("twoMenu")) {

		}

	}

}
