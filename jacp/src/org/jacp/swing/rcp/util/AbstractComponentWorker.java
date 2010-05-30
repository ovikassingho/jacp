package org.jacp.swing.rcp.util;

import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Map;

import javax.swing.JComponent;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;

import org.jacp.api.action.IAction;
import org.jacp.api.base.ISubComponent;

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
			((JScrollPane) validContainer).getViewport().add(editor.getName(),
					editor.getRoot());
		} else {
			handleAdd(validContainer, editor.getRoot(), editor.getName());

		}
		validContainer.setEnabled(true);
		validContainer.setVisible(true);
	}

	/**
	 * enables component an add to container
	 * 
	 * @param validContainer
	 * @param uiComponent
	 * @param name
	 */
	private void handleAdd(final Container validContainer,
			final Container uiComponent, final String name) {
		uiComponent.setEnabled(true);
		uiComponent.setVisible(true);
		validContainer.add(name, uiComponent);
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
		}; // Thread END
		worker.start();

	}

	protected abstract ISubComponent<Container, ActionListener, ActionEvent, Object> runHandleSubcomponent(
			final ISubComponent<Container, ActionListener, ActionEvent, Object> component,
			final IAction<ActionEvent, Object> action);

	/**
	 * removes old ui component of subcomponent form parent ui component
	 * 
	 * @param parent
	 * @param currentContainer
	 */
	protected void handleOldComponentRemove(final Container parent,
			final Container currentContainer) {
		final Thread worker = new Thread() {
			@Override
			public void run() {
				SwingUtilities.invokeLater(new Runnable() {
					@Override
					public void run() {
						// run in EventDispatchThread
						parent.remove(currentContainer);
					}
				}); // SWING UTILS END
			} // run end
		}; // Thread END
		worker.start();
	}

	/**
	 * set new ui component to parent ui component
	 * 
	 * @param component
	 * @param parent
	 * @param currentTaget
	 */
	protected void handleNewComponentValue(
			final ISubComponent<Container, ActionListener, ActionEvent, Object> component,
			final Map<String, Container> targetComponents,
			final Container parent, final String currentTaget) {
		if (currentTaget.equals(component.getTarget())) {
			addComponentByType(parent, component);
		} else { // currentTarget.length < 2 Happens when component changed
			// target from one perspective to an other
			final String validId = currentTaget.length() < 2 ? getTargetComponentId(component
					.getTarget())
					: component.getTarget();
			handleTargetChange(component, targetComponents, validId);

		}
	}

	/**
	 * handle component when target has changed
	 * 
	 * @param component
	 * @param targetComponents
	 */
	private void handleTargetChange(
			final ISubComponent<Container, ActionListener, ActionEvent, Object> component,
			final Map<String, Container> targetComponents, final String target) {
		final Container validContainer = getValidContainerById(
				targetComponents, target);
		if (validContainer != null) {
			addComponentByType(validContainer, component);
		} else {
			// handle target outside current perspective
			component.getParentPerspective().delegateTargetChange(
					component.getTarget(), component);
		}
	}

	/**
	 * runs subcomponents handle method
	 * 
	 * @param component
	 * @param action
	 * @return
	 */
	protected Container prepareAndHandleComponent(
			final ISubComponent<Container, ActionListener, ActionEvent, Object> component,
			final IAction<ActionEvent, Object> action) {
		final Container editorComponent = component.handle(action);
		component.setRoot(editorComponent);
		editorComponent.setVisible(true);
		editorComponent.setEnabled(true);
		return editorComponent;
	}

	/**
	 * returns the message target component id
	 * 
	 * @param messageId
	 * @return
	 */
	protected String getTargetComponentId(final String messageId) {
		final String[] targetId = getTargetId(messageId);
		if (!isLocalMessage(messageId)) {
			return targetId[1];
		}
		return messageId;
	}

	/**
	 * when id has no separator it is a local message
	 * 
	 * @param messageId
	 * @return
	 */
	protected boolean isLocalMessage(final String messageId) {
		return !messageId.contains(".");
	}

	/**
	 * returns target message with perspective and component name
	 * 
	 * @param messageId
	 * @return
	 */
	protected String[] getTargetId(final String messageId) {
		return messageId.split("\\.");
	}

}
