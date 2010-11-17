package org.jacp.swing.demo1.editors;

import java.awt.Container;
import java.awt.event.ActionEvent;
import java.util.Map;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.JTextField;

import org.jacp.api.action.IAction;
import org.jacp.api.componentLayout.Layout;
import org.jacp.swing.rcp.component.ASwingComponent;
import org.springframework.jmx.export.annotation.ManagedResource;
@ManagedResource(objectName = "org.jacp:name=DemoMessagePerformanceEditorConsumer", description = "a state ful swing component")
public class DemoMessagePerformanceEditorConsumer extends ASwingComponent {

	private JPanel panel = null;
	private int counter = 0;
	private final JTextField text1 = new JTextField();
	private long startTime;
	private final JLabel label = new JLabel();
	private final JLabel label1 = new JLabel();
	private final JLabel label2 = new JLabel();
	final JSplitPane pane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
	private boolean block = false;

	@Override
	public void handleMenuEntries(final Container meuneBar) {
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
		if (action.getLastMessage() instanceof String) {
			if (action.getLastMessage().equals("stop")) {
				final long stopTime = System.currentTimeMillis();

				label.setText("stop Time: " + (stopTime - startTime));
				text1.setText("count: " + counter);
				block = true;
			} else if (action.getLastMessage().equals("start")) {
				startTime = System.currentTimeMillis();
				text1.setText("count: " + counter);
				counter++;
				block = false;
			} else {
				text1.setText("count: " + counter + " block:"+block);
				if (!block) {
				    counter++;
				}
			}
			label1.setText("message: " + action.getLastMessage());
			System.out.println("message: " + action.getLastMessage());

		} else if(action.getLastMessage() instanceof Long){
			System.out.println("LLLOOONNNGGG" +action.getLastMessage());
			label2.setText("stop Time bg component: " + action.getLastMessage());


		}else if(action.getLastMessage() instanceof Integer){
			System.out.println("Integer" +action.getLastMessage());
			counter=counter + ((Integer)action.getLastMessage());
			text1.setText("count: " + counter);
			


		}
/*		try {
		    Thread.currentThread().sleep(10);
		} catch (InterruptedException e) {
		    // TODO Auto-generated catch block
		    e.printStackTrace();
		}*/
		return panel;
	}

	@Override
	public void handleBarEntries(Map<Layout, Container> bars) {
	    // TODO Auto-generated method stub
	    
	}

}
