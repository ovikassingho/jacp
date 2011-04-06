package org.jacp.swing.test.UIComponents;

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
 * Unit test ui component one, this component is for local and inter component
 * test
 * 
 * @author Andy Moncsek
 * 
 */
public class UnitTestUIComponentOne extends ASwingComponent {

	final JPanel panelEditorOne = new JPanel();
	final JButton buttonOneEditorOne = new JButton("ButtonOneEditorOne");
	final JTextField fieldOne = new JTextField();
	final private AtomicInteger counter = new AtomicInteger();

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
		if (action.getLastMessage().equals("oneComponentOneButtonOne")) {
			counter.set(0);
			fieldOne.setText("message: " + action.getLastMessage() + " from: "
					+ action.getSourceId());
		} else if (action.getLastMessage().equals("init")) {
			buttonOneEditorOne.setName("ButtonOneEditorOne");
			final IActionListener<ActionListener, ActionEvent, Object> listenerBottomOne = getActionListener();
			listenerBottomOne.getAction()
					.addMessage("id002", "oneComponentOne");
			buttonOneEditorOne.addActionListener(listenerBottomOne
					.getListener());
			fieldOne.setName("oneTextFiledComponentOne");
			fieldOne.setText("message: " + action.getLastMessage() + " from: "
					+ action.getSourceId());
		} else if (action.getLastMessage().equals("oneComponentTwo")) {
			SwingUtilities.invokeLater(new Runnable() {

				@Override
				public void run() {
					// TODO Auto-generated method stub
					fieldOne.setText("counter: " + counter.getAndIncrement());

				}
			});
		}
		panelEditorOne.add(buttonOneEditorOne);
		panelEditorOne.add(fieldOne);

		return panelEditorOne;
	}
}
