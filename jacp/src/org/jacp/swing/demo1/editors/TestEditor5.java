package org.jacp.swing.demo1.editors;

import java.awt.Container;
import java.awt.event.ActionEvent;

import javax.swing.JPanel;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;

import org.jacp.api.action.IAction;
import org.jacp.swing.rcp.editor.ASwingEditor;

public class TestEditor5 extends ASwingEditor {

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
	public Container handle(final IAction<Object, ActionEvent> action) {

		System.out.println("Editor5 CALL");
		Long j = 0L;

		while (j < 100000000L) {
			j++;
			// System.out.print("test"+j+" ");

		}

		return handleViewLayout3();
	}

	private Container handleViewLayout3() {
		final DefaultMutableTreeNode top = new DefaultMutableTreeNode(
				"HAllo Welt Editor 5");
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

}
