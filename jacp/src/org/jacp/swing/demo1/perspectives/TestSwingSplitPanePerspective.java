/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jacp.swing.demo1.perspectives;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTree;
import javax.swing.SwingConstants;
import javax.swing.tree.DefaultMutableTreeNode;

import org.jacp.api.action.IActionListener;
import org.jacp.swing.rcp.action.SwingAction;
import org.jacp.swing.rcp.componentLayout.SwingPerspectiveLayout;
import org.jacp.swing.rcp.perspective.ASwingPerspective;

/**
 * 
 * @author amo
 */
public class TestSwingSplitPanePerspective extends
		ASwingPerspective<JSplitPane> {

	private Container handleEditorLayout() {
		final JTabbedPane pane = new JTabbedPane();
		pane.setTabPlacement(SwingConstants.BOTTOM);
		// pane.addTab("test1", handleViewLayout());
		/* pane.addTab("test2", handleViewLayout1()); */
		return pane;
	}

	private Container handleViewLayout1() {
		final DefaultMutableTreeNode top = new DefaultMutableTreeNode(
				"The Blubb Series");
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
	public void addMenuEntries(final JMenu meuBar) {
		// meuBar.setText("blubber");
		final JMenuItem quitItem = new JMenuItem("Test");
		final IActionListener<ActionListener, ActionEvent, Object> listener = getActionListener();
		listener.getAction().setMessage("test");
		quitItem.addActionListener(listener.getListener());
		meuBar.add(quitItem);

		final JMenuItem quitItem2 = new JMenuItem("Test2");
		final IActionListener<ActionListener, ActionEvent, Object> listener2 = getActionListener();
		listener2.getAction().setMessage("tester");
		quitItem2.addActionListener(listener2.getListener());
		meuBar.add(quitItem2);

	}

	@Override
	public void handleBarEntries(final Container toolBar,
			final Container bottomBar) {
		final JButton add = new JButton(new ImageIcon(Toolkit
				.getDefaultToolkit().getImage("NSImage://NSColorPanel")));
		// add.putClientProperty("JButton.buttonType", "segmentedTextured");
		add.putClientProperty("JButton.segmentPosition", "first");
		add.setFocusable(false);
		add.setSize(new Dimension(10, 10));
		final IActionListener<ActionListener, ActionEvent, Object> listener = getActionListener();
		listener.getAction().setMessage("id02", "test");
		add.addActionListener(listener.getListener());
		toolBar.add(add);

		if (bottomBar != null) {
			final JButton add2 = new JButton(new ImageIcon(Toolkit
					.getDefaultToolkit().getImage("NSImage://NSColorPanel")));
			// add2.putClientProperty("JButton.buttonType",
			// "segmentedTextured");
			add2.putClientProperty("JButton.segmentPosition", "first");
			add2.setFocusable(false);
			final IActionListener<ActionListener, ActionEvent, Object> listener2 = getActionListener();
			listener2.getAction().setMessage("id01","tester");
			add2.addActionListener(listener2.getListener());
			bottomBar.add(add2);
		}
	}

	@Override
	public void handleInitialLayout(final SwingAction action,
			final SwingPerspectiveLayout<JSplitPane> perspectiveLayout) {

		if (action.getMessage().equals("test")) {
			final JSplitPane splitPane = new JSplitPane(
					JSplitPane.VERTICAL_SPLIT);
			final JSplitPane test = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
			final JScrollPane scrollPaneView = new JScrollPane(
					handleViewLayout1());
			splitPane.add(scrollPaneView, JSplitPane.BOTTOM);
			final JTabbedPane editorTabs = (JTabbedPane) handleEditorLayout();
			final JScrollPane scrollPaneEditor = new JScrollPane(editorTabs);
			test.add(scrollPaneEditor, JSplitPane.RIGHT);
			splitPane.add(test, JSplitPane.TOP);
			perspectiveLayout.registerTargetLayoutComponent("view",
					scrollPaneView);
			perspectiveLayout.registerTargetLayoutComponent("editor",
					editorTabs);
			// splitPane.setDividerLocation(100);
			perspectiveLayout.setRootLayoutComponent(splitPane);

		} else {

			final JSplitPane splitPane = new JSplitPane(
					JSplitPane.HORIZONTAL_SPLIT);
			splitPane.setDividerLocation(300);
			final JSplitPane test = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
			final JTabbedPane panelTest = new JTabbedPane();
			panelTest.setTabPlacement(SwingConstants.LEFT);
			// panelTest.add(handleViewLayout1());
			final JScrollPane scrollPaneView = new JScrollPane(panelTest);

			splitPane.add(scrollPaneView, JSplitPane.LEFT);

			final JTabbedPane editorTabs = (JTabbedPane) handleEditorLayout();
			final JTabbedPane editorTabs2 = (JTabbedPane) handleEditorLayout();
			final JScrollPane scrollPaneEditor = new JScrollPane(editorTabs);
			final JScrollPane scrollPaneEditor2 = new JScrollPane(editorTabs2);
			test.add(scrollPaneEditor, JSplitPane.TOP);
			test.add(scrollPaneEditor2, JSplitPane.BOTTOM);
			splitPane.add(test, JSplitPane.RIGHT);
			perspectiveLayout.registerTargetLayoutComponent("editor0",
					panelTest);
			perspectiveLayout.registerTargetLayoutComponent("editor1",
					editorTabs);
			perspectiveLayout.registerTargetLayoutComponent("editor2",
					editorTabs2);
			test.setDividerLocation(200);
			perspectiveLayout.setRootLayoutComponent(splitPane);
		}

		System.out.println("CALLL_00");
		// splitPane.setVisible(false);

	}

}