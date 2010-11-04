package org.jacp.swing.demo1.editors;

import java.awt.Container;
import java.awt.event.ActionEvent;
import java.util.Map;

import javax.swing.JPanel;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;

import org.jacp.api.action.IAction;
import org.jacp.api.componentLayout.Layout;
import org.jacp.swing.rcp.component.ASwingComponent;

public class TestEditor4 extends ASwingComponent {

	@Override
	public void handleMenuEntries(final Container meuneBar) {
		// TODO Auto-generated method stub

	}



	@Override
	public Container handleAction(final IAction<ActionEvent, Object> action) {

		System.out.println("Editor4 CALL");
		Long j = 0L;

		while (j < 100000000L) {
			j++;
			// System.out.print("test"+j+" ");

		}
		System.out.println("Editor4 got message: " + action.getMessage());
		if (action.getMessage().equals("test")) {
			return handleViewLayout4();
		} else {
			return handleViewLayout3();
		}

	}

	private Container handleViewLayout3() {
		final DefaultMutableTreeNode top = new DefaultMutableTreeNode(
				"HAllo Welt Editor 4");
		createNodes(top);
		final JTree tree = new JTree(top);
		final JPanel panel = new JPanel();
		panel.add(tree);
		return panel;
	}

	private Container handleViewLayout4() {
		final DefaultMutableTreeNode top = new DefaultMutableTreeNode(
				"Neue Nachricht erhalten");
		createNodes(top);
		final JTree tree = new JTree(top);
		final JPanel panel = new JPanel();
		panel.add(tree);
		return panel;
	}

	private void createNodes(final DefaultMutableTreeNode top) {
		DefaultMutableTreeNode category = null;
		DefaultMutableTreeNode book = null;

		category = new DefaultMutableTreeNode("Books for Java Programmers");
		top.add(category);
		book = new DefaultMutableTreeNode("rrkhiu");
		category.add(book);

	}



	@Override
	public void handleBarEntries(Map<Layout, Container> bars) {
	    // TODO Auto-generated method stub
	    
	}

}
