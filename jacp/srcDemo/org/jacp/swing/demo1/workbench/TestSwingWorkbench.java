/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jacp.swing.demo1.workbench;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.Map;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JToolBar;
import javax.swing.KeyStroke;

import org.jacp.api.componentLayout.Layout;
import org.jacp.api.util.WorkspaceMode;
import org.jacp.swing.rcp.action.SwingAction;
import org.jacp.swing.rcp.componentLayout.SwingWorkbenchLayout;
import org.jacp.swing.rcp.workbench.ASwingWorkbench;

/**
 * 
 * @author Andy Moncsek
 */
public class TestSwingWorkbench extends ASwingWorkbench {

    /**
	 * 
	 */
    private static final long serialVersionUID = -3314308084499117301L;

    public TestSwingWorkbench() {
	super("my Test Workbench");

    }

    @Override
    public JMenu handleMenuEntries(final JMenu menuBar) {
	final JMenu defaultMenu = menuBar;

	final JMenuItem menuItem = new JMenuItem();
	menuItem.setText("Test");
	final JMenuItem saveAsItem = new JMenuItem("Save As...");
	saveAsItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S,
		(java.awt.event.InputEvent.SHIFT_MASK | (Toolkit
			.getDefaultToolkit().getMenuShortcutKeyMask()))));
	saveAsItem.addActionListener(new ActionListener() {

	    @Override
	    public void actionPerformed(final ActionEvent e) {
		System.out.println("Save As...");
	    }
	});
	defaultMenu.add(saveAsItem);
	defaultMenu.add(menuItem);
	return defaultMenu;
    }

    @Override
    public void handleBarEntries(final Map<Layout, Container> bars) {
	final Container toolBar = bars.get(Layout.NORTH);
	final Container bottomBar = bars.get(Layout.SOUTH);
	final JButton add = new JButton(new ImageIcon(Toolkit
		.getDefaultToolkit().getImage("NSImage://NSAddTemplate")));
	add.putClientProperty("JButton.buttonType", "segmentedTextured");
	add.putClientProperty("JButton.segmentPosition", "first");
	add.setFocusable(false);
	toolBar.add(add);

	if (bottomBar != null) {
	    final JButton add2 = new JButton(new ImageIcon(Toolkit
		    .getDefaultToolkit().getImage("NSImage://NSAddTemplate")));
	    add2.putClientProperty("JButton.buttonType", "segmentedTextured");
	    add2.putClientProperty("JButton.segmentPosition", "first");
	    add2.setFocusable(false);
	    bottomBar.add(add2);
	}
    }

    @Override
    public void handleInitialLayout(final SwingAction action,
	    final SwingWorkbenchLayout layout) {
	layout.setWorkspaceMode(WorkspaceMode.SINGLE_PANE);
	layout.setLayoutManager(new BorderLayout());
	layout.registerToolBar(Layout.NORTH, new JToolBar());
	layout.registerToolBar(Layout.SOUTH, new JToolBar());
	layout.setWorkbenchXYSize(1024, 600);
    }

}
