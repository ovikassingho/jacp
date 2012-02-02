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
		listenerThree.getAction().addMessage("id02", "messageFromPerspectiveThree");

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
		System.out.println("TEST");
		final JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
		splitPane.setName("splitPanePerspectiveThree");
		final JPanel panelOne = new JPanel();
		panelOne.setName("panelOnePerspectiveThree");
		final JPanel panelTwo = new JPanel();
		panelTwo.setName("panelTwoPerspectiveThree");
		if (action.getLastMessage().equals("messageFromPerspectiveTwo")) {
			splitPane.setOrientation(JSplitPane.VERTICAL_SPLIT);
			splitPane.setDividerLocation(256);
			
			JButton buttonOne = new JButton("ButtonOnePerspectiveThreeTOP");
			buttonOne.setName("ButtonOnePerspectiveThreeTOP");
			
			JButton buttonTwo = new JButton("BottonTwoPerspectiveThreeBOTTOM");
			buttonTwo.setName("BottonTwoPerspectiveThreeBOTTOM");
			
			panelOne.add(buttonOne);
			panelTwo.add(buttonTwo);
		} else  {
			splitPane.setOrientation(JSplitPane.HORIZONTAL_SPLIT);
			splitPane.setDividerLocation(512);
			
			JButton buttonOne = new JButton("ButtonOnePerspectiveThreeLEFT");
			buttonOne.setName("ButtonOnePerspectiveThreeLEFT");
			
			JButton buttonTwo = new JButton("BottonTwoPerspectiveThreeRIGHT");
			buttonTwo.setName("BottonTwoPerspectiveThreeRIGHT");
			
			panelOne.add(buttonOne);
			panelTwo.add(buttonTwo);
		}
		splitPane.add(panelOne);	
		splitPane.add(panelTwo);
		perspectiveLayout.setRootComponent(splitPane);
	}

}
