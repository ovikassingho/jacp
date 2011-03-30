package org.jacp.swing.test.UIComponents;

import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextField;

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

	@Override
	public void handleMenuEntries(Container meuneBar) {
		// TODO Auto-generated method stub

	}

	@Override
	public void handleBarEntries(Map<Layout, Container> bars) {
		// TODO Auto-generated method stub

	}

	@Override
	public Container handleAction(IAction<ActionEvent, Object> action) {
		final JPanel panelEditorOne = new JPanel();
		final JButton buttonOneEditorOne = new JButton("ButtonOneEditorOne");
		buttonOneEditorOne.setName("ButtonOneEditorOne");

		final IActionListener<ActionListener, ActionEvent, Object> listenerBottomOne = getActionListener();
		listenerBottomOne.getAction().addMessage("id002", "oneComponentOne");
		buttonOneEditorOne.addActionListener(listenerBottomOne.getListener());
		JTextField fieldOne = new JTextField();
		fieldOne.setName("oneTextFiledComponentOne");
		fieldOne.setText("messgae: "+action.getLastMessage()+ " from: "+action.getSourceId());
		panelEditorOne.add(buttonOneEditorOne);
		panelEditorOne.add(fieldOne);
		return panelEditorOne;
	}

}
