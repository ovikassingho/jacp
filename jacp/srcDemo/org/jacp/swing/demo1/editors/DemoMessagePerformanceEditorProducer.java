package org.jacp.swing.demo1.editors;

import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;

import org.jacp.api.action.IAction;
import org.jacp.api.action.IActionListener;
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

	@Override
	public void handleMenuEntries(final Container meuneBar) {
		// TODO Auto-generated method stub

	}

	@Override
	public void handleBarEntries(final Container toolBar,
			final Container bottomBar) {
		// TODO Auto-generated method stub

	}

	@Override
	public Container handleAction(final IAction<ActionEvent, Object> action) {

		if (action.getMessage().equals("begin")) {
			int p = 0;
			System.out.println("BEGIN_ACTION" + action.getMessage() + panel);
			final IActionListener<ActionListener, ActionEvent, Object> listener2 = getActionListener();
			listener2.getAction().setMessage("id09", "start");
			listener2.getListener().actionPerformed(action.getActionEvent());
			while (p < 999) {
				final IActionListener<ActionListener, ActionEvent, Object> listener3 = getActionListener();
				listener3.getAction().setMessage("id09", "test" + p);
				listener3.getListener()
						.actionPerformed(action.getActionEvent());
				p++;

			}
			final IActionListener<ActionListener, ActionEvent, Object> listener3 = getActionListener();
			listener3.getAction().setMessage("id09", "stop");
			listener3.getListener().actionPerformed(action.getActionEvent());
		} else {
			button.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(final ActionEvent e) {
					final int p = 0;
					final IActionListener<ActionListener, ActionEvent, Object> listener2 = getActionListener();
					listener2.getAction().setMessage("id08", "begin");
					listener2.getListener().actionPerformed(e);

				}
			});
		}

		panel.add(button);
		return panel;
	}

}
