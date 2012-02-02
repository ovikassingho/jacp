package org.jacp.project.JACP.UnitTests.component;

import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import org.jacp.api.action.IAction;
import org.jacp.api.action.IActionListener;
import org.jacp.api.componentLayout.Layout;
import org.jacp.project.JACP.Util.util.ExternalCounter;
import org.jacp.swing.rcp.component.ASwingComponent;


/**
 * Unit test ui component two, this component is for local and inter component
 * test
 * 
 * @author Andy Moncsek
 * 
 */
public class UnitTestUIComponentTwoNoMembers extends ASwingComponent {

	private static final String COUNTERNAME = "oneTextFieldComponentTwo";

	@Override
	public void handleMenuEntries(Container meuneBar) {
		// TODO Auto-generated method stub

	}

	@Override
	public void handleBarEntries(Map<Layout, Container> bars) {
		// ////////////// TOOL BARS ////////////////////
		final Container toolBar = bars.get(Layout.NORTH);
		final JButton perspectiveOneComponentButtoneOne = new JButton();
		perspectiveOneComponentButtoneOne.setText("POneComponentTwoButton");
		perspectiveOneComponentButtoneOne.setName("POneComponentTwoButton");

		final IActionListener<ActionListener, ActionEvent, Object> listenerOne = getActionListener();
		listenerOne.getAction().setMessage("oneComponentTwoButtonOne");

		perspectiveOneComponentButtoneOne.addActionListener(listenerOne
				.getListener());
		toolBar.add(perspectiveOneComponentButtoneOne);
		// //////////////TOOL BARS END ////////////////////

	}

	@Override
	public Container handleAction(IAction<ActionEvent, Object> action) {

		JPanel panelEditorTwo = new JPanel();

		JButton buttonOneEditorTwo = new JButton("ButtonOneEditorTwo");
		buttonOneEditorTwo.setName("ButtonOneEditorTwo");
		final IActionListener<ActionListener, ActionEvent, Object> listenerBottomOne = getActionListener();
		listenerBottomOne.getAction().addMessage("id001", "oneComponentTwo");
		buttonOneEditorTwo.addActionListener(listenerBottomOne.getListener());

		JTextField fieldTwo = new JTextField();
		fieldTwo.setName("oneTextFieldComponentTwo");

		if (action.getLastMessage().equals("oneComponentTwoButtonOne")) {
			ExternalCounter.getInstance().resetCounter(COUNTERNAME);
			fieldTwo.setText("message: " + action.getLastMessage() + " from: "
					+ action.getSourceId());
		} else if (action.getLastMessage().equals("oneComponentOne")) {

			fieldTwo.setText("counter: "
					+ ExternalCounter.getInstance().incrementCounter(
							COUNTERNAME));

		}

		panelEditorTwo.add(buttonOneEditorTwo);
		panelEditorTwo.add(fieldTwo);
		return panelEditorTwo;
	}
}
