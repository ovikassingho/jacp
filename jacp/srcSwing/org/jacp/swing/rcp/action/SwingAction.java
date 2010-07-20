/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jacp.swing.rcp.action;

import java.awt.event.ActionEvent;
import java.util.HashMap;
import java.util.Map;

import org.jacp.api.action.IAction;

/**
 * represents an action which is fired by an component, has a target and a
 * message targeting the component itself or an other component
 * 
 * @author Andy Moncsek
 */
public final class SwingAction implements IAction<ActionEvent, Object> {

	private final ThreadLocal<Map<String, Object>> tMessages = new ThreadLocal<Map<String, Object>>() {
		private final Map<String, Object> messages = new HashMap<String, Object>();

		@Override
		protected Map<String, Object> initialValue() {
			return messages;
		}
	};
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
		this.target = target;
		setMessage(message);

	}

	@Override
	public void setMessage(final Object message) {
		this.message = message;
		target = target != null ? target : getSourceId();
		getMessageList().put(target, message);
	}

	@Override
	public void setMessage(final String id, final Object message) {
		target = id;
		this.message = message;
		getMessageList().put(id, message);
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
		return tMessages.get();
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
	public IAction<ActionEvent, Object> clone() {
		final IAction<ActionEvent, Object> clone = new SwingAction(sourceId);
		clone.setActionEvent(event);
		return clone;
	}
}
