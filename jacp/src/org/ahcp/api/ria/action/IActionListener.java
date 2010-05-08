/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.ahcp.api.ria.action;

/**
 * handles implementation specific ActionListener L defines the basic listener
 * type E defines the basic action type M defines the basic message type
 * 
 * @author Andy Moncsek
 */
public interface IActionListener<L, E, M> {

	/**
	 * notify component when action fired
	 * 
	 * @param action
	 */
	public abstract void notifyComponents(IAction<M, E> action);

	/**
	 * set Action to listener
	 * 
	 * @param action
	 */
	public abstract void setAction(IAction<M, E> action);

	/**
	 * returns the action
	 * 
	 * @return
	 */
	public IAction<M, E> getAction();

	/**
	 * returns implemetation specific ActionListener
	 * 
	 * @return
	 */
	public abstract L getListener();
}
