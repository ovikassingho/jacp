package org.jacp.swing.rcp.observers;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutionException;
import java.util.logging.Logger;

import org.jacp.api.action.IAction;
import org.jacp.api.component.IComponent;
import org.jacp.api.observers.IObserver;

/**
 * Observer handles message notification and notifies correct components
 * 
 * @author Andy Moncsek
 * 
 */
public abstract class ASwingObserver
		extends
		org.jacp.swing.rcp.util.SwingWorker<IAction<ActionEvent, Object>, IAction<ActionEvent, Object>>
		implements IObserver<ActionListener, ActionEvent, Object> {

	private final Logger logger = Logger.getLogger(this.getClass().getName());

	private volatile BlockingQueue<IAction<ActionEvent, Object>> messages = new ArrayBlockingQueue<IAction<ActionEvent, Object>>(
			10000);

	@Override
	protected IAction<ActionEvent, Object> doInBackground() throws Exception {
		while (true) {
			log(" observer thread size" + messages.size());
			final IAction<ActionEvent, Object> action = messages.take();
			final Map<String, Object> messages = action.getMessageList();
			for (final String targetId : messages.keySet()) {
				log(" handle message to: " + targetId);
				handleMessage(targetId, action);
			}
			log(" observer thread DONE");

		}
	}

	@Override
	protected void done() {
		// TODO this should only happen when an error occurred
		try {
			this.get();
		} catch (final InterruptedException e) {
			e.printStackTrace();
		} catch (final ExecutionException e) {
			e.printStackTrace();
		}
	}

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
	 * when id has no separator it is a local message // TODO remove code
	 * duplication
	 * 
	 * @param messageId
	 * @return
	 */
	protected boolean isLocalMessage(final String messageId) {
		return !messageId.contains(".");
	}

	/**
	 * returns target message with perspective and component name // TODO remove
	 * code duplication
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
	public <M extends IComponent<ActionListener, ActionEvent, Object>> M getObserveableById(
			final String id, final List<M> components) {
		for (final M p : components) {
			if (p.getId().equals(id)) {
				return p;
			}
		}
		return null;
	}

	@Override
	public void handle(final IAction<ActionEvent, Object> action) {
		synchronized (action) {
			messages.add(action);
		}

	}

	protected void log(final String message) {
		logger.fine(message);
	}

}
