package org.jacp.swing.demo1.editors;

import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;

import org.jacp.api.action.IAction;
import org.jacp.api.action.IActionListener;
import org.jacp.swing.rcp.editor.ASwingEditor;

public class DemoMessagePerformanceEditorProducer extends ASwingEditor {
	
	private JPanel panel=null;

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
		
		final JButton button = new JButton("send 1000 messages");
		button.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(final ActionEvent e) {
				int p = 0;

				while (p < 1000) {
					final IActionListener<ActionListener, ActionEvent, Object> listener3 = getActionListener();
					listener3.getAction().setMessage("id09", "test1");
					listener3.getListener().actionPerformed(e);
					p++;

				}
				final IActionListener<ActionListener, ActionEvent, Object> listener3 = getActionListener();
				listener3.getAction().setMessage("id09", "stop");
				listener3.getListener().actionPerformed(e);

			}
		});
		
		panel.add(button);
		return panel;
	}

}
