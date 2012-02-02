package org.jacp.swing.demo1.editors;

import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JSplitPane;

import org.jacp.api.action.IAction;
import org.jacp.api.action.IActionListener;
import org.jacp.api.componentLayout.Layout;
import org.jacp.swing.rcp.component.ASwingComponent;

/**
 * This demo editor acts as message producer; this demo shows an good and a bad
 * variant of using components and their specific way of handling Variant A:
 * defines a while loop inside an "actionPerfomed"
 * 
 * @author Andy Moncsek
 * 
 */
public class DemoMessagePerformanceEditorProducer extends ASwingComponent {

    private final JPanel panel = new JPanel();
    final JButton button = new JButton("send 1000 messages");
    final JButton button2 = new JButton("send 1000 bg messages");
    final JButton button3 = new JButton("send 1000 stateless");
    final JButton button4 = new JButton("send stateless");
    final JSplitPane pane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);

    @Override
    public void handleMenuEntries(final Container meuneBar) {
	// TODO Auto-generated method stub

    }

    @Override
    public Container handleAction(final IAction<ActionEvent, Object> action) {

	if (action.getLastMessage() instanceof String) {
	    if (action.getLastMessage().equals("begin")) {
		int p = 0;
		System.out.println("BEGIN_ACTION" + action.getLastMessage());
		final IActionListener<ActionListener, ActionEvent, Object> listener2 = getActionListener();
		listener2.getAction().addMessage("id09", "start");
		listener2.getListener()
			.actionPerformed(action.getActionEvent());
		while (p < 10000) {
		    final IActionListener<ActionListener, ActionEvent, Object> listener3 = getActionListener();
		    String val = "test" + p;
		    listener3.getAction().addMessage("id09", val);
		    System.out.println("Producer Val.: " + val);
		    listener3.getListener().actionPerformed(
			    listener3.getAction().getActionEvent());
		    p++;

		}
		final IActionListener<ActionListener, ActionEvent, Object> listener3 = getActionListener();
		listener3.getAction().addMessage("id09", "stop");
		listener3.getListener()
			.actionPerformed(action.getActionEvent());
	    } else if (action.getLastMessage().equals("begin1")) {
		int p = 0;
		System.out.println("BEGIN_ACTION2" + action.getLastMessage());
		final IActionListener<ActionListener, ActionEvent, Object> listener2 = getActionListener();
		listener2.getAction().addMessage("id11", "start");
		listener2.getListener()
			.actionPerformed(action.getActionEvent());
		while (p < 2) {
		    final IActionListener<ActionListener, ActionEvent, Object> listener3 = getActionListener();
		    listener3.getAction().addMessage("id11", "test" + p);
		    listener3.getListener().actionPerformed(
			    listener3.getAction().getActionEvent());
		    p++;

		}
		final IActionListener<ActionListener, ActionEvent, Object> listener3 = getActionListener();
		listener3.getAction().addMessage("id11", "stop");
		listener3.getListener()
			.actionPerformed(action.getActionEvent());
	    } else if (action.getLastMessage().equals("begin2")) {
		int p = 0;
		System.out.println("BEGIN_ACTION3" + action.getLastMessage());
		final IActionListener<ActionListener, ActionEvent, Object> listener2 = getActionListener();
		listener2.getAction().addMessage("id12", "start");
		listener2.getListener()
			.actionPerformed(action.getActionEvent());
		while (p < 50) {
		    final IActionListener<ActionListener, ActionEvent, Object> listener3 = getActionListener();
		    listener3.getAction().addMessage("id12", p * 1000);
		    listener3.getListener().actionPerformed(
			    listener3.getAction().getActionEvent());
		    p++;

		}
		final IActionListener<ActionListener, ActionEvent, Object> listener3 = getActionListener();
		listener3.getAction().addMessage("id11", "stop");
		listener3.getListener()
			.actionPerformed(action.getActionEvent());
	    } else if (action.getLastMessage().equals("begin3")) {
		File expotImagesDir = new File("/Users/amo/Pictures/Export");
		//if (expotImagesDir.isDirectory()) {
		//    for (final File file : expotImagesDir.listFiles()) {
			final IActionListener<ActionListener, ActionEvent, Object> listener = getActionListener();
			listener.getAction().addMessage("id13", expotImagesDir);
			listener.getListener().actionPerformed(
				listener.getAction().getActionEvent());
		//    }
	//	}

	    } else {
		button.addActionListener(new ActionListener() {

		    @Override
		    public void actionPerformed(final ActionEvent e) {
			final IActionListener<ActionListener, ActionEvent, Object> listener2 = getActionListener();
			listener2.getAction().addMessage("id08", "begin");
			listener2.getListener().actionPerformed(e);

		    }
		});
		final boolean isBlocked = isBlocked();
		button2.addActionListener(new ActionListener() {

		    @Override
		    public void actionPerformed(final ActionEvent e) {
			System.out.println("blocked" + isBlocked);
			final IActionListener<ActionListener, ActionEvent, Object> listener2 = getActionListener();
			listener2.getAction().addMessage("id08", "begin1");
			listener2.getListener().actionPerformed(e);

		    }
		});
		button3.addActionListener(new ActionListener() {

		    @Override
		    public void actionPerformed(final ActionEvent e) {
			System.out.println("blocked" + isBlocked);
			final IActionListener<ActionListener, ActionEvent, Object> listener2 = getActionListener();
			listener2.getAction().addMessage("id08", "begin2");
			listener2.getListener().actionPerformed(e);

		    }
		});
		button4.addActionListener(new ActionListener() {

		    @Override
		    public void actionPerformed(final ActionEvent e) {
			System.out.println("blocked" + isBlocked);
			final IActionListener<ActionListener, ActionEvent, Object> listener2 = getActionListener();
			listener2.getAction().addMessage("id08", "begin3");
			listener2.getListener().actionPerformed(e);

		    }
		});

		pane.add(button);
		JSplitPane frame = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
		JSplitPane frame2 = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
		frame.add(button2);
		frame.add(frame2);
		frame2.add(button3);
		frame2.add(button4);
		pane.add(frame);
		// pane.add(button3);

		panel.add(pane);
	    }
	}
	return panel;
    }

    @Override
    public void handleBarEntries(Map<Layout, Container> bars) {
	// TODO Auto-generated method stub

    }

}
