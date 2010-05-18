package org.jacp.swing.demo1.editors;

import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.tree.DefaultMutableTreeNode;

import org.jacp.api.action.IAction;
import org.jacp.api.action.IActionListener;
import org.jacp.swing.rcp.editor.ASwingEditor;

public class TestSwingEditor extends ASwingEditor {

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

		System.out.println("Editor1 CALL");
		Long i = 0L;
		while (i < 100000000L) {
			i++;

		}

		return handleViewLayout3();
	}

	private Container handleViewLayout3() {
		final DefaultMutableTreeNode top = new DefaultMutableTreeNode(
				"HAllo Welt NEUUU");
		createNodes(top);
		final JScrollPane wrapper = new JScrollPane();
		final JPanel panel = new JPanel();
		final JComponent comp = new JComponent() {

			/**
			 * 
			 */
			private static final long serialVersionUID = 1998547411050919174L;

		};

		final IActionListener<ActionListener, ActionEvent, Object> listener2 = getActionListener();
		listener2.getAction().setMessage("id04", "test");
		final IActionListener<ActionListener, ActionEvent, Object> listener3 = getActionListener();
		listener3.getAction().setMessage("id02.id06", "test");

		final JButton button = new JButton("message to editor2");
		final JButton button2 = new JButton("message test");
		final JButton button3 = new JButton(
				"message to Editor4 in Perspective2");
		button.addActionListener(listener2.getListener());
		button3.addActionListener(listener3.getListener());
		button2.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(final ActionEvent e) {
				int p = 0;
				int count = 4;
				while (p < 2) {
					final IActionListener<ActionListener, ActionEvent, Object> listener3 = getActionListener();
					if (count == 4) {
						listener3.getAction().setMessage("id04", "test");
						count = 5;
					} else {
						listener3.getAction().setMessage("id05", "test");
						count = 4;
					}

					listener3.getListener().actionPerformed(e);
					p++;

				}

			}
		});

		panel.add(button);
		panel.add(button2);
		panel.add(button3);
		wrapper.getViewport().add(comp);
		wrapper.getComponents();

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
