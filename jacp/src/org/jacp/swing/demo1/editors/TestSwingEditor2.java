package org.jacp.swing.demo1.editors;

import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;

import org.jacp.api.action.IAction;
import org.jacp.api.action.IActionListener;
import org.jacp.swing.rcp.editor.ASwingEditor;

public class TestSwingEditor2 extends ASwingEditor {

	int i = 0;
	int p = 2;

	@Override
	public void addMenuEntries(final Container meuneBar) {
		// TODO Auto-generated method stub

	}

	@Override
	public void handleBarEntries(final Container toolBar,
			final Container bottomBar) {
		// TODO Auto-generated method stub

	}

	@Override
	public Container handle(final IAction<ActionEvent, Object> action) {

		if (action.getMessage().equals("test")) {
			Long j = 0L;

			while (j < 100000000L) {
				j++;
				// System.out.print("test"+j+" ");

			}

			final int z = i % 3;
			setTarget("editor" + z);
			i = i + 1;
			System.out.println("Editor2 CALL 1: ");
		} else if (action.getMessage().equals("testBLA")) {
			setTarget("id0" + p + ".editor0");
			if (p == 2) {
				p = 1;
			} else {
				p = 2;
			}
			System.out.println("Editor2 CALL 2: " + p);
		} else {
			System.out.println("Editor2 CALL: " + action.getMessage());
		}

		return handleViewLayout3();
	}

	private Container handleViewLayout3() {
		final JPanel panel = new JPanel();
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

		final DefaultMutableTreeNode top = new DefaultMutableTreeNode(
				"HAllo Welt Editor 2");
		createNodes(top);
		final JTree tree = new JTree(top);

		panel.add(button);
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

}
