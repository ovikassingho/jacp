package org.jacp.project.JACP.UnitTests.perspecitve;

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

/**
 * This class represents a perspective and acts as a wrapper for services
 * @author Andy Moncsek
 *
 */
public class UnitTestPerspectiveServiceWrapper extends ASwingPerspective {

	@Override
	public void handleMenuEntries(JMenu menuBar) {
		final JMenuItem perspectiveOneMenuItem = new JMenuItem("PerspectiveServiceOne");
		perspectiveOneMenuItem.setName("PerspectiveServiceOne");
		final IActionListener<ActionListener, ActionEvent, Object> listener = getActionListener();
		listener.getAction().setMessage("oneServiceMenu");
		perspectiveOneMenuItem.addActionListener(listener.getListener());
		menuBar.add(perspectiveOneMenuItem);
	}

	@Override
	public void handleBarEntries(Map<Layout, Container> bars) {
		
		//////////////// TOOL BARS ////////////////////
		final Container toolBar = bars.get(Layout.NORTH);
		final JButton perspectiveOneButtoneOne = new JButton();
		perspectiveOneButtoneOne.setText("PerspectiveServiceOneToolBarButton");
		perspectiveOneButtoneOne.setName("PerspectiveServiceOneToolBarButton");

		final IActionListener<ActionListener, ActionEvent, Object> listenerOne = getActionListener();
		listenerOne.getAction().addMessage("id01.id001","oneServiceButtonOne");
		
		perspectiveOneButtoneOne.addActionListener(listenerOne.getListener());
		toolBar.add(perspectiveOneButtoneOne);
		////////////////TOOL BARS END ////////////////////
		
		////////////////BOTTOM BARS ////////////////////
		final Container bottomBar = bars.get(Layout.SOUTH);
		final JButton perspectiveOneButtonBottomOne = new JButton();
		perspectiveOneButtonBottomOne.setText("PerspectiveServiceOneBottomBarButton");
		perspectiveOneButtonBottomOne.setName("PerspectiveServiceOneBottomBarButton");

		final IActionListener<ActionListener, ActionEvent, Object> listenerBottomOne = getActionListener();
		listenerBottomOne.getAction().addMessage("id01.id001","oneServiceButtonBottomOne");
		
		perspectiveOneButtonBottomOne.addActionListener(listenerBottomOne.getListener());
		bottomBar.add(perspectiveOneButtonBottomOne);
		////////////////BOTTOM BARS END////////////////////
	}


	@Override
	public void handlePerspective(SwingAction action,
			SwingPerspectiveLayout perspectiveLayout) {
		
	}

}
