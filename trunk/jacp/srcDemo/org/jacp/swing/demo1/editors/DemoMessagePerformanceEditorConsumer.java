package org.jacp.swing.demo1.editors;

import java.awt.Container;
import java.awt.event.ActionEvent;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.JTextField;

import org.jacp.api.action.IAction;
import org.jacp.swing.rcp.component.ASwingComponent;

public class DemoMessagePerformanceEditorConsumer extends ASwingComponent {

	private JPanel panel = null;
	private int counter = 0;
	private final JTextField text1 = new JTextField();
	private long startTime;
	private final JLabel label = new JLabel();
	private final JLabel label1 = new JLabel();
	private final JLabel label2 = new JLabel();
	final JSplitPane pane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);

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
		if (panel == null) {
			panel = new JPanel();
			panel.add(label);
			panel.add(text1);
			panel.add(label1);
			panel.add(label2);
		}
		if (action.getMessage() instanceof String) {
			if (action.getMessage().equals("stop")) {
				final long stopTime = System.currentTimeMillis();

				label.setText("stop Time: " + (stopTime - startTime));
			} else if (action.getMessage().equals("start")) {
				startTime = System.currentTimeMillis();
				text1.setText("count: " + counter);
				counter++;
			} else {
				text1.setText("count: " + counter);
				counter++;
			}
			label1.setText("message: " + action.getMessage());

		} else if(action.getMessage() instanceof Long){
			System.out.println("LLLOOONNNGGG" +action.getMessage());
			label2.setText("stop Time bg component: " + action.getMessage());


		}
		try {
		    Thread.currentThread().sleep(10);
		} catch (InterruptedException e) {
		    // TODO Auto-generated catch block
		    e.printStackTrace();
		}
		return panel;
	}

}
