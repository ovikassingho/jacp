/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jacp.api.action;

import java.util.Map;

/**
 * represents an action used by specific listener, targets a component and
 * contains a message, every target get a specific instance of an action (clone) containing only his specific message and action event
 * 
 * @param <M>
 *            defines the type of message 
 * @param <A>
 *            defines the type of ActionEvent
 * @author Andy Moncsek
 */
public interface IAction<A, M> extends Cloneable {

	/**
	 * set message for target component: OWN
	 * 
	 * @param message
	 */
	public abstract void setMessage(final M message);

	/**
	 * set message for target component
	 * 
	 * @param targetId
	 * @param message
	 */
	public abstract void setMessage(final String targetId, final M message);

	/**
	 * get action message
	 * 
	 * @return
	 */
	public abstract M getMessage();

	/**
	 * returns message list with target id's
	 * 
	 * @return
	 */
	public abstract Map<String, M> getMessageList();

	/**
	 * get caller id
	 * 
	 * @return
	 */
	public abstract String getSourceId();

	/**
	 * set implementation specific event
	 * 
	 * @param event
	 */
	public abstract void setActionEvent(final A event);

	/**
	 * get implementation specific action event
	 * 
	 * @return
	 */
	public abstract A getActionEvent();

	/**
	 * clone action and containing event
	 * 
	 * @return
	 */
	public abstract IAction<A, M> clone();

	/**
	 * returns action target id
	 * 
	 * @return
	 */
	public abstract String getTargetId();

}
