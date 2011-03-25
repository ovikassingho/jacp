package org.jacp.swing.test.UIComponents;

import java.awt.Container;
import java.awt.event.ActionEvent;
import java.util.Map;

import org.jacp.api.action.IAction;
import org.jacp.api.componentLayout.Layout;
import org.jacp.swing.rcp.component.ASwingComponent;
/**
 * Unit test ui component two, this component is for local and inter component test
 * @author Andy Moncsek
 *
 */
public class UnitTestUIComponentTwo extends ASwingComponent {

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
		// TODO Auto-generated method stub
		return null;
	}
}
