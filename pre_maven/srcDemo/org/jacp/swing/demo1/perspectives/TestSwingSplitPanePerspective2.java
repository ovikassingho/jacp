/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jacp.swing.demo1.perspectives;

import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.Map;

import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import org.jacp.api.action.IActionListener;
import org.jacp.api.component.ISubComponent;
import org.jacp.api.componentLayout.IPerspectiveLayout;
import org.jacp.api.componentLayout.Layout;
import org.jacp.swing.rcp.action.SwingAction;
import org.jacp.swing.rcp.componentLayout.SwingPerspectiveLayout;
import org.jacp.swing.rcp.perspective.ASwingPerspective;

import com.explodingpixels.macwidgets.IAppWidgetFactory;

/**
 * 
 * @author amo
 */
public class TestSwingSplitPanePerspective2 extends ASwingPerspective {

	private final JTextField text1 = new JTextField();
	int i = 0;

	private Container handleEditorLayout() {
		final JTabbedPane pane = new JTabbedPane();
		pane.setTabPlacement(SwingConstants.BOTTOM);
		final JPanel panel = new JPanel();
		final List<ISubComponent<ActionListener, ActionEvent, Object>> myeditors = getSubcomponents();
		for (final ISubComponent<ActionListener, ActionEvent, Object> e : myeditors) {
			final JTextField field = new JTextField(e.getName());
			panel.add(field);

		}
		text1.setText("count: " + i);
		i++;
		panel.add(text1);
		pane.add(panel);
		return pane;
	}

	private Container handleViewLayout() {
		final JTabbedPane pane = new JTabbedPane();
		pane.setTabPlacement(SwingConstants.LEFT);

		return pane;
	}

	@Override
	public void handleMenuEntries(final JMenu meuBar) {
		// meuBar.setText("blubber");
		final JMenuItem quitItem = new JMenuItem("Test2");

		final IActionListener<ActionListener, ActionEvent, Object> listener = getActionListener();
		listener.getAction().addMessage("id01", "test");
		quitItem.addActionListener(listener.getListener());
		meuBar.add(quitItem);

		final JMenuItem quitItem2 = new JMenuItem("Test3");
		final IActionListener<ActionListener, ActionEvent, Object> listener2 = getActionListener();
		listener2.getAction().setMessage("test");
		quitItem2.addActionListener(listener2.getListener());
		meuBar.add(quitItem2);
	}

	@Override
	public void handlePerspective(final SwingAction action,
			final SwingPerspectiveLayout perspectiveLayout) {
		final JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
		splitPane.setDividerLocation(300);

		final JTabbedPane editorTabs = (JTabbedPane) handleEditorLayout();
		final JTabbedPane viewTabs = (JTabbedPane) handleViewLayout();
		final JScrollPane scrollPaneEditor = new JScrollPane(editorTabs);
		final JScrollPane scrollPaneView = new JScrollPane(viewTabs);
		IAppWidgetFactory.makeIAppScrollPane(scrollPaneView);
		IAppWidgetFactory.makeIAppScrollPane(scrollPaneEditor);
		splitPane.add(scrollPaneView, JSplitPane.LEFT);

		splitPane.add(scrollPaneEditor, JSplitPane.RIGHT);
		perspectiveLayout.registerTargetLayoutComponent("view", viewTabs);
		perspectiveLayout.registerTargetLayoutComponent("editor0", editorTabs);
		// splitPane.setDividerLocation(100);
		perspectiveLayout.setRootComponent(splitPane);
		System.out.println("perspective2");
	}


	@Override
	public void handleBarEntries(Map<Layout, Container> bars) {
	    // TODO Auto-generated method stub
	    
	}


}
