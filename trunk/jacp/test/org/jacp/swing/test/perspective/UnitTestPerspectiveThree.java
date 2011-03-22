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

public class UnitTestPerspectiveThree extends ASwingPerspective {

	@Override
	public void handleMenuEntries(JMenu menuBar) {
		final JMenuItem perspectiveThreeMenuItem = new JMenuItem(
				"PerspectiveThree");
		perspectiveThreeMenuItem.setName("PerspectiveThree");
		final IActionListener<ActionListener, ActionEvent, Object> listener = getActionListener();
		listener.getAction().setMessage("threeMenu");
		perspectiveThreeMenuItem.addActionListener(listener.getListener());
		menuBar.add(perspectiveThreeMenuItem);

	}

	@Override
	public void handleBarEntries(Map<Layout, Container> bars) {
		////////////////TOOL BARS ////////////////////
		final Container toolBar = bars.get(Layout.NORTH);
		final JButton perspectiveThreeButtoneOne = new JButton();
		perspectiveThreeButtoneOne.setText("PerspectiveThreeToolBarButton");
		perspectiveThreeButtoneOne.setName("PerspectiveThreeToolBarButton");
		final IActionListener<ActionListener, ActionEvent, Object> listenerThree = getActionListener();
		listenerThree.getAction().setMessage("threeButtonOne");

		perspectiveThreeButtoneOne.addActionListener(listenerThree.getListener());
		toolBar.add(perspectiveThreeButtoneOne);
		////////////////TOOL BARS END ////////////////////	
		
		////////////////BOTTOM BARS ////////////////////
		final Container bottomBar = bars.get(Layout.SOUTH);
		final JButton perspectiveThreeButtonBottomOne = new JButton();
		perspectiveThreeButtonBottomOne.setText("PerspectiveThreeBottomBarButton");
		perspectiveThreeButtonBottomOne.setName("PerspectiveThreeBottomBarButton");

		final IActionListener<ActionListener, ActionEvent, Object> listenerBottomThree = getActionListener();
		listenerBottomThree.getAction().setMessage("threeButtonBottomOne");
		
		perspectiveThreeButtonBottomOne.addActionListener(listenerBottomThree.getListener());
		bottomBar.add(perspectiveThreeButtonBottomOne);
		////////////////BOTTOM BARS END////////////////////

	}

	@Override
	public void handlePerspective(SwingAction action,
			SwingPerspectiveLayout perspectiveLayout) {
		System.out.println(action.getLastMessage());
		if (action.getLastMessage().equals("threeMenu")) {

		} else if (action.getLastMessage().equals("threeButtonOne")) {

		}

	}

}
