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
		listenerTwo.getAction().addMessage("id03", "messageFromPerspectiveTwo");

		perspectiveTwoButtoneOne.addActionListener(listenerTwo.getListener());
		toolBar.add(perspectiveTwoButtoneOne);
		////////////////TOOL BARS END ////////////////////
		
		////////////////BOTTOM BARS ////////////////////
		final Container bottomBar = bars.get(Layout.SOUTH);
		final JButton perspectiveTwoButtonBottomOne = new JButton();
		perspectiveTwoButtonBottomOne.setText("PerspectiveTwoBottomBarButton");
		perspectiveTwoButtonBottomOne.setName("PerspectiveTwoBottomBarButton");

		final IActionListener<ActionListener, ActionEvent, Object> listenerBottomTwo = getActionListener();
		listenerBottomTwo.getAction().setMessage("twoButtonBottomOne");
		
		perspectiveTwoButtonBottomOne.addActionListener(listenerBottomTwo.getListener());
		bottomBar.add(perspectiveTwoButtonBottomOne);
		////////////////BOTTOM BARS END////////////////////
	}

	@Override
	public void handlePerspective(SwingAction action,
			SwingPerspectiveLayout perspectiveLayout) {
		System.out.println(action.getLastMessage());
		System.out.println("TEST");
		final JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
		splitPane.setName("splitPanePerspectiveTwo");
		final JPanel panelOne = new JPanel();
		panelOne.setName("panelOnePerspectiveTwo");
		final JPanel panelTwo = new JPanel();
		panelTwo.setName("panelTwoPerspectiveTwo");
		if (action.getLastMessage().equals("messageFromPerspectiveThree")) {
			splitPane.setOrientation(JSplitPane.VERTICAL_SPLIT);
			splitPane.setDividerLocation(256);
			
			JButton buttonOne = new JButton("ButtonOnePerspectiveTwoTOP");
			buttonOne.setName("ButtonOnePerspectiveTwoTOP");
			
			JButton buttonTwo = new JButton("BottonTwoPerspectiveTwoBOTTOM");
			buttonTwo.setName("BottonTwoPerspectiveTwoBOTTOM");
			
			panelOne.add(buttonOne);
			panelTwo.add(buttonTwo);
		} else  {
			splitPane.setOrientation(JSplitPane.HORIZONTAL_SPLIT);
			splitPane.setDividerLocation(512);
			
			JButton buttonOne = new JButton("ButtonOnePerspectiveTwoLEFT");
			buttonOne.setName("ButtonOnePerspectiveTwoLEFT");
			
			JButton buttonTwo = new JButton("BottonTwoPerspectiveTwoRIGHT");
			buttonTwo.setName("BottonTwoPerspectiveTwoRIGHT");
			
			panelOne.add(buttonOne);
			panelTwo.add(buttonTwo);
		}
		splitPane.add(panelOne);	
		splitPane.add(panelTwo);
		perspectiveLayout.setRootComponent(splitPane);
	}

}
