package org.jacp.swing.demo1.editors;

import java.awt.Container;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Map;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.tree.DefaultMutableTreeNode;

import org.jacp.api.action.IAction;
import org.jacp.api.action.IActionListener;
import org.jacp.api.componentLayout.Layout;
import org.jacp.swing.rcp.component.ASwingComponent;

public class TestSwingEditor extends ASwingComponent {

	@Override
	public void handleMenuEntries(final Container meuneBar) {
		// TODO Auto-generated method stub

	}



	@Override
	public Container handleAction(final IAction<ActionEvent, Object> action) {

		System.out.println("Editor1 CALL::" + action.getLastMessage()+" : source: "+action.getSourceId());
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
		listener2.getAction().addMessage("id04", "test");
		final IActionListener<ActionListener, ActionEvent, Object> listener3 = getActionListener();
		listener3.getAction().addMessage("id02.id06", "test");
		final IActionListener<ActionListener, ActionEvent, Object> listener4 = getActionListener();
		listener4.getAction().addMessage("id01.id03", "test11");
		final IActionListener<ActionListener, ActionEvent, Object> listener5 = getActionListener();
		listener5.getAction().addMessage("id01.id13", "start");
		final IActionListener<ActionListener, ActionEvent, Object> listener6 = getActionListener();
		listener6.getAction().addMessage("id01.id13", "stop");

		final JButton button = new JButton("message to editor2");
		final JButton button2 = new JButton("message test");
		final JButton button3 = new JButton(
				"message to Editor4 in Perspective2");
		final JButton button4 = new JButton("local message");
		final JButton button5 = new JButton("start ping/pong");
		final JButton button6 = new JButton("stop ping/pong");
		button.addActionListener(listener2.getListener());
		button3.addActionListener(listener3.getListener());
		button4.addActionListener(listener4.getListener());
		button5.addActionListener(listener5.getListener());
		button6.addActionListener(listener6.getListener());
		button2.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(final ActionEvent e) {
				int p = 0;
				int count = 4;
				while (p < 2) {
					final IActionListener<ActionListener, ActionEvent, Object> listener3 = getActionListener();
					if (count == 4) {
						listener3.getAction().addMessage("id04", "test1");
						count = 5;
					} else {
						listener3.getAction().addMessage("id05", "test");
						count = 4;
					}

					listener3.getListener().actionPerformed(e);
					p++;

				}

			}
		});

		panel.add(button);
		panel.add(button5);
		panel.add(button6);
		panel.add(button2);
		panel.add(button4);
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



	@Override
	public void handleBarEntries(Map<Layout, Container> bars) {
		final Container bottomBar = bars.get(Layout.SOUTH);
	    final JButton add2 = new JButton(new ImageIcon(Toolkit
		    .getDefaultToolkit().getImage("NSImage://NSColorPanel")));
	    // add2.putClientProperty("JButton.buttonType",
	    // "segmentedTextured");

	    add2.setFocusable(false);
	    final IActionListener<ActionListener, ActionEvent, Object> listener2 = getActionListener();
	    listener2.getAction().addMessage("id01", "tester");
	    add2.addActionListener(listener2.getListener());
	    add2.setText("editor");
	    add2.putClientProperty("JButton.segmentPosition", "first");
	    bottomBar.add(add2);
	    
	}

}
