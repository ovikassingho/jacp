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
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;
import java.util.Map;

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
import org.jacp.api.component.ISubComponent;
import org.jacp.api.componentLayout.IPerspectiveLayout;
import org.jacp.api.componentLayout.Layout;
import org.jacp.swing.rcp.action.SwingAction;
import org.jacp.swing.rcp.componentLayout.SwingPerspectiveLayout;
import org.jacp.swing.rcp.perspective.ASwingPerspective;

import com.explodingpixels.macwidgets.IAppWidgetFactory;
import com.explodingpixels.macwidgets.SourceList;
import com.explodingpixels.macwidgets.SourceListCategory;
import com.explodingpixels.macwidgets.SourceListClickListener;
import com.explodingpixels.macwidgets.SourceListItem;
import com.explodingpixels.macwidgets.SourceListModel;
import com.explodingpixels.macwidgets.SourceListClickListener.Button;

/**
 * 
 * @author amo
 */
public class TestSwingSplitPanePerspective extends ASwingPerspective {

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
    public void handleMenuEntries(final JMenu meuBar) {
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
    public void handleBarEntries(Map<Layout, Container> bars) {

	final Container toolBar = bars.get(Layout.NORTH);
	final Container bottomBar = bars.get(Layout.SOUTH);
	final JButton add = new JButton(new ImageIcon(Toolkit
		.getDefaultToolkit().getImage("NSImage://NSColorPanel")));
	add.setText("perspective2");
	final JButton add_1 = new JButton(new ImageIcon(Toolkit
		.getDefaultToolkit().getImage("NSImage://NSColorPanel")));

	add_1.setText("perspective3");
	add_1.putClientProperty("JButton.segmentPosition", "first");
	add_1.setFocusable(false);
	add_1.setSize(new Dimension(10, 10));
	add.putClientProperty("JButton.segmentPosition", "first");
	add.setFocusable(false);
	add.setSize(new Dimension(10, 10));
	final IActionListener<ActionListener, ActionEvent, Object> listener = getActionListener();
	listener.getAction().setMessage("id02", "test");
	final IActionListener<ActionListener, ActionEvent, Object> listener_1 = getActionListener();
	listener_1.getAction().setMessage("id01_1", "test");
	add.addActionListener(listener.getListener());
	add_1.addActionListener(listener_1.getListener());
	toolBar.add(add);
	toolBar.add(add_1);
	if (bottomBar != null) {
	    final JButton add2 = new JButton(new ImageIcon(Toolkit
		    .getDefaultToolkit().getImage("NSImage://NSColorPanel")));
	    // add2.putClientProperty("JButton.buttonType",
	    // "segmentedTextured");

	    add2.setFocusable(false);
	    final IActionListener<ActionListener, ActionEvent, Object> listener2 = getActionListener();
	    listener2.getAction().setMessage("id01", "tester");
	    add2.addActionListener(listener2.getListener());
	    add2.setText("perspective1");
	    add2.putClientProperty("JButton.segmentPosition", "first");
	    bottomBar.add(add2);
	}
    }

    @Override
    public void handlePerspective(final SwingAction action,
	    final SwingPerspectiveLayout perspectiveLayout) {

	if (action.getMessage().equals("test")) {
	    final JSplitPane splitPane = new JSplitPane(
		    JSplitPane.VERTICAL_SPLIT);
	    final JSplitPane test = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
	    final JScrollPane scrollPaneView = new JScrollPane(
		    handleViewLayout1());
	    IAppWidgetFactory.makeIAppScrollPane(scrollPaneView);
	    splitPane.add(scrollPaneView, JSplitPane.BOTTOM);
	    final JTabbedPane editorTabs = (JTabbedPane) handleEditorLayout();
	    final JScrollPane scrollPaneEditor = new JScrollPane(editorTabs);
	    IAppWidgetFactory.makeIAppScrollPane(scrollPaneEditor);
	    test.add(scrollPaneEditor, JSplitPane.RIGHT);
	    splitPane.add(test, JSplitPane.TOP);
	    perspectiveLayout.registerTargetLayoutComponent("view",
		    scrollPaneView);
	    perspectiveLayout.registerTargetLayoutComponent("editor",
		    editorTabs);
	    // splitPane.setDividerLocation(100);
	    perspectiveLayout.setRootComponent(splitPane);

	} else {

	    final JSplitPane splitPane = new JSplitPane(
		    JSplitPane.HORIZONTAL_SPLIT);
	    splitPane.setDividerLocation(200);
	    final JSplitPane test = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
	    final JTabbedPane panelTest = new JTabbedPane();
	    panelTest.setTabPlacement(SwingConstants.LEFT);

	    SourceListModel model = new SourceListModel();
	    SourceListCategory category = new SourceListCategory("Editoren");
	    model.addCategory(category);
	    final List<ISubComponent<ActionListener, ActionEvent, Object>> components = this
		    .getSubcomponents();
	    for (final ISubComponent<ActionListener, ActionEvent, Object> comp : components) {
		SourceListItem item = new SourceListItem(comp.getName());

		model.addItemToCategory(item, category);

	    }

	    SourceList sourceList = new SourceList(model);
	    sourceList
		    .addSourceListClickListener(new SourceListClickListener() {

			@Override
			public void sourceListItemClicked(SourceListItem arg0,
				Button arg1, int arg2) {
			    for (final ISubComponent<ActionListener, ActionEvent, Object> comp : components) {
				if (comp.getName().equals(arg0.getText())) {
				    final IActionListener<ActionListener, ActionEvent, Object> listener3 = getActionListener();
				    listener3.getAction().setMessage(
					    comp.getId(), "test");
				    listener3.getListener().actionPerformed(
					    null);
				}
			    }

			}

			@Override
			public void sourceListCategoryClicked(
				SourceListCategory arg0, Button arg1, int arg2) {
			    // TODO Auto-generated method stub

			}
		    });

	    // panelTest.add(handleViewLayout1());
	    final JScrollPane scrollPaneView = new JScrollPane(
		    sourceList.getComponent());
	    IAppWidgetFactory.makeIAppScrollPane(scrollPaneView);
	    splitPane.add(scrollPaneView, JSplitPane.LEFT);

	    final JTabbedPane editorTabs = (JTabbedPane) handleEditorLayout();
	    final JTabbedPane editorTabs2 = (JTabbedPane) handleEditorLayout();
	    final JScrollPane scrollPaneEditor = new JScrollPane(editorTabs);
	    IAppWidgetFactory.makeIAppScrollPane(scrollPaneEditor);
	    final JScrollPane scrollPaneEditor2 = new JScrollPane(editorTabs2);
	    IAppWidgetFactory.makeIAppScrollPane(scrollPaneEditor2);
	    test.add(scrollPaneEditor, JSplitPane.TOP);
	    test.add(scrollPaneEditor2, JSplitPane.BOTTOM);
	    splitPane.add(test, JSplitPane.RIGHT);
	    /*
	     * perspectiveLayout.registerTargetLayoutComponent("editor0",
	     * panelTest);
	     */
	    perspectiveLayout.registerTargetLayoutComponent("editor0",
		    editorTabs);
	    perspectiveLayout.registerTargetLayoutComponent("editor1",
		    editorTabs2);
	    test.setDividerLocation(200);
	    perspectiveLayout.setRootComponent(splitPane);
	}

	System.out.println("CALLL_00");
	// splitPane.setVisible(false);

    }

}
