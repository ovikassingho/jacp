package org.ahcp.swing.ria.util;

import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Map;

import javax.swing.JComponent;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;

import org.ahcp.api.ria.action.IAction;
import org.ahcp.api.ria.base.ISubComponent;

public abstract class AbstractComponentWorker
		extends
		SwingWorker<ISubComponent<Container, ActionListener, ActionEvent, Object>, Container> {

	/**
	 * find valid target component in perspective
	 * 
	 * @param targetComponents
	 * @param id
	 * @return
	 */
	protected Container getValidContainerById(
			final Map<String, Container> targetComponents, final String id) {
		return targetComponents.get(id);
	}

	/**
	 * find valid target and add type specific new component TODO add to
	 * abstract worker
	 * 
	 * @param layout
	 * @param editor
	 */
	protected void addComponentByType(
			final Container validContainer,
			final ISubComponent<Container, ActionListener, ActionEvent, Object> editor) {
		if (validContainer instanceof JScrollPane) {
			((JScrollPane) validContainer).getViewport().add(editor.getRoot());
		} else {
			validContainer.add(editor.getName(), editor.getRoot());
		}
		validContainer.setEnabled(true);
		invalidateHost(validContainer);
	}

	protected void invalidateHost(final Container host) {
		// run in EventDispatchThread
		final Thread worker = new Thread() {
			@Override
			public void run() {
				SwingUtilities.invokeLater(new Runnable() {
					@Override
					public void run() {
						if (host instanceof JComponent) {
							((JComponent) host).revalidate();
						} else {
							host.invalidate();
						}
						host.repaint();

					}
				}); // SWING UTILS END
			} // run end
		}; // thred END
		worker.start();

	}

	protected abstract ISubComponent<Container, ActionListener, ActionEvent, Object> runHandleSubcomponent(
			final ISubComponent<Container, ActionListener, ActionEvent, Object> component,
			final IAction<Object, ActionEvent> action);

}
