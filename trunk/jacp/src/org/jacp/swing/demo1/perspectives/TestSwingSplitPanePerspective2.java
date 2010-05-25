/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jacp.swing.demo1.perspectives;

import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.SwingConstants;

import org.jacp.api.action.IActionListener;
import org.jacp.swing.rcp.action.SwingAction;
import org.jacp.swing.rcp.componentLayout.SwingPerspectiveLayout;
import org.jacp.swing.rcp.perspective.ASwingPerspective;

/**
 * 
 * @author amo
 */
public class TestSwingSplitPanePerspective2 extends
		ASwingPerspective<JSplitPane> {

	private Container handleEditorLayout() {
		final JTabbedPane pane = new JTabbedPane();
		pane.setTabPlacement(SwingConstants.BOTTOM);
		return pane;
	}

	private Container handleViewLayout() {
		final JTabbedPane pane = new JTabbedPane();
		pane.setTabPlacement(SwingConstants.LEFT);
		return pane;
	}

	@Override
	public void addMenuEntries(final JMenu meuBar) {
		// meuBar.setText("blubber");
		final JMenuItem quitItem = new JMenuItem("Test2");

		final IActionListener<ActionListener, ActionEvent, Object> listener = getActionListener();
		listener.getAction().setMessage("id01", "test");
		quitItem.addActionListener(listener.getListener());
		meuBar.add(quitItem);

		final JMenuItem quitItem2 = new JMenuItem("Test3");
		final IActionListener<ActionListener, ActionEvent, Object> listener2 = getActionListener();
		listener2.getAction().setMessage("test");
		quitItem2.addActionListener(listener2.getListener());
		meuBar.add(quitItem2);
	}

	@Override
	public void handleInitialLayout(final SwingAction action,
			final SwingPerspectiveLayout<JSplitPane> perspectiveLayout) {
		final JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
		splitPane.setDividerLocation(300);

		final JTabbedPane editorTabs = (JTabbedPane) handleEditorLayout();
		final JTabbedPane viewTabs = (JTabbedPane) handleViewLayout();
		final JScrollPane scrollPaneEditor = new JScrollPane(editorTabs);
		final JScrollPane scrollPaneView = new JScrollPane(viewTabs);

		splitPane.add(scrollPaneView, JSplitPane.LEFT);

		splitPane.add(scrollPaneEditor, JSplitPane.RIGHT);
		perspectiveLayout.registerTargetLayoutComponent("view", viewTabs);
		perspectiveLayout.registerTargetLayoutComponent("editor0", editorTabs);
		// splitPane.setDividerLocation(100);
		perspectiveLayout.setRootLayoutComponent(splitPane);
		System.out.println("perspective2");
	}

	@Override
	public void handleBarEntries(final Container toolBar,
			final Container bottomBar) {

	}
}
