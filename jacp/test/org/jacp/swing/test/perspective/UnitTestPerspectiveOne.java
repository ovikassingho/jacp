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

public class UnitTestPerspectiveOne extends ASwingPerspective {

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
		final Container toolBar = bars.get(Layout.NORTH);
		final JButton perspectiveOneButtoneOne = new JButton();
		perspectiveOneButtoneOne.setText("PerspectiveOne");
		perspectiveOneButtoneOne.setName("PerspectiveOne");

		final IActionListener<ActionListener, ActionEvent, Object> listenerOne = getActionListener();
		listenerOne.getAction().setMessage("oneButtonOne");
		
		perspectiveOneButtoneOne.addActionListener(listenerOne.getListener());
		toolBar.add(perspectiveOneButtoneOne);

	}

	@Override
	public void handlePerspective(SwingAction action,
			SwingPerspectiveLayout perspectiveLayout) {
		if (action.getLastMessage().equals("oneMenu")) {

		} else 	if (action.getLastMessage().equals("oneButtonOne")) {

		}

	}

}
