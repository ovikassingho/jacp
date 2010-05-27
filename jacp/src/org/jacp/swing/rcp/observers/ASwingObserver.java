package org.jacp.swing.rcp.observers;

import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.Map;

import org.jacp.api.action.IAction;
import org.jacp.api.base.IComponent;
import org.jacp.api.observers.IObserver;

/**
 * Observer handles message notification and notifies correct components
 * 
 * @author Andy Moncsek
 * 
 */
public abstract class ASwingObserver implements
		IObserver<Container, ActionListener, ActionEvent, Object> {

	/**
	 * returns cloned action with valid message TODO add to interface
	 * 
	 * @param action
	 * @param message
	 * @return
	 */
	protected IAction<ActionEvent, Object> getValidAction(
			final IAction<ActionEvent, Object> action, final String target,
			final Object message) {
		final IAction<ActionEvent, Object> actionClone = action.clone();
		actionClone.setMessage(target, message);
		return actionClone;
	}
	/**
	 * when id has no separator it is a local message // TODO remove code duplication
	 * 
	 * @param messageId
	 * @return
	 */
	protected boolean isLocalMessage(final String messageId) {
		return !messageId.contains(".");
	}

	/**
	 * returns target message with perspective and component name // TODO remove code duplication
	 * 
	 * @param messageId
	 * @return
	 */
	protected String[] getTargetId(final String messageId) {
		return messageId.split("\\.");
	}

	/**
	 * returns the message target perspective id
	 * 
	 * @param messageId
	 * @return
	 */
	protected String getTargetPerspectiveId(final String messageId) {
		final String[] targetId = getTargetId(messageId);
		if (!isLocalMessage(messageId)) {
			return targetId[0];
		}
		return messageId;
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

	@Override
	public <M extends IComponent<Container, ActionListener, ActionEvent, Object>> M getObserveableById(
			final String id, final List<M> components) {
		for (final M p : components) {
			if (p.getId().equals(id)) {
				return p;
			}
		}
		return null;
	}

	@Override
	public synchronized void handle(final IAction<ActionEvent, Object> action) {
		final Map<String, Object> messages = action.getMessageList();
		for (final String targetId : messages.keySet()) {
			handleMessage(targetId, action);
		}

	}

}
