package org.jacp.swing.demo1.editors;

import java.awt.Container;
import java.awt.event.ActionEvent;

import javax.swing.JPanel;
import javax.swing.JTextField;

import org.jacp.api.action.IAction;
import org.jacp.swing.rcp.editor.ASwingEditor;

public class DemoMessagePerformanceEditorConsumer extends ASwingEditor {
	
	private JPanel panel=null;
	private int counter=0;
	private final JTextField text1 = new JTextField();
	private long start;

	@Override
	public void addMenuEntries(final Container meuneBar) {
		// TODO Auto-generated method stub

	}

	@Override
	public void handleBarEntries(final Container toolBar,
			final Container bottomBar) {
		// TODO Auto-generated method stub

	}

	@Override
	public Container handle(final IAction<ActionEvent, Object> action) {
		if(panel==null) {
			panel = new JPanel();
		}
		
		
		

		
		if(action.getMessage().equals("stop")){
			long stopTime = System.currentTimeMillis();
			System.out.println("Time: " + (stopTime-start));
		} else if(action.getMessage().equals("start")) {
			start = System.currentTimeMillis();
			text1.setText("count: " + counter);
			counter++;
			panel.add(text1);
		} else {
			text1.setText("count: " + counter);
			counter++;
			panel.add(text1);
		}

		
		return panel;
	}

}
