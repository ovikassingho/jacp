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
		
		//////////////// TOOL BARS ////////////////////
		final Container toolBar = bars.get(Layout.NORTH);
		final JButton perspectiveOneButtoneOne = new JButton();
		perspectiveOneButtoneOne.setText("PerspectiveOneToolBarButton");
		perspectiveOneButtoneOne.setName("PerspectiveOneToolBarButton");

		final IActionListener<ActionListener, ActionEvent, Object> listenerOne = getActionListener();
		listenerOne.getAction().setMessage("oneButtonOne");
		
		perspectiveOneButtoneOne.addActionListener(listenerOne.getListener());
		toolBar.add(perspectiveOneButtoneOne);
		////////////////TOOL BARS END ////////////////////
		
		////////////////BOTTOM BARS ////////////////////
		final Container bottomBar = bars.get(Layout.SOUTH);
		final JButton perspectiveOneButtonBottomOne = new JButton();
		perspectiveOneButtonBottomOne.setText("PerspectiveOneBottomBarButton");
		perspectiveOneButtonBottomOne.setName("PerspectiveOneBottomBarButton");

		final IActionListener<ActionListener, ActionEvent, Object> listenerBottomOne = getActionListener();
		listenerBottomOne.getAction().setMessage("oneButtonBottomOne");
		
		perspectiveOneButtonBottomOne.addActionListener(listenerBottomOne.getListener());
		bottomBar.add(perspectiveOneButtonBottomOne);
		////////////////BOTTOM BARS END////////////////////
	}

	@Override
	public void handlePerspective(SwingAction action,
			SwingPerspectiveLayout perspectiveLayout) {
		System.out.println(action.getLastMessage());
		final JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
		splitPane.setName("splitPanePerspectiveOne");
		final JPanel panelOne = new JPanel();
		panelOne.setName("panelOnePerspectiveOne");
		final JPanel panelTwo = new JPanel();
		panelTwo.setName("panelTwoPerspectiveOne");
		if (action.getLastMessage().equals("oneMenu")) {
			
		} else 	if (action.getLastMessage().equals("oneButtonOne")) {
			splitPane.setOrientation(JSplitPane.VERTICAL_SPLIT);
			splitPane.setDividerLocation(256);
			
			JButton buttonOne = new JButton("ButtonOnePerspectiveOneTOP");
			buttonOne.setName("ButtonOnePerspectiveOneTOP");
			
			JButton buttonTwo = new JButton("BottonTwoPerspectiveOneBOTTOM");
			buttonTwo.setName("BottonTwoPerspectiveOneBOTTOM");
			
			panelOne.add(buttonOne);
			panelTwo.add(buttonTwo);
		} else {
			splitPane.setOrientation(JSplitPane.HORIZONTAL_SPLIT);
			splitPane.setDividerLocation(512);
			
			JButton buttonOne = new JButton("ButtonOnePerspectiveOneLEFT");
			buttonOne.setName("ButtonOnePerspectiveOneLEFT");
			
			JButton buttonTwo = new JButton("BottonTwoPerspectiveOneRIGHT");
			buttonTwo.setName("BottonTwoPerspectiveOneRIGHT");
			
			panelOne.add(buttonOne);
			panelTwo.add(buttonTwo);
		}
	


	
		splitPane.add(panelOne);	
		splitPane.add(panelTwo);
		perspectiveLayout.setRootComponent(splitPane);
	}

}
