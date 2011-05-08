package org.jacp.swing.demo1.editors;

import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;

import org.jacp.api.action.IAction;
import org.jacp.api.action.IActionListener;
import org.jacp.api.componentLayout.Layout;
import org.jacp.swing.rcp.component.ASwingComponent;

public class TestSwingEditor2 extends ASwingComponent {

	int i = 0;
	int p = 2;

	@Override
	public void handleMenuEntries(final Container meuneBar) {
		// TODO Auto-generated method stub

	}



	@Override
	public Container handleAction(final IAction<ActionEvent, Object> action) {

		if (action.getLastMessage().equals("test")) {
			Long j = 0L;

			while (j < 1000000000L) {
				j++;

			}
			System.out.println("DONE COUNT: ");
			final int z = i % 2;
			setExecutionTarget("editor" + z);
			i = i + 1;
			System.out.println("Editor2 CALL 1: ");
		} else if (action.getLastMessage().equals("test1")) {
			Long j = 0L;

			while (j < 100000L) {
				j++;

			}
			System.out.println("DONE COUNT111: ");
			final int z = i % 2;
			setExecutionTarget("editor" + z);
			i = i + 1;
			System.out.println("Editor2 CALL 1: ");
		} else if (action.getLastMessage().equals("testBLA")) {
			setExecutionTarget("id0" + p + ".editor0");
			if (p == 2) {
				p = 1;
			} else {
				p = 2;
			}
			System.out.println("Editor2 CALL 2: " + p);
		} else {
			System.out.println("Editor2 CALL: " + action.getLastMessage());
		}

		return handleViewLayout3();
	}

	private Container handleViewLayout3() {
		final JPanel panel = new JPanel();
		final JSplitPane pane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
		final JSplitPane pane1 = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
		final JButton button = new JButton("move editor to perspecive 2");
		button.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(final ActionEvent e) {

				final IActionListener<ActionListener, ActionEvent, Object> listener3 = getActionListener();
				System.out.println("action");
				listener3.getAction().setMessage("testBLA");
				listener3.getListener().actionPerformed(e);

			}
		});
		
		final JButton button2 = new JButton("send message to bg component");
		button2.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(final ActionEvent e) {

				final IActionListener<ActionListener, ActionEvent, Object> listener3 = getActionListener();
				System.out.println("action");
				listener3.getAction().addMessage("id10","hello");
				listener3.getListener().actionPerformed(e);

			}
		});

		final DefaultMutableTreeNode top = new DefaultMutableTreeNode(
				"HAllo Welt Editor 2");
		createNodes(top);
		final JTree tree = new JTree(top);

		pane.add(button);
		pane1.add(button2);
		pane1.add(tree);
		pane.add(pane1);

		panel.add(pane);
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
