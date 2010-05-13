package org.jacp.swing.demo1.editors;

import java.awt.Container;
import java.awt.event.ActionEvent;

import javax.swing.JPanel;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;

import org.jacp.api.action.IAction;
import org.jacp.swing.rcp.editor.ASwingEditor;

public class TestSwingEditor2 extends ASwingEditor {

	int i = 0;

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
	public Container handle(final IAction<ActionEvent,Object> action) {

		System.out.println("Editor2 CALL");
		Long j = 0L;

		while (j < 100000000L) {
			j++;
			// System.out.print("test"+j+" ");

		}

		final int z = i % 3;
		setTarget("editor" + z);
		i = i + 1;
		return handleViewLayout3();
	}

	private Container handleViewLayout3() {
		final DefaultMutableTreeNode top = new DefaultMutableTreeNode(
				"HAllo Welt Editor 2");
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
