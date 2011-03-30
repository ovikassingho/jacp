package org.jacp.swing.test.UIComponents;

import java.awt.Container;
import java.awt.event.ActionEvent;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JPanel;

import org.jacp.api.action.IAction;
import org.jacp.api.componentLayout.Layout;
import org.jacp.swing.rcp.component.ASwingComponent;
/**
 * Unit test ui component one, this component is for local and inter component test
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
		panelEditorOne.add(buttonOneEditorOne);
		System.out.println("EDITOR_ONE: "+action.getLastMessage());
		return panelEditorOne;
	}

}
