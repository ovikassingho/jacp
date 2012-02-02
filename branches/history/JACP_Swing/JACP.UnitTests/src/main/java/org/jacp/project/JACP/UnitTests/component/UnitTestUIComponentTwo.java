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
import org.jacp.swing.rcp.component.ASwingComponent;

/**
 * Unit test ui component two, this component is for local and inter component
 * test
 * 
 * @author Andy Moncsek
 * 
 */
public class UnitTestUIComponentTwo extends ASwingComponent {
	final JPanel panelEditorTwo = new JPanel();
	final JButton buttonOneEditorTwo = new JButton("ButtonOneEditorTwo");
	final JTextField fieldTwo = new JTextField();
	final private AtomicInteger counter = new AtomicInteger();

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
		if (action.getLastMessage().equals("oneComponentTwoButtonOne")) {
			counter.set(0);
			fieldTwo.setText("message: " + action.getLastMessage() + " from: "
					+ action.getSourceId());
		} else if (action.getLastMessage().equals("init")) {
			buttonOneEditorTwo.setName("ButtonOneEditorTwo");
			final IActionListener<ActionListener, ActionEvent, Object> listenerBottomOne = getActionListener();
			listenerBottomOne.getAction()
					.addMessage("id001", "oneComponentTwo");
			buttonOneEditorTwo.addActionListener(listenerBottomOne
					.getListener());
			fieldTwo.setName("oneTextFiledComponentTwo");
			fieldTwo.setText("message: " + action.getLastMessage() + " from: "
					+ action.getSourceId());
		} else if (action.getLastMessage().equals("oneComponentOne")) {

			SwingUtilities.invokeLater(new Runnable() {

				@Override
				public void run() {
					// TODO Auto-generated method stub
					fieldTwo.setText("counter: " + counter.getAndIncrement());

				}
			});

		}

		panelEditorTwo.add(buttonOneEditorTwo);
		panelEditorTwo.add(fieldTwo);
		return panelEditorTwo;
	}
}
