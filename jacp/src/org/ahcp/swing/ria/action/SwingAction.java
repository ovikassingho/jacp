/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.ahcp.swing.ria.action;

import java.awt.event.ActionEvent;
import java.util.HashMap;
import java.util.Map;

import org.ahcp.api.ria.action.IAction;

/**
 * 
 * @author Andy Moncsek
 */
public final class SwingAction implements IAction<Object, ActionEvent> {

	private final Map<String, Object> messages = new HashMap<String, Object>();
	private Object message;
	private final String sourceId;
	private ActionEvent event;
	private String target;

	public SwingAction(final String sourceId) {
		this.sourceId = sourceId;
	}

	public SwingAction(final String sourceId, final Object message) {
		this.sourceId = sourceId;
		setMessage(message);
	}

	public SwingAction(final String sourceId, final String target,
			final Object message) {
		this.sourceId = sourceId;
		setMessage(message);
		this.target = target;
	}

	@Override
	public void setMessage(final Object message) {
		this.message = message;
		target = getSourceId();
		messages.put(getSourceId(), message);
	}

	@Override
	public void setMessage(final String id, final Object message) {
		target = id;
		this.message = message;
		messages.put(id, message);
	}

	@Override
	public String getSourceId() {
		return sourceId;
	}

	@Override
	public Object getMessage() {
		return message;
	}

	@Override
	public Map<String, Object> getMessageList() {
		return messages;
	}

	@Override
	public void setActionEvent(final ActionEvent event) {
		this.event = event;
	}

	@Override
	public ActionEvent getActionEvent() {
		return event;
	}

	@Override
	public String getTargetId() {
		return target;
	}

	@Override
	public IAction<Object, ActionEvent> clone() {
		final IAction<Object, ActionEvent> clone = new SwingAction(sourceId);
		clone.setActionEvent(event);
		return clone;
	}
}
