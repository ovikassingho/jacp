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
 * Unit test ui component one, this component is for local and inter component
 * test
 * 
 * @author Andy Moncsek
 * 
 */
public class UnitTestUIComponentOneNoMembers extends ASwingComponent {

	
	private static final String COUNTERNAME = "oneTextFieldComponentOneCounter";
	@Override
	public void handleMenuEntries(Container menuBar) {

	}

	@Override
	public void handleBarEntries(Map<Layout, Container> bars) {
		// ////////////// TOOL BARS ////////////////////
		final Container toolBar = bars.get(Layout.NORTH);
		final JButton perspectiveOneComponentButtoneOne = new JButton();
		perspectiveOneComponentButtoneOne.setText("POneComponentOneButton");
		perspectiveOneComponentButtoneOne.setName("POneComponentOneButton");

		final IActionListener<ActionListener, ActionEvent, Object> listenerOne = getActionListener();
		listenerOne.getAction().setMessage("oneComponentOneButtonOne");

		perspectiveOneComponentButtoneOne.addActionListener(listenerOne
				.getListener());
		toolBar.add(perspectiveOneComponentButtoneOne);
		// //////////////TOOL BARS END ////////////////////
	}

	@Override
	public Container handleAction(IAction<ActionEvent, Object> action) {

		final JButton buttonOneEditorOne = new JButton("ButtonOneEditorOne");

		buttonOneEditorOne.setName("ButtonOneEditorOne");
		final IActionListener<ActionListener, ActionEvent, Object> listenerBottomOne = getActionListener();
		listenerBottomOne.getAction().addMessage("id002", "oneComponentOne");
		buttonOneEditorOne.addActionListener(listenerBottomOne.getListener());

		JPanel panelEditorOne = new JPanel();
		JTextField fieldOne = new JTextField();
		fieldOne.setName("oneTextFieldComponentOne");

		if (action.getLastMessage().equals("oneComponentOneButtonOne")) {
			ExternalCounter.getInstance().resetCounter(COUNTERNAME);
			fieldOne.setText("message: " + action.getLastMessage() + " from: "
					+ action.getSourceId());
		} else if (action.getLastMessage().equals("oneComponentTwo")) {
			fieldOne.setText("counter: " + ExternalCounter.getInstance().incrementCounter(COUNTERNAME));
		}
		panelEditorOne.add(buttonOneEditorOne);
		panelEditorOne.add(fieldOne);

		return panelEditorOne;
	}
}
